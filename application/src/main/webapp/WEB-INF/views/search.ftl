<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "searchpage.Title"/></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="/resources/js/player.js"></script>
<script src="/resources/js/main.js"></script>
<script src="/resources/js/track.js"></script>
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
    </div>
    <a id="scroll-to-top" href="#top" title="Scroll to top"><@spring.message "searchpage.ScrollToTop"/></a>
</div>

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

    var setOfTags = _.template(
            $("script.tagBtnTemplate").html()
    );

    $("input[name=artist]").autocomplete(artistAutocomplete(function () {
        var tag = $('#tags .active').attr('id');
        if (tag === "popular") {
            return null;
        }
        return tag;
    }));

    $("#search-form").on("submit", function () {

        search().then(function (track) {
            $("#status-message").text("Found: " + track.length);
            $("#tracks").find("thead").after(
                    trackTable(track)
            );
        });
        /* Prevent default */
        return false;
    });

    $("#tracks").on("click", ".add-song-button", function (e) {
        console.log("add track");
    });

    function clearTracks() {
        $("#tracks tr:gt(0)").remove();
    }

    function getPopularTracks() {
        $('#status-message').text('Popular songs');
        $.when($.get("<@spring.url "/tracks/popular"/>"))
                .then(function (response) {
                    clearTracks();
                    $("#tracks").find("thead").after(
                            trackTable(response)
                    );
                });
    }

    function getTracksByTag(tag) {
        clearTracks();

        $.getJSON("<@spring.url "/api/search"/>", {tags: tag})
        <#--$.when($.get("/tracks/popular/tag=" + tag.id))-->
                .then(function (track) {
                    $('#status-message').text('Top 20 for tag "' + tag + '":');
                    $("#tracks").find("thead").after(
                            trackTable(track)
                    );
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