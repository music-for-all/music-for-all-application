/**
 * @author ENikolskiy on 6/24/2016.
 */
function Playlist() {

    var self = this;

    self.remove = function (id) {
        return $.when(
            $.ajax({
                type: "DELETE",
                url: "/playlist/" + id
            }));
    };

    self.create = function (name) {
        return $.when($.post("/playlist", {"name": name}));
    };

    self.get = function (id) {
        return $.when($.get("/playlist", {"id": id}));
    };
}