<#import "macros/macros.ftl" as m>
<#import "macros/popup_macros.ftl" as p>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "welcomepage.PageTitle"/></title>
<script src="<@spring.url "/resources/js/chunksplayer.js" />"></script>
<script src="<@spring.url "/resources/js/track.js" />"></script>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<script src="/resources/js/player.js"></script>
<link href="<@spring.url "/resources/css/additionalTracksTable.css" />" rel="stylesheet">
<link href="<@spring.url "/resources/css/player.css" />" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<link href="/resources/css/welcomepage.css" rel="stylesheet">
</@m.head>

<@m.body>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><@spring.message "projectName"/></a>
        </div>
        <div id="navbar" class="navbar-right navbar-collapse collapse ">
            <ul class="nav navbar-nav">
                <li>
                    <div class="navbar-text">
                        <#if RequestParameters.error??>
                            <div class="text-danger"><@spring.message "welcomepage.LoginError"/>:
                                <#if Session.SPRING_SECURITY_LAST_EXCEPTION??>
                                ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
                                </#if>
                            </div>
                        <#elseif RequestParameters.logout??>
                            <div class="text-success"><@spring.message "welcomepage.LogoutMessage"/></div>
                        </#if>
                    </div>
                </li>
                <li>
                    <a href="#" id="popover"><span class="glyphicon glyphicon-log-in"></span>
                        <@spring.message "welcomepage.Login"/></a>

                    <div class="hide" id="popover-content">
                        <@m.form "/welcome", "POST", "login">
                            <label for="inputEmail" class="sr-only"><@spring.message "welcomepage.Email"/></label>
                            <input type="text" id="inputEmail" name="email" class="form-control"
                                   placeholder="Email"
                                   required />
                            <label for="inputPassword" class="sr-only"><@spring.message "welcomepage.Password"/></label>
                            <input type="password" id="inputPassword" name="password" class="form-control"
                                   placeholder="Password"
                                   required/>
                            <input type="checkbox" name="_spring_security_remember_me" id="remember-me"/>
                            <label for="remember-me"><@spring.message "welcomepage.RememberMe"/></label>
                            <input type="submit" class="btn btn-lg btn-primary btn-block" value="Log in"/>
                        </@m.form>
                        <div class="divider">
                            <span>or</span>
                        </div>
                        <form class="social">
                            <ul class="list-inline intro-social-buttons">
                                <li>
                                    <a href="<@spring.url "/auth/google"/>" class="google">
                                        <span class="fa fa-google fa-fw"></span>
                                    </a>
                                </li>
                                <li>
                                    <a href="<@spring.url "/auth/twitter"/>" class="twitter">
                                        <span class="fa fa-twitter fa-fw"></span>
                                    </a>
                                </li>
                                <li>
                                    <a href="<@spring.url "/auth/facebook"/>" class="facebook">
                                        <span class="fa fa-facebook fa-fw"></span>
                                    </a>
                                </li>
                            </ul>
                        </form>
                    </div>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false"><i class="fa fa-globe" aria-hidden="true"></i><span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="?lang=en">English</a></li>
                        <li><a href="?lang=ru">Русский</a></li>
                        <li><a href="?lang=ua">Українська</a></li>
                    </ul>
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
                    <div class="container">
                    <h1><@spring.message "projectName"/></h1>
                    <h3><@spring.message "welcomepage.Intro"/></h3>
                    <div class="top-table well" id="tops">
                        <table id="tracks" class="table table-hover table-striped table-condensed no-checkbox tracks-table ">
                            <thead>
                            <tr>
                                <td><@spring.message "welcomepage.Actions"/></td>
                                <td><@spring.message "welcomepage.Artist"/></td>
                                <td><@spring.message "welcomepage.Title"/></td>
                                <td><@spring.message "welcomepage.Duration"/></td>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


    <@m.addTrackRowTemplate/>
    <@p.player_Footer/>

<script type="text/javascript">

    var track = new Track();

    _.templateSettings.variable = "data";
    var trackTable = _.template(
            $("script.addTrackRowTemplate").html()
    );

    $.when($.get("<@spring.url "/tracks/popular"/>"))
            .then(function (response) {
                    response.forEach(function (track) {
                        $("#tracks").find("thead").after(
                                trackTable(track)
                        );
                    });
            });
</script>
<script type="text/javascript">
    $(document).ready(function() {

        $("#popover").popover({
            html: true,
            content: function () {
                return $("#popover-content").html();
            },
            placement: "bottom"
        });

        $("#popover").on("click", function() {return false;});

        $("#popover").focus();

        /* Set focus on the name input field when the pop-up window has been shown. */
        $("#popover").on("shown.bs.popover", function () {

            // TODO
//            $("#inputEmail").focus();
            $(".facebook").focus();
        });
    });
</script>
</@m.body>
</html>