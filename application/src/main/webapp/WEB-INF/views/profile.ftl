<#import "macros/macros.ftl" as m>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Profile - Music For All</title>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Profile/>

<div id="container" class="container">
    <div class="col-md-4 col-md-offset-4 text-center">
        <h3>User Information</h3>

        <h3>Hello, ${username}</h3>
        <@m.logoutForm/>
    </div>
</div>
</@m.body>
</html>