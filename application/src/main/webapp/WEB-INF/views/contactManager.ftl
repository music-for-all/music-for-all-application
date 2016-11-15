<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<@m.head>
<title><@spring.message "contactpage.Title"/></title>
<script src="<@spring.url "/resources/js/following.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.1/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.full.min.js"></script>

<link href="<@spring.url "/resources/css/contactManager.css" />" rel="stylesheet"/>
<link href="<@spring.url "/resources/css/notification.css" />" rel="stylesheet"/>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet"/>

<script src="<@spring.url "/resources/js/user.js" />"></script>
<script src="<@spring.url "/resources/js/chunksplayer.js" />"></script>
<script src="<@spring.url "/resources/js/autocompleteConfig.js"/>"></script>
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
                    <input id="username" class="form-control" type="text"
                           placeholder="<@spring.message "placeholder.Username"/>"/>

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
    <tr id="<%= contact.userId %>">
        <td>
            <i class="fa fa-user" aria-hidden="true"></i>
        </td>
        <td>
            <a id="user_link" href="/user/show?user_id=<%= contact.userId %>"><%= contact.username %></a>
        </td>
        <td>
            <button type="button" class="btn btn-default" onclick="follow('<%= contact.userId %>')">
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
    <tr id="<%= contact.userId %>">
        <td>
            <i class="fa fa-user" aria-hidden="true"></i>
        </td>
        <td>
            <a href="/user/show?user_id=<%= contact.userId %>"><%= contact.username %></a>
        </td>
        <td>
            <div class="track-container <%= contact.track ? 'playing' : '' %>">
                <%= contact.track ? contact.track.name : ""%>
            </div>
        </td>
        <td>
            <button type="button" class="btn btn-xs btn-success start-stream-button">
                <span class='glyphicon glyphicon-play' aria-hidden='true'></span>
            </button>
            <button type="button" class="btn btn-xs btn-warning stop-stream-button" onclick="leftCurrentStream()">
                <span class='glyphicon glyphicon-pause' aria-hidden='true'></span>
            </button>
            <button type="button" class="btn btn-default" onclick="unsubscribe('<%= contact.userId %>')">
                <i class="fa fa-user-times" aria-hidden="true"></i>
            </button>
        </td>
    </tr>
    <% }); %>
    </tbody>
</script>
<script type="text/javascript">
    var user = new User();
    var stompClient;
    var player = new ChunksPlayer();
    var stream = new Stream();

    _.templateSettings.variable = "data";

    var userFollowersRow = _.template(
            $("script.followersRow").html()
    );

    var userFollowingRow = _.template(
            $("script.followingRow").html()
    );

    $("#username").autocomplete(userAutocomplete());

    $("#results").on("click", ".start-stream-button", function (e) {
        leftCurrentStream();
        $("#results").find("tr").removeClass("active");
        var row = $(this).closest("tr");
        row.addClass("active");
        var id = row.attr("id");
        joinStream(id);
    });

    $("#results").on("click", ".stop-stream-button", function (e) {
        leftCurrentStream();
        $("#results").find("tr").removeClass("active");
    });

    function connect() {
        var socket = new SockJS(dict.contextPath + "/sockjs");
        stompClient = Stomp.over(socket);

        var token = $("meta[name='_csrf']").attr("content");
        var headers = {
            "X-CSRF-TOKEN": token
        };
        stompClient.connect(headers, function (frame) {
        });
    }

    function disconnect() {
        if (stompClient) {
            stompClient.disconnect();
        }
    }

    function updateStreamRow(track) {
        var row = $("#results").find("tr.active .track-container");
        if (track) {
            row.addClass("playing");
            $(row).text(track.name);
        } else {
            row.removeClass("playing");
            $(row).text(" ");
        }
    }

    var radioSub;

    function joinStream(userId) {
        if (!radioSub) {
            radioSub = stompClient.subscribe(dict.contextPath + "/radio/subscribers/" + userId, onMessage);
        }
    }

    function onMessage(data) {
        var response = JSON.parse(data.body);
        var track = response.track;
        updateStreamRow(track);
        if (track) {
            if (!player.isCurrentTrack(track)) {
                player.reset();
            }
            player.playChunk(track, response.partId);
        } else {
            player.pause();
        }
    }

    function leftCurrentStream() {
        if (radioSub) {
            radioSub.unsubscribe();
            radioSub = null;
            player.reset();
        }
    }

    function getFollowing() {
        clearContacts();
        user.getFollowing().then(function (users) {
            var ids = users.map(function (user) {
                return user.userId;
            });
            stream.streamsByUsers(ids).then(function (userToTrack) {
                users.forEach(function (user) {
                    var track = userToTrack[user.userId];
                    if (track) {
                        user.track = track;
                    }
                });

                $("#results").find("thead").after(
                        userFollowingRow(users)
                );
            });
        });
    }

    function getFollowers() {
        clearContacts();
        user.getFollowers().then(function (users) {
            $("#results").find("thead").after(
                    userFollowersRow(users)
            );
        });
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
        var username = $("#username").val();
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

    $(document).ready(function () {
        connect();
        getFollowing();
        $("#search-form").on("submit", function () {
            search();
            return false;
        });
    });

    window.onbeforeunload = function () {
        disconnect();
    };
</script>
</@m.body>
</html>