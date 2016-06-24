/**
 * @author ENikolskiy on 6/24/2016.
 */
function Playlist() {

    var self = this;

    self.delete = function (id) {
        return $.when($.delete("/playlist", {"id": id}));
    };

    self.create = function (name) {
        return $.when($.post("/playlist", {"name": name}));
    };

    self.songs = function (id) {
        return $.when($.get("/playlist", {"id": id}));
    };
}