#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "following.PageTitle"/></title>
<link href="/resources/css/following.css" rel="stylesheet">
<link href="/resources/css/font-awesome.min.css" rel="stylesheet">

</@m.head>

<@m.body>

    <@m.navigation/>

<div class="container">
    <h2>Following activity</h2>

    <div id="following">
        <div class="username text-center" data-toggle="collapse" data-target="#username1">USERNAME1</div>
        <div class="activities">
            <div class="row activity">
                <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
            </div>
            <div class="row activity">
                <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
            </div>

            <div id="username1" class="collapse">
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row  activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
            </div>
        </div>
    </div>
    <div id="following">
        <div class="username text-center" data-toggle="collapse" data-target="#username2">USERNAME2</div>
        <div class="activities">
            <div class="row activity">
                <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
            </div>
            <div class="row activity">
                <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
            </div>

            <div id="username2" class="collapse  ">
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row  activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
            </div>
        </div>
    </div>
    <div id="following">
        <div class="username text-center" data-toggle="collapse" data-target="#username3">USERNAME3</div>
        <div class="activities">
            <div class="row activity">
                <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
            </div>
            <div class="row activity">
                <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
            </div>

            <div id="username3" class="collapse  ">
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
                <div class="row  activity">
                    <div class="col-xs-6 col-sm-9 event">TRACK1</div>
                    <div class="col-xs-6 col-sm-3 date"> 11:11 11.11</div>
                </div>
            </div>
        </div>
    </div>
</div>

</@m.body>
</html>

