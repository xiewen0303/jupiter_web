$(function () {
    $("#adminForm").validation({icon: false});
    $(document).on('click',"#updatePasswd", function (event) {
        console.info("updatePwd is in");
        if ($("#adminForm").valid(this) == false) {
            return false;
        } else {
            doUpdatePwd();
        }
    });
});

function doUpdatePwd(){
    $("#updatePasswd").attr("disabled",true);

    //验证表单
    var newPassword = $('#newPassword').val();
    var reNewPassword = $('#reNewPassword').val();
    if (newPassword != reNewPassword) {
        $.scojs_message('两次密码不一致请重新输入', $.scojs_message.TYPE_ERROR);
        $("#newPassword").focus();
        $("#updatePasswd").removeAttr("disabled");
        return false;
    }

    $.ajax({
        processData: false,
        type: 'GET',
        url: "/admin/updateLocalPwd",
        dataType: 'json',
        data: $('#adminForm').serialize(),
        success: function(data){
            if(data.code == 200){
                $.scojs_message('修改管理员密码成功', $.scojs_message.TYPE_OK);
                $('#oldPassword').val("");
                $('#newPassword').val("");
                $('#reNewPassword').val("");
                $("#updatePasswd").removeAttr("disabled");
                $("#adminForm").validation({icon: false});
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
                $("#updatePasswd").removeAttr("disabled");
                $("#adminForm").validation({icon: false});
            }
        },
        error: function (data) {
            $.scojs_message('修改管理员密码异常', $.scojs_message.TYPE_ERROR);
            $("#updatePasswd").removeAttr("disabled");
            $("#adminForm").validation({icon: false});
        }
    });
}