<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
    <@m.head>
        <title><@spring.message "uploadFile.Title"/></title>
        <script src="//cdn.jsdelivr.net/bootstrap.tagsinput/0.4.2/bootstrap-tagsinput.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.5/validator.min.js"></script>
        <link href="/resources/css/filespage.css" rel="stylesheet">
        <script src="resources/js/track.js"></script>
    </@m.head>

    <@m.body>

    <@m.navigation/>

    <div id="container" class="container">
        <div id="result" class="col-md-4 col-md-offset-4 " role="alert"></div>
        <div class="col-md-4 col-md-offset-4 text-center">
            <button type="button" class="btn btn-success" onclick="addTrack()"><@spring.message "uploadFile.Upload"/>
            </button>
            <button type="button" onclick="copyForm()" class="btn btn-default"><@spring.message "uploadFile.AddMore"/>
            </button>
            <button type="button" onclick="clearForms()" class="btn btn-default"><@spring.message "uploadFile.Clear"/>
            </button>
        </div>

        <div name="uploadFormContainer" class="col-md-4 col-md-offset-4 well  ">
            <form method="POST" name="uploadForm" role="form" data-toggle="validator" action="javascript:void(null);"
                  onsubmit="">
                <input type="text" name="artist" class="form-control" placeholder="<@spring.message "placeholder.Artist"/>" data-minlength="2"
                       maxlength="30" required/>
                <input type="text" name="name" class="form-control" placeholder="<@spring.message "placeholder.Title"/>" data-minlength="2"
                       maxlength="30" required/>

                <div class="form-group">
                    <h4 class="control-label text-center"><@spring.message "uploadFile.TagsCaption"/></h4>
                    <input type="text" name="tags" class="form-control"
                           data-role="tagsinput" placeholder="<@spring.message "placeholder.Tags"/>"/>
                </div>
                <input type="file" name="file" required>
            </form>
        </div>
    </div>
    </@m.body>
    <@m.footer />

<script type="text/javascript">
    const max_length_error = 200;
    var track = new Track();

    function validateForm() {
        var validator = $("form[name="uploadForm"]:last").data("bs.validator");
        validator.validate();
        if (!validator.hasErrors()) {
            return true;
        } else {
            showMessage("<@spring.message "uploadFile.ValidatorError"/>", "warning");
            return false;
        }
    }

    function clearForms() {
        $("div[name=uploadFormContainer]").not(":first").remove();
        $("div[name="uploadFormContainer"]").find("input").val("").end()
        $("input[name=tags]").tagsinput("removeAll");
        $("#result").hide();
    }

    function copyForm() {
        if (!validateForm()) {
            return;
        }
        $("div[name="uploadFormContainer"]:last").clone()
                .find("input:text").val("").end()
                .find(".bootstrap-tagsinput:last").remove().end()
                .appendTo("#container");
        $("input[name=tags]:last").tagsinput();
        $("form[name="uploadForm"]:last").validator();
    }

    function showMessage(message, type) {
        $("#result").removeClass();
        $("#result").addClass("alert alert-dismissible collapse alert-" + type);
        $("#result").html(message);
        $("#result").show();
    }

    function addTrack() {
        if (!validateForm()) {
            return;
        }
        $("form[name=uploadForm]").each(function () {
            var obj = new Object();
            obj.name = $(this).find("input[name=artist]").val() + " - " + $(this).find("input[name=name]").val();
            obj.location = "unknown";
            if ($(this).find("input[name=tags]").val() != "") {
                obj.tags = $(this).find("input[name=tags]").val().split(",");
            }
            var formData = new FormData();
            formData.append("track", new Blob([JSON.stringify(obj)], {
                type: "application/json"
            }));
            formData.append("file", $(this).find("input[name=file]")[0].files[0]);
            track.createJson(formData)
                    .then(function (data) {
                        showMessage(data, "success");
                    }, function (xhr, status, error) {
                        if (xhr.responseText.length < max_length_error) {
                            showMessage("(" + obj.artist + " - " + obj.name + ")" + ": " + xhr.responseText, "danger");
                        } else {
                            showMessage("(" + obj.artist + " - " + obj.name + ")" + ": " + error, "danger");
                        }
                        return false;
                    });
        });
    }

</script>
</html>