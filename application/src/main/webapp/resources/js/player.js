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
        $(".tracks-table tr").removeClass("active").removeClass("current");
        $(currentRow).closest("tr").addClass("active").addClass("current");
    }

    $(".tracks-table").on("click", ".play-button", function () {
        var $trackHtml = $(this).closest("tr");
        if ($('tr.current') != $trackHtml) {
            changeActiveTrackRow($trackHtml);
        } else {
            player.resume();
            $(this).closest("tr").addClass("active");
        }
    });

    $(".tracks-table").on("click", ".pause-button", function () {
        player.pause();
        $(this).closest("tr").removeClass("active");
    });

    $('#nextFooterBtn').bind('click', function() {
        if ($("tr.current").next().attr("id")) {
            changeActiveTrackRow($("tr.current").next());
        }
    });

    $('#prevFooterBtn').bind('click', function() {
        if ($("tr.current").prev().attr("id")) {
            changeActiveTrackRow($("tr.current").prev());
        }
    });

    $('#playFooterBtn').bind('click', function() {
        player.resume();
        $("tr.current").addClass("active");
    });

    $('#pauseFooterBtn').bind('click', function() {
        player.pause();
        $("tr.current").removeClass("active");
    });
});
