<#import "macros/macros.ftl" as m>
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title>Search</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="/resources/js/searchpage.js"></script>
<link href="/resources/css/searchpage.css" rel="stylesheet">
<link href="/resources/css/font-awesome.min.css" rel="stylesheet">
</@m.head>
<@m.body>

    <@m.navigation [
    {"isActive": false, "url": '/main', "title": "Main"},
    {"isActive": true, "url": '/search', "title": "Search"}]/>

<div class="container">
    <form class="form-inline text-center " id="searchForm" method="post">
        <div class="input-group " placeholder="Search">
            <input id="word" class="form-control" type="text" value="" placeholder="Search" name="q"
                   autofocus="autofocus"/>

            <div class="input-group-btn">
                <button id="searchButton" data-style="slide-left" class="btn btn-success " type="button"
                        onclick="search()"><i id="icon" class="fa fa-search"></i>Search
                </button>
            </div>
        </div>

        <label class="checkbox-inline">
            <input type="checkbox" name="category" value="Artists"> Artists
        </label>
        <label class="checkbox-inline">
            <input type="checkbox" name="category" value="Titles"> Titles
        </label>
        <label class="checkbox-inline">
            <input type="checkbox" name="category" value="Tags"> Tags
        </label>

    </form>
    <div id="resultsd" class="well ">
        <table id="results" class="table table-hover table-striped table-condensed ">
            <thead>
            <tr>

                <th>Actions</th>
                <th>Artist</th>
                <th>Title</th>
                <th>Duration</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</@m.body>
</html>