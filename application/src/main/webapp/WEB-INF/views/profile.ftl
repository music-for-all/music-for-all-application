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
    <div class="well col-md-8 col-md-offset-1 text-center">
        <h4>${user.username}</h4>
        <h3><@spring.message "profilepage.UserInfo"/></h3>
        <@m.logoutForm/>

        <div class="profile-picture">
            <img src="${user.picture}" alt="User picture"/>
        </div>
        <div class="row well">
            <div class="col-sm-4">
                <@spring.message "profilepage.FirstName" />
            </div>
            <div class="col-sm-4">
                ${user.firstName}
            </div>
        </div>
        <div class="row well">
            <div class="col-sm-4">
                <@spring.message "profilepage.LastName" />
            </div>
            <div class="col-sm-4">
                ${user.lastName}
            </div>
        </div>
        <div class="row well">
            <div class="col-sm-4">
                <@spring.message "profilepage.Email" />
            </div>
            <div class="col-sm-4">
                ${user.email}
            </div>
        </div>
    </div>
</div>
</@m.body>
</html>