<#import "macros/macros.ftl" as m>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Upload File Request Page</title>
<script src="http://malsup.github.com/jquery.form.js"></script>
</@m.head>


<@m.body>
<form id="uploadForm" method="POST" action="/files" enctype="multipart/form-data">
    <input name="file" id="file" type="file"/><br/>
</form>

<button value="Submit" onclick="uploadJqueryForm()">Upload</button>
<i>Using JQuery Form Plugin</i><br/>

<audio id="player" src="/files/Angel Duster.mp3" controls preload="none"></audio>

<div id="result"></div>
<script>
    function uploadJqueryForm() {
        $('#result').html('');

        $("#uploadForm").ajaxForm({
            success: function (data) {
                $('#result').html(data);
            },
            dataType: "text"
        }).submit();
    }
</script>
</@m.body>
</html>