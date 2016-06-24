/**
 * @author ENikolskiy on 6/24/2016.
 */
function Playlist() {

    var self = this;

    self.delete = function (id) {
        var dfr = $.Deferred();
        $.ajax({
            type: "DELETE",
            url: "/playlist",
            data: {"id": id},
            success: function (response) {
                dfr.resolve(response);
            },
            error: function () {
                dfr.reject();
            }
        });
        return dfr.promise();
    };

    self.create = function (name) {
        return $.when($.post("/playlist", {"name": name}));
    };

    self.songs = function (id) {
        return $.when($.get("/playlist", {"id": id}));
    };
}