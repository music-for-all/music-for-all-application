<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<@m.head>
<title><@spring.message "followingpage.Title"/></title>
<link href="/resources/css/following.css" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="/resources/js/following.js"></script>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Feed/>

<div class="container title">
    <h2><@spring.message "followingpage.Title"/></h2>

    <div id="followingsHistories">
    </div>
</div>

<script type="text/template" class="groupedHistories">


    <%
    _.each(data, function(feeds, user){
    var id = user.match( /id=(.+?),/ )[1];
    var username = user.match( /username='(.+?)'/ )[1];
    %>
    <div id="following_user">
        <div class="username text-center" data-toggle="collapse" data-target="#<%= id %>">
            <%= username %>
        </div>
        <div class="activities">
            <%_.each(feeds, function(feed, index){
            if(index == 2 ){ %>
            <div id="<%= id %>" class="collapse">
                <% } %>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event"><%= feed.content %></div>
                    <%
                    var date = new Date(feed.date);
                    var dateString = date.getHours() + ":" + date.getMinutes();
                    %>
                    <div class="col-xs-6 col-sm-3 date"><%= dateString %></div>
                </div>
                <% });
                if(_.size(feeds) > 2){%>
                </div>
            <% } %>
        </div>
    </div>
    <% }); %>
</script>

<script type="text/javascript">
    _.templateSettings.variable = "data";

    var userGroupedHistories = _.template(
            $("script.groupedHistories").html()
    );

    function getGroupedHistories() {
        getHistories().then(function (users) {
            $("#followingsHistories").html(
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

