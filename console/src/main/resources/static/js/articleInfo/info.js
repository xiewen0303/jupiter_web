
function sureAuditAricleInfo(f) {
    var param = {};
    $.extend(param,$(f.form).serializeObject());

    $.ajax({
        processData: true,
        type: 'post',
        url: "/articleInfo/auditAricleInfo",
        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: 'json',
        data: param,
        success: function(data){
            if(data.code == 200){
                $.scojs_message('提交成功', $.scojs_message.TYPE_OK);
                //隐藏
                setTimeout(function(){
                    $("#auditAricleInfo").modal('hide');
                    window.location.href = '/articleInfo/search';
                },1000);
            } else {
                $.scojs_message('提交失败', $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $("#auditAricleInfo").modal('hide');
            $.scojs_message('提交异常', $.scojs_message.TYPE_ERROR);
        }
    });
}

function auditAricleInfo(){
    var res = template("template", {"articleId": $("#articleId").val()});
    $("#modal-dialog").html(res);

    $('#auditAricleInfo').modal("show");
}

/**
 * 编辑功能
 */
function editorArticleInfo(){
    window.location.href = '/articleInfo/editorArticleInfo?id='+$("#articleId").val();
}