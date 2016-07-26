<#import "macros/macros.ftl" as m>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Music For All</title>
</@m.head>
<@m.body>
<h2>Spring Demo</h2>

<p>
    <a href="index" class="btn btn-default">Main Page</a>
</p>

    <@m.form "/login","post", "login-form">
    <div class="form-group">
        <label for="inputEmail">Email</label>
        <input type="email" class="form-control" id="inputEmail"
               placeholder="Email">
    </div>
    <div class="form-group">
        <label for="inputPassword">Password</label>
        <input type="password" class="form-control" id="inputPassword"
               placeholder="Password">
    </div>
    <div class="checkbox">
        <label><input type="checkbox">Remember me</label>
    </div>
    <button type="submit" class="btn btn-primary">Login</button>
    </@m.form>
</@m.body>
</html>