/**
 * @author ENikolskiy on 6/24/2016.
 */
function Playlist() {
    
    var self = this;
    
    self.delete = function (id, header, token) {
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
    
    self.create = function (name, header, token) {
        var dfr = $.Deferred();
        return $.when($.post("/playlist/create", {"name": name}));

        /*return $.ajax({
            type: "POST",
            url: ,
            data: ,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },/!*
            success: function (response) {
                dfr.resolve(response);
            },*!/
            error: function () {
                dfr.reject();
            }
        });*/
        // return dfr.promise();
    };
    
    self.songs = function (id, header, token) {
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
}