<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as p>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "mainpage.Title"/></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="<@spring.url "/resources/js/playlist.js" />"></script>
<script src="<@spring.url "/resources/js/track.js" />"></script>
<script src="<@spring.url "/resources/js/player.js" />"></script>
<script src="<@spring.url "/resources/js/main.js" />"></script>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<link href="<@spring.url "/resources/css/mainpage.css" />" rel="stylesheet"/>
<link href="<@spring.url "/resources/css/switch.css" />" rel="stylesheet"/>
</@m.head>

<@m.body>
    <@p.popUpAdd "addPlaylistModal"/>
    <@p.popUpDelete "deletePlaylistModal"/>

    <@m.navigation m.pages.Main/>

<div class="container">

    <section id="tracks-section" class="well col-md-9 ">
        <table id="tracks" class="table table-hover table-striped table-condensed ">
            <thead>
            <tr>
                <th><@spring.message "welcomepage.Actions"/></th>
                <th><@spring.message "welcomepage.Artist"/></th>
                <th><@spring.message "welcomepage.Title"/></th>
                <th><@spring.message "welcomepage.Duration"/></th>
            </tr>
            </thead>
        </table>
    </section>

    <a class="btn btn-success" href="<@spring.url '${m.pages.Add.url}' />" title="Upload">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
    </a>

    <section id="playlists-section" class="well  col-md-2 col-md-offset-1  ">
        <button id="createPlaylistButton" class="btn  btn-success btn-block " type="button">
            <@spring.message "mainpage.CreatePlaylist"/></button>
        <ul id="playlists" class="nav nav-pills nav-stacked"></ul>
    </section>

    <section id="recommendations-section" class="well  col-md-9 col-md-offset-0">
        <h4><@spring.message "mainpage.YouMightAlsoLike"/></h4>
        <label class="switch" id="change-multiselect-state">
            <input type="checkbox">

            <div class="slider round"></div>
        </label>
        <button type="button" id="add-many" class="btn btn-md btn-success"
                title="<@spring.message "label.AddToPlaylist"/>">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>
        <ul id="recommendations" class="nav nav-pills nav-stacked no-checkbox">
        </ul>
    </section>
</div>
<!-- end .container -->

<script type="text/template" class="trackRowTemplate">
    <tr id="<%= data.id %>">
        <td>
            <button type="button" class="btn btn-xs btn-success play-track-button">
                <span class='glyphicon glyphicon-play' aria-hidden='true'></span>
            </button>
            <button type="button" class="btn btn-xs btn-warning pause-track-button">
                <span class="glyphicon glyphicon-pause" aria-hidden="true"></span>
            </button>
            <button type="button" class="btn btn-xs btn-danger delete-song-button">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
            </button>
            <button class="btn btn-xs btn-primary like-button"><@spring.message "mainpage.Like" /></button>
            <span class="glyphicon num-likes" aria-hidden="true"></span>
        </td>
        <td>
            <%= data.name %>
        </td>
        <td>
            <%= data.artist %>
        </td>
        <td>
            <audio id="audio_<%= data.id %>" controls preload="none">
                <source type="audio/mp3" src="<@spring.url "/files/<%= data.id %>/0"/>">
            </audio>
        </td>
    </tr>
</script>
<script type="text/template" class="playlistRowTemplate">
    <li id="<%= data.id %>" title="<%= data.name %>">
        <div class="input-group">
            <a type="button" class="btn btn-default btn-block" data-value="<%= data.name %>">
                <%= data.name %>
            </a>

            <div class="input-group-btn">
                <button type="button" id="removePlaylistButton" class="btn btn-danger" onclick="deletePlaylist(this)">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </button>
            </div>
        </div>
    </li>
</script>

<script type="text/template" class="recommendationRowTemplate">
    <li id="<%= data.id %>" title="<%= data.artist %> - <%= data.title %> - <%= data.album %>">
        <div class="checkbox">
            <label>
                <input type="checkbox"/>
            </label>
        </div>
        <button type="button" class="btn btn-xs btn-success add-one">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>
        <div class="input-group">
            <a type="button" class="btn btn-default btn-block" data-value="<%= data.name %>">
                <%= data.name %>
            </a>
            <span class="col-sm-2 form-control"><%= data.artist %></span>
            <span class="col-sm-2 form-control"><%= data.title %></span>
            <span class="col-sm-2 form-control">Liked: <span class="glyphicon num-likes"
                                                             aria-hidden="true"></span></span>
        </div>
    </li>
</script>

<script type="text/javascript">
    "use strict";
    var playlist = new Playlist();
    var track = new Track();

    _.templateSettings.variable = "data";
    var trackRow = _.template(
            $("script.trackRowTemplate").html()
    );

    var playlistRow = _.template(
            $("script.playlistRowTemplate").html()
    );

    var recommendationRow = _.template(
            $("script.recommendationRowTemplate").html()
    );

    /*
     * When a playlist is clicked, mark it active, then fetch tracks of the playlist,
     * then populate the tracks table.
     */
    $("#playlists").on("click", "a", function (e) {
        e.preventDefault();
        $("#playlists").find("li").removeClass("active");
        $(this).closest("li").addClass("active");
        refreshTrackTable();
    });

    $("#add-many").hide();

    $("#add-many").on("click", function (e) {
        var tracksIds = $("#recommendations").find("li").filter(function (row) {
            return $(this).find("input:checkbox").is(":checked");
        }).map(function (row) {
            return $(this).attr("id");
        }).toArray();

        var playlistId = $("#playlists").find("li.active").attr("id");

        playlist.addTracks(playlistId, tracksIds)
                .then(function () {
                    refreshTrackTable();
                    refreshRecommendationTable();
                });
    });

    $("#recommendations").on("click", ".add-one", function (e) {
        var playlistId = $("#playlists").find("li.active").attr("id");
        var trackId = $(this).closest("li").attr("id");

        playlist.addTracks(playlistId, [trackId])
                .then(function () {
                    refreshTrackTable();
                    refreshRecommendationTable();
                });
    });

    $("#change-multiselect-state").on("click", "input", function (e) {
        var recommendations = $("#recommendations");
        recommendations.removeClass("no-checkbox");
        recommendations.removeClass("no-plus-button");
        if (this.checked) {
            recommendations.addClass("no-plus-button");
            $("#add-many").show();
        } else {
            recommendations.addClass("no-checkbox");
            $("#add-many").hide();
        }
    });

    $("#createPlaylistButton").on("click", function (e) {
        $("#addPlaylistModal").modal("show");
    });

    $("#acceptCreatingPlaylistButton").on("click", function (e) {
        playlist.create($("#inputNamePlaylist").val())
                .then(function (playlist) {
                    addPlaylist(playlist);
                    $("#addPlaylistModal").modal("hide");
                });
    });

    $("#tracks").on("click", ".delete-song-button", function (e) {
        var row = $(this).closest("tr");
        var id = row.attr("id");
        var playlistToRemove = $("#playlists").find("li.active");
        playlist.removeTrack(playlistToRemove.attr("id"), id)
                .then(function () {
                    row.remove();
                });
    });

    function deletePlaylist(e) {
        $("#playlists").find("li").removeClass("active");
        $(e).closest("li").addClass("active");
        $("#deletePlaylistModal").modal("show");
    }

    function refreshTrackTable() {
        clearTracks();
        var id = $("#playlists").find("li.active").attr("id");
        playlist.get(id)
                .then(function (response) {
                    response.tracks.forEach(function (track) {
                        $("#tracks").append(trackRow(track));
                        updateLikeCount(track.id);
                    });
                });
    }

    $("#acceptRemovingPlaylistButton").on("click", function (e) {
        var playlistToRemove = $("#playlists").find("li.active");
        playlist.remove(playlistToRemove.attr("id"))
                .then(function () {
                    playlistToRemove.remove();
                    clearTracks();
                });
    });

    function clearTracks() {
        $("#tracks tr:gt(0)").remove();
    }

    function clearPlaylists() {
        $("#playlists").find("li").remove();
    }

    function clearRecommendations() {
        $("#recommendations").find("li").remove();
    }

    function addPlaylist(playlist) {
        $("#playlists").append(
                playlistRow(playlist)
        );
    }

    function addRecommendedTrack(track) {
        $("#recommendations").append(
                recommendationRow(track)
        );
    }

    function updateLikeCount(id) {
        track.getLikeCount(id)
                .then(function (likeCount) {
                    $("#tracks #" + id + " .num-likes").text(likeCount);
                    $("#recommendations #" + id + " .num-likes").text(likeCount);
                });
    }

    function refreshRecommendationTable() {
        clearRecommendations();
        track.getRecommendedTracks()
                .then(function (tracks) {
                    $.each(tracks, function (i, track) {
                        addRecommendedTrack(track);
                        updateLikeCount(track.id);
                    });
                });
    }

    $(document).ready(function () {

        /* Fetch all playlists of the current user, and populate the list of playlists with them. */
        playlist.all()
                .then(function (playlists) {
                    $.each(playlists, function () {
                        addPlaylist(this);
                    });
                })
                .done(function () {
                    /* For testing purpose, select the first playlist (named 'Hype'). */
                    $("#playlists #1 a").trigger("click");
                });

        refreshRecommendationTable();

        /* Handle the Like button (Ajax). */
        $("#tracks").on("click", ".like-button", function () {

            /* The id of a track is stored in the containing <tr> element. */
            var id = $(this).closest("tr").attr("id");
            track.like(id)
                    .then(function () {
                        $("#" + id + " .like-button").css("opacity", "0.5");
                        updateLikeCount(id);
                    });
        });

        /* Set focus on the name input field when the modal window has been shown. */
        $("#addPlaylistModal").on("shown.bs.modal", function () {
            $("#inputNamePlaylist").focus();
        });

        /* Event handler for the 'Return' key. */
        $("#inputNamePlaylist").on("keydown", function (e) {

            if (e.keyCode == 0xD) {
                $("#acceptCreatingPlaylistButton").trigger("click");
            }
        });

        /* Set focus on the name input field when the modal window has been shown. */
        $("#deletePlaylistModal").on("shown.bs.modal", function () {
            $("#acceptRemovingPlaylistButton").focus();
        });
    });
</script>
</@m.body>
</html>