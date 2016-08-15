<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE HTML>
<html lang="en-US">
<@m.head>
<title>Web Audio API - Playing a chunked audio file</title>
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
    var player = new ChunksPlayer();
    var trackManager = new Track();

    $("#playBtn").on("click", function () {
        player.resume();
    });

    $("#pauseBtn").on("click", function () {
        player.pause();
    });
    $(document).ready(function () {
        trackManager.get(1)
                .then(function (track) {
                    if (track) {
                        player.play(track);
                    }
                });
    });
</script>
</@m.body>
</html>