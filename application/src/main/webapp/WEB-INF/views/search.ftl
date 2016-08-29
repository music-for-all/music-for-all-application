<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "searchpage.Title"/></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="/resources/js/player.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.full.min.js"></script>
<script src="<@spring.url "/resources/js/searchpage.js"/>"></script>
<script src="<@spring.url "/resources/js/autocompleteConfig.js"/>"></script>
<link href="<@spring.url "/resources/css/searchpage.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/font-awesome.min.css"/>" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet"/>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Search/>

<div class="container">

    <div class="well well-sm">
        <div id="tags" data-toggle="buttons">
            <label id="popular" class="btn btn-success active" onclick="getPopularTracks()">
                <input type="radio" autocomplete="off">
                popular
            </label>
        </div>
    </div>

    <form id="search-form" class="form-inline text-center ">
        <div class="input-group">
            <input id="title" class="form-control" type="text" value="" placeholder="<@spring.message "placeholder.Title"/>"
                   name="title" autofocus="autofocus" />
            <input id="artist" class="form-control" type="text" value="" placeholder="<@spring.message "placeholder.Artist"/>"
                   name="artist" />
            <input id="album" class="form-control" type="text" value="" placeholder="<@spring.message "placeholder.Album"/>"
                   name="album" />
            <div class="input-group-btn">
                <input id="searchButton" data-style="slide-left" class="btn btn-success "
                       type="submit" value="<@spring.message "searchpage.SubmitButton"/>" />
            </div>
        </div>
    </form>

    <span id="status-message" class="well"></span>

    <div id="resultsd" class="well ">
        <table id="results" class="table table-hover table-striped table-condensed ">
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
                    <button type="button" class="btn btn-xs btn-success">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    </button>
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

<script type="text/template" class="tagBtnTemplate">
    <% _.each(data, function(tag){ %>
    <label id="<%= tag.name %>" class="btn btn-success" onclick="getTracksByTag(this)">
    <input type="radio" autocomplete="off">
     <%= tag.name %>
    </label>
    <% }); %>
</script>

<script type="text/template" class="trackRowTemplate">
    <tbody>
    <% _.each(data, function(track){ %>
    <tr id="<%= track.id %>">
        <td>
            <button type="button" class="btn btn-xs btn-success" onclick="onPlay(" audio_
            <%= track.id %>")">
            <span class="glyphicon glyphicon-play" aria-hidden="true"/>
            </button>
            <button type="button" class="btn btn-xs btn-warning pause-track-button"
                    onclick="onPause(" audio_
            <%= track.id %>")">
            <span class="glyphicon glyphicon-pause" aria-hidden="true"/>
            </button>
            <button type="button" class="btn btn-xs btn-danger delete-song-button">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"/>
            </button>
        </td>
        <td>
            <%= track.name %>
        </td>
        <td>
        </td>
        <td>
            <audio id="audio_<%= track.id %>" controls preload="none">
                <source type="audio/mp3" src="/files/<%= track.location %>">
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

    var setOfTags = _.template(
            $("script.tagBtnTemplate").html()
    );

    $("input[name=artist]").autocomplete(artistAutocomplete(function () {
        return $("select[name=tags]").val()
    }));

    function clearTracks() {
        $("#results tr:gt(0)").remove();
    }

    function getPopularTracks() {
        $('#status-message').text('Popular songs');
        $.when($.get("<@spring.url "/tracks/popular"/>"))
                .then(function (response) {
                    clearTracks();
                    $("#results").find("thead").after(
                            trackTable(response)
                    );
                });
    }

    function getTracksByTag(tag) {
        console.log(tag);
        $.when($.get("<@spring.url "/tracks/popular/tag="/>" + tag.id))
                .then(function (response) {
                    $('#status-message').text('Top 20 for tag "' + tag.id + '":');
                });
    }

    function getPopularTags() {
        $.when($.get("<@spring.url "/tags/popular"/>"))
                .then(function (response) {
                    $("#popular").after(
                            setOfTags(response)
                    );
                });
    }

    getPopularTracks();
    getPopularTags();
</script>
</@m.body>
</html>