/**
 * @author ENikolskiy on 6/26/2016.
 */
function Track() {

    var self = this;

    self.remove = function (id) {
        return $.when(
            $.ajax({
                url: "/tracks/" + id,
                type: "DELETE"
            }));
    };

    self.create = function (name) {
        return $.when($.post("/tracks", {"name": name}));
    };

    self.get = function (id) {
        return $.when($.get("/tracks/" + id));
    };

    self.all = function () {
        return $.when($.get("/tracks"));
    };
}