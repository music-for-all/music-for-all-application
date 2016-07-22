<#import "/spring.ftl" as spring />
<#macro head>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="//code.jquery.com/jquery-2.2.4.min.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

    <link rel="stylesheet"
          href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"/>
    <link rel="stylesheet"
          href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
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

<#macro form action method id>
<form id="${id}" action="<@spring.url "${action}" />" method="${method}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <#nested>
</form>
</#macro>

<#macro logoutForm>
    <@form '/logout',"post", "logout-form">
    <input type="submit" value="Log out" class="btn btn-default"/>
    </@form>
</#macro>