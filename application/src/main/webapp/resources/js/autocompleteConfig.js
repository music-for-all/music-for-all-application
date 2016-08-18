
function artistAutocomplete(tags) {
    return {
        source: function (request, response) {
            var data = {
                artistName: request.term
            };
            if (tags.call() != null) {
                $.extend(data, {tags: tags})
            }
            $.ajax({
                url: dict.contextPath + "/artist",
                data: data,
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

function tagAutocomplete(placeholder) {
    return {
        ajax: {
            url: dict.contextPath + "/tags",
            delay: 250,
            data: function (params) {
                return {
                    tagName: params.term
                };
            },
            processResults: function (data, params) {
                return {
                    results: data.map(function (item) {
                        return {id: item.name, text: item.name};
                    })
                };
            }
        },
        allowClear: true,
        multiple: true,
        placeholder: placeholder,
        minimumInputLength: 2,
        templateResult: function (data) {
            return data.text;
        },
        tags: true,
        tokenSeparators: [' ']
    }
}