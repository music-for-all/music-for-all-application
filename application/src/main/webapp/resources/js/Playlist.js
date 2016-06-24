/**
 * @author ENikolskiy on 6/24/2016.
 */
function Playlist() {
}

Playlist.prototype.delete = function (id, header, token) {
    var dfr = $.Deferred();
    $.ajax({
        type: "DELETE",
        url: "/playlist",
        data: {"id": id},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (response) {
            dfr.resolve(response);
        },
        error: function () {
            dfr.reject();
        }
    });
    return dfr.promise();
};

Playlist.prototype.create = function (name, header, token) {
    var dfr = $.Deferred();
    $.ajax({
        type: "POST",
        url: "/playlist",
        data: {"name": name},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (response) {
            dfr.resolve(response);
        },
        error: function () {
            dfr.reject();
        }
    });
    return dfr.promise();
};

Playlist.prototype.songs = function (id, header, token) {
    var dfr = $.Deferred();
    $.ajax({
        type: "GET",
        url: "/playlist",
        data: {"id": id},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (response) {
            dfr.resolve(response);
        },
        error: function () {
            dfr.reject();
        }
    });
    return dfr.promise();
};