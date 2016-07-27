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

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Music for all</a>
        </div>

        <form class="navbar-form navbar-left" id="searchForm" method="post">
            <div class="input-group " placeholder="Search">
                <input id="word" class="form-control" type="text" value="" placeholder="Search" name="q"/>

                <div class="input-group-btn">
                    <button id="searchButton" data-style="slide-left" class="btn btn-success " type="button">
                        <i id="icon" class="fa fa-search"></i>
                    </button>
                </div>
            </div>
        </form>

        <div id="navbar" class="navbar-right navbar-collapse collapse ">
            <ul class="nav navbar-nav">
                <li>
                    <div class="navbar-text">
                        <#if RequestParameters.error??>
                            <div class="text-danger">Failed to login:
                                <#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
                                    ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
                                </#if>
                            </div>
                        <#elseif RequestParameters.logout??>
                            <div class="text-success">You have been logged out</div>
                        </#if>
                    </div>
                </li>
                <li>
                    <a href="#"><span class="glyphicon glyphicon-user"></span> Sign Up</a>
                </li>
                <li>
                    <a href="#" id="popover"><span class="glyphicon glyphicon-log-in"></span> Log In</a>

                    <div class="hide" id="popover-content">
                        <@m.form "/welcome", "POST", "login">
                            <label for="inputUsername" class="sr-only">Username</label>
                            <input type="text" id="inputUsername" name="username" class="form-control"
                                   placeholder="Username"
                                   required autofocus/>
                            <label for="inputPassword" class="sr-only">Password</label>
                            <input type="password" id="inputPassword" name="password" class="form-control"
                                   placeholder="Password"
                                   required/>
                            <input type="checkbox" name="_spring_security_remember_me" id="remember-me"/>
                            <label for="remember-me">Remember me</label>
                            <input type="submit" class="btn btn-lg btn-primary btn-block" value="Log in"/>
                        </@m.form>
                        <hr class="divider"></hr>
                        <form>
                            <ul class="list-inline intro-social-buttons">
                                <li>
                                    <a href="#" class="btn btn-default "><i class="fa fa-google fa-fw"></i>
                                        <span class="network-name">Google</span></a>
                                </li>
                                <li>
                                    <a href="#" class="btn btn-default "><i class="fa fa-twitter fa-fw"></i>
                                        <span class="network-name">Twitter</span></a>
                                </li>
                                <li>
                                    <a href="#" class="btn btn-default "><i class="fa fa-facebook fa-fw"></i>
                                        <span class="network-name">Facebook</span></a>
                                </li>
                            </ul>
                        </form>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="intro-header">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="intro-message">
                    <h1>Music for all</h1>

                    <h3>Sign in and manage your songs</h3>
                    <div class="top-table well" id="tops">
                        <table id="top" class="table table-hover table-striped table-condensed ">
                            <thead>
                            <tr>
                                <th>Actions</th>
                                <th>Artist</th>
                                <th>Title</th>
                                <th>Duration</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $('#popover').popover({
        html: true,
        content: function () {
            return $("#popover-content").html();
        },
        placement: "bottom"
    });
</script>
</@m.body>
</html>