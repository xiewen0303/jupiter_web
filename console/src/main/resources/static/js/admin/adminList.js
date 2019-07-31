$(function () {
    searchList();
    //添加验证
    $(document).on('click',"#addAdmin", function (event) {
        if ($("#adminForm").valid(this) == false) {
            return false;
        } else {
            doAdd();
        }
    });

    $(document).on('click',"#updateAdmin", function (event) {
        if ($("#adminForm").valid(this) == false) {
            return false;
        } else {
            doUpdate();
        }
    });

    $(document).on('click',"#updatePasswd", function (event) {
        if ($("#adminForm").valid(this) == false) {
            return false;
        } else {
            doUpdatePwd();
        }
    });
});


function searchList(f) {
    var param = {};
    if(f){
        $.extend(param,$(f.form).serializeObject());
    }
    $("#tbody").datagrid({
        url:'/admin/adminList',
        data:param
    });
}

/**
 * 点击添加弹出添加框
 */
function initAddAdmin() {
    $("#addAdmin").removeAttr("disabled");
    formtemplate(0);
    $merchantIdList.merchantSelect();
    $("#adminForm").validation({icon: false});
    $roleId.val(null);
    $merchantIdList.val(null);
    $('#adminModal').modal("show");
}

function checkForm(){
    var passwd = $('#password').val();
    var repasswd = $('#repasswd').val();
    if (passwd != repasswd) {
        $.scojs_message('两次密码不一致请重新输入', $.scojs_message.TYPE_ERROR);
        $("#passwd").focus();
        return false;
    }
    return true;
}

function doAdd() {
    $("#addAdmin").attr("disabled",true);

    //验证表单
    var ckRes = checkForm();
    if(!ckRes){
        $("#addAdmin").removeAttr("disabled");
        return false;
    }

    $.ajax({
        processData: false,
        type: 'POST',
        url: "/admin/saveAdmin",
        data: $('#adminForm').serialize(),
        success: function(data){
            if(data.code == 200){
                searchList(1,10);
                $.scojs_message('添加管理员成功', $.scojs_message.TYPE_OK);
                $('#adminModal').modal("hide");
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
                $("#addAdmin").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message("系统异常,添加管理员失败,请联系管理员", $.scojs_message.TYPE_ERROR);
            $("#addAdmin").removeAttr("disabled");
        }
    });
}

function initEditAdminInfo(id) {
    $("#updateAdmin").removeAttr("disabled");
    $.ajax({
        processData: false,
        type: 'GET',
        url: "/admin/info/init/"+id,
        success: function(data){
            if(data.code == 200){
                formtemplate(data.module);
                $merchantIdList.merchantSelect();
                $roleId.val(data.module.roleId).trigger("change");
                $merchantIdList.val(data.module.merchantIdList).trigger("change");
                $('#adminModal').modal("show");
            } else {
                $.scojs_message('获取管理员账号信息失败', $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $.scojs_message('获取管理员账号信息异常', $.scojs_message.TYPE_ERROR);
        }
    });
}

function doUpdate(){
    $("#updateAdmin").attr("disabled",true);

    $.ajax({
        processData: false,
        type: 'POST',
        url: "/admin/updateAdmin",
        dataType: 'json',
        data: $('#adminForm').serialize(),
        success: function(data){
            if(data.code == 200){
                searchList();
                $.scojs_message('管理员信息修改成功', $.scojs_message.TYPE_OK);
                $('#adminModal').modal("hide");
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
                $("#updateAdmin").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message('管理员信息修改异常', $.scojs_message.TYPE_ERROR);
            $("#updateAdmin").removeAttr("disabled");
        }
    });
}

function initUpdatePwd(id) {
    $("#updatePasswd").attr("disabled",true);
    formtemplate(1);
    $("#adminForm").validation({icon: false});
    $('#adminModal').modal("show");
    $("#passwdId").val(id);
}

function doUpdatePwd(){
    $("#updatePasswd").attr("disabled",true);

    //验证表单
    var passwd = $('#passwordReset').val();
    var repasswd = $('#repasswdReset').val();
    if (passwd != repasswd) {
        $.scojs_message('两次密码不一致请重新输入', $.scojs_message.TYPE_ERROR);
        $("#passwordReset").focus();
        $("#updatePasswd").removeAttr("disabled");
        return false;
    }

    $.ajax({
        processData: false,
        type: 'POST',
        url: "/admin/updateAdminPwd",
        dataType: 'json',
        data: $('#adminForm').serialize(),
        success: function(data){
            if(data.code == 200){
                searchList();
                $.scojs_message('修改管理员密码成功', $.scojs_message.TYPE_OK);
                $('#adminModal').modal("hide");
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
                $("#updatePasswd").removeAttr("disabled");
            }
        },
        error: function (data) {
            $.scojs_message('修改管理员密码异常', $.scojs_message.TYPE_ERROR);
            $("#updatePasswd").removeAttr("disabled");
        }
    });
}

function formtemplate(data){
    var templateoutput;
    if(data == 0)
    {
        templateoutput = template('tpl-form',{add:true});
        $("#form-modal").html("").html(templateoutput);
    }else if (data == 1) {
        templateoutput = template('tpl-form',{edit:true});
        $("#form-modal").html("").html(templateoutput);
    } else {
            templateoutput = template('tpl-form',data);
            $("#form-modal").html("").html(templateoutput);
    }
    $("#adminForm").validation({icon: false});
    $roleId = $("#roleId").select2({language: 'zh-CN'});
    $merchantIdList = $("#merchantIdList").select2({language: 'zh-CN'});
}

function forbidAdmin(id){
    bootbox.confirm({
        title: "禁用管理员",
        message: "是否禁用该管理员",
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
                    url: "/admin/forbidAdmin/"+id,
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function(data){
                        if(data.code == 200){
                            searchList();
                            $.scojs_message('禁用管理员成功', $.scojs_message.TYPE_OK);
                        } else {
                            $.scojs_message('禁用管理员失败', $.scojs_message.TYPE_ERROR);
                        }
                    },
                    error: function (data) {
                        $.scojs_message('禁用管理员异常', $.scojs_message.TYPE_ERROR);
                    }
                });
            }
        }
    });
}

function enableAdmin(id){
    bootbox.confirm({
        title: "启用管理员",
        message: "是否启用该管理员",
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
                    url: "/admin/enableAdmin/"+id,
                    contentType: 'application/json',
                    dataType: 'json',
                    success: function(data){
                        if(data.code == 200){
                            searchList();
                            $.scojs_message('启用管理员成功', $.scojs_message.TYPE_OK);
                        } else {
                            $.scojs_message('启用管理员失败', $.scojs_message.TYPE_ERROR);
                        }
                    },
                    error: function (data) {
                        $.scojs_message('启用管理员异常', $.scojs_message.TYPE_ERROR);
                    }
                });
            }
        }
    });
}