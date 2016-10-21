<#import "macros/macros.ftl" as m>
<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<@m.head>
<title><@spring.message "contactpage.Title"/></title>
<script src="<@spring.url "/resources/js/history.js" />"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.5/validator.min.js"></script>
<link href="<@spring.url "/resources/css/achievements.css" />" rel="stylesheet">
</@m.head>
<@m.body>
    <@m.navigation />

<div class="container">
    <form method="POST" name="achievementForm" role="form" data-toggle="validator" action="javascript:void(null);"
          onsubmit="">
        <div class="form-group">
            <div class="fields">
                <label for="eventTypes">Event type:</label>
                <select class="form-control" id="eventTypes">
                </select>

                <label for="count">Count:</label>
                <input type="number" class="form-control" id="count" min="0" max="1000" required>
            </div>

            <label for="script">Script:</label>
            <textarea class="form-control" rows="5" id="script" required></textarea>
        </div>
    </form>

    <button id="clearBtn" type="button" class="btn btn-default">Clear</button>
    <button id="saveBtn" type="button" class="btn btn-primary">Save</button>
    <span id="message"></span>
</div>


<script type="text/javascript">
    const history = new History();

    function createAchievement() {
        var achievement = {};
        achievement.script = $("#script").val();
        achievement.eventType = $("#eventTypes").val();
        achievement.count = $("#count").val();
        return achievement;
    }

    function saveAchievement() {
        if (!validateForm()) {
            return;
        }
        var achievement = createAchievement();
        $.when($.ajax({
            url: dict.contextPath + "/achievements",
            type: "POST",
            data: JSON.stringify(achievement),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })).fail(function () {
            $("#message").html("failed");
            console.log("fail");
        }).done(function () {
            clear();
            console.log("done");
        });
    }

    function validateForm() {
        var validator = $("form[name=achievementForm]:last").data("bs.validator");
        validator.validate();
        if (validator.hasErrors()) {
            $("#message").html("error");
            return false;
        } else {
            $("#message").html("");
            return true;
        }
    }

    function clear() {
        $("#script").val("");
        $("#count").val("");
        $("#message").html("");
    }

    function updateEventTypesSelect(types) {
        var eventTypeSelect = $("#eventTypes");
        types.forEach(function (type) {
            eventTypeSelect.append($("<option></option>")
                    .attr("value", type)
                    .text(type));
        });
    }

    $("#saveBtn").on("click", function (e) {
        saveAchievement();
    });

    $("#clearBtn").on("click", function (e) {
        clear();
    });

    $(document).ready(function () {
        history.types()
                .then(function (types) {
                    updateEventTypesSelect(types);
                });
    });
</script>
</@m.body>
</html>