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

    // $('#uploadFile').fileinput({
    //     theme: 'fas',
    //     showUpload: false,
    //     showCaption: false,
    //     browseClass: "btn btn-primary btn-lg",
    //     fileType: "any",
    //     previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
    //     overwriteInitial: false,
    //     initialPreviewAsData: true
    // });


});


//
// //初始化fileinput控件（第一次初始化）
// function initFileInput(ctrlName, uploadUrl) {
//     var control = $('#' + ctrlName);
//     control.fileinput({
//         language: 'zh', //设置语言
//         uploadUrl: uploadUrl, //上传的地址
//         showUpload: false, //是否显示上传按钮
//         allowedFileExtensions : ['jpg', 'png','gif'],//接收的文件后缀
//         //uploadAsync: false, //插件支持同步和异步
//
//     });
//     // .on("fileuploaded", function(event, data) {
//     //     //上传图片后的回调函数，可以在这做一些处理
//     // });
// }


// $(function(){
//     //指定上传controller访问地址
//     var path = 'http://localhost:8080/sbootstrap/upload';
//     //页面初始化加载initFileInput()方法传入ID名和上传地址
//     initFileInput("uploadFile",path);
// })


function addBannerSubmit(f) {

    var param = new FormData(document.querySelector("form"));

    //显示
    $("#loadingModal").modal('show');

    $.ajax({
        // processData: true,
        processData: false,  // 不处理数据
        contentType: false,   // 不设置内容类型
        type: 'post',
        url: "/articleInfo/submitAddArticle",
        // contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: 'json',
        data: param,
        success: function(data){
            if(data.code == 200){
                $.scojs_message('提交成功', $.scojs_message.TYPE_OK);
            } else {
                $.scojs_message('提交失败', $.scojs_message.TYPE_ERROR);
            }
            //隐藏
            $("#loadingModal").modal('hide');

            setTimeout("window.location.href = '/articleBanner/search'",1000);
        },
        error: function (data) {
            $("#loadingModal").modal('hide');
            $.scojs_message('提交异常', $.scojs_message.TYPE_ERROR);
        }
    });
}

