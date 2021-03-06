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
    _.each(data, function(feedsByUser){
    %>
    <div class="following_user">
        <div class="username text-center" data-toggle="collapse" data-target="#<%= feedsByUser.user.id %>">
            <%= feedsByUser.user.userData.username %>
        </div>
        <div class="activities">
            <%_.each(feedsByUser.feeds, function(feed, index){
            if(index == 2 ){ %>
            <div id="<%=  feedsByUser.user.id  %>" class="collapse">
                <% } %>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event"><%= feed.content %></div>
                    <div class="col-xs-6 col-sm-3 date"><%= feed.date %></div>
                </div>
                <% });
                if(_.size(feedsByUser.feeds) > 2){%>
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

    $(document).ready(function () {
        getGroupedHistories()
    });

</script>
</@m.body>
</html>

