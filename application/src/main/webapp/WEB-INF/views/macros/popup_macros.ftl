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
<#assign nameCaption>
    <@spring.message "popupmacro.NameCaption"/>
</#assign>
<#assign namePlaceholder>
    <@spring.message "popupmacro.NamePlaceholder"/>
</#assign>
<#assign actionClose>
    <@spring.message "popupmacro.ActionClose"/>
</#assign>
<#assign actionAdd>
    <@spring.message "popupmacro.ActionAdd"/>
</#assign>

<#macro popUpAdd id>
    <@popUp id>
        <@popupHead "${addPlaylistCaption}"/>

        <@popupBody>
        <div class="form-group">
            <label for="inputNamePlaylist" class="col-sm-2 control-label">${nameCaption}:</label>
            <input type="text" class="form-control" id="inputNamePlaylist" placeholder="${namePlaceholder}">
        </div>
        </@popupBody>

        <@popupFooter>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">${actionClose}</button>
        <button id="acceptCreatingPlaylistButton" type="button" class="btn btn-success">${actionAdd}</button>
        </@popupFooter>
    </@popUp>
</#macro>

<#assign deleteHeader>
    <@spring.message "popupmacro.DeleteHeader"/>
</#assign>
<#assign actionNo>
    <@spring.message "popupmacro.ActionNo"/>
</#assign>
<#assign actionYes>
    <@spring.message "popupmacro.ActionYes"/>
</#assign>

<#macro popUpDelete id>
    <@popUp id>
        <@popupHead "Deleting playlist"/>

        <@popupBody>
        <p>${deleteHeader}</p>
        </@popupBody>

        <@popupFooter>
        <button type="button" class="btn btn-success" data-dismiss="modal">${actionNo}</button>
        <button type="button" id="acceptRemovingPlaylistButton" class="btn btn-danger" data-dismiss="modal">
        ${actionYes}
        </button>
        </@popupFooter>
    </@popUp>
</#macro>