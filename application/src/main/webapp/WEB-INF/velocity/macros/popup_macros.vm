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

<#macro popUpAdd id>
    <@popUp id>
        <@popupHead "Adding playlist"/>

        <@popupBody>
        <div class="form-group">
            <label for="inputNamePlaylist" class="col-sm-2 control-label">Name:</label>
            <input type="text" class="form-control" id="inputNamePlaylist" placeholder="Name">
        </div>
        </@popupBody>

        <@popupFooter>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button id="acceptCreatingPlaylistButton" type="button" class="btn btn-success">Add</button>
        </@popupFooter>
    </@popUp>
</#macro>

<#macro popUpDelete id>
    <@popUp id>
        <@popupHead "Deleting playlist"/>

        <@popupBody>
        <p>Are you sure?</p>
        </@popupBody>

        <@popupFooter>
        <button type="button" class="btn btn-success" data-dismiss="modal">No</button>
        <button type="button" id="acceptRemovingPlaylistButton" class="btn btn-danger" data-dismiss="modal">
            Yes
        </button>
        </@popupFooter>
    </@popUp>
</#macro>