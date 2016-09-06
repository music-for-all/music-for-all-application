<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as popup>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "searchpage.Title"/></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.full.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="<@spring.url "/resources/js/searchpage.js"/>"></script>
<script src="<@spring.url "/resources/js/playlist.js"/>"></script>
<script src="<@spring.url "/resources/js/history.js"/>"></script>
<script src="<@spring.url "/resources/js/autocompleteConfig.js"/>"></script>
<link href="<@spring.url "/resources/css/searchpage.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/switch.css" />" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet"/>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Search/>

    <@popup.playlistsPopup "playlistsModal"/>

<div class="container">
    <form id="search-form" class="form-inline text-center ">
        <div class="input-group">
            <input id="title" class="form-control" type="text" value=""
                   placeholder="<@spring.message "placeholder.Title"/>"
                   name="title" autofocus="autofocus"/>
            <input id="artist" class="form-control" type="text" value=""
                   placeholder="<@spring.message "placeholder.Artist"/>"
                   name="artist"/>
            <input id="album" class="form-control" type="text" value=""
                   placeholder="<@spring.message "placeholder.Album"/>"
                   name="album"/>
            <select class="form-control" id="tags" placeholder="<@spring.message "placeholder.Album"/>"></select>

            <div class="input-group-btn">
                <input id="searchButton" data-style="slide-left" class="btn btn-success "
                       type="submit" value="<@spring.message "searchpage.SubmitButton"/>"/>
            </div>
        </div>
    </form>

    <span id="status-message" class="well"></span>

    <div id="resultsd" class="well">
        <label class="switch" id="change-multiselect-state">
            <input type="checkbox">

            <div class="slider round"></div>
        </label>
        <button type="button" id="add-many" class="btn btn-md btn-success hidden"
                title="<@spring.message "label.AddToPlaylist"/>">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
        </button>

        <table id="results" class="table table-hover table-striped table-condensed no-checkbox">
            <thead>
            <tr>
                <th></th>
                <th><@spring.message "songTable.Artist"/></th>
                <th><@spring.message "songTable.Title"/></th>
                <th><@spring.message "songTable.Album"/></th>
                <th><@spring.message "songTable.Tags"/></th>
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
<script type="text/javascript">
    var placeholder = "<@spring.message "placeholder.Tags"/>";
    $("#tags").select2(tagAutocomplete(placeholder));
    var playlist = new Playlist();

    _.templateSettings.variable = "data";
    var playlistRow = _.template(
            $("script.playlistRowTemplate").html()
    );

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