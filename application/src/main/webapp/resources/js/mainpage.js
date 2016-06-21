function addRow(Artist, Title, Duration) {
    $('#results').append('<tr><td><button type="button" class="btn btn-xs btn-success">' +
        '<span class="glyphicon glyphicon-play" aria-hidden="true"></span></button> ' +
        '<button type="button" class="btn btn-xs btn-danger" onclick="DeleteSongFunction(this)"> ' +
        '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button></td>' +
        '<td>' + Artist + ' </td><td>' + Title + ' </td><td>' + Duration + ' </td></tr>');
}

function addRowArray(Artist, Title, Duration) {

    for (var i = 0; i < Title.length; i++) {
        $('#results').append('<tr><td><button type="button" class="btn btn-xs btn-success">' +
            '<span class="glyphicon glyphicon-play" aria-hidden="true"></span></button> ' +
            '<button type="button" class="btn btn-xs btn-danger" onclick="DeleteSongFunction(this)"> ' +
            '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button></td>' +
            '<td>' + Artist[i] + ' </td><td>' + Title[i] + ' </td><td>' + Duration[i] + ' </td></tr>');
    }
}

function clearAll() {
    $("#results").find("tr:not(:first)").remove();
}

function DeleteSongFunction(o) {
    var p = o.parentNode.parentNode;
    p.parentNode.removeChild(p);
    //Write: Send request for deleting song to server
}

function clearAllPlaylists() {
    $("#playlists").find("li:not(:first)").remove();
}

function addPlaylist(Name) {
    if (Name == "") {
        Name = "Untitled";
    }
    if (Name.length > 20) {
        Name = Name.substr(0, 20); //Trimming long line
    }
    $('#playlists').append(' <li ><a href="#" data-value="' + Name + '">' + Name + '</a></li>');
}

function dummy() { //Only for demonstration, delete after merging with ajax
    var min = 2;
    var max = 15;
    var rand = min + Math.random() * (max - min)
    rand = Math.round(rand);
    clearAll();
    for (var i = 0; i < rand; i++) {
        addRow("test data", "test data", "2:22");
    }
}




