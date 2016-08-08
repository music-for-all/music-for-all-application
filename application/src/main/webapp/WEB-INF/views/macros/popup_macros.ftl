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
            <label for="inputNamePlaylist" class="col-sm-2 control-label"> <@spring.message "popupmacro.NameCaption"/>:</label>
            <input type="text" class="form-control" id="inputNamePlaylist" placeholder="<@spring.message "popupmacro.NamePlaceholder"/>">
        </div>
        </@popupBody>

        <@popupFooter>
        <button type="button" class="btn btn-secondary" data-dismiss="modal"> <@spring.message "popupmacro.ActionClose"/></button>
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
        <button type="button" class="btn btn-success" data-dismiss="modal"><@spring.message "popupmacro.ActionNo"/></button>
        <button type="button" id="acceptRemovingPlaylistButton" class="btn btn-danger" data-dismiss="modal">
            <@spring.message "popupmacro.ActionYes"/>
        </button>
        </@popupFooter>
    </@popUp>
</#macro>