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
/* end $(document).ready() */

/**
 * Performs an Ajax-based search; populates the results table with found tracks.
 */
function search() {

    const MAX_QUERY = 16;
    const MIN_QUERY = 2;
    var query = $("#query").val();
    /* Do not proceed with queries which are empty or one-character long. */
    if (query.length < MIN_QUERY) {
        return;
    } else if (query.length > MAX_QUERY) {
        /* Truncate the query string to the maximum allowed size. */
        query = query.substr(0, MAX_QUERY);
    }

    var category = $("select[name=category] option:selected").val();
    console.log("Search query: " + query);
    console.log("Category: " + category);

    /* Empty the previous search results, if any. */
    $("#results tr:gt(1)").remove();

    $.ajax({
        type: "GET",
        url: "/api/search",
        data: {
            query: query,
            category: category
        },
        dataType: "json"

    }).fail(function (xhr, status, errorThrown) {

        $("#status-message").text("Service is currently unavailable. Please try again.");
        console.log(status + ": " + xhr.status + " " + errorThrown);


    }).done(function (results) {

        console.log(results);
        $("#status-message").text("Found: " + results.length);
        populateResultsTable(results);
    });
}

/**
 * Inserts the search results into the results table.
 * @param items the items to be inserted
 */
function populateResultsTable(items) {

    $.each(items, function (i, item) {
        /* Create a new row from the template row, and populate it with the current item. */
        var $row = $("#row-template").clone().removeAttr("id").show();
        $row.find("td:eq(0) button:eq(0)").on("click", function() {playPreview(item.id)});
        $row.find("td:eq(0) button:eq(1)").on("click", function() {addToPlaylist(item.id)});
        $row.find("td:eq(1)").text(item.name);
        $row.find("td:eq(2)").text(item.id);
        $row.find("td:eq(3)").text(item.location);
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