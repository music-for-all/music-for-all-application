<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "welcomepage.PageTitle"/></title>
<script src="/resources/js/player.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<link href="/resources/css/welcomepage.css" rel="stylesheet">
<link href="/resources/css/font-awesome.min.css" rel="stylesheet">
</@m.head>

<@m.body>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><@spring.message "projectName"/></a>
        </div>

        <form class="navbar-form navbar-left" id="searchForm" method="post">
            <div class="input-group " placeholder="Search">
                <input id="word" class="form-control" type="text" value=""
                       placeholder="<@spring.message "welcomepage.Search"/>" name="q"/>

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
                    <a href="#"><span class="glyphicon glyphicon-user"></span> <@spring.message "welcomepage.SignUp"/>
                    </a>
                </li>
                <li>
                    <a href="#" id="popover"><span class="glyphicon glyphicon-log-in"></span>
                        <@spring.message "welcomepage.Login"/></a>

                    <div class="hide" id="popover-content">
                        <@m.form "/welcome", "POST", "login">
                            <label for="inputUsername" class="sr-only"><@spring.message "welcomepage.Username"/></label>
                            <input type="text" id="inputUsername" name="username" class="form-control"
                                   placeholder="Username"
                                   required />
                            <label for="inputPassword" class="sr-only"><@spring.message "welcomepage.Password"/></label>
                            <input type="password" id="inputPassword" name="password" class="form-control"
                                   placeholder="Password"
                                   required/>
                            <input type="checkbox" name="_spring_security_remember_me" id="remember-me"/>
                            <label for="remember-me"><@spring.message "welcomepage.RememberMe"/></label>
                            <input type="submit" class="btn btn-lg btn-primary btn-block" value="Log in"/>
                        </@m.form>
                        <hr class="divider"/>
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
                        <table id="tracks" class="table table-hover table-striped table-condensed ">
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

<script type="text/template" class="trackRowTemplate">
    <tbody>
    <% _.each(data, function(track){ %>
    <tr id="<%= track.id %>">
        <td>
            <button type='button' class='btn btn-xs btn-success' onclick="onPlay('audio_<%= track.id %>')">
                <span class='glyphicon glyphicon-play' aria-hidden='true'/>
            </button>
            <button type='button' class='btn btn-xs btn-warning pause-track-button'
                    onclick="onPause('audio_<%= track.id %>')">
                <span class='glyphicon glyphicon-pause' aria-hidden='true'/>
            </button>
            <button type='button' class='btn btn-xs btn-danger delete-song-button'>
                <span class='glyphicon glyphicon-remove' aria-hidden='true'/>
            </button>
        </td>
        <td>
            <%= track.name %>
        </td>
        <td>
        </td>
        <td>
            <audio id='audio_<%= track.id %>' controls>
                <source type="audio/mp3" src="/files/<%= track.location %>">
            </audio>
        </td>
    </tr>
    <% }); %>
    </tbody>
</script>

<script type="text/javascript">
    _.templateSettings.variable = "data";
    var trackTable = _.template(
            $("script.trackRowTemplate").html()
    );

    $.when($.get('<@spring.url "/tracks/popular"/>'))
            .then(function (response) {
                $("#tracks").find("thead").after(
                        trackTable(response)
                );
            });
</script>
<script type="text/javascript">
    $(document).ready(function() {

        $('#popover').popover({
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

            $("#inputUsername").focus();
        });
    });
</script>
</@m.body>
</html>