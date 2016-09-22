<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "uploadFile.Title"/></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.5/validator.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.full.min.js"></script>
<script src="<@spring.url "/resources/js/track.js"/>"></script>
<script src="<@spring.url "/resources/js/history.js"/>"></script>
<script src="<@spring.url "/resources/js/autocompleteConfig.js"/>"></script>
<link href="<@spring.url "/resources/css/filespage.css" />" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet"/>
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
            <input type="text" name="artist" class="form-control" placeholder="<@spring.message "placeholder.Artist"/>"
                   data-minlength="2"
                   maxlength="30" required/>
            <input type="text" name="name" class="form-control" placeholder="<@spring.message "placeholder.Title"/>"
                   data-minlength="2"
                   maxlength="30" required/>

            <div class="form-group" name="tagsContainer">
                <h4 class="control-label text-center"><@spring.message "uploadFile.TagsCaption"/></h4>
                <select class="form-control" id="tags" name="tags"></select>
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
    var placeholder = "<@spring.message "placeholder.Tags"/>";

    $("#tags").select2(tagAutocomplete(placeholder));

    $("input[name=artist]").autocomplete(artistAutocomplete(function () {
        return $("select[name=tags]").val()
    }));
    
    function validateForm() {
        var validator = $("form[name=uploadForm]:last").data("bs.validator");
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
        $("div[name=uploadFormContainer]").find("input").val("").end();
        $("#tags").val(null).trigger("change");
        $("#result").hide();
    }

    function copyForm() {
        if (!validateForm()) {
            return;
        }
        $("div[name=uploadFormContainer]:last").clone()
                .find("input:text").val("").end()
                .find(".select2-container:last").remove().end()
                .find(".select2-hidden-accessible").remove().end()
                .appendTo("#container");

        var s = $("<select id=\"tags\" name=\"tags\" class=\"form-control\" />");
        $(s).appendTo('div[name=tagsContainer]:last');
        $("input[name=artist]:last").autocomplete(artistAutocomplete(function () {
            return $("div[name=uploadFormContainer]:last").find("select[name=tags]").val()
        }));
        $("select[name=tags]:last").select2(tagAutocomplete(placeholder));
        $("form[name=uploadForm]:last").validator();
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
            var obj = {};
            var artist = {};
            artist.artistName=$(this).find("input[name=artist]").val();
            artist.tags=$(this).find("#tags").val();
            obj.name = $(this).find("input[name=name]").val() ;
            obj.artist = artist;
            obj.location = "unknown";
            obj.tags = $(this).find("#tags").val();

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
                            showMessage("(" + obj.artist.artistName + " - " + obj.name + ")" + ": " + xhr.responseText, "danger");
                        } else {
                            showMessage("(" + obj.artist.artistName + " - " + obj.name + ")" + ": " + error, "danger");
                        }
                        return false;
                    });
        });
    }

</script>
</html>