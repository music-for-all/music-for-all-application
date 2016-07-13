/**
 * @author ENikolskiy on 6/26/2016.
 */
function Track() {

    var self = this;

    self.createJson = function (tags, artist, name) {
        var obj = new Object();
        obj.tags = tags.split(",");
        obj.artist = artist;
        obj.name = name;
        obj.location = "unknown";
        return JSON.stringify(obj);
    }
}