
$(function () {
    $("#datePickertab_1").datetimeInit({'singleDatePicker':false,'timePicker':false},$("#beginTimeStr"),$("#endTimeStr"));

    searchList(1);
})

function searchList(f) {
    var param = {};
    $.extend(param,$("form[name='searchForm']").serializeObject());
    $("#tbody").datagrid({
        url:'/articleInfo/submitSearch',
        data:param,
        autoRowHeight:true,
        striped:true
    });
}

function addInfo() {
    window.location.href = '/articleInfo/addArticle';
}

function infoArticle(id) {
    window.location.href = '/articleInfo/articleInfo?id='+id;
}


/**
 *
 * @param id
 * @param state 5,删除,2:下架，3:上架
 */
function operationBanner(id,type) {
    var param = "id="+id+"&type="+type;
    $.ajax({
        // processData: true,
        processData: false,  // 不处理数据
        type: 'post',
        url: "/articleInfo/updateOfficiaArticle",
        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: 'json',
        data: param,
        success: function(data){
            if(data.code == 200){
                $.scojs_message(data.msg, $.scojs_message.TYPE_OK);
                //刷新一下页面数据
                searchList(1);
            } else {
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $.scojs_message('提交异常', $.scojs_message.TYPE_ERROR);
        }
    });
}

function sureDelete(id){
    operationBanner(id,5);
    $('#deleteBannerModel').modal("hide");
}

/**
 * 点击添加弹出添加框
 */
function deleteBannerModel(id,bannerName) {
    var res = template("deleteInfo", {"id": id,"bannerName":bannerName});
    $("#modal-dialog").html(res);

    $('#deleteBannerModel').modal("show");
}

