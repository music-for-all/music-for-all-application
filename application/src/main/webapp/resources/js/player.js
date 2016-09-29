"use strict";


jQuery(document).ready(function () {

    var $trackHtml;
    var player = new ChunksPlayer();
    var track = new Track();

    function writeToFoot(tr) {
        $("#nameInFoot").html(tr.name);
        $("#artistInFoot").html(tr.artist);
    }

    function startTrack(trackId) {
            track.get(trackId).then(function (tr) {
                player.play(tr);
                writeToFoot(tr);
            });
    }

    function changeActiveTrackRow(currentRow) {
        startTrack(currentRow[0].id);
        $(".tracks-table tr").removeClass("active");
        $(currentRow).closest("tr").addClass("active");
    }

    $(".tracks-table").on("click", ".play-button", function () {
        $trackHtml = $(this).closest("tr");
        if (player.getTrackId() != $trackHtml[0].id) {
            changeActiveTrackRow($trackHtml);
        } else {
            player.resume();
            $trackHtml.addClass("active");
        }
    });

    $(".tracks-table").on("click", ".pause-button", function () {
        player.pause();
        $(this).removeClass("active");
    });
    
    $('#nextFooterBtn').bind('click', function() {
        if ($trackHtml.next()[0].id) {
            $trackHtml = $trackHtml.next();
            changeActiveTrackRow($trackHtml);
        }
    });

    $('#prevFooterBtn').bind('click', function() {
        if ($trackHtml.prev()[0].id) {
            $trackHtml = $trackHtml.prev();
            changeActiveTrackRow($trackHtml);
        }
    });

    $('#playFooterBtn').bind('click', function() {
        player.resume();
        $trackHtml.addClass("active");
    });

    $('#pauseFooterBtn').bind('click', function() {
        player.pause();
        $trackHtml.removeClass("active");
    });
});
