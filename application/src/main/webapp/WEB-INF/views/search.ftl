<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as popup>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "searchpage.Title"/></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="<@spring.url "/resources/js/player.js"/>"></script>
<script src="<@spring.url "/resources/js/track.js"/>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.full.min.js"></script>
<script src="<@spring.url "/resources/js/searchpage.js"/>"></script>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<script src="<@spring.url "/resources/js/autocompleteConfig.js"/>"></script>
<script src="<@spring.url "/resources/js/playlist.js"/>"></script>
<link href="<@spring.url "/resources/css/searchpage.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/switch.css" />" rel="stylesheet">
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

    <span id="status-message" class="well"></span>

    <div id="tracks-results" class="well ">
        <label class="switch" id="change-multiselect-state">
            <input type="checkbox">

            <div class="slider round"></div>
        </label>
        <button type="button" id="add-many" class="btn btn-md btn-success hidden"
                title="<@spring.message "label.AddToPlaylist"/>">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>

        <table id="tracks" class="table table-hover table-striped table-condensed ">
            <thead>
            <tr>
                <th><@spring.message "songTable.Actions"/></th>
                <th><@spring.message "songTable.Artist"/></th>
                <th><@spring.message "songTable.Title"/></th>
                <th><@spring.message "songTable.Duration"/></th>
            </tr>
            </thead>

            <tr id="row-template" style="display: none">
                <td>
                    <button type="button" class="btn btn-xs btn-success">
                        <span class="glyphicon glyphicon-play" aria-hidden="true"></span>
                    </button>
                    <button type="button" class="btn btn-xs btn-success add-button">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    </button>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox"/>
                        </label>
                    </div>
                </td>
                <td><@spring.message "songTable.Artist"/></td>
                <td><@spring.message "songTable.Title"/></td>
                <td><@spring.message "songTable.Album"/></td>
                <td><@spring.message "songTable.Tags"/></td>
            </tr>

        </table>
    </div>
    <a id="scroll-to-top" href="#top" title="Scroll to top"><@spring.message "searchpage.ScrollToTop"/></a>
</div>
<script type="text/template" class="playlistRowTemplate">
    <li id="<%= data.id %>" title="<%= data.name %>">
        <div class="input-group">
            <a type="button" class="btn btn-default btn-block" data-value="<%= data.name %>">
                <%= data.name %>
            </a>
        </div>
    </li>
</script>

<script type="text/template" class="tagBtnTemplate">
    <% _.each(data, function(tag){ %>
    <label id="<%= tag.name %>" class="btn btn-success" name="tag" onclick="getTracksByTag(this.id)">
        <input type="radio">
        <%= tag.name %>
    </label>
    <% }); %>
</script>

<script type="text/template" class="trackRowTemplate">
    <tbody>
    <% _.each(data, function(track){ %>
    <tr id="<%= track.id %>">
        <td>
            <button type="button" class="btn btn-xs btn-success play-track-button">
                <span class='glyphicon glyphicon-play' aria-hidden='true'></span>
            </button>
            <button type="button" class="btn btn-xs btn-warning pause-track-button">
                <span class="glyphicon glyphicon-pause" aria-hidden="true"></span>
            </button>
            <button type="button" class="btn btn-xs btn-success add-song-button">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            </button>
            <button class="btn btn-xs btn-primary like-button"><@spring.message "mainpage.Like" /></button>
            <span class="glyphicon num-likes" aria-hidden="true"></span>
        </td>
        <td>
            <%= track.name %>
        </td>
        <td>
            <%= track.artist %>
        </td>
        <td>
            <audio id="audio_<%= track.id %>" controls preload="none">
                <source type="audio/mp3" src="<@spring.url "/files/<%= track.id %>/0"/>">
            </audio>
        </td>
    </tr>
    <% }); %>
    </tbody>
</script>
<script type="text/javascript">
    _.templateSettings.variable = "data";
    var trackTable = _.template(
            $("script.trackRowTemplate").html()
    );

    var playlistRow = _.template(
            $("script.playlistRowTemplate").html()
    );

    var setOfTags = _.template(
            $("script.tagBtnTemplate").html()
    );

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
        $("#tracks").find("thead").after(
                trackTable(tracks)
        );
        tracks.forEach(function (track) {
            updateLikeCount(track.id);
        });
    }


    $("#search-form").on("submit", function () {
        search().then(function (tracks) {
            $("#status-message").text("Found: " + tracks.length);
            buildTrackTable(tracks);
        });
        /* Prevent default */
        return false;
    });

    $("#tracks").on("click", ".add-song-button", function () {
        console.log("add track");
    });
    function getPopularTracks() {
        $('#status-message').text('Popular songs');
        popularTracks().then(function (response) {
            buildTrackTable(response)
        });
    }

    function getTracksByTag(tag) {
        $('#status-message').text('Top songs for tag "' + tag + '":');
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
                .then(function(likeCount) {
                    $("#tracks #" + id + " .num-likes").text(likeCount);
                });
    }


    $(document).ready(function () {

        $("#tracks").on("click", ".like-button", function () {

            /* The id of a track is stored in the containing <tr> element. */
            var id = $(this).closest("tr").attr("id");
            track.like(id)
                    .then(function() {
                        $("#" + id + " .like-button").css("opacity", "0.5");
                        updateLikeCount(id);
                    });
        });

        getPopularTracks();
        getPopularTags();
    });
    
    var playlist = new Playlist();

    $("#change-multiselect-state").on("click", "input", function (e) {
        var results = $("#results");
        results.removeClass("no-checkbox");
        results.removeClass("no-plus-button");
        if (this.checked) {
            results.addClass("no-plus-button");
            $("#add-many").removeClass("hidden");
        } else {
            results.addClass("no-checkbox");
            $("#add-many").addClass("hidden");
        }
    });

    $("#playlistsModal").on("shown.bs.modal", function () {
        playlist.all().then(function (playlists) {
            var rows = $("#playlistsModal").find("#playlists");
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

    $("#createPlaylistButton").on("click", function (e) {

        playlist.create($("#inputNamePlaylist").val())
                .then(function (playlist) {
                    $("#inputNamePlaylist").val("");
                    var rows = $("#playlistsModal").find("#playlists");
                    rows.append(playlistRow(playlist));
                });
    });

    function getSelectedTracksIds() {
        return $("#results").find("td").filter(function (row) {
            return $(this).find("input:checkbox").is(":checked");
        }).map(function (row) {
            return $(this).closest("tr").attr("id");
        }).toArray();
    }

    function addPlaylists(playlists) {
        var rows = $("#playlistsModal").find("#playlists");
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