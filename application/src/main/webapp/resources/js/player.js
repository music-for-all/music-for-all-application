"use strict";


jQuery(document).ready(function () {

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
        var $trackHtml = $(this).closest("tr");
        if (player.getTrackId() != $trackHtml.attr("id")) {
            changeActiveTrackRow($trackHtml);
        } else {
            player.resume();
            $trackHtml.addClass("active");
        }
    });

    $(".tracks-table").on("click", ".pause-button", function () {
        player.pause();
        $(this).closest("tr").removeClass("active");
    });

    $('#nextFooterBtn').bind('click', function() {
        var rowTrackId  = '#' + player.getTrackId();
        if ($(rowTrackId).next().attr("id")) {
            changeActiveTrackRow($(rowTrackId).next());
        }
    });

    $('#prevFooterBtn').bind('click', function() {
        var rowTrackId = '#' + player.getTrackId();
        if ($(rowTrackId).prev().attr("id")) {
            changeActiveTrackRow($(rowTrackId).prev());
        }
    });

    $('#playFooterBtn').bind('click', function() {
        player.resume();
        $('#' + player.getTrackId()).addClass("active");
    });

    $('#pauseFooterBtn').bind('click', function() {
        player.pause();
        $('#' + player.getTrackId()).removeClass("active");
    });
});
