/**
 * @author ENikolskiy on 6/26/2016.
 */
function Track() {

    var self = this;

    self.remove = function (id) {
        return $.when(
            $.ajax({
                url: "/track/" + id,
                type: "DELETE"
            }));
    };

    self.create = function (name) {
        return $.when($.post("/track", {"name": name}));
    };

    self.get = function (id) {
        return $.when($.get("/track/" + id));
    };
}