function addRow(Artist, Title, Duration){
    $('#results').append('<tr><td>  <button type="button" class="btn btn-xs btn-success"> ' +
        '<span class="glyphicon glyphicon-play"aria-hidden="true"></span></button> ' +
        '<button type="button" class="btn btn-xs btn-success"> ' +
        '<span class="glyphicon glyphicon-plus"aria-hidden="true"></span></button> ' +
        '</td><td>' + Artist + ' </td><td>' + Title + ' </td><td>' + Duration + ' </td></tr>');
}

function clearAll(){
    $("#results").find("tr:not(:first)").remove();
}

function dummy(searchQuery, count){ //Only for demonstration, delete after merging with ajax
    clearAll();
    for(var i=0; i<parseInt(count);i++){
        addRow(searchQuery, searchQuery, "2:22");
    }
}
function search(){
    var searchQuery = $("#word").val(); //Key-word for searching
    var selectedCategory = []; //Array with categories
    $("input:checkbox[name=category]:checked").each(function(){
        selectedCategory.push($(this).val());
    });
    console.log("Query for searching: " + searchQuery);
    console.log("Genres for searching: " + selectedCategory);
    if(searchQuery!="") {
        dummy(searchQuery,15);
    }
}

