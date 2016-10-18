"use strict";

var baseUrl = dict.contextPath + "/profile";

function updateUserData() {

    var profileData = {};

    var username = $("#username").val();

    if (username.length < 2 || username.length > 16) {
        $("#fail-message").text("Fail: new username must be more than 2 and less than 16 in length!");
        return;
    }

    if (username !== $("#panelUsername").text()) {
        profileData.username = username;
    }

    var firstName = $("#firstName").val();

    if (firstName !== $("#tdFirstName").text()) {
        profileData.firstName = firstName;
    }

    var lastName = $("#lastName").val();

    if (lastName !== $("#tdLastName").text()) {
        profileData.lastName = lastName;
    }

    var bio = $("#user_profile_bio").val();

    if (bio !== $("#tdBio").text()) {
        profileData.bio = bio;
    }

    return $.when($.get(baseUrl + "/update", profileData));
}

function updatePassword() {

    var password = $("#password").val();

    if ((password.length > 0 && password.length < 4) || password.length > 128) {
        $("#fail-message").text("Fail: new password must be more than 4 and less than 128 in length!");
        return;
    }

    var confirmPassword = $("#confirmPassword").val();

    var profileData = {};

    if (password !== confirmPassword) {
        $("#fail-message").text("Fail: passwords do not match!");
        return;
    } else {
        profileData.password = password;
    }

    return $.when($.get(baseUrl + "/update/password", profileData));
}

function getCurrentUser() {
    return $.when($.get(baseUrl + "/currentUser"));
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