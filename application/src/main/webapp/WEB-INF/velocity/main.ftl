<#import "macros/macros.vm" as m>
<#import "macros/popup_macros.vm" as p>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Main</title>
<script src="/resources/js/playlist.js"></script>
<script src="/resources/js/track.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<link href="/resources/css/mainpage.css" rel="stylesheet"/>
<link href="/resources/css/font-awesome.min.css" rel="stylesheet"/>
</@m.head>

<@m.body>
    <@p.popUpAdd "addPlaylistModal"/>
    <@p.popUpDelete "deletePlaylistModal"/>

    <@m.navigation [{"isActive": true, "url": '/main', "title": "Main"},
    {"isActive": false, "url": '/search', "title": "Search"}]/>

<div class="container">
    <div id="resultsd" class="well col-md-9 ">
        <table id="results" class="table table-hover table-striped table-condensed ">
            <thead>
            <tr>
                <th>Actions</th>
                <th>Artist</th>
                <th>Title</th>
                <th>Duration</th>
            </tr>
            </thead>
        </table>
    </div>

    <div class="well  col-md-2 col-md-offset-1  ">
        <button id="createPlaylistButton" class="btn  btn-success btn-block " type="button">Create playlist</button>
        <button id="removePlaylistButton" class="btn  btn-danger btn-block" type="button">Remove selected</button>

        <ul id="playlists" class="nav nav-pills nav-stacked"></ul>

    </div>
</div>
<script type="text/template" class="trackRowTemplate">
    <tbody>
    <% _.each(data, function(track){ %>
    <tr id="<%= track.id %>">
        <td>
            <button type='button' class='btn btn-xs btn-success'>
                <span class='glyphicon glyphicon-play' aria-hidden='true'></span>
            </button>
            <button type='button' class='btn btn-xs btn-danger delete-song-button'>
                <span class='glyphicon glyphicon-remove' aria-hidden='true'></span>
            </button>
        </td>
        <td>
            <%= track.name %>
        </td>
    </tr>
    <% }); %>
    </tbody>
</script>
<script type="text/template" class="playlistRowTemplate">
    <li id="<%= data.id %>" title="<%= data.name %>">
        <a href='#' data-value="<%= data.name %>">
            <%= data.name %>
        </a>
    </li>
</script>
<script type="text/javascript">
    var contextPath = "<@spring.url "" />";
    var playlist = new Playlist(contextPath);
    var track = new Track(contextPath);

    _.templateSettings.variable = "data";
    var trackTable = _.template(
            $("script.trackRowTemplate").html()
    );

    var playlistTable = _.template(
            $("script.playlistRowTemplate").html()
    );


    var playlists = $('#playlists');
    playlists.on('click', 'a', function (e) {
        e.preventDefault();
        playlists.find("li").removeClass("active");
        $(this).closest('li').addClass('active');
        clearTracks();
        playlist.get(playlists.find('li.active').attr('id'))
                .then(function (response) {
                    $("#results").find("thead").after(
                            trackTable(response.tracks)
                    );
                });
    });

    $('#acceptRemovingPlaylistButton').on('click', function (e) {
        var playlistToRemove = $("#playlists").find("li.active");
        playlist.remove(playlistToRemove.attr('id'))
                .then(function () {
                    playlistToRemove.remove();
                    clearTracks();
                });
    });

    $('#acceptCreatingPlaylistButton').on('click', function (e) {
        playlist.create(document.getElementById('inputNamePlaylist').value)
                .then(function (playlist) {
                    addPlaylist(playlist);
                    $('#addPlaylistModal').modal('hide');
                });
    });

    $('#removePlaylistButton').on('click', function (e) {
        $('#deletePlaylistModal').modal('show');
    });

    $('#createPlaylistButton').on('click', function (e) {
        $('#addPlaylistModal').modal('show');
    });

    $("#results").on("click", ".delete-song-button", function (e) {
        var row = $(this).closest("tr");
        var id = row.attr("id");
        track.remove(id).then(function () {
            row.remove();
        });
    });

    function clearTracks() {
        $("#results").find("tr:not(:first)").remove();
    }

    function clearPlaylists() {
        $("#playlists").find("li").remove();
    }

    function addPlaylist(playlist) {
        $("#playlists").append(
                playlistTable(playlist)
        );
    }

    $(document).ready(function () {
        playlist.all()
                .then(function (response) {
                    $.each(response, function () {
                        addPlaylist(this);
                    });
                });
    });
</script>
</@m.body>
</html>