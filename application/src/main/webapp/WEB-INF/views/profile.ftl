<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<script src="<@spring.url "/resources/js/user.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<link href="/resources/css/profilepage.css" rel="stylesheet"/>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Profile/>
<div id="container" class="container">
</div>

<script type="text/template" class="profile">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xs-offset-0 col-sm-offset-0 col-md-offset-3 col-lg-offset-3">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title"><%= data.username %></h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <% if (data.options && data.options.picture) { %>
                        <div class="col-md-3 col-lg-3 " align="center">
                            <img src="<%= data.options.picture %>" alt="User picture" class="img-circle img-responsive">
                        </div>
                        <% } %>
                        <div class=" col-md-9 col-lg-9 ">
                            <table class="table table-user-information">
                                <tbody>
                                <tr>
                                    <td><@spring.message "profilepage.FirstName" /></td>
                                    <td><%= data.firstName %></td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.LastName" /></td>
                                    <td><%= data.lastName %></td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.Email" /></td>
                                    <td><%= data.email %></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>
<script type="text/javascript">
    var user = new User();
    _.templateSettings.variable = "data";
    var profileRow = _.template(
            $("script.profile").html()
    );

    $(document).ready(function () {
        user.me().then(function (me) {
            $("#container").append(profileRow(me));
        });
    });
</script>
</@m.body>
</html>