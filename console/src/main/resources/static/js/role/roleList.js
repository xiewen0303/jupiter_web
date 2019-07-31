$(function () {
    searchList();

    $(document).on('click',"#addRole", function (event) {
        if ($("#roleForm").valid(this) == false) {
            return false;
        } else {
            doAdd();
        }
    });

    $(document).on('click',"#updateRole", function (event) {
        if ($("#roleForm").valid(this) == false) {
            return false;
        } else {
            doUpdate();
        }
    });
})

function searchList(f) {
    var param = {};
    if(f){
        $.extend(param,$(f.form).serializeObject());
    }
    $("#tbody").datagrid({
        url:'/role/roleList',
        data:param
    });
}

/**
 * 点击添加弹出添加框
 */
function initAddRole() {
    $("#addRole").removeAttr("disabled");
    formtemplate(0);
    $("#roleForm").validation({icon: false});
    $('#roleModal').modal("show");
}

function doAdd() {
    $("#addRole").attr("disabled",true);

    $.ajax({
        processData: false,
        type: 'POST',
        url: "/role/saveRole",
        data: $('#roleForm').serialize(),
        success: function(data){
            if(data.code == 200){
                searchList(1,10);
                $.scojs_message('添加角色成功', $.scojs_message.TYPE_OK);
                $('#roleModal').modal("hide");
            } else {
                $.scojs_message('添加角色失败', $.scojs_message.TYPE_ERROR);
                $("#addRole").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message("系统异常,添加角色失败,请联系管理员", $.scojs_message.TYPE_ERROR);
            $("#addRole").removeAttr("disabled");
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
    $("#roleForm").validation({icon: false});
}

function initEditRoleInfo(id) {
    $("#updateRole").removeAttr("disabled");
    $.ajax({
        processData: false,
        type: 'GET',
        url: "/role/roleEdit/init/"+id,
        success: function(data){
            if(data.code == 200){
                formtemplate(data.module);
                $('#roleModal').modal("show");
            } else {
                $.scojs_message('获取角色信息失败', $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $.scojs_message('获取角色信息异常', $.scojs_message.TYPE_ERROR);
        }
    });
}

function doUpdate(){
    $("#updateRole").attr("disabled",true);

    $.ajax({
        processData: false,
        type: 'POST',
        url: "/role/updateRole",
        dataType: 'json',
        data: $('#roleForm').serialize(),
        success: function(data){
            if(data.code == 200){
                searchList();
                $.scojs_message('角色信息修改成功', $.scojs_message.TYPE_OK);
                $('#roleModal').modal("hide");
            } else {
                $.scojs_message('角色信息修改失败', $.scojs_message.TYPE_ERROR);
                $("#updateRole").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message('角色信息修改异常', $.scojs_message.TYPE_ERROR);
            $("#updateRole").removeAttr("disabled");
        }
    });
}

function checkAllTree() {
    $resourcesTree.jstree(true).check_all();
}

function uncheckAllTree() {
    $resourcesTree.jstree(true).uncheck_all();
}

function authorize() {
    $("#updateResource").attr("disabled",true);
    var permissionList = $('#resourcesTree').jstree(true).get_selected();
    var treeRoleId = $("#treeRoleId").val();
    $.ajax({
        processData: false,
        type: 'POST',
        url: "/permission/permission/authorize",
        contentType: 'application/json',
        data: JSON.stringify({
            roleId: treeRoleId,
            permissionList:permissionList
        }),
        dataType: 'json',
        success: function(data){
            if(data.code == 200){
                $('#resourcesTreeModel').modal("hide");
                $.scojs_message(data.msg, $.scojs_message.TYPE_OK);
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
                $("#updateResource").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message('角色授权失败', $.scojs_message.TYPE_ERROR);
            $("#updateResource").removeAttr("disabled");
        }
    });

}

var $resourcesTree;
function initAuthorize(roleId) {
    $("#updateResource").removeAttr("disabled");
    //初始化之前需要先清空资源树 否则不会生成新的树
    $.jstree.destroy('#resourcesTree');
    $('#resourcesTreeModel').modal("show");
    $('#treeRoleId').val(roleId);
    $.ajax({
        cache: false,
        processData: false,
        type: 'GET',
        url: "/permission/permissionTree/"+roleId,
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $resourcesTree = $('#resourcesTree').jstree({
                'core': {
                    'data': data.module
                },
                "plugins": ["checkbox"],
                "checkbox": {
                    "three_state": false,//父子级别级联选择
                }
            })
        }
    });

}