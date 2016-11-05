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

    var trackData = {};

    var artist = $("#artist").val();
    var tag = $("#topTags .active").attr("id");
    var title = $("#title").val();
// var tags = $("#tags").val();
    /* If all the fields are empty, do not proceed with search. */
    if (!title && !artist && !tag) {
        return;
    }

    trackData.tags = $("#tags").val();

    if (tag !== "popular") {
        trackData.tags.add(tag);
    }

    /* Truncate the query string to the maximum allowed size. */
    const MAX_QUERY = 16;
    if (title && title.length > MAX_QUERY) {
        title = title.substr(0, MAX_QUERY);
    }

    if (artist && artist.length > MAX_QUERY) {
        artist = artist.substr(0, MAX_QUERY);
    }

    /* If a field is empty or too short, ignore it. */
    const MIN_QUERY = 2;

    if (artist.length >= MIN_QUERY) {
        trackData.artistName = artist;
    }

    if (title.length >= MIN_QUERY) {
        trackData.trackName = title;
    }

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
}
