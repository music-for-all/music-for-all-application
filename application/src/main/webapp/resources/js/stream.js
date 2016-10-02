"use strict";

function Stream() {

    var self = this;
    var baseUrl = dict.contextPath + "/stream";

    self.start = function (trackId) {
        return $.when($.post(baseUrl + "/track/" + trackId));
    };

    self.stop = function () {
        return $.when($.post(baseUrl + "/stop"));
    };

    self.streamsByUsers = function (userIds) {
        return $.when($.get(baseUrl, {
            ids: userIds
        }));
    };

    self.publish = function (toPublish) {
        return $.when($.post(baseUrl + "/publish/" + toPublish));
    };
}