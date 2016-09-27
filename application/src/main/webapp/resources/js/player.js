"use strict";


jQuery(document).ready(function () {

    var $trackHtml;
    var player = new ChunksPlayer();
    var track = new Track();

    function writeToFoot(tr) {
        $("#nameInFoot").html(tr.name);
        $("#artistInFoot").html(tr.artist);
    }

    function playTrack(trackId) {
        if (player.getTrackId() == trackId) {
            player.resume();
        }
        else {
            removePausingState();
            track.get(trackId).then(function (tr) {
                player.play(tr);
                writeToFoot(tr);
            });
        }
    }

    function removePausingState() {
        if ($trackHtml) {
            var prevBttn = $trackHtml.children().children().first();
            prevBttn.removeClass('btn-warning').find('span').removeClass('glyphicon-pause');
        }
    }
    
    function changeStateButton() {
        if ($trackHtml) {
            var bttn = $trackHtml.children().children().first();
            bttn.toggleClass('btn-warning').find('span').toggleClass('glyphicon-pause');
        }
    }
    /* Make the play button to invoke play on the corresponding player. */

    $(".tracks-table").on("click", ".play-pause-button", function () {
        if (!$(this).hasClass('btn-warning')) {
            playTrack($(this).closest("tr")[0].id);
            $trackHtml = $(this).closest("tr");
        }
        else {
            player.pause();
        }
        changeStateButton()
    });


    $('#nextFooterBtn').bind('click', function() {
        if ($trackHtml.next()[0].id) {
            playTrack($trackHtml.next()[0].id);
            $trackHtml = $trackHtml.next();
            changeStateButton()
        }
    });

    $('#prevFooterBtn').bind('click', function() {
        if ($trackHtml.prev()[0].id) {
            playTrack($trackHtml.prev()[0].id);
            $trackHtml = $trackHtml.prev();
            changeStateButton()
        }
    });

    $('#playFooterBtn').bind('click', function() {
        player.resume();
        changeStateButton()
    });

    $('#pauseFooterBtn').bind('click', function() {
        player.pause();
        changeStateButton()
    });
});
