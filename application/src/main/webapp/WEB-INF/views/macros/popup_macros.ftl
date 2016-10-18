<#import "/spring.ftl" as spring />

<#macro popUp id>
<div id="${id}" class="modal fade">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <#nested>
        </div>
    </div>
</div>
</#macro>

<#macro popupHead title>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
        <span aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title">${title}</h4>
</div>
</#macro>

<#macro popupBody>
<div class="modal-body">
    <#nested>
</div>
</#macro>

<#macro popupFooter>
<div class="modal-footer">
    <#nested>
</div>
</#macro>

<#assign addPlaylistCaption>
    <@spring.message "popupmacro.AddingPlaylist"/>
</#assign>

<#macro popUpAdd id>
    <@popUp id>
        <@popupHead "${addPlaylistCaption}"/>

        <@popupBody>
        <div class="form-group">
            <label for="inputNamePlaylist" class="col-sm-2 control-label"> <@spring.message "popupmacro.NameCaption"/>
                :</label>
            <input type="text" class="form-control" id="inputNamePlaylist"
                   placeholder="<@spring.message "popupmacro.NamePlaceholder"/>">
        </div>
        </@popupBody>

        <@popupFooter>
        <button type="button" class="btn btn-secondary"
                data-dismiss="modal"> <@spring.message "popupmacro.ActionClose"/></button>
        <button id="acceptCreatingPlaylistButton" type="button" class="btn btn-success">
            <@spring.message "popupmacro.ActionAdd"/></button>
        </@popupFooter>
    </@popUp>
</#macro>

<#assign deleteInfo>
    <@spring.message "popupmacro.Deleting"/>
</#assign>

<#macro popUpDelete id>
    <@popUp id>
        <@popupHead "${deleteInfo}"/>

        <@popupBody>
        <p><@spring.message "popupmacro.DeleteHeader"/></p>
        </@popupBody>

        <@popupFooter>
        <button type="button" class="btn btn-success"
                data-dismiss="modal"><@spring.message "popupmacro.ActionNo"/></button>
        <button type="button" id="acceptRemovingPlaylistButton" class="btn btn-danger" data-dismiss="modal">
            <@spring.message "popupmacro.ActionYes"/>
        </button>
        </@popupFooter>
    </@popUp>
</#macro>

<#macro player_Footer>
    <nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
        <div class="container-fluid player-content">
                <div class="col-md-4">
                    <button id="prevFooterBtn" type="button" class="btn btn-default .btn-link">
                        <span class='glyphicon glyphicon-step-backward' aria-hidden='true'></span>
                    </button>
                    <button id="playFooterBtn" type="button" class="btn btn-default btn-success play-track-button">
                        <span class='glyphicon glyphicon-play' aria-hidden='true'></span>
                    </button>
                    <button type="button" id="pauseFooterBtn" class="btn btn-default btn-warning pause-track-button">
                        <span class="glyphicon glyphicon-pause" aria-hidden="true"></span>
                    </button>
                    <button id="nextFooterBtn" type="button" class="btn btn-default .btn-link">
                        <span class='glyphicon glyphicon-step-forward' aria-hidden='true'></span>
                    </button>
                </div>
                <div class="col-md-4">
                    <h5 class="text-center" id="nameInFoot"></h5>
                    <h5 class="text-center" id="artistInFoot"></h5>
                </div>
                <div class="col-md-4">
                </div>
        </div>
    </nav>
</#macro>

<#assign playlistsPopupCaption>
    <@spring.message "label.AddToPlaylist"/>
</#assign>

<#macro playlistsPopup id>
    <@popUp id>
        <@popupHead "${playlistsPopupCaption}"/>

        <@popupBody>
        <ul id="addToPlaylist" class="nav nav-pills nav-stacked"></ul>
        <br/>
        <div class="form-group">
            <button id="createPlaylistBtn" type="button" class="btn btn-success btn-md"
                    title="<@spring.message "popupmacro.ActionAdd"/>">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            </button>

            <input type="text" class="form-control" id="inputNameAddPlaylist"
                   placeholder="<@spring.message "popupmacro.NamePlaceholder"/>">
        </div>
        </@popupBody>

        <@popupFooter>
        <button type="button" id="closePopupButton" class="btn" data-dismiss="modal">
            <@spring.message "popupmacro.ActionClose"/>
        </button>
        </@popupFooter>
    </@popUp>
</#macro>

<#macro followersPopup id>
<script type="text/template" class="followersPopup">
    <div id="${id}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th></th>
                        <th><@spring.message "popupmacro.UserName"/></th>
                        <th><@spring.message "popupmacro.FirstName"/></th>
                        <th><@spring.message "popupmacro.LastName"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <% _.each(data, function(usr){ %>
                    <tr>
                        <td>
                            <div class="avatar">
                                <img src=" <%= usr.userData.picture %>" class="img-responsive img-rounded">
                            </div>
                        </td>
                        <td><a href="/user/show?user_id=<%= usr.id %>"><%= usr.userData.username %></a></td>
                        <td><%= usr.userData.firstName %></td>
                        <td><%= usr.userData.lastName %></td>
                    </tr>
                    <% }); %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</script>
</#macro>

<#macro followingPopup id>
<script type="text/template" class="followingPopup">
    <div id="${id}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel"
         aria-hidden="true">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th></th>
                        <th><@spring.message "popupmacro.UserName"/></th>
                        <th><@spring.message "popupmacro.FirstName"/></th>
                        <th><@spring.message "popupmacro.LastName"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <% _.each(data, function(usr){ %>
                    <tr>
                        <td>
                            <div class="avatar">
                                <img src=" <%= usr.userData.picture %>" class="img-responsive img-rounded">
                            </div>
                        </td>
                        <td><a href="/user/show?user_id=<%= usr.id %>"><%= usr.userData.username %></a></td>
                        <td><%= usr.userData.firstName %></td>
                        <td><%= usr.userData.lastName %></td>
                    </tr>
                    <% }); %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</script>
</#macro>