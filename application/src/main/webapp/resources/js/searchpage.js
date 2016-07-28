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

    $("#tags").select2({
        ajax: {
            url: "/api/search/tags",
            delay: 250,
            data: function (params) {
                return {
                    tagName: params.term
                };
            },
            processResults: function (data, params) {
                return {
                    results: data.map(function (item) {
                        return item.name;
                    })
                };
            }
        },
        allowClear: true,
        multiple: true,
        placeholder: "Click here and start typing to search.",
        escapeMarkup: function (markup) {
            return markup;
        },
        minimumInputLength: 2,
        templateResult: function (data) {
            return data.text;
        }
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

    /* If a field is empty or too short, ignore it. */
    const MIN_QUERY = 2;

    if (title.length < MIN_QUERY) {
        title = null;
    }
    if (artist.length < MIN_QUERY) {
        artist = null;
    }
    if (album.length < MIN_QUERY) {
        album = null;
    }
    if (tags.length < MIN_QUERY) {
        tags = null;
    }

    /* If all the fields are empty, do not proceed with search. */
    if (!title && !artist && !album && !tags) {
        return;
    }

    /* Truncate the query string to the maximum allowed size. */
    const MAX_QUERY = 16;

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

    /* If tags are provided, make them a string with comma-separated values. */
    if (tags) {
        tags = tags.split(/[\s,;]+/).join(",");
    }

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
/* End of search() */

/**
 * Inserts the search results into the results table.
 * @param items the items to be inserted
 */
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