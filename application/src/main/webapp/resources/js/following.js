"use strict";

var started = true;

function getHistories() {
    return $.when($.get(dict.contextPath + "/feed" + "/histories"));
}

function getNotificationNum() {
    return $.when($.get(dict.contextPath + "/notifications/unreadCount"));
}

function subscribeToNotifications() {
    return $.when($.post(dict.contextPath + "/notifications/subscribe", {"type": "FOLLOWER"}))
        .done(function (num) {
            if (num > 0) {
                $(".notifications-reminder").css("display", "inline-block");
                $(".notifications-reminder #notification-num").text(num);
            } else if (num <= 0) {
                $(".notifications-reminder").css("display", "none");
            }
            setTimeout(subscribeToNotifications, 0);
        }).fail(function () {
            setTimeout(subscribeToNotifications, 0);
        });
}