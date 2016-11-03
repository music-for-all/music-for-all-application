"use strict";

var started = true;

function getHistories() {
    return $.when($.get(dict.contextPath + "/feed/histories"));
}

function getNotifications() {
    handleNotificationRequest($.get(dict.contextPath + "/notifications"));
}

function subscribeToNotifications() {
    handleNotificationRequest($.post(dict.contextPath + "/notifications"));
}

function handleNotificationRequest(request) {
    $.when(request)
        .done(function (notifications) {
            var num = notifications ? notifications.reduce(function (sum, notification) {
                return sum + notification.count;
            }, 0) : 0;

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