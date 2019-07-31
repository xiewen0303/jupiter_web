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
    //$("#addRole").removeAttr("disabled");
    // formtemplate(0);
    //$("#roleForm").validation({icon: false});
    $('#categoryInfos').modal("show");
}


function addLabelInfo() {
    window.location.href = '/label/addLabel';
}

function searchList(f) {
    var param = {};
    if(f){
        $.extend(param,$(f.form).serializeObject());
    }
    $("#tbody").datagrid({
        url:'/label/searchLabels',
        data:param
    });
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
        "plugins" : [ "wholerow", "checkbox", "types"]
    })
});


