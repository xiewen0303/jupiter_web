$(function() {
    var res1 = template("template", {"list": mappingLists});
    $("#addUrl").append(res1);

    $.each(mappingLists,function(index,data){
        var dataTmp = $("#"+data.id);
        dataTmp.val(data.origin);
    });
})


function appendText()
{
    var addHtml = $("#addSourceSite").html();
    var context="<div class=\"form-group\">" +
        addHtml+
        "</div>";
    $("#addEmptyUrl").append(context);    // 追加新元素
}



function submitCategory(f) {
    var param = {};
    $('[name="mappings"]').each(function(index,param){
        if(param.value.replace(' ','') == ''){
            param.value = ' ';
        }
    });
    $.extend(param,$("form[name='searchForm']").serializeObject());
    $.ajax({
        processData: true,
        type: 'post',
        url: "/category/subModifyCategory",
        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: 'json',
        data: param,
        success: function(data){
            if(data.code == 200){
                $.scojs_message(data.msg, $.scojs_message.TYPE_OK);
                setTimeout("window.location.href = '/category/search'",1000);
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $.scojs_message('提交异常', $.scojs_message.TYPE_ERROR);
        }
    });
}