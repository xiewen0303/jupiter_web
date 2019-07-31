$(function () {
    searchList();

    $(document).on('click',"#addPermission", function (event) {
        if ($("#permissionForm").valid(this) == false) {
            return false;
        } else {
            doAdd();
        }
    });

    $(document).on('click',"#updatePermission", function (event) {
        if ($("#permissionForm").valid(this) == false) {
            return false;
        } else {
            doUpdate();
        }
    });
})

/**
 * 点击添加弹出添加框
 */
function initAddPermission() {
    $("#addPermission").removeAttr("disabled");
    formtemplate(0);
    $("#permissionForm").validation({icon: false});
    $parentId.val(null);
    $('#permissionModal').modal("show");
}

function doAdd() {
    $("#addPermission").attr("disabled",true);

    $.ajax({
        processData: false,
        type: 'POST',
        url: "/permission/savePermission",
        data: $('#permissionForm').serialize(),
        success: function(data){
            if(data.code == 200){
                searchList();
                $.scojs_message('添加权限成功', $.scojs_message.TYPE_OK);
                $('#permissionModal').modal("hide");
            } else {
                $.scojs_message('添加权限失败', $.scojs_message.TYPE_ERROR);
                $("#addPermission").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message("系统异常,添加权限失败,请联系管理员", $.scojs_message.TYPE_ERROR);
            $("#addPermission").removeAttr("disabled");
        }
    });
}

function initEditPermissionInfo(id) {
    $("#updatePermission").removeAttr("disabled");
    $.ajax({
        processData: false,
        type: 'GET',
        url: "/permission/permissionEdit/init/"+id,
        success: function(data){
            if(data.code == 200){
                formtemplate(data.module);
                $parentId.val(data.module.parentId).trigger("change");
                $('#permissionModal').modal("show");
            } else {
                $.scojs_message('获取权限信息失败', $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $.scojs_message('获取权限信息异常', $.scojs_message.TYPE_ERROR);
        }
    });
}

function doUpdate(){
    $("#updatePermission").attr("disabled",true);

    $.ajax({
        processData: false,
        type: 'POST',
        url: "/permission/updatePermission",
        dataType: 'json',
        data: $('#permissionForm').serialize(),
        success: function(data){
            if(data.code == 200){
                searchList();
                $.scojs_message('权限信息修改成功', $.scojs_message.TYPE_OK);
                $('#permissionModal').modal("hide");
            } else {
                $.scojs_message('权限信息修改失败', $.scojs_message.TYPE_ERROR);
                $("#updatePermission").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message('权限信息修改异常', $.scojs_message.TYPE_ERROR);
            $("#updatePermission").removeAttr("disabled");
        }
    });
}

function formtemplate(data){
    var templateoutput;
    if(data == 0)
    {
        templateoutput = template('tpl-form',{add:true});
        $("#form-modal").html("").html(templateoutput);
    } else {
        templateoutput = template('tpl-form',data);
        $("#form-modal").html("").html(templateoutput);
    }
    $("#permissionForm").validation({icon: false});
    $parentId = $("#parentId").select2({language: 'zh-CN'});
}

function searchList(f) {
    var param = {};
    if(f){
        $.extend(param,$(f.form).serializeObject());
    }
    $("#tbody").datagrid({
        url:'/permission/permissionList',
        data:param
    });
}

function deletePermission(id){
    bootbox.confirm({
        title: "删除",
        message: "是否删除权限信息",
        buttons: {
            confirm: {
                label: '是',
                className: 'btn-info'
            },
            cancel: {
                label: '否',
                className: 'btn-default'
            }
        },
        callback: function (result) {
            if(result){
                $.ajax({
                    processData: false,
                    type: 'GET',
                    url: "/permission/deletePermission/"+id,
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function(data){
                        if(data.code == 200){
                            searchList();
                            $.scojs_message('删除权限信息成功', $.scojs_message.TYPE_OK);
                        } else {
                            $.scojs_message('删除权限信息失败', $.scojs_message.TYPE_ERROR);
                        }
                    },
                    error: function (data) {
                        $.scojs_message('删除权限信息异常', $.scojs_message.TYPE_ERROR);
                    }
                });
            }
        }
    });
}