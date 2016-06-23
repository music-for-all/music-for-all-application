function addRow(Artist, Title, Duration) {
    $('#results').append('<tr><td><button type="button" class="btn btn-xs btn-success">' +
        '<span class="glyphicon glyphicon-play" aria-hidden="true"></span></button> ' +
        '<button type="button" class="btn btn-xs btn-danger" onclick="DeleteSongFunction(this)"> ' +
        '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button></td>' +
        '<td>' + Artist + ' </td><td>' + Title + ' </td><td>' + Duration + ' </td></tr>');
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

function addPlaylist(Name, Id) {
    if (Name == "") {
        Name = "Untitled";
    }
    if (Name.length > 20) {
        Name = Name.substr(0, 20); //Trimming long line
    }
    $('#playlists').append(' <li id="' + Id + '"><a href="#" data-value="' + Name + '">' + Name + '</a></li>');
}

function ajaxGetPlaylist(Play) {
    $.ajax({
        type: "GET",
        url: "/getPlayList",
        dataType: "json",
        data: "playlistID=" + Play,
        success: function (response) {
            $.each(response, function () {
                addRow(this.id, this.name, this.location);//Only for demonstration
            });
        },
        error: function () {
            alert('Error while request..');
        }
    });
}

function ajaxGetPlaylists() {
    $.ajax({
        type: "GET",
        url: "/getPlayLists",
        dataType: "json",
        success: function (response) {
            $.each(response, function () {
                addPlaylist(this.name, this.id);
            });
        },
        error: function () {
            alert('Error while request..');
        }
    });
}

function ajaxAddPlaylist(Name) {
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: "POST",
        url: "/addPlaylist",
        data: "playlist=" + Name,
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

function ajaxDeletePlaylist(Delete) {
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        type: "POST",
        url: "/deletePlaylist",
        data: "deleteID=" + Delete,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            console.log("Request to delete was submitted successfully");
        },
        error: function () {
            alert('Error while request..' + Delete);
        }
    });
}