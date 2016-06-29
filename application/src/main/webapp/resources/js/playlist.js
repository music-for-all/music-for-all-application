/**
 * @author ENikolskiy on 6/24/2016.
 */
function Playlist() {

    var self = this;

    self.remove = function (id) {
        return $.when(
            $.ajax({
                url: "/playlists/" + id,
                type: "DELETE"
            }));
    };

    self.create = function (name) {
        return $.when($.post("/playlists", {"name": name}));
    };

    self.get = function (id) {
        return $.when($.get("/playlists/" + id));
    };

    self.all = function () {
        return $.when($.get("/playlists"));
    };
}