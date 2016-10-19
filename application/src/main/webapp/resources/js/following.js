"use strict";

function getHistories() {
    return $.when($.get(dict.contextPath + "/feed" + "/histories"));
}

function getNumOfNotification() {
    return $.when($.get(dict.contextPath + "/num_of_unread"));
}
function getNotificationNumPolling() {
    getNotificationNum();
    setInterval(getNotificationNum, 3000);
}
function getNotificationNum() {
    getNumOfNotification().then(function (num) {
        if (num && num > 0) {
            $(".notifications-reminder").css("display", "inline-block");
            $(".notifications-reminder #notification-num").text(num);
        } else {
            $(".notifications-reminder").css("display", "none");
        }
    });
}