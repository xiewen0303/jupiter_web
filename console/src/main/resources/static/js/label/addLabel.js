function addLabelSubmit(f) {
    var param = {};
    $.extend(param, $(f.form).serializeObject());

    $.ajax({
        processData: true,
        type: 'post',
        url: "/label/submitAddLabel",
        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
        dataType: 'json',
        data: param,
        success: function(data){
            if(data.code == 200){
                $.scojs_message('提交成功', $.scojs_message.TYPE_OK);
            } else {
                $.scojs_message('提交失败', $.scojs_message.TYPE_ERROR);
            }
        },
        error: function (data) {
            $.scojs_message('转账提交异常', $.scojs_message.TYPE_ERROR);
        }
    });
}

/**
 * 获取选中的checkbox
 */
function setCheckedIds(){
    //获取选中的节点
    var nodes=$("#using_json").jstree("get_checked");
    $("#categoryName").val(nodes);
}

/**
 * 点击添加弹出添加框
 */
function selectCategory() {
    $('#categoryInfos').modal("show");
}

$(function(){

    $(document).on('click',"#selectBtn", function (event) {
        if ($("#accountForm").valid(this) == false) {
            return false;
        } else {
            setCheckedIds();
        }
    });

    var viewInfos = [];

    $.ajax({
        // processData: true,
        type: 'post',
        url: "/category/getCategoryInfos",
        contentType: 'application/json;charset=UTF-8',
        dataType: 'json',
        async: false,
        // data: param,
        success: function(data){
            if(data.code == 200){
                viewInfos = data.module.data;
            } else {
                console.error(data.code+"===="+data.msg);
            }
        },
        error: function (data) {
            console.error(data.code+"===="+data.msg);
        }
    });

    $("#using_json").jstree({
        "core": {
            "multiple": false,//单选
            "data": viewInfos
        },
        "checkbox" : {
            "keep_selected_style" : false,
            "three_state": false
        },
        "plugins" : [ "wholerow", "checkbox" ,"types" ]
    })
});