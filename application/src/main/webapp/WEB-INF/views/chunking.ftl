<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as p>
<#import "/spring.ftl" as spring />
<!DOCTYPE HTML>
<html lang="en-US">
<@m.head>
<meta charset="UTF-8">
<title>Web Audio API - Playing a chunked mp3 file</title>
<script type="text/javascript" src="<@spring.url "/resources/js/chunksplayer.js" />"></script>
<script type="text/javascript" src="<@spring.url "/resources/js/track.js" />"></script>
</@m.head>
<@m.body>
<button type="button" class="btn btn-xs btn-success play-track-button" id="playBtn">
    <span class='glyphicon glyphicon-play' aria-hidden='true'></span>
</button>
<button type="button" class="btn btn-xs btn-warning pause-track-button" id="pauseBtn">
    <span class="glyphicon glyphicon-pause" aria-hidden="true"></span>
</button>
<script type="text/javascript">
    var player = new MP3ChunksPlayer();
    var track = new Track("<@spring.url ""/>");

    $("#playBtn").on("click", function () {

    });

    $("#pauseBtn").on("click", function () {

    });
    $(document).ready(function () {
        track.get(4)
                .then(function (response) {
                    player.play(response);
                });
    });
</script>
</@m.body>
</html>