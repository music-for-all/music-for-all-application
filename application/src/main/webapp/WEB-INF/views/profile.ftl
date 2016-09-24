<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "profilepage.Title"/></title>
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
                                </tbody>
                            </table>
                            <div class="text-success" id="status-message"></div>
                        </div>
                    </div>

                    <div class="row hidden" id="editProfileTable">
                        <div class="col-md-3 col-lg-3 " align="center">
                            <img src="${user.picture}" alt="User picture" class="img-circle img-responsive">
                            <span class="btn btn-file btn-sm btn-success">
                                 <span class="glyphicon glyphicon-open input-place"></span>Upload new picture<input
                                    type="file" name="filedata" accept="image/*"></span>
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
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button class="btn btn-default hidden" id="back">
                        <span class="glyphicon glyphicon-arrow-left"></span> Back
                    </button>
                    <button class="btn btn-default hidden" id="update">
                        <span class="glyphicon"></span> Update
                    </button>
                    <button class="btn btn-default" id="edit">
                        <span class="glyphicon glyphicon-edit"></span> Edit profile
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $("#edit").on("click", function (e) {
        $("#back").removeClass("hidden");
        $("#update").removeClass("hidden");
        $("#editProfileTable").removeClass("hidden");
        $("#infoProfileTable").addClass("hidden");
        $("#edit").addClass("hidden");
    });

    $("#back").on("click", function (e) {
        showProfileTable();
        $("#status-message").text('');
    });

    $("#update").on("click", function (e) {
        showProfileTable();
        $("#status-message").text('updated');
    });

    function showProfileTable() {
        $("#back").addClass("hidden");
        $("#update").addClass("hidden");
        $("#editProfileTable").addClass("hidden");
        $("#infoProfileTable").removeClass("hidden");
        $("#edit").removeClass("hidden");
    }
</script>
</@m.body>
</html>