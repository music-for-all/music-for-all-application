<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "followingpage.Title"/></title>
<link href="/resources/css/following.css" rel="stylesheet">
<link href="/resources/css/font-awesome.min.css" rel="stylesheet">
<script src="/resources/js/following.js"></script>
</@m.head>

<@m.body>

    <@m.navigation/>

<div class="container">
    <h2><@spring.message "followingpage.Title"/></h2>

    <div id="followingsHistories">
    </div>
</div>

<script type="text/template" class="groupedHistories">
    <% _.each(data, function(user, histories){%>
    <div id="following_user">
        <div class="username text-center" data-toggle="collapse" data-target="#<%= user.email %>"><%= user.username %>
        </div>
        <div class="activities">
            <% _.every(histories, function(history, index){
            if(index == 2 ){ %>
            <div id="<%= user.email %>" class="collapse">
                <% } %>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event"><%history.eventType%></div>
                    <div class="col-xs-6 col-sm-3 date"><%history.date%></div>
                </div>
                <% });
                if(_.size(histories) > 2){%>
                </div>
            <% } %>
        </div>
    </div>
    <% }); %>
</script>

<script type="text/javascript">

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

