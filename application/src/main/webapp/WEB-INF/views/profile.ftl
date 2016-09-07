<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<link href="/resources/css/profilepage.css" rel="stylesheet"/>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Profile/>

<div id="container" class="container">
    <div class="well col-md-4 col-md-offset-4 text-center">
        <h3><@spring.message "profilepage.UserInfo"/></h3>

        <h3><@spring.message "profilpage.Hello"/>, ${username}</h3>
        <@m.logoutForm/>
    </div>

    <h3>Hello, <span>${facebookProfile.name}</span>!</h3>
    <div class="pic">
        <img src="http://graph.facebook.com/" +  ${facebookProfile.id}} + "/picture" alt="profile image"/>
    </div>
    <div class="detail">
        <ul>
            <li>Social ID: ${profile.id}</li>
            <li><Name: ${profile.name}/li>
            <li>Location: ${profile.loc}</li>
            <li>E-mail: ${facebookProfile.email}</li>
            <li>Profile URL: ${profile.url}</li>
        </ul>
    </div>
</div>
</@m.body>
</html>