<#import "/spring.ftl" as spring />
<#macro head>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <script src="<@spring.url "/resources/js/dict.js" />"></script>
    <script type="text/javascript">
        dict.contextPath = "<@spring.url "" />";
    </script>
    <script src="//code.jquery.com/jquery-2.2.4.min.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="<@spring.url "/resources/js/stream.js" />"></script>

    <link rel="stylesheet"
          href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"/>
    <link rel="stylesheet"
          href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic"
          rel="stylesheet"/>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="<@spring.url "/resources/img/favicon.ico"/>"/>
    <#nested>
</head>
</#macro>

<#macro body>
<body>
    <@logoutForm/>
    <#nested>
<script type="text/javascript">
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    $.ajaxSetup({
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    });

    window.onbeforeunload = function () {
        new Stream().stop();
    };
</script>
</body>
</#macro>

<#macro form action method id enctype="">
<form id="${id}" action="<@spring.url "${action}" />" method="${method}" <#if method=="post"||method=="POST">
      enctype="${enctype}"</#if>>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <#nested>
</form>
</#macro>

<#macro logoutForm>
    <@form '/logout',"post", "logout-form", "application/x-www-form-urlencoded">
    </@form>
</#macro>

<#assign searchCaption>
    <@spring.message "macros.Search"/>
</#assign>
<#assign mainCaption>
    <@spring.message "macros.Main"/>
</#assign>
<#assign addCaption>
    <@spring.message "macros.Add"/>
</#assign>
<#assign profileCaption>
    <@spring.message "macros.Profile"/>
</#assign>
<#assign feedCaption>
    <@spring.message "macros.Feed"/>
</#assign>
<#assign contactsCaption>
    <@spring.message "macros.Contacts"/>
</#assign>

<#assign pages = {"Main": {"url": '/main', "title": "${mainCaption}", "icon": "fa-th-list"},
"Search": {"url": '/search', "title": "${searchCaption}", "icon": "fa-search"},
"Add": {"url": '/uploadFile', "title": "${addCaption}", "icon": "fa-plus"},
"Profile": {"url": '/profile', "title": "${profileCaption}", "icon": "fa-cog"},
"Contacts": {"url": '/contactManager', "title": "${contactsCaption}", "icon": "fa-users"},
"Feed": {"url": '/feed', "title": "${feedCaption}", "icon": "fa-newspaper-o"},
"WithoutActivePage": {"url": ''}}>

<#macro navigation activePage=pages.WithoutActivePage>
    <#assign items = [pages.Main, pages.Contacts, pages.Search, pages.Feed]>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"><@spring.message "projectName"/></a>
        </div>

        <div id="navbar" class="navbar-right navbar-collapse collapse ">
            <ul class="nav navbar-nav">
                <#list items as item>
                    <@navigationItem item activePage/>
                </#list>
                <li <#if pages.Profile.url == activePage.url>class="active"</#if>>
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">${profileCaption}<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href=<@spring.url '/profile'/>><@spring.message "macros.MyProfile"/></a></li>
                        <li><a href="#" onclick="document.getElementById('logout-form').submit()">
                            <@spring.message "macros.Logout"/></a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false"><i class="fa fa-globe" aria-hidden="true"></i><span
                            class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<@spring.url '?lang=en'/>">English</a></li>
                        <li><a href="<@spring.url '?lang=ru'/>">Русский</a></li>
                        <li><a href="<@spring.url '?lang=ua'/>">Українська</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
</#macro>

<#macro navigationItem item activePage>
<li <#if item.url == activePage.url>class="active"</#if>>
    <a href=<@spring.url '${item.url}'/>>
        <i class="fa ${item.icon}"></i> ${item.title}
    </a>
    <#if item.url ==  pages.Feed.url && activePage.url != pages.Feed.url>
        <span class="fa-stack fa-lg notifications-reminder">
            <i class="fa fa-comment fa-stack-1x"></i>
            <strong class="fa-stack-1x" id="notification-num"></strong>
        </span>
    </#if>
</li>
</#macro>

<#macro footer>
<footer class="container-fluid">
    <p><@spring.message "projectName"/> ©</p>
</footer>
</#macro>

<#macro addTrackRowTemplate>
<script type="text/template" class="addTrackRowTemplate">
    <tr id="<%= data.id %>">
        <td>
            <button type="button" class="btn btn-xs btn-success play-button">
                <span class='glyphicon glyphicon-play' aria-hidden='true'></span>
            </button>
            <button type="button" class="btn btn-xs btn-warning pause-button">
                <span class='glyphicon glyphicon-pause' aria-hidden='true'></span>
            </button>
            <button type="button" class="btn btn-xs btn-success add-song-button">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            </button>
            <div class="checkButton" data-toggle="buttons">
                <label class="btn btn-success btn-xs check">
                    <input type="checkbox"/>
                    <span class="glyphicon glyphicon-ok"></span>
                </label>
            </div>
            <button type="button" class="btn btn-xs btn-danger delete-song-button">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
            </button>
            <button class="btn btn-xs btn-primary like-button"><@spring.message "mainpage.Like" /></button>
            <span class="glyphicon num-likes" aria-hidden="true"></span>
        </td>
        <td>
            <%= data.name %>
        </td>
        <td>
            <%= data.artist ? "<a href=\"/artist/" + data.artist.id + "\">" + data.artist.name + "</a>"
                                 : "<@spring.message "followingpage.unknown" />" %>
        </td>
        <td>n/a</td>
    </tr>
</script>
</#macro>

<#macro playlistRowTemplate>
<script type="text/template" class="playlistRowTemplate">
    <li id="<%= data.id %>" title="<%= data.name %>">
        <div class="input-group">
            <a type="button" class="btn btn-default btn-block" data-value="<%= data.name %>">
                <%= data.name %>
            </a>
        </div>
    </li>
</script>
</#macro>

<#macro playlistRowTemplateWithoutDeleting >
<script type="text/template" class="playlistRowTemplateWithoutDeleting">
    <ul class="nav nav-pills nav-stacked">
        <% _.each(data, function(playlist){ %>
        <li id="<%= playlist.id %>">
            <a href="#">
                <%= playlist.name %>
            </a>
        </li>
        <% }); %>
    </ul>
</script>
</#macro>
