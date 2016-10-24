<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as p>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<!--suppress JSUnresolvedFunction -->
<html lang="en">
<@m.head>
<title><@spring.message "mainpage.Title"/></title>
<script src="<@spring.url "/resources/js/following.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="<@spring.url "/resources/js/playlist.js" />"></script>
<script src="<@spring.url "/resources/js/chunksplayer.js" />"></script>
<script src="<@spring.url "/resources/js/track.js" />"></script>
<script src="<@spring.url "/resources/js/player.js" />"></script>
<script src="<@spring.url "/resources/js/main.js" />"></script>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<link href="<@spring.url "/resources/css/notification.css" />" rel="stylesheet"/>
<link href="<@spring.url "/resources/css/mainpage.css" />" rel="stylesheet"/>
<link href="<@spring.url "/resources/css/switch.css" />" rel="stylesheet"/>
<link href="<@spring.url "/resources/css/player.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/additionalTracksTable.css" />" rel="stylesheet">
</@m.head>

<@m.body>
    <@p.popUpAdd "addPlaylistModal"/>
    <@p.popUpDelete "deletePlaylistModal"/>
    <@p.playlistsPopup "playlistsModal"/>


    <@m.navigation m.pages.Main/>

<div class="container">
    <div class="col-md-10">
        <section id="tracks-section" class="well col-md-11">
            <table id="tracks"
                   class="table table-hover table-striped table-condensed tracks-table no-checkbox no-plus-button">
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

        <section id="recommendations-section" class="well col-md-11 col-md-offset-0">
            <h4><@spring.message "mainpage.YouMightAlsoLike"/></h4>
            <label class="switch pull-left" id="change-multiselect-state">
                <input type="checkbox">

                <div class="slider round"></div>
            </label>
            <button type="button" id="add-many" class="btn btn-md btn-success hidden"
                    title="<@spring.message "label.AddToPlaylist"/>">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            </button>

            <table id="recommendations"
                   class="table table-hover table-striped table-condensed no-checkbox tracks-table no-delete-button">
                <thead>
                <tr>
                    <th><@spring.message "songTable.Actions"/></th>
                    <th><@spring.message "songTable.Artist"/></th>
                    <th><@spring.message "songTable.Title"/></th>
                    <th><@spring.message "songTable.Duration"/></th>
                </tr>
                </thead>
            </table>
        </section>
    </div>
    <div class="row">
        <section id="playlists-section" class="well col-md-2">
            <button id="createPlaylistButton" class="btn  btn-success btn-block " type="button">
                <@spring.message "mainpage.CreatePlaylist"/></button>
            <ul id="playlists" class="nav nav-pills nav-stacked"></ul>
        </section>
    </div>
</div>
    <@p.player_Footer/>

<script type="text/template" class="playlistButtonTemplate">
    <li id="<%= data.id %>" class="playlists" title="<%= data.name %>">
        <div class="input-group">
            <a type="button" class="btn btn-default btn-block playlist" data-value="<%= data.name %>">
                <%= data.name %>
            </a>

            <div class="input-group-btn">
                <button type="button" id="removePlaylistButton" class="btn btn-danger"
                        onclick="deletePlaylist(this)">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </button>
            </div>
        </div>
    </li>
</script>

    <@m.addTrackRowTemplate/>

    <@m.playlistRowTemplate/>

<script type="text/javascript">
    "use strict";
    var playlist = new Playlist();
    var track = new Track();

    _.templateSettings.variable = "data";
    var trackRow = _.template(
            $("script.addTrackRowTemplate").html()
    );

    var playlistButton = _.template(
            $("script.playlistButtonTemplate").html()
    );

    var playlistRow = _.template(
            $("script.playlistRowTemplate").html()
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


    $("#createPlaylistButton").on("click", function (e) {
        refreshTrackTable();
    });

    $("#playlistsModal").on("shown.bs.modal", function () {
        playlist.all().then(function (playlists) {
            var rows = $("#playlistsModal").find("#addToPlaylist");
            rows.find("li").remove();
            addPlaylists(playlists);
        });
    });

    $("#playlistsModal").on("click", "li", function (e) {
        var tracksIds = getSelectedTracksIds();
        var playlistId = $(this).attr("id");

        playlist.addTracks(playlistId, tracksIds)
                .then(function () {
                    $("#playlistsModal").modal("hide");
                })
    });

    $("#createPlaylistBtn").on("click", function (e) {
        playlist.create($("#inputNameAddPlaylist").val())
                .then(function (playlist) {
                    $("#inputNameAddPlaylist").val("");
                    var rows = $("#playlistsModal").find("#addToPlaylist");
                    rows.append(playlistRow(playlist));
                });
    });

    function getSelectedTracksIds() {
        return $("#recommendations").find("td").filter(function (row) {
            return $(this).find("input:checkbox").is(":checked");
        }).map(function (row) {
            return $(this).closest("tr").attr("id");
        }).toArray();
    }

    function addPlaylists(playlists) {
        var rows = $("#playlistsModal").find("#addToPlaylist");
        playlists.forEach(function (playlist) {
            rows.append(playlistRow(playlist));
        });
    }

    function showPlaylistPopup(tracksIds) {
        if (tracksIds.length <= 0)return;
        $("#playlistsModal").modal("show");
        $("#playlistsModal").off("click", "li").on("click", "li", function (e) {
            var playlistId = $(this).attr("id");

            playlist.addTracks(playlistId, tracksIds)
                    .then(function () {
                        $("#playlistsModal").modal("hide");
                        refreshRecommendationTable();
                        refreshPlaylistTable();
                        refreshTrackTable();
                    })
        });
    }

    $("#add-many").on("click", function (e) {
        var tracksIds = getSelectedTracksIds();
        if (tracksIds.length > 0) {
            showPlaylistPopup(tracksIds);
        }
    });

    $("#recommendations").on("click", ".add-song-button", function () {
        var trackId = $(this).closest("tr").attr("id");
        showPlaylistPopup([trackId]);
    });

    $("#change-multiselect-state").on("click", "input", function (e) {
        var tracks = $("#recommendations");
        tracks.removeClass("no-checkbox");
        tracks.removeClass("no-plus-button");
        if (this.checked) {
            tracks.addClass("no-plus-button");
            $("#add-many").removeClass("hidden");
        } else {
            tracks.addClass("no-checkbox");
            $("#add-many").addClass("hidden");
        }
    });


    $("#createPlaylistButton").on("click", function (e) {
        $("#addPlaylistModal").modal("show");
    });

    $("#acceptCreatingPlaylistButton").on("click", function (e) {
        playlist.create($("#inputNamePlaylist").val())
                .then(function (playlist) {
                    addPlaylist(playlist);
                    $("#inputNamePlaylist").val("");
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
        $("#playlists").empty();
    }

    function clearRecommendations() {
        $("#recommendations tr:gt(0)").remove();
    }

    function addPlaylist(playlist) {
        $("#playlists").append(
                playlistButton(playlist)
        );
    }

    function addRecommendedTrack(track) {
        $("#recommendations").append(trackRow(track));
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
    function refreshPlaylistTable() {
        clearPlaylists();
        /* Fetch all playlists of the current user, and populate the list of playlists with them. */
        playlist.all()
                .then(function (playlists) {
                    $.each(playlists, function () {
                        addPlaylist(this);
                    });
                })
                .done(function () {
                    /* For testing purpose, select the first playlist (named 'Hype'). */
                    $("#playlists li:first").addClass("active");
                    $("#playlists li:first a").trigger("click");
                });
    }

    $(document).ready(function () {

        refreshPlaylistTable();

        refreshRecommendationTable();

        /* Handle the Like button (Ajax). */
        $(".tracks-table").on("click", ".like-button", function () {

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