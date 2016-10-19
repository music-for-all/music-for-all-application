"use strict";

function getHistories() {
    return $.when($.get(dict.contextPath + "/feed" + "/histories"));
}

function getNumOfUnreadNews() {
    return $.when($.get(dict.contextPath + "/num_of_unread"));
}
function viewNumLongPoll() {
    setInterval(function () {

        var ureadNum = getNumOfUnreadNews();
        if (ureadNum > 0) {
            $("#num").text(ureadNum);
        }
    }, 10000);
}