<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<link href="/resources/css/userpage.css" rel="stylesheet"/>
</@m.head>
<@m.body>
    <@m.navigation m.pages.Profile/>

<div id="container" class="container">
    <div class="row">
        <div class="col-md-2 well">
            <img class="img-responsive img-rounded" src="${user.picture}">
            <div class="profile-info">
                <div class="caption">
                    <h3>${user.firstName} ${user.lastName}</h3>
                </div>
                <div class="profile-usertitle-job">
                    <p>${user.username}</p>
                </div>
            </div>
            <div class="btn-group-vertical">
                <button class="btn btn-primary" type="button">
                    Followers <span class="badge">${followers?size}</span>
                </button>
                <button class="btn btn-primary" type="button">
                    Followings <span class="badge">${followings?size}</span>
                </button>
            </div>

            <div class="profile-playlists">
                <ul class="nav">
                    <li>
                        <a href="#">Playlist 1</a>
                    </li>
                    <li>
                        <a href="#">Playlist 2</a>
                    </li>
                    <li>
                        <a href="#">Playlist 3</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="col-md-9">
            <div class="tracks">
                Tracks from playlist
            </div>
        </div>
    </div>
</div>
</@m.body>
</html>