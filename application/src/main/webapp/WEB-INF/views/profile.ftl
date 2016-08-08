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
</div>
</@m.body>
</html>