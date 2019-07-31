
$(function() {
    var param = {};
    $.extend(param,$("form[name='searchForm']").serializeObject());
    $("#tbody").datagrid({
        url:'/articleCategory/getCategoryInfoPages',
        data:param
    });
})


function searchList(f) {
    var param = {};
    $.extend(param,$(f.form).serializeObject());
    $("#tbody").datagrid({
        url:'/articleCategory/getCategoryInfoPages',
        data:param
    });
}

function addCategoryInfo() {
    window.location.href = '/articleCategory/addCategory';
}

/**
 *
 * @param id
 * @param state 1,删除,2:下架，3:上架
 */
function operationCategory(id,type) {
    var param = "categoryId="+id+"&type="+type;
    $.ajax({
        // processData: true,
        processData: false,  // 不处理数据
        type: 'post',
        url: "/articleCategory/updateCategory",
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

/**
 *
 * @param id
 */
function modifyCategory(id) {
    window.location.href = '/articleCategory/modifyCategory?categoryId='+id;
}

function sureDelete(id){
    operationCategory(id,1);
    $('#deleteCategoryModel').modal("hide");
}

/**
 * 点击添加弹出添加框
 */
function deleteCategoryModel(id,name) {
    var res = template("deleteInfo", {"id": id,"name":name});
    $("#modal-dialog").html(res);

    $('#deleteCategoryModel').modal("show");
}