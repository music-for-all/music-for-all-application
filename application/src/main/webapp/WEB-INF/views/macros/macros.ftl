<#import "/spring.ftl" as spring />
<#macro head>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <script src="//code.jquery.com/jquery-2.2.4.min.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

    <link rel="stylesheet"
          href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"/>
    <link rel="stylesheet"
          href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link href="http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic"
          rel="stylesheet"/>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <#nested>
</head>
</#macro>

<#macro body>
<body>
    <#nested>
<script type="text/javascript">
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");

    $.ajaxSetup({
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    });
</script>
</body>
</#macro>

<#macro form action method id enctype="">
<form id="${id}" action="<@spring.url "${action}" />" method="${method}" <#if method=="post"||method=="POST"> enctype="${enctype}"</#if>>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <#nested>
</form>
</#macro>

<#macro logoutForm>
    <@form '/logout',"post", "logout-form", "application/x-www-form-urlencoded">
    <input type="submit" value="Log out" class="btn btn-default"/>
    </@form>
</#macro>

<#assign pages = {"Main": {"url": '/main', "title": "Main"},
"Search": {"url": '/search', "title": "Search"},
"Add": {"url": '/uploadFile', "title": "Add track"},
"Profile": {"url": '/profile', "title": "Profile"},
"WithoutActivePage": {"url": ''}}>

<#macro navigation activePage=pages.WithoutActivePage>
    <#assign items = [pages.Profile, pages.Main, pages.Search]>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Music for all</a>
        </div>

        <div id="navbar" class="navbar-right navbar-collapse collapse ">
            <ul class="nav navbar-nav">
                <#list items as item>
                    <@navigationItem item activePage/>
                </#list>
            </ul>
        </div>
    </div>
</nav>
</#macro>

<#macro navigationItem item activePage>
<li <#if item.url == activePage.url>class="active"</#if> >
    <a href=<@spring.url '${item.url}'/>>
        <i class="fa"></i> ${item.title}
    </a>
</li>
</#macro>

<#macro footer>
<footer class="container-fluid">
    <p>Music for all ©</p>
</footer>
</#macro>