"use strict";

var baseUrl = dict.contextPath + "/profile";

function updateData() {

    clearMessage();

    var password = $("#password").val();

    if ((password.length > 0 && password.length < 4) || password.length > 128) {
        $("#fail-message").text("Fail: new password must be more than 4 and less than 128 in length!");
        return;
    }

    var confirmPassword = $("#confirmPassword").val();

    if (password.length > 0) {
        if (password !== confirmPassword) {
            $("#fail-message").text("Fail: passwords do not match!");
            return;
        }
    }

    var username = $("#username").val();

    if (username.length < 2 || username.length > 16) {
        $("#fail-message").text("Fail: new username must be more than 2 and less than 16 in length!");
        return;
    }

    var profileData = {
        username: username,
        password: password,
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val(),
        bio: $("#user_profile_bio").val()
    };

    return $.when($.get(baseUrl + "/update", profileData));
}

function getCurrentUser() {
    return $.when($.get(baseUrl + "/currentUser"))
}

function uploadFile(formData) {
    return $.when(
        $.ajax({
            url: "/files/picture",
            type: "POST",
            data: formData,
            cache: false,
            processData: false,
            contentType: false
        }));
}