
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
