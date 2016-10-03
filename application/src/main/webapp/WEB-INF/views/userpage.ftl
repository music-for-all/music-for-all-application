<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<#import "macros/popup_macros.ftl" as p>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<link href="/resources/css/userpage.css" rel="stylesheet"/>
<script src="/resources/js/user.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
</@m.head>
<@m.body>
    <@m.navigation m.pages.Profile/>

    <@p.usersPopup followers "followerspopup"></@p.usersPopup>
    <@p.usersPopup following "followingpopup"></@p.usersPopup>
<div id="container" class="container">
    <div class="row">
        <div class="col-md-2 well">
            <img class="img-responsive img-rounded" src="${user.picture}">
            <div id="userHeader" class="profile-info">
            </div>
            <div class="btn-group-vertical">
                <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#followerspopup">
                    <@spring.message "userpage.Followers"/> <span class="badge">${followers?size}</span>
                </button>
                <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#followingpopup">
                    <@spring.message "userpage.Following"/> <span class="badge">${following?size}</span>
                </button>
            </div>

            <div class="profile-playlists">
                <@m.playlistRow playlists></@m.playlistRow>
            </div>
        </div>
        <div class="col-md-9">
            <div class="tracks">
                Tracks from playlist
            </div>
        </div>
    </div>
</div>
<script type="text/template" class="userHeader">
    <div class="profile-info">
        <div class="caption">
            <h3><%= data.firstName %> <%= data.lastName %></h3>
        </div>
        <div class="profile-usertitle-job">
            <p><%= data.username %></p>
        </div>
    </div>
</script>

<script type="text/javascript">

    var user = new User();

    _.templateSettings.variable = "data";

    var userHeader = _.template(
            $("script.userHeader").html()
    );

    $('li > a').click(function () {
        $('li').removeClass();
        $(this).parent().addClass('active');
    });

    $(document).ready(function () {
        user.getUser(${user_id}).then(function (req_user) {
            $("#userHeader").append(
                    userHeader(req_user)
            );
        });
    });
</script>
</@m.body>
</html>