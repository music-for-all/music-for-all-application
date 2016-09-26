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
                    <h3 class="panel-title">${user.username}</h3>
                </div>
                <div class="panel-body">
                    <div class="row" id="infoProfileTable">
                        <div class="col-md-3 col-lg-3 " align="center">
                            <img src="${user.picture}" alt="User picture" class="img-circle img-responsive">
                        </div>
                        <div class=" col-md-9 col-lg-9 ">
                            <table class="table table-user-information">
                                <tbody>
                                <tr>
                                    <td><@spring.message "profilepage.FirstName" /></td>
                                    <td>${user.firstName}</td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.LastName" /></td>
                                    <td>${user.lastName}</td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.Email" /></td>
                                    <td>${user.email}</td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.Bio" /></td>
                                    <td>${user.bio}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="row hidden" id="editProfileTable">
                        <div class="col-md-3 col-lg-3 " align="center">
                            <img src="${user.picture}" alt="User picture" class="img-circle img-responsive">
                            <span class="btn btn-file btn-sm btn-success">
                                 <span class="glyphicon glyphicon-open input-place"></span> <@spring.message "profilepage.UploadPicture" />
                                <input type="file" name="filedata" accept="image/*"></span>
                        </div>
                        <div class=" col-md-9 col-lg-9 ">
                            <table class="table table-user-information">
                                <tbody>
                                <tr>
                                    <td>Username</td>
                                    <td><input id="username" class="form-control" type="text"
                                               value="${user.username}"/></td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.FirstName" /></td>
                                    <td><input id="firstName" class="form-control" type="text"
                                               value="${user.firstName}"/></td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.LastName" /></td>
                                    <td><input id="lastName" class="form-control" type="text"
                                               value="${user.lastName}"/></td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.Password" /></td>
                                    <td><input id="password" class="form-control" type="text"
                                               placeholder="<@spring.message "profilepage.NewPassword" />"/></td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.ConfirmPassword" /></td>
                                    <td><input id="confirmPassword" class="form-control" type="text"
                                               placeholder="<@spring.message "profilepage.ConfirmPassword" />"/></td>
                                </tr>
                                <tr>
                                    <td><@spring.message "profilepage.Bio" /></td>
                                    <td><textarea class="form-control" cols="30"
                                                  data-bio-label="remaining"
                                                  data-max-length="140"
                                                  id="user_profile_bio" maxlength="140" rows="4"
                                                  placeholder="<@spring.message "profilepage.BioPlaceholder" />">${user.bio}</textarea>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="text-success" id="success-message"></div>
                    <div class="text-danger" id="fail-message"></div>
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
    $("#edit").on("click", function (e) {
        clearMessage();
        $("#back").removeClass("hidden");
        $("#update").removeClass("hidden");
        $("#editProfileTable").removeClass("hidden");
        $("#infoProfileTable").addClass("hidden");
        $("#edit").addClass("hidden");
    <#--$("#username").val("${user.username}");-->
    });

    $("#back").on("click", function (e) {
        showProfileTable();
        $("#success-message").text('');
        $("#fail-message").text('');
    });

    function showProfileTable() {
        $("#back").addClass("hidden");
        $("#update").addClass("hidden");
        $("#editProfileTable").addClass("hidden");
        $("#infoProfileTable").removeClass("hidden");
        $("#edit").removeClass("hidden");
    }

    $("#update").on("click", function (e) {
        update();
    });

    function clearMessage() {
        $("#success-message").text('');
        $("#fail-message").text('');
    }
</script>
</@m.body>
</html>