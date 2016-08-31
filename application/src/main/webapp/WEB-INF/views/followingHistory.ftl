<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "followingpage.Title"/></title>
<link href="/resources/css/following.css" rel="stylesheet">
<link href="/resources/css/font-awesome.min.css" rel="stylesheet">

</@m.head>

<@m.body>

    <@m.navigation/>

<div class="container">
    <h2><@spring.message "followingpage.Title"/></h2>

    <div id="followingsHistories">
    </div>
</div>

<script type="text/template" class="groupedHistories">
    <% _.each(data, function(user, histories){
    var i = 0;%>

    <div id="following_user">
        <div class="username text-center" data-toggle="collapse" data-target="#username1"><%= user.username %></div>
        <div class="activities">

            <% _.each(histories, function(history){
            i++;
            if(i <= 2){%>
            <div class="row activity">
                <div class="col-xs-6 col-sm-9 event"><%history.eventType%></div>
                <div class="col-xs-6 col-sm-3 date"><%history.date%></div>
            </div>
            <%} else if (i > 2 && i <= 30){%>
            <div id="user" class="collapse">
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event"><%history.eventType%></div>
                    <div class="col-xs-6 col-sm-3 date"><%history.date%></div>
                </div>
            </div>
            <% } else break;}); %>
        </div>
    </div>
    <% }); %>
</script>

<script type="text/javascript">

    var userGroupedHistories = _.template(
            $("script.groupedHistories").html()
    );

    function getGroupedHistories() {
        self.getGroupedHistories().then(function (users) {
            $("#followingsHistories").after(
                    userGroupedHistories(users)
            );
        })
    }

    jQuery(document).ready(function () {
        getGroupedHistories()
    });

</script>
</@m.body>
</html>

