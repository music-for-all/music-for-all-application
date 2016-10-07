<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<@m.head>
<title><@spring.message "contactpage.Title"/></title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<link href="/resources/css/contactManager.css" rel="stylesheet"/>
<script src="/resources/js/user.js"></script>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Contacts/>

<div class="container">
    <div class="well col-md-8 col-md-offset-2 text-center">
        <div class="col-md-2">
            <div id="selector" class="btn-group-vertical" data-toggle="buttons">
                <label class="btn btn-default active" onclick="getFollowing()">
                    <input type="radio"><@spring.message "contactpage.Following"/>
                </label>
                <label class="btn btn-default" onclick="getFollowers()">
                    <input type="radio"><@spring.message "contactpage.Followers"/>
                </label>
            </div>
        </div>
        <div class="col-md-offset-1 col-md-6">
            <form id="search-form" class="form-inline text-center ">
                <div class="input-group">
                    <input id="name" class="form-control" type="text" value=""/>
                    <div class="input-group-btn">
                        <input id="searchButton" data-style="slide-left" class="btn btn-success "
                               type="submit" value="<@spring.message "contactpage.SubmitSearch"/>"/>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-md-offset-1 col-md-6">
        <span id="message" class="alert alert-block">
        </span>
        </div>
        <div class="tab-content col-md-offset-1 col-md-6">
            <div class="top-table" id="tops">
                <table id="results" class="table table-hover table-striped table-condensed ">
                    <thead>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/template" class="followersRow">
    <tbody>
    <% _.each(data, function(contact){ %>
    <tr id="<%= contact.id %>">
        <td>
            <i class="fa fa-user" aria-hidden="true"></i>
        </td>
        <td>
            <a id="user_link" href="/user/show?user_id=<%= contact.id %>"><%= contact.username %></a>
        </td>
        <td>
            <button type="button" class="btn btn-default" onclick="follow('<%= contact.id %>')">
                <i class="fa fa-user-plus" aria-hidden="true"></i>
            </button>
        </td>
    </tr>
    <% }); %>
    </tbody>
</script>
<script type="text/template" class="followingRow">
    <tbody>
    <% _.each(data, function(contact){ %>
    <tr id="<%= contact.id %>">
        <td>
            <i class="fa fa-user" aria-hidden="true"></i>
        </td>
        <td>
            <a href="/user/show?user_id=<%= contact.id %>"><%= contact.username %></a>
        </td>
        <td>
            <button type="button" class="btn btn-default" onclick="unsubscribe('<%= contact.id %>')">
                <i class="fa fa-user-times" aria-hidden="true"></i>
            </button>
        </td>
    </tr>
    <% }); %>
    </tbody>
</script>
<script type="text/javascript">
    var user = new User();

    _.templateSettings.variable = "data";

    var userFollowersRow = _.template(
            $("script.followersRow").html()
    );

    var userFollowingRow = _.template(
            $("script.followingRow").html()
    );

    function getFollowing() {
        clearContacts();
        user.getFollowing().then(function (users) {
            $("#results").find("thead").after(
                    userFollowingRow(users)
            );
        })
    }

    function getFollowers() {
        clearContacts();
        user.getFollowers().then(function (users) {
            $("#results").find("thead").after(
                    userFollowersRow(users)
            );
        })
    }

    function unsubscribe(id) {
        user.unfollow(id).then(function () {
            document.getElementById(id).closest("tr").remove();
            $("#message").text("You have successfully unsubscribed");
        });
    }

    function follow(id) {
        user.follow(id).then(function () {
            $("#message").text("You have successfully subscribed");
        });
    }

    function search() {
        var username = $("#name").val();
        clearContacts();
        user.search(username).then(function (users) {
            $("#results").find("thead").after(
                    userFollowersRow(users)
            );
        })
    }

    function clearContacts() {
        $("#results tr").remove();
        $("#message").text("");
    }

    jQuery(document).ready(function () {
        getFollowing();
        $("#search-form").on("submit", function () {
            search();
            return false;
        });
    });
</script>
</@m.body>
</html>