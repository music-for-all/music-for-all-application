<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<@m.head>
<title><@spring.message "searchpage.Title"/></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="/resources/js/searchpage.js"></script>
<link href="/resources/css/searchpage.css" rel="stylesheet">
<link href="/resources/css/font-awesome.min.css" rel="stylesheet">
</@m.head>
<@m.body>

    <@m.navigation m.pages.Search/>

<div class="container">
    <form id="search-form" class="form-inline text-center ">
        <div class="input-group">
            <input id="title" class="form-control" type="text" value="" placeholder="<@spring.message "placeholder.Title"/>"
                   name="title" autofocus="autofocus" />
            <input id="artist" class="form-control" type="text" value="" placeholder="<@spring.message "placeholder.Artist"/>"
                   name="artist" />
            <input id="album" class="form-control" type="text" value="" placeholder="<@spring.message "placeholder.Album"/>"
                   name="album" />
            <input id="tags" class="form-control" type="text" value="" placeholder="<@spring.message "placeholder.Tags"/>"
                   name="tags" />
            <div class="input-group-btn">
                <input id="searchButton" data-style="slide-left" class="btn btn-success "
                       type="submit" value="<@spring.message "searchpage.SubmitButton"/>" />
            </div>
        </div>
    </form>

    <span id="status-message" class="well"></span>
    <div id="resultsd" class="well ">
        <table id="results" class="table table-hover table-striped table-condensed ">
            <thead>
            <tr>
                <th></th>
                <th><@spring.message "songTable.Artist"/></th>
                <th><@spring.message "songTable.Title"/></th>
                <th><@spring.message "songTable.Album"/></th>
                <th><@spring.message "songTable.Tags"/></th>
            </tr>
            </thead>

            <tr id="row-template" style="display: none">
                <td>
                    <button type="button" class="btn btn-xs btn-success">
                        <span class="glyphicon glyphicon-play" aria-hidden="true"></span>
                    </button>
                    <button type="button" class="btn btn-xs btn-success">
                        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    </button>
                </td>
                <td><@spring.message "songTable.Artist"/></td>
                <td><@spring.message "songTable.Title"/></td>
                <td><@spring.message "songTable.Album"/></td>
                <td><@spring.message "songTable.Tags"/></td>
            </tr>

        </table>
    </div>
    <a id="scroll-to-top" href="#top" title="Scroll to top"><@spring.message "searchpage.ScrollToTop"/></a>
</div>
</@m.body>
</html>