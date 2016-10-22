<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as popup>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "searchpage.Title"/></title>
<script src="<@spring.url "/resources/js/following.js" />"></script>
<script src="<@spring.url "/resources/js/chunksplayer.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="<@spring.url "/resources/js/track.js"/>"></script>
<script src="<@spring.url "/resources/js/playlist.js"/>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.full.min.js"></script>
<script src="<@spring.url "/resources/js/searchpage.js"/>"></script>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<script src="<@spring.url "/resources/js/autocompleteConfig.js"/>"></script>
<script src="<@spring.url "/resources/js/player.js" />"></script>
<link href="<@spring.url "/resources/css/notification.css" />" rel="stylesheet"/>
<link href="<@spring.url "/resources/css/searchpage.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/switch.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/player.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/additionalTracksTable.css" />" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet"/>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Search/>

    <@popup.playlistsPopup "playlistsModal"/>

<div class="container">

    <div class="well well-sm">
        <div id="tags" data-toggle="buttons">
            <label id="popular" name="tag" class="btn btn-success active" onclick="getPopularTracks()">
                <input type="radio">
                popular
            </label>
        </div>
    </div>

    <form id="search-form" class="form-inline text-center ">
        <div class="input-group">
            <input id="artist" class="form-control" type="text" value=""
                   placeholder="<@spring.message "placeholder.Artist"/>"
                   name="artist" autofocus="autofocus"/>

            <div class="input-group-btn">
                <button id="searchButton" data-style="slide-left" class="btn btn-success "
                        type="submit">
                    <i class="fa fa-search"></i>
                </button>
            </div>
        </div>
    </form>

    <span id="status-message" class="well well-sm"></span>

    <div id="tracks-results" class="well">
        <label class="switch pull-left" id="change-multiselect-state">
            <input type="checkbox">

            <div class="slider round"></div>
        </label>
        <button type="button" id="add-many" class="btn btn-md btn-success hidden"
                title="<@spring.message "label.AddToPlaylist"/>">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>

        <table id="tracks"
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
    </div>
    <button id="scroll-to-top" class="btn btn-default" title="Scroll to top">
        <span class="glyphicon glyphicon-arrow-up" aria-hidden="true"></span>
    </button>
</div>

<script type="text/template" class="tagBtnTemplate">
    <% _.each(data, function(tag){ %>
    <label id="<%= tag.name %>" class="btn btn-success" name="tag" onclick="getTracksByTag(this.id)">
        <input type="radio">
        <%= tag.name %>
    </label>
    <% }); %>
</script>

    <@m.addTrackRowTemplate/>

    <@m.playlistRowTemplate/>

    <@popup.player_Footer/>

<script type="text/javascript">
    _.templateSettings.variable = "data";
    var trackTable = _.template(
            $("script.addTrackRowTemplate").html()
    );

    var playlistRow = _.template(
            $("script.playlistRowTemplate").html()
    );

    var setOfTags = _.template(
            $("script.tagBtnTemplate").html()
    );

    var playlist = new Playlist();
    var track = new Track();

    $("input[name=artist]").autocomplete(artistAutocomplete(function () {
        var tag = $('#tags .active').attr('id');
        if (tag === "popular") {
            return null;
        }
        return tag;
    }));

    function buildTrackTable(tracks) {
        $("#tracks tr:gt(0)").remove();
        tracks.forEach(function (track) {
            $("#tracks").append(trackTable(track));
            updateLikeCount(track.id);
        });
    }

    $("#search-form").on("submit", function () {
        search().then(function (tracks) {
            $("#status-message").text('<@spring.message "searchpage.Found"/>' + " :" + tracks.length);
            buildTrackTable(tracks);
        });
        return false;
    });

    $("#tracks").on("click", ".add-song-button", function () {
        var trackId = $(this).closest("tr").attr("id");
        showPlaylistPopup([trackId]);
    });

    function getPopularTracks() {
        $('#status-message').text('<@spring.message "searchpage.TopSongs"/>');
        popularTracks().then(function (response) {
            buildTrackTable(response)
        });
    }

    function getTracksByTag(tag) {
        $('#status-message').text('<@spring.message "searchpage.TopSongsForTag"/> "' + tag + '":');
        getTracks(tag).then(function (tracks) {
            buildTrackTable(tracks);
        });
    }

    function getPopularTags() {
        popularTags().then(function (tags) {
            $("#popular").after(
                    setOfTags(tags)
            );
        });
    }

    function updateLikeCount(id) {
        track.getLikeCount(id)
                .then(function (likeCount) {
                    $("#tracks #" + id + " .num-likes").text(likeCount);
                });
    }

    $(document).ready(function () {
        $("#tracks").on("click", ".like-button", function () {
            var id = $(this).closest("tr").attr("id");
            track.like(id)
                    .then(function () {
                        $("#" + id + " .like-button").css("opacity", "0.5");
                        updateLikeCount(id);
                    });
        });
        getPopularTracks();
        getPopularTags();
        updateNotification();
    });

    $("#change-multiselect-state").on("click", "input", function (e) {
        var tracks = $("#tracks");
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

    $("#add-many").on("click", function (e) {
        var tracksIds = getSelectedTracksIds();
        if (tracksIds.length > 0) {
            showPlaylistPopup(tracksIds);
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

    function getSelectedTracksIds() {
        return $("#tracks").find("td").filter(function (row) {
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
                    })
        });
    }
</script>
</@m.body>
</html>