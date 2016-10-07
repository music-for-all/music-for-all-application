<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<#import "macros/popup_macros.ftl" as p>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<link href="/resources/css/userpage.css" rel="stylesheet"/>
<script src="/resources/js/social.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
</@m.head>
<@m.body>
    <@m.navigation m.pages.Profile/>
<div id="popupBlock">
    <@p.followersPopup "followersPopup"></@p.followersPopup>
    <@p.followingPopup "followingPopup"></@p.followingPopup>
</div>
<div id="container" class="container">
    <div class="row">
        <div class="col-md-2 well">

            <div id="userHeader" class="profile-info">
            </div>
            <div class="btn-group-vertical">
                <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#followersPopup">
                    <@spring.message "userpage.Followers"/>
                </button>
                <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#followingPopup">
                    <@spring.message "userpage.Following"/>
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

<script type="text/javascript">

    var social = new Social();

    _.templateSettings.variable = "data";

    var userHeader = _.template(
            $("script.userHeader").html()
    );

    var followersPopup = _.template(
            $("script.followersPopup").html()
    );

    var followingPopup = _.template(
            $("script.followingPopup").html()
    );

    $('li > a').click(function () {
        $('li').removeClass();
        $(this).parent().addClass('active');
    });

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
    });
</script>
</@m.body>
</html>