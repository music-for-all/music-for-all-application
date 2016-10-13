<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<#import "macros/popup_macros.ftl" as p>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<link href="<@spring.url "/resources/css/userpage.css" />" rel="stylesheet"/>
<script src="<@spring.url "/resources/js/social.js" />"></script>
<script src="<@spring.url "/resources/js/playlist.js" />"></script>
<script src="<@spring.url "/resources/js/track.js" />"></script>
<script src="<@spring.url "/resources/js/history.js" />"></script>

<script src="<@spring.url "/resources/js/chunksplayer.js" />"></script>
<script src="<@spring.url "/resources/js/player.js" />"></script>
<link href="<@spring.url "/resources/css/additionalTracksTable.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/player.css" />" rel="stylesheet">

<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
</@m.head>
<@m.body>
    <@m.navigation m.pages.Profile/>
<div id="popupBlock">
    <@p.followersPopup "followersPopup"></@p.followersPopup>
       <@p.followingPopup "followingPopup"></@p.followingPopup>
     <@p.playlistsPopup "playlistsModal"/>
</div>
<div id="container" class="container">
    <div class="row">
        <div class="col-md-2 well">
            <div id="userHeader" class="profile-info">
            </div>
            <div class="btn-group">
                <button class="btn btn-success" type="button" data-toggle="modal" data-target="#followersPopup">
                    <@spring.message "userpage.Followers"/>
                </button>
                <button class="btn btn-success" type="button" data-toggle="modal" data-target="#followingPopup">
                    <@spring.message "userpage.Following"/>
                </button>
            </div>

            <div class="profile-playlists">
                <hr>
                <@m.playlistRowTemplateWithoutDeleting ></@m.playlistRowTemplateWithoutDeleting>
            </div>
        </div>
        <div class="col-md-9">
            <div class="tracks">
                <section id="tracks-section" class="well col-md-9">
                    <table id="tracks"
                           class="table table-hover table-striped table-condensed no-checkbox tracks-table no-delete-button">
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
            </div>
        </div>
    </div>
</div>
<script type="text/template" class="userHeader">
    <img class="img-responsive img-rounded" src="<%= data.picture %>">
    <div class="profile-info">
        <div class="caption">
            <h3><%= data.firstName %> <%= data.lastName %></h3>
        </div>
        <div class="profile-usertitle-job">
            <p><%= data.username %></p>
        </div>
    </div>
</script>

    <@p.player_Footer/>
    <@m.addTrackRowTemplate/>
    <@m.playlistRowTemplate ></@m.playlistRowTemplate>

<script type="text/javascript">

    var social = new Social();
    var playlist = new Playlist();
    var track = new Track();

    _.templateSettings.variable = "data";

    var trackRow = _.template(
            $("script.addTrackRowTemplate").html()
    );

    var userHeader = _.template(
            $("script.userHeader").html()
    );

    var followersPopup = _.template(
            $("script.followersPopup").html()
    );

    var followingPopup = _.template(
            $("script.followingPopup").html()
    );

    var playlistRowTemplate = _.template(
            $("script.playlistRowTemplateWithoutDeleting").html()
    );

    var playlistRow = _.template(
            $("script.playlistRowTemplate").html()
    );

    $(".profile-playlists").on("click", "a", function (e) {
        e.preventDefault();
        $(".profile-playlists").find("li").removeClass("active");
        $(this).closest("li").addClass("active");

        refreshTrackTable();
    });

    $("#add-many").on("click", function (e) {
        var tracksIds = getSelectedTracksIds();
        if (tracksIds.length > 0) {
            showPlaylistPopup(tracksIds);
        }
    });

    $("#tracks-section").on("click", ".add-song-button", function () {
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

    $("#createPlaylistBtn").on("click", function (e) {
        playlist.create($("#inputNameAddPlaylist").val())
                .then(function (playlist) {
                    $("#inputNameAddPlaylist").val("");
                    var rows = $("#playlistsModal").find("#addToPlaylist");
                    rows.append(playlistRow(playlist));
                });
    });

    $("#acceptCreatingPlaylistButton").on("click", function (e) {
        playlist.create($("#inputNamePlaylist").val())
                .then(function (playlist) {
                    addPlaylist(playlist);
                    $("#inputNamePlaylist").val("");
                    $("#addPlaylistModal").modal("hide");
                });
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

    function updateLikeCount(id) {
        track.getLikeCount(id)
                .then(function (likeCount) {
                    $("#tracks #" + id + " .num-likes").text(likeCount);
                    $("#recommendations #" + id + " .num-likes").text(likeCount);
                });
    }

    function addPlaylists(playlists) {
        var rows = $("#playlistsModal").find("#addToPlaylist");
        playlists.forEach(function (playlist) {
            rows.append(playlistRow(playlist));
        });
    }

    function refreshTrackTable() {
        clearTracks();
        var id = $(".profile-playlists").find("li.active").attr("id");
        playlist.get(id)
                .then(function (response) {
                    response.tracks.forEach(function (track) {
                        $("#tracks").append(trackRow(track));
                        updateLikeCount(track.id);
                    });
                });
    }

    function clearTracks() {
        $("#tracks tr:gt(0)").remove();
    }

    function showPlaylistPopup(tracksIds) {
        if (tracksIds.length <= 0)return;
        $("#playlistsModal").modal("show");
        $("#playlistsModal").off("click", "li").on("click", "li", function (e) {
            var playlistId = $(this).attr("id");

            playlist.addTracks(playlistId, tracksIds)
                    .then(function () {
                        $("#playlistsModal").modal("hide");
                    })
        });
    }

    $(document).ready(function () {
        social.getUser(${user_id}).then(function (req_user) {
            $("#userHeader").append(
                    userHeader(req_user)
            );
        });

        social.getUserFollowers(${user_id}).then(function (ufollowers) {
            $("#popupBlock").append(
                    followersPopup(ufollowers)
            );
        });

        social.getUserFollowing(${user_id}).then(function (ufollowing) {
            $("#popupBlock").append(
                    followingPopup(ufollowing)
            );
        });

        social.getUserPlaylists(${user_id}).then(function (uplaylists) {
            $(".profile-playlists").append(
                    playlistRowTemplate(uplaylists)
            );
            $(".profile-playlists li:first").addClass("active");
            $(".profile-playlists li:first a").trigger("click");
        });

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
    });
</script>
</@m.body>
</html>