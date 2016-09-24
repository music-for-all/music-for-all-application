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

                    <div class="row" id="editProfileTable">
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
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@m.body>
</html>