<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Add song</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.5/validator.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.full.min.js"></script>
<script src="<@spring.url "/resources/js/track.js"/>"></script>
<script src="<@spring.url "/resources/js/select2config.js"/>"></script>
<link href="<@spring.url "/resources/css/filespage.css" />" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet"/>
</@m.head>

<@m.body>

    <@m.navigation  m.pages.Add/>

<div id="container" class="container">
    <div id="result" class="col-md-4 col-md-offset-4 " role="alert"></div>
    <div class="col-md-4 col-md-offset-4 text-center">
        <button type="button" class="btn btn-success" onclick="addTrack()">Upload
        </button>
        <button type="button" onclick="copyForm()" class="btn btn-default">Add more
        </button>
        <button type="button" onclick="clearForms()" class="btn btn-default">Clear forms
        </button>
    </div>

    <div name="uploadFormContainer" class="col-md-4 col-md-offset-4 well  ">
        <form method="POST" name="upForm" role="form" data-toggle="validator" action="javascript:void(null);"
              onsubmit="">
            <input type="text" name="artist" class="form-control" placeholder="Artist" data-minlength="2"
                   maxlength="30" required/>
            <input type="text" name="name" class="form-control" placeholder="Title" data-minlength="2"
                   maxlength="30" required/>

            <div class="form-group">
                <h4 class="control-label text-center">Tags (optional)</h4>
                <select class="form-control" id="tags"></select>
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
    var contextPath = "<@spring.url "" />";
    $("#tags").select2(tagSearchConfig(contextPath));

    function validateForm() {
        var validator = $('form[name="upForm"]:last').data("bs.validator");
        validator.validate();
        if (!validator.hasErrors()) {
            return true;
        } else {
            showMessage("Size of fields must be from 2 to 30", "warning");
            return false;
        }
    }

    function clearForms() {
        $('div[name=uploadFormContainer]').not(':first').remove();
        $('div[name="uploadFormContainer"]').find("input").val("").end();
        $("#tags").empty();
        $('#result').hide();
    }

    function copyForm() {
        if (!validateForm()) {
            return;
        }
        var clone = $('div[name="uploadFormContainer"]:last').clone();
        clone.find("input:text").val("").end()
                .appendTo('#container');
        clone.find("select").empty();
        $('form[name="upForm"]:last').validator();
    }

    function showMessage(message, type) {
        $("#result").removeClass();
        $('#result').addClass("alert alert-dismissible collapse alert-" + type);
        $('#result').html(message);
        $('#result').show();
    }

    function addTrack() {
        if (!validateForm()) {
            return;
        }
        $("form[name=upForm]").each(function () {
            var obj = new Object();
            obj.name = $(this).find('input[name=artist]').val() + " - " + $(this).find('input[name=name]').val();
            obj.location = "unknown";
            obj.tags = $(this).find("#tags").val();

            var formData = new FormData();
            formData.append("track", new Blob([JSON.stringify(obj)], {
                type: "application/json"
            }));
            formData.append("file", $(this).find('input[name=file]')[0].files[0]);
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