<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as p>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Main</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="/resources/js/playlist.js"></script>
<script src="/resources/js/track.js"></script>
<script src="/resources/js/player.js"></script>
<link href="/resources/css/font-awesome.min.css" rel="stylesheet"/>
<link href="/resources/css/mainpage.css" rel="stylesheet"/>
</@m.head>

<@m.body>
    <@p.popUpAdd "addPlaylistModal"/>
    <@p.popUpDelete "deletePlaylistModal"/>

    <@m.navigation m.pages.Main/>

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

    <a class="btn btn-success" href=<@spring.url '${m.pages.Add.url}'/>>
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
    </a>

    <div class="well  col-md-2 col-md-offset-1  ">
        <button id="createPlaylistButton" class="btn  btn-success btn-block " type="button">Create playlist</button>
        <ul id="playlists" class="nav nav-pills nav-stacked"></ul>

    </div>
</div>
<script type="text/template" class="trackRowTemplate">
    <tbody>
    <% _.each(data, function(track){ %>
    <tr id="<%= track.id %>">
        <td>
            <button type='button' class='btn btn-xs btn-success' onclick="onPlay('audio_<%= track.id %>')">
                <span class='glyphicon glyphicon-play' aria-hidden='true'/>
            </button>
            <button type='button' class='btn btn-xs btn-warning pause-track-button'
                    onclick="onPause('audio_<%= track.id %>')">
                <span class='glyphicon glyphicon-pause' aria-hidden='true'/>
            </button>
            <button type='button' class='btn btn-xs btn-danger delete-song-button'>
                <span class='glyphicon glyphicon-remove' aria-hidden='true'/>
            </button>
        </td>
        <td>
            <%= track.name %>
        </td>
        <td>
        </td>
        <td>
            <audio id='audio_<%= track.id %>' controls>
                <source type="audio/mp3" src="/files/<%= track.location %>">
            </audio>
        </td>
    </tr>
    <% }); %>
    </tbody>
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

    $('#playlists').on('click', 'a', function (e) {
        e.preventDefault();
        $("#playlists").find("li").removeClass("active");
        $(this).closest('li').addClass('active');
        clearTracks();
        playlist.get($('#playlists li.active').attr('id'))
                .then(function (response) {
                    $("#results").find("thead").after(
                            trackTable(response.tracks)
                    );
                });
    });

    $('#acceptRemovingPlaylistButton').on('click', function (e) {
        var playlistToRemove = $("#playlists").find("li.active")
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

    function deletePlaylist(e) {
        $("#playlists").find("li").removeClass("active");
        $(e).closest('li').addClass('active');
        $('#deletePlaylistModal').modal('show');
    }

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