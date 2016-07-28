<#import "macros/macros.ftl" as m>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Music For All</title>
</@m.head>
<@m.body>
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
    <@m.logoutForm/>
</@m.body>
</html>