<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<@m.head>
<title><@spring.message "contactpage.Title"/></title>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<link href="<@spring.url "/resources/css/achievements.css" />" rel="stylesheet">
</@m.head>
<@m.body>
    <@m.navigation />

<div class="container">
    <div class="form-group">
        <div class="fields">
            <label for="eventTypes">Event type:</label>
            <select class="form-control" id="eventTypes">
            </select>

            <label for="count">Count:</label>
            <input type="number" class="form-control" id="count" min="0" max="1000">
        </div>

        <label for="script">Script:</label>
        <textarea class="form-control" rows="5" id="script"></textarea>

    </div>

    <button id="saveBtn" type="button" class="btn btn-primary">Save</button>
</div>


<script type="text/javascript">
    const history = new History();

    function saveAchievement() {
        var achievement = {};
        achievement.script = $("#script").val();
        achievement.eventType = $("#eventTypes").val();
        achievement.count = $("#count").val();
        $.when($.post("/achievements", {"achievement": achievement})).then(function () {

        });
    }

    $("#saveBtn").on("click", function (e) {
        saveAchievement();
    });

    $(document).ready(function () {
        history.types()
                .then(function (types) {
                    const eventTypeSelect = $("#eventTypes");
                    types.forEach(function (type) {
                        eventTypeSelect.append($("<option></option>")
                                .attr("value", type)
                                .text(type));
                    });
                });
    });

</script>
</@m.body>
</html>