
$(function() {
   searchList(1);
})


function searchList(f) {
    var param = {};
    $.extend(param,$("form[name='searchForm']").serializeObject());
    $("#tbody").datagrid({
        url:'/articleBanner/getBannerInfoPages',
        data:param,
        autoRowHeight:true,
        striped:true
    });
}


function addBannerInfo() {
    window.location.href = '/articleBanner/addBanner';
}


/**
 *
 * @param id
 * @param state 1,删除,2:下架，3:上架
 */
function operationBanner(id,type) {
    var param = "bannerId="+id+"&type="+type;
    $.ajax({
        // processData: true,
        processData: false,  // 不处理数据
        type: 'post',
        url: "/articleBanner/updateBanner",
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

function modifyBanner(id) {
    window.location.href = '/articleBanner/modifyBanner?bannerId='+id;
}

function sureDelete(id){
    operationBanner(id,1);
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