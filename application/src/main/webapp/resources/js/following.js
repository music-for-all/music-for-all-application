"use strict";

function getHistories() {
    return $.when($.get(dict.contextPath + "/feed" + "/histories"));
}

function getNotificationNum() {
    return $.when($.get(dict.contextPath + "/num_of_unread"));
}

function updateNotification() {
    getNotificationNum().done(function (num) {
        if (num > 0) {
            $(".notifications-reminder").css("display", "inline-block");
            $(".notifications-reminder #notification-num").text(num);
        } else {
            $(".notifications-reminder").css("display", "none");
        }
        setTimeout(updateNotification, 0);
    }).fail(function () {
        setTimeout(updateNotification, 0);
    });
}