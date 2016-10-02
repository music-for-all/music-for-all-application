<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
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
                        <div class="col-md-3 col-lg-3 " align="center">
                            <img id="userPicture"
                                 alt="User picture"
                                 class="img-circle img-responsive">
                            <img id="uploadPicture"
                                 alt="User picture"
                                 class="img-circle img-responsive hidden">
                            <div id="upload" class="hidden">
                                <span class="btn btn-file btn-sm btn-success">
                                    <span class="glyphicon glyphicon-open input-place"></span> <@spring.message "profilepage.UploadPicture" />
                                    <input type="file" id="image-input" name="filedata" onchange="upload(this);"
                                           accept="image/*">
                            </div>
                        </div>
                        <div id="infoProfileTable">
                            <div class=" col-md-9 col-lg-9 ">
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
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="hidden" id="editProfileTable">
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
                    <button class="btn btn-success hidden" id="update">
                        <span class="glyphicon"></span><@spring.message "profilepage.Update" />
                    </button>
                    <button class="btn btn-default" id="edit">
                        <span class="glyphicon glyphicon-edit"></span> <@spring.message "profilepage.EditProfile" />
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    const max_length_error = 200;

    function showProfileTable() {
        $("#back").addClass("hidden");
        $("#uploadPicture").addClass("hidden");
        $("#update").addClass("hidden");
        $("#editProfileTable").addClass("hidden");
        $("#upload").addClass("hidden");
        $("#infoProfileTable").removeClass("hidden");
        $("#userPicture").removeClass("hidden");
        $("#edit").removeClass("hidden");
    }

    function hideProfileTable() {
        $("#back").removeClass("hidden");
        $("#userPicture").addClass("hidden");
        $("#update").removeClass("hidden");
        $("#editProfileTable").removeClass("hidden");
        $("#upload").removeClass("hidden");
        $("#infoProfileTable").addClass("hidden");
        $("#uploadPicture").removeClass("hidden");
        $("#edit").addClass("hidden");
    }

    function clearMessage() {
        $("#success-message").text('');
        $("#fail-message").text('');
    }

    function updateFields(user) {
        document.getElementById("userPicture").src = user.picture;
        document.getElementById("uploadPicture").src = user.picture;
        $("#panelUsername").text(user.username);
        $("#tdFirstName").text(user.firstName);
        $("#tdLastName").text(user.lastName);
        $("#tdBio").text(user.bio);
        $("#tdEmail").text(user.email);
        $("#username").val(user.username);
        $("#firstName").val(user.firstName);
        $("#lastName").val(user.lastName);
        $("#user_profile_bio").text(user.bio);
    }

    $("#edit").on("click", function () {
        clearMessage();
        hideProfileTable();
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
        updateData().then(function (user) {
            updateFields(user);
            $("#success-message").text("Updated");
            showProfileTable();
        }, function () {
            $("#fail-message").text("Fail");
        });
    }

    $("#update").on("click", function () {
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

    $(document).ready(function () {
        getCurrentUser().then(function (user) {
            updateFields(user);
        });
    });
</script>
</@m.body>
</html>