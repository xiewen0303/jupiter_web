function submitCategory(f) {
    var param = {};
    $.extend(param,$("form[name='searchForm']").serializeObject());
    $.ajax({
        processData: true,
        type: 'post',
        url: "/articleCategory/subModifyCategory",
        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: 'json',
        data: param,
        success: function(data){
            if(data.code == 200){
                $.scojs_message(data.msg, $.scojs_message.TYPE_OK);
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
            }
            setTimeout("window.location.href = '/articleCategory/search'",1000);
        },
        error: function (data) {
            $.scojs_message('提交异常', $.scojs_message.TYPE_ERROR);
        }
    });
}