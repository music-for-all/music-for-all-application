"use strict";

function getHistories() {
    return $.when($.get(dict.contextPath + "/feed" + "/histories"));
};

function getNumOfUnreadNews() {
    return $.when($.get(dict.contextPath + "/num_of_unread"));
};
