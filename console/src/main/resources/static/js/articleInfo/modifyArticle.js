//
$(function(){
    $('#uploadFile').fileinput({
        language: 'zh',　　　　　　　　//使用中文
        theme: 'fa',
        showUpload: false, //是否显示上传按钮,跟随文本框的那个
        showClose: true,
        showRemove: true,
        //dropZoneEnabled: false,//是否显示拖拽区域
        showPreview :true, //是否显示预览
        // allowedFileExtensions: ['jpg', 'png', 'gif'],　　　　//允许的文件格式
        // overwriteInitial: true,
        // maxFileSize: 1000,　　　　　　　　　　　　　　　　　　　　//文件最大尺寸
        maxFilesNum: 1,　　　　　　　　　　　　　　　　　　　　　　//文件最大数量
        // layoutTemplates: {
        //     actionDelete: '',
        //     actionUpload: ''
        // },
        slugCallback: function (filename) {
            return filename.replace('(', '_').replace(']', '_');
        },
    });
});


function saveAricleInfo(f) {
    //显示
    $("#loadingModal").modal('show');

    var param = new FormData(document.querySelector("form"));
    $.ajax({
        // processData: true,
        processData: false,  // 不处理数据
        contentType: false,   // 不设置内容类型
        type: 'post',
        url: "/articleInfo/subArticleInfo",
        // contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: 'json',
        data: param,
        success: function(data){
            if(data.code == 200){
                $.scojs_message('提交成功', $.scojs_message.TYPE_OK);
                setTimeout(function(){
                    //隐藏
                    $("#loadingModal").modal('hide');
                    window.location.href = '/articleInfo/articleInfo?id='+$("#articleId").val();
                },1000);
            } else {
                $.scojs_message('提交失败', $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $("#loadingModal").modal('hide');
            $.scojs_message('提交异常', $.scojs_message.TYPE_ERROR);
        }
    });
}
