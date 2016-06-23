function login() {
    var login = $("#inputLogin").val();
    var pass = $("#inputPassword").val();
    alert("Login: " + login + ", Pass: " + pass);
    if (searchQuery != "") {
        dummy(searchQuery, 15);
    }
}