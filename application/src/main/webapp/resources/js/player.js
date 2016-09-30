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
        $(".tracks-table tr").removeClass("playing").removeClass("active");
        $(currentRow).closest("tr").addClass("playing").addClass("active");
    }

    $(".tracks-table").on("click", ".play-button", function () {
        var $trackHtml = $(this).closest("tr");
        if ($('tr.active') != $trackHtml) {
            changeActiveTrackRow($trackHtml);
        } else {
            player.resume();
            $(this).closest("tr").addClass("playing");
        }
    });

    $(".tracks-table").on("click", ".pause-button", function () {
        player.pause();
        $(this).closest("tr").removeClass("playing");
    });

    $('#nextFooterBtn').bind('click', function() {
        if ($("tr.active").next().attr("id")) {
            changeActiveTrackRow($("tr.active").next());
        }
    });

    $('#prevFooterBtn').bind('click', function() {
        if ($("tr.active").prev().attr("id")) {
            changeActiveTrackRow($("tr.active").prev());
        }
    });

    $('#playFooterBtn').bind('click', function() {
        player.resume();
        $("tr.active").addClass("playing");
    });

    $('#pauseFooterBtn').bind('click', function() {
        player.pause();
        $("tr.active").removeClass("playing");
    });
});
