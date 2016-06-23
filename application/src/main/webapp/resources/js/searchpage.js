function addRow(Artist, Title, Duration, Id) {
    $('#results').append('<tr><td>  <button type="button" class="btn btn-xs btn-success"> ' +
        '<span class="glyphicon glyphicon-play"aria-hidden="true"></span></button> ' +
        '<button type="button" class="btn btn-xs btn-success" onclick="ajaxAddSong(' + Id + ')"> ' +
        '<span class="glyphicon glyphicon-plus"aria-hidden="true"></span></button> ' +
        '</td><td>' + Artist + ' </td><td>' + Title + ' </td><td>' + Duration + ' </td></tr>');
}

function clearAll() {
    $("#results").find("tr:not(:first)").remove();
}

function ajaxSearch(searchQuery, selectedCategory) {
    clearAll();
    $.ajax({
        type: "GET",
        url: "/searchQuery",
        dataType: "json",
        contentType: 'application/json',
        data:  ({
            search: searchQuery,
            category: JSON.stringify(selectedCategory)
        }),
        success: function (response) {
            $.each(response, function () {
                addRow(this.id, this.name, this.location, this.id);//Only for demonstration
            });
        },
        error: function () {
            alert('Error while request..');
        }
    });
}

function search() {
    var searchQuery = $("#word").val(); //Key-word for searching
    if (searchQuery.length > 40) {
        searchQuery = searchQuery.substr(0, 40); //Trimming long line
    }
    var selectedCategory = []; //Array with categories
    $("input:checkbox[name=category]:checked").each(function () {
        selectedCategory.push($(this).val());
    });
    console.log("Query for searching: " + searchQuery);
    console.log("Genres for searching: " + selectedCategory);
    if (searchQuery != "") {
        ajaxSearch(searchQuery, selectedCategory);
    }
}

function ajaxAddSong(AddId) {
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: "POST",
        url: "/addSong",
        data: "songId=" + AddId,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            console.log("Request to add was submitted successfully");
        },
        error: function () {
            alert('Error while request..');
        }
    });
}

