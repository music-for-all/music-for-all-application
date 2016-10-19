<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as p>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<link href="/resources/css/profilepage.css" rel="stylesheet"/>
<script src="/resources/js/artist.js"></script>
<script src="/resources/js/artistProfile.js"></script>
<script src="<@spring.url "/resources/js/chunksplayer.js" />"></script>
<script src="<@spring.url "/resources/js/track.js" />"></script>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<script src="<@spring.url "/resources/js/player.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<link href="<@spring.url "/resources/css/additionalTracksTable.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/player.css" />" rel="stylesheet">
</@m.head>
<@m.body>
    <@m.navigation m.pages.Profile/>
<div id="container" class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-12 ">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 id="artist-name" class="panel-title">${artistName}</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class=" col-md-9 col-lg-9 ">
                            <img id="artist-picture" src="" alt="Artist picture" class="img-responsive left artist-image">
                            <h3><@spring.message "artist.Bio" /></h3>
                            <span id="artist-bio"></span>

                            <div class="top-tracks well clear" id="top-tracks">
                                <h3><@spring.message "artist.TopTracks" /></h3>
                                <table id="tracks" class="table table-hover table-striped table-condensed no-checkbox tracks-table ">
                                    <thead>
                                    <tr>
                                        <th><@spring.message "welcomepage.Actions"/></th>
                                        <th><@spring.message "welcomepage.Title"/></th>
                                        <th><@spring.message "welcomepage.Artist"/></th>
                                        <th><@spring.message "welcomepage.Duration"/></th>
                                    </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
    <@m.addTrackRowTemplate/>
    <@p.player_Footer/>

<script type="text/javascript">

    $(document).ready(function() {

        var track = new Track();
        _.templateSettings.variable = "data";
        var trackTable = _.template(
                $("script.addTrackRowTemplate").html()
        );

        $.when($.get("<@spring.url "/tracks/topTracksOf/" + ${artistName} />"))
                .then(function (response) {
                    response.forEach(function (track) {
                        $("#tracks").find("thead").after(
                                trackTable(track)
                        );
                    });
                });
    });
</script>
</@m.body>
</html>
