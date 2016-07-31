<#import "macros/macros.ftl" as m>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Friends</title>
<link href="/resources/css/contactManager.css" rel="stylesheet"/>
</@m.head>
<@m.body>

    <@m.navigation m.pages.Contacts/>

<div id="container" class="container">
    <div class="well col-md-8 col-md-offset-2 text-center">
        <div class="col-md-2">
            <ul class="nav nav-pills nav-stacked">
                <li class="active"><a href="#tab1" data-toggle="tab">Following</a></li>
                <li><a href="#tab2" data-toggle="tab">Followers</i></a></li>
            </ul>
        </div>
        <div class="col-md-offset-1 col-md-6">
            <div class="input-group ">
                <input id="word" class="form-control" type="text" value="" placeholder="Search" name="q"/>

                <div class="input-group-btn">
                    <button id="searchButton" data-style="slide-left" class="btn btn-success " type="button">
                        <i id="icon" class="fa fa-search"></i>
                    </button>
                </div>
            </div>
        </div>
        <div class="tab-content col-md-offset-1 col-md-6">
            <div class="tab-pane active" id="tab1">

            </div>
            <div class="tab-pane" id="tab2">

            </div>
        </div>
    </div>
</div>
<i class="icon-user"></i>

</@m.body>
</html>