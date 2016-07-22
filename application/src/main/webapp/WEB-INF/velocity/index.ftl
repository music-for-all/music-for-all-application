<#import "macros/macros.ftl" as macros>
<!DOCTYPE html>
<html lang="en">
<@macros.head>
<title>Music For All</title>
</@macros.head>
<@macros.body>
<h2>Music For All</h2>

<h3>User list</h3>
<ul>
    <#list userList as user>
        <li>${user.username} (${user.email})</li>
    </#list>
</ul>
<p>
    <a href="profile" class="btn btn-primary">Profile</a>
</p>
    <@macros.logoutForm/>
</@macros.body>
</html>