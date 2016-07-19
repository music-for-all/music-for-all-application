/**
 * @author ENikolskiy on 6/26/2016.
 */

function Track(contextPath) {

    var self = this;
    var baseUrl = contextPath + "/tracks";

    self.createJson = function (tags, artist, name, file) {
        var obj = new Object();
        obj.tags = tags.split(",");
        obj.artist = artist;
        obj.name = name;
        obj.location = "unknown";

        var formData = new FormData();
        formData.append('track', new Blob([JSON.stringify(obj)], {
            type: "application/json"
        }));
        formData.append("file", file);
        $.ajax({
            url: "/files",
            type: 'POST',
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                showMessage(data, "success");
                clearInputs();
            },
            error: function (xhr, status, error) {
                if (xhr.responseText.length < max_length_error) {
                    showMessage(xhr.responseText, "danger");
                } else {
                    showMessage(error, "danger");
                }
            }
        });
    }

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