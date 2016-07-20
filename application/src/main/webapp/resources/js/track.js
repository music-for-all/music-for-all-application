/**
 * @author ENikolskiy on 6/26/2016.
 */

function Track(contextPath) {

    var self = this;
    var baseUrl = contextPath + "/tracks";

    self.createJson = function (formData) {
        return $.when(
            $.ajax({
                url: "/files",
                type: 'POST',
                data: formData,
                cache: false,
                processData: false,
                contentType: false
            }));
    };

    self.remove = function (id) {
        return $.when(
            $.ajax({
                url: baseUrl + "/" + id,
                type: "DELETE"
            }));
    };

    self.create = function (name) {
        return $.when($.post(baseUrl, {"name": name}));
    };

    self.get = function (id) {
        return $.when($.get(baseUrl + "/" + id));
    };

    self.all = function () {
        return $.when($.get(baseUrl));
    };
}