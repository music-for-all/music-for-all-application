"use strict";

jQuery(document).ready(function () {

    /* Handle the search form (Ajax). */
    $("#search-form").on("submit", function () {

        search();
        /* Prevent default */
        return false;
    });

    /* Handle the scroll-to-top link. */
    $("#scroll-to-top").on("click", function (event) {
        window.scrollTo(0, 0);
        return false;
    });
});

function search() {

    var artist = $("#artist").val();
    var tag = $('#tags .active').attr('id');

    /* If a field is empty or too short, ignore it. */
    const MIN_QUERY = 2;

    if (artist.length < MIN_QUERY) {
        artist = null;
    }

    /* If all the fields are empty, do not proceed with search. */
    if (!artist && !tag) {
        return;
    }

    /* Truncate the query string to the maximum allowed size. */
    const MAX_QUERY = 16;

    if (artist && artist.length > MAX_QUERY) {
        artist = artist.substr(0, MAX_QUERY);
    }

    var trackData = {
        artist: artist,
        tags: tags
    };
    console.log(trackData);

    /* Empty the previous search results, if any. */
    $("#results tr:gt(1)").remove();

    $.ajax({
        type: "GET",
        url: "/api/search",
        data: trackData,
        dataType: "json"

    }).fail(function (xhr, status, errorThrown) {
        var message = status + ": " + xhr.status + " " + errorThrown;
        $("#status-message").text(message);
        console.log(message);

    }).done(function (results) {
        console.log(results);
        $("#status-message").text("Found: " + results.length);
        populateResultsTable(results);
    });
}

function populateResultsTable(items) {

    $.each(items, function (i, item) {
        /* Create a new row from the template row, and populate it with the current item. */
        var $row = $("#row-template").clone().removeAttr("id").show();

        var tagsArray = [];
        $(item.tags).each(function (i, tag) {
            tagsArray.push(tag.name);
        });

        $row.find("td:eq(0) button:eq(0)").on("click", function () {
            playPreview(item.id)
        });
        $row.find("td:eq(0) button:eq(1)").on("click", function () {
            addToPlaylist(item.id)
        });
        $row.find("td:eq(1)").text(item.artist);
        $row.find("td:eq(2)").text(item.title);
        $row.find("td:eq(3)").text(item.album);
        $row.find("td:eq(4)").text(tagsArray.join(" "));
        $("#results").append($row);
    });
}


function playPreview(id) {
    console.log("Play: " + id);
}

function addToPlaylist(id) {
    console.log("Add: " + id);
}