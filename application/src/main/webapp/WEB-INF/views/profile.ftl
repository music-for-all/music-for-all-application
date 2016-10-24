<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
<script src="<@spring.url "/resources/js/user.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
<link href="<@spring.url "/resources/css/profilepage.css" />" rel="stylesheet"/>
<link href="<@spring.url "/resources/css/switch.css" />" rel="stylesheet"/>
<script src="<@spring.url "/resources/js/profile.js"/>"></script>
<link href="/resources/css/profilepage.css" rel="stylesheet"/>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Profile/>
<div id="container" class="container">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xs-offset-0 col-sm-offset-0 col-md-offset-3 col-lg-offset-3">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title" id="panelUsername"></h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="picture col-md-3 col-lg-3 " align="center">
                            <img id="userPicture"
                                 alt="User picture"
                                 class="profile img-circle img-responsive">
                            <img id="uploadPicture"
                                 alt="User picture"
                                 class="editProfile img-circle img-responsive hidden">
                            <div id="upload" class="editProfile hidden">
                                <span class="btn btn-file btn-sm btn-success">
                                    <span class="glyphicon glyphicon-open input-place"></span> <@spring.message "profilepage.UploadPicture" />
                                    <input type="file" id="image-input" name="filedata" onchange="upload(this);"
                                           accept="image/*">
                            </div>
                        </div>
                        <div class="profile">
                            <div class="col-md-9 col-lg-9 ">
                                <table class="table table-user-information">
                                    <tbody>
                                    <tr>
                                        <td><@spring.message "profilepage.FirstName" /></td>
                                        <td id="tdFirstName"></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.LastName" /></td>
                                        <td id="tdLastName"></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.Email" /></td>
                                        <td id="tdEmail"></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.Bio" /></td>
                                        <td id="tdBio"></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.Broadcasting" /></td>
                                        <td>
                                            <label class="switch pull-left">
                                                <input type="checkbox" id="publicRadio">
                                                <div class="slider round"></div>
                                            </label>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="editProfile hidden">
                            <div class=" col-md-9 col-lg-9 ">
                                <table class="table table-user-information">
                                    <tbody>
                                    <tr>
                                        <td>Username</td>
                                        <td><input id="username" class="form-control" type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.FirstName" /></td>
                                        <td><input id="firstName" class="form-control" type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.LastName" /></td>
                                        <td><input id="lastName" class="form-control" type="text"/></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.Bio" /></td>
                                        <td><textarea class="form-control" cols="30"
                                                      data-bio-label="remaining"
                                                      data-max-length="140"
                                                      id="user_profile_bio" maxlength="140" rows="4"
                                                      placeholder="<@spring.message "profilepage.BioPlaceholder" />"></textarea>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="hidden editProfilePassword">
                            <div class=" col-md-9 col-lg-9 ">
                                <table class="table table-user-information">
                                    <tbody>
                                    <tr>
                                        <td><@spring.message "profilepage.Password" /></td>
                                        <td><input id="password" class="form-control" type="password"
                                                   placeholder="<@spring.message "profilepage.NewPassword" />"/></td>
                                    </tr>
                                    <tr>
                                        <td><@spring.message "profilepage.ConfirmPassword" /></td>
                                        <td><input id="confirmPassword" class="form-control" type="password"
                                                   placeholder="<@spring.message "profilepage.ConfirmPassword" />"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class=" col-md-9 col-lg-9 ">
                            <div class="text-success" id="success-message"></div>
                            <div class="text-danger" id="fail-message"></div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button class="btn btn-default hidden" id="back">
                        <span class="glyphicon glyphicon-arrow-left"></span> <@spring.message "profilepage.Back" />
                    </button>
                    <button class="btn btn-default" id="edit">
                        <span class="glyphicon glyphicon-edit"></span> <@spring.message "profilepage.EditProfile" />
                    </button>
                    <button class="btn btn-default" id="editPassword">
                        <span class="glyphicon glyphicon-edit"></span> <@spring.message "profilepage.EditPassword" />
                    </button>
                    <span class="pull-right">
                    <button class="editProfile btn btn-success hidden" id="updateUser">
                        <span class="glyphicon"></span><@spring.message "profilepage.Update" />
                    </button>
                    <button class="editProfilePassword btn btn-success hidden" id="updatePassword">
                        <span class="glyphicon"></span><@spring.message "profilepage.Update" />
                    </button>
                        </span>
                </div>
            </div>
        </div>
    </div>
</script>
<script type="text/template" class="achievements">
    <div class="achievements">
        <% _.each(data, function(ua){ %>
        <div class="achievement" title="<%= ua.achievement.name %>">
            <% if (ua.status === "DONE") { %>
            <i class="fa fa-check-circle" aria-hidden="true"></i>
            <% } %>
        </div>
        <% }); %>
    </div>
</script>
<script type="text/javascript">
    const max_length_error = 200;

    var stream = new Stream();

    var achievementsRow = _.template(
            $("script.achievements").html()
    );
    
    function showProfileTable() {
        $("#back").addClass("hidden");
        $("#edit").removeClass("hidden");
        $("#editPassword").removeClass("hidden");
        $(".editProfile").addClass("hidden");
        $(".profile").removeClass("hidden");
        $(".editProfilePassword").addClass("hidden");
    }

    function showEditProfileTable() {
        $("#back").removeClass("hidden");
        $("#edit").addClass("hidden");
        $("#editPassword").removeClass("hidden");
        $(".editProfile").removeClass("hidden");
        $(".profile").addClass("hidden");
        $(".editProfilePassword").addClass("hidden");
    }

    function showEditProfilePasswordTable() {
        $("#back").removeClass("hidden");
        $("#editPassword").addClass("hidden");
        $("#edit").removeClass("hidden");
        $(".editProfile").addClass("hidden");
        $(".editProfilePassword").removeClass("hidden");
        $(".profile").addClass("hidden");
    }

    function clearMessage() {
        $("#confirmPassword").text('');
        $("#password").text('');
        $("#success-message").text('');
        $("#fail-message").text('');
    }

    function updateFields(user) {
        document.getElementById("userPicture").src = user.userData.picture;
        document.getElementById("uploadPicture").src = user.userData.picture;
        $("#panelUsername").text(user.userData.username);
        $("#tdFirstName").text(user.userData.firstName);
        $("#tdLastName").text(user.userData.lastName);
        $("#tdBio").text(user.userData.bio);
        $("#tdEmail").text(user.email);
        $("#username").val(user.userData.username);
        $("#firstName").val(user.userData.firstName);
        $("#lastName").val(user.userData.lastName);
        $("#user_profile_bio").text(user.userData.bio);
        if (user.userData.publicRadio) {
            $("#publicRadio").prop("checked", true);
        } else {
            $("#publicRadio").prop("checked", false);
        }
    }

    $("#publicRadio").on("click", function () {
        stream.switchRadio();
    });

    $("#edit").on("click", function () {
        clearMessage();
        showEditProfileTable();
    });

    $("#editPassword").on("click", function () {
        clearMessage();
        showEditProfilePasswordTable();
    });

    $("#back").on("click", function () {
        showProfileTable();
        $("#success-message").text('');
        $("#fail-message").text('');
    });

    function upload(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $("#uploadPicture")
                        .attr('src', e.target.result);
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    function updateProfileData() {
        clearMessage();
        updateUserData().then(function () {
            $("#success-message").text("Updated");
            getCurrentUser().then(function (user) {
                updateFields(user);
            });
            showProfileTable();
        }, function () {
            $("#fail-message").text("Fail");
        });
    }

    $("#updateUser").on("click", function () {
        var formData = new FormData();
        var files = document.getElementById("image-input").files[0];
        if (files !== undefined) {
            formData.append("file", files);
            uploadFile(formData)
                    .then(updateProfileData(), function (xhr, status, error) {
                        if (xhr.responseText.length < max_length_error) {
                            $("#fail-message").text(xhr.responseText);
                        } else {
                            $("#fail-message").text(error);
                        }
                    });
        } else {
            updateProfileData();
        }
    });

    $("#updatePassword").on("click", function () {
        clearMessage();
        updatePassword().then(function () {
            $("#success-message").text("Updated");
            getCurrentUser().then(function (user) {
                updateFields(user);
            });
            showProfileTable();
        }, function () {
            $("#fail-message").text("Fail");
        });
    });

    $(document).ready(function () {
        getCurrentUser().then(function (user) {
            updateFields(user);
            user.achievements(user.id).then(function (achievements) {
                $("#container").append(achievementsRow(achievements));
            });
        });
    });
</script>
</@m.body>
</html>