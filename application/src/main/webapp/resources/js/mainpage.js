function addRow(Artist, Title, Duration, Id) {
    $("#results").append("<tr><td><button type='button' class='btn btn-xs btn-success'>" +
    "<span class='glyphicon glyphicon-play'aria-hidden='true'></span></button>" +
    "<button type='button' class='btn btn-xs btn-danger' onclick='DeleteSongFunction(this," + Id + ")'>" +
    "<span class='glyphicon glyphicon-remove'aria-hidden='true'></span></button>" +
    "</td><td>" + Artist + "</td><td>" + Title + "</td><td>" + Duration + "</td></tr>");
}

function clearAll() {
    $("#results").find("tr:not(:first)").remove();
}

function clearAllPlaylists() {
    $("#playlists").find("li:not(:first)").remove();
}

function addPlaylist(Name, Id) {
    if (Name === "") {
        Name = "Untitled";
    }
    if (Name.length > 20) {
        Name = Name.substr(0, 20); //Trimming long line
    }
    $("#playlists").append("<li id='" + Id + "'><a href='#' data-value='" + Name + "'>" + Name + "</a></li>");
}

function DeleteSongFunction(o, Id) {
    var p = o.parentNode.parentNode;
    p.parentNode.removeChild(p);
}
