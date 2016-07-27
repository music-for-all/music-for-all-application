<#import "macros/macros.ftl" as m>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Welcome</title>
<script src="resources/js/welcomepage.js"></script>
<link href="/resources/css/welcomepage.css" rel="stylesheet">
<link href="/resources/css/font-awesome.min.css" rel="stylesheet">
</@m.head>

<@m.body>
<div class="intro-header">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="intro-message">
                    <h1>Music for all</h1>

                    <h3>Sign in and manage your songs</h3>
                    <hr class="intro-divider">
                    <ul class="list-inline intro-social-buttons">
                        <li>
                            <a href="#" class="btn btn-default "><i class="fa fa-google fa-fw"></i> <span
                                    class="network-name">Google</span></a>
                        </li>
                        <li>
                            <a href="#" class="btn btn-default "><i class="fa fa-twitter fa-fw"></i> <span
                                    class="network-name">Twitter</span></a>
                        </li>
                        <li>
                            <a href="#" class="btn btn-default "><i class="fa fa-facebook fa-fw"></i> <span
                                    class="network-name">Facebook</span></a>
                        </li>
                    </ul>

                    <#if RequestParameters.error??>
                        <div>
                            Failed to login:
                            <#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
                                ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
                            </#if>
                        </div>
                    <#elseif RequestParameters.logout??>
                        <div>
                            You have been logged out.
                        </div>
                    </#if>
                    <@m.form "/welcome", "POST", "login">
                        <div class="col-md-12">
                            <div class="col-md-4 col-md-offset-4">
                                <label for="inputUsername" class="sr-only">Username</label>
                                <input type="text" id="inputUsername" name="username" class="form-control"
                                       placeholder="Username"
                                       required autofocus/>
                                <label for="inputPassword" class="sr-only">Password</label>
                                <input type="password" id="inputPassword" name="password" class="form-control"
                                       placeholder="Password"
                                       required/>
                                <input type="checkbox" name="_spring_security_remember_me" id="remember-me"/><label
                                    for="remember-me">Remember me</label>
                                <input type="submit" class="btn btn-lg btn-primary btn-block" value="Log in"/>
                            </div>
                        </div>
                    </@m.form>
                </div>
            </div>
        </div>
    </div>
</div>
</@m.body>
</html>