$(function () {
    $("#datePickertab_1").datetimeInit({'singleDatePicker':false,'timePicker':false},$("#logStart"),$("#logEnd"));

    searchList();
})

function searchList(f) {
    var param = {};
    if (f) {
        $.extend(param, $(f.form).serializeObject());
    }
    $("#tbody").datagrid({
        url: '/log/logList',
        data: param
    });
}