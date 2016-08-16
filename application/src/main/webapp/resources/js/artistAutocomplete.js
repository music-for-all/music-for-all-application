
function artSearchConfig(tags) {
    return {
        source:   function (request, response) {
            var th = $(this);
            $.ajax({
                url: '/artist',
                data: {
                    artistName: request.term,
                    tags: $(tags).val()
                },
                traditional: true,
                success: function (data) {
                    var array = data.error ? [] : $.map(data, function (item) {
                        return {
                            label: item
                        };
                    });
                    response(array);
                }
            });
        }
    }
}