"use strict";

jQuery(document).ready(function () {

    /* Handle the scroll-to-top link. */
    $("#scroll-to-top").on("click", function (event) {
        window.scrollTo(0, 0);
        return false;
    });
});

var baseUrl = dict.contextPath;

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

    if (tag === "popular") {
        tag = null;
    }
    /* Truncate the query string to the maximum allowed size. */
    const MAX_QUERY = 16;

    if (artist && artist.length > MAX_QUERY) {
        artist = artist.substr(0, MAX_QUERY);
    }

    var trackData = {
        artist: artist,
        tags: tag
    };

    return $.when($.getJSON(baseUrl + "/api/search", trackData));
}

function popularTags() {
    return $.when($.get(baseUrl + "/tags/popular"));
}

function popularTracks() {
    return $.when($.get(baseUrl + "/tracks/popular"));
}

function getTracks(tag) {
    return $.when($.getJSON(baseUrl + "/api/search", {tags: tag}));
// $.when($.get("/tracks/popular/tag=" + tag.id))
}
