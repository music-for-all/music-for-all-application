"use strict";

jQuery(document).ready(function () {

    /* Handle the search form (Ajax). */
    $("#search-form").on("submit", function (e) {

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
/* end $(document).ready() */

/**
 * Performs an Ajax-based search; populates the results table with found tracks.
 */
function search() {

    var title = $("#title").val();
    var artist = $("#artist").val();
    var album = $("#album").val();
    var tags = $("#tags").val();

    /* Do not proceed with queries which are empty or one-character long. */
    const MIN_QUERY = 2;
    const MAX_QUERY = 16;

    if (title == "" || title.length < MIN_QUERY) {
        title = null;
    }
    if (artist == "" || title.length < MIN_QUERY) {
        artist = null;
    }
    if (album == "" || title.length < MIN_QUERY) {
        album = null;
    }
    if (tags == "" || title.length < MIN_QUERY) {
        tags = null;
    }

    /* If all the fields are empty or too short, do not proceed with search. */
    if (!title && !artist && !album && !tags)
        return;
    
    /* Truncate the query string to the maximum allowed size. */
    if (title && title.length > MAX_QUERY) {
        title = title.substr(0, MAX_QUERY);
    }
    if (artist && artist.length > MAX_QUERY) {
        artist = artist.substr(0, MAX_QUERY);
    }
    if (album && album.length > MAX_QUERY) {
        album = album.substr(0, MAX_QUERY);
    }
    if (tags && tags.length > MAX_QUERY) {
        tags = tags.substr(0, MAX_QUERY);
    }

    /* If tags are provided, create an array of tags. */
    tags = !tags ? null : tags.split(/[\s,;]+/);

    var trackData = {
        title: title,
        artist: artist,
        album: album,
        tags: tags
    };
    console.log(trackData);

    /* Empty the previous search results, if any. */
    $("#results tr:gt(1)").remove();

    $.ajax({
        type: "POST",
        url: "/api/search",
        contentType: "application/json",
        data: JSON.stringify(trackData),
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
}   /* End of search() */

/**
 * Inserts the search results into the results table.
 * @param items the items to be inserted
 */
function populateResultsTable(items) {

    $.each(items, function (i, item) {
        /* Create a new row from the template row, and populate it with the current item. */
        var $row = $("#row-template").clone().removeAttr("id").show();

        var tagsArray = [];
        $(item.tags).each(function(i, tag) { tagsArray.push(tag.name) });

        $row.find("td:eq(0) button:eq(0)").on("click", function() {playPreview(item.id)});
        $row.find("td:eq(0) button:eq(1)").on("click", function() {addToPlaylist(item.id)});
        $row.find("td:eq(1)").text(item.artist);
        $row.find("td:eq(2)").text(item.title);
        $row.find("td:eq(3)").text(item.album);
        $row.find("td:eq(4)").text(tagsArray.join(" "));
        $("#results").append($row);
    });
}

/**
 * Fetches the track with the given id and plays it.
 * @param id the id of the track
 */
function playPreview(id) {
    console.log("Play: " + id);
}

/**
 * Adds the track with the given id to the current playlist.
 * @param id the id of the track
 */
function addToPlaylist(id) {
    console.log("Add: " + id);
}