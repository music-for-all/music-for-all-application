"use strict";

function getHistories() {
    return $.when($.get(dict.contextPath + "/feed" + "/histories"));
}

function getNumOfUnreadNews() {
    return $.when($.get(dict.contextPath + "/num_of_unread"));
}
function viewNumLongPoll() {
    setInterval(function () {
        var unreadNum = getNumOfUnreadNews();
        unreadNum.then(function (num) {
            if (num && num > 0) {
                $("#num").text(num);
            }
        });
    }, 10000);
}