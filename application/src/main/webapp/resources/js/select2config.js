"use strict";

function tagSearchConfig(contextPath) {
    return {
        ajax: {
            url: contextPath + "/tags",
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
        placeholder: "Tags",
        minimumInputLength: 2,
        templateResult: function (data) {
            return data.text;
        },
        tags: true,
        tokenSeparators: [' ']
    }
}