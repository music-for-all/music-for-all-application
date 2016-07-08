/**
 * @author ENikolskiy on 6/26/2016.
 */
function Track() {

    var self = this;

    self.create = function (tags, artist, name) {
        return $.when($.post("/tracks", {"tags": tags, "artist": artist, "name": name}));
    };
}