/**
 * Created by liuwei on 2017/4/14.
 */
var urls = [];
$(function () {
    /**
     * 子菜单 弹开
     *
     *获取路径
     *
     */
    getAllMenu();
    var url = window.location.pathname;
    $("._active").removeClass("active");
    $(".sidebar a").each(function () {
        var _this = $(this);
        if ($.inArray(url, urls) != -1) {
            if (_this.attr("href") == url) {
                $.cookie("_urlPath", url,{path:'/'});
                _this.parents("li").addClass("active");
                _this.parents("ul").first().addClass("menu-open");
                return false;
            }
        } else {
            console.info("url is ->2"+url);
            var _url = $.cookie("_urlPath");
            if (_this.attr("href") == _url) {
                _this.parents("li").addClass("active");
                _this.parents("ul").first().addClass("menu-open");
                return false;
            }
        }
    });

    var $dropzoneImgUpload = $(".dropzoneImgUpload");
    $dropzoneImgUpload.each(function(){
        var $this = $(this);
        var readOnly = $this.data("img-readonly") == true ? true : false;
        $this.dropzone({
            url: "/file/fileUpload",
            maxFiles: 10,
            clickable: !(readOnly),
            maxFilesize: 50,
            acceptedFiles: ".png,.jpg",
            dictFileTooBig: "上传文件过大 ({{filesize}}M). 最大可上传: {{maxFilesize}}M.",
            dictInvalidFileType: "图片类型错误，请上传.png,.jpg格式",
            dictResponseError: "错误{{statusCode}}！请稍后重试.",
            error: function(file, message) {
                bootbox.alert(message);
            },
            success: function (data) {
                imgUpload(data,this);
                $(this.element).siblings(".progress").detach();
            },
            uploadprogress:function(file,progress){
                var processbar = $('<div class="progress-bar progress-bar-warning progress-bar-striped"  role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" > <span class="sr-only">Complete</span> </div>')
                var dom = $('<div class="progress progress-sm active"  style="position: absolute;bottom: 0;width: 100%;z-index: 10; display: none; margin-bottom: 0;"   ></div>');
                dom.append(processbar);
                $(this.element).after(dom);
                dom.show();
                processbar.attr('aria-valuenow',progress);
                processbar.css("width",progress+"%")
            },
            /* uploadprogress:function(a,b,c){
             /!*layer.load(3);*!/
             progress(b);

             },*/
        });
    })

    $(".dropzoneImgUploadLoc").dropzone({
        url: "/file/fileUpload?fileType=2",
        maxFiles: 10,
        maxFilesize: 50,
        acceptedFiles: ".png,.jpg",
        dictFileTooBig: "上传文件过大 ({{filesize}}M). 最大可上传: {{maxFilesize}}M.",
        dictInvalidFileType: "图片类型错误，请上传.png,.jpg格式",
        dictResponseError: "错误{{statusCode}}！请稍后重试.",
        error: function(file, message) {
            bootbox.alert(message);
        },
        success: function (data) {
            imgUpload(data,this);
            $(this.element).siblings(".progress").detach();
        },
        uploadprogress:function(file,progress){
            var processbar = $('<div class="progress-bar progress-bar-warning progress-bar-striped"  role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" > <span class="sr-only">Complete</span> </div>')
            var dom = $('<div class="progress progress-sm active"  style="position: absolute;bottom: 0;width: 100%;z-index: 10; display: none; margin-bottom: 0;"   ></div>');
            dom.append(processbar);
            $(this.element).after(dom);
            dom.show();
            processbar.attr('aria-valuenow',progress);
            processbar.css("width",progress+"%")
        },
        /* uploadprogress:function(a,b,c){
         /!*layer.load(3);*!/
         progress(b);

         },*/
    });


    function imgUpload(data,_this) {
        var id = _this.element.id;
        var rep = JSON.parse(data.xhr.response);
        if (rep.success){
            if(null != id && "" != id){
                $("#" + id).attr("src", rep.module.filePath + rep.module.fileName);
                $("#" + id).attr("value", rep.module.fileName);
                $("#" + id).attr("data-val",rep.module.fileName);
            }else{
                _this.element.setAttribute("src",rep.module.filePath + rep.module.fileName);
                _this.element.setAttribute("value",rep.module.fileName);
                _this.element.setAttribute("data-val",rep.module.fileName);
            }
        } else {
        }

    }
});

$( document ).ajaxError(function( event, request, settings ) {
    if (request.status == 401) {
        window.location.href = "/unauthor";
    }

});

function getAllMenu() {
    $(".sidebar a").each(function () {
        var url = $(this).attr("href");
        if (url != "#") {
            urls.push(url);
        }
    });
}

$.prototype.serializeObject = function () {
    var object = {};
    var array = this.serializeArray();
    if(array){
        $.each(array,function (i,e) {
            if(null != array[i].value && "" != array[i].value){
                if(array[i].name in object){
                    object[array[i].name] = object[array[i].name]+","+array[i].value;
                }else{
                    object[array[i].name] = array[i].value;
                }
            }
        });
    }
    return object;
}

$.prototype.secarFormSubmit = function (p) {
    var _this = this;
    var formData = {};
    formData = this.serializeObject();
    var ajax = $.extend({
        type: 'POST',
        data:formData,
        beforeSend:function (xhr) {

            $(_this).find("button").attr("disabled",true);
            $(_this).find("a").attr("disabled",true);

            $(_this).validation();
            if(!$(_this).valid()){
                $(_this).find("button").attr("disabled",false);
                $(_this).find("a").attr("disabled",false);
                return false;
            }

            return true;
        },
        error: function (data) {
            $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
        }
    },p,{
        success:function(data){
            $(_this).find("button").attr("disabled",false);
            $(_this).find("a").attr("disabled",false);
            if(data.code != 200){
                $.scojs_message(data.msg, $.scojs_message.TYPE_ERROR);
            }else{
                p.success(data);
            }
        }
    });

    $.ajax(ajax);
}

$.secarAjaxPost = function (p) {

    var formData = {};
    if(p.data){
        /*for(var key in p.data){
            formData.append(key,p.data[key]);
        }*/
        formData = p.data
    }
    $.ajax($.extend({
        type:'POST',
        dataType:'json',
        data:formData
    },p,{
        success:function (data) {
            if(data.code != 200){
                $.scojs_message(data.errorMessage, $.scojs_message.TYPE_ERROR);
            }else{
                p.success(data);
            }
        }
    }));
}

$.prototype.datagrid = function (p) {

    this.templateHtml = $(this).data("templateHtml");
    if(!this.templateHtml){
        this.templateHtml = $(this).find("script").html();
        $(this).data("templateHtml",this.templateHtml);
    }
    var _this = this;
    var options = {
        pagination:true,
        pageNo:1,
        pageSize:10,
        page:1,
        currentCount:0
    };
    if(!p){
        p = {};
        options = $(this).data("options");
        p = options;
    }else{
        p.data = p.data?p.data:{};
        options = p?$.extend(options,p):options;
        $(this).data("options",options);
    }

    var postData = function (){
        var formData = options.data;
        if(formData){
            for(var key in formData){
                formData[key] = $.trim(formData[key]);
            }
            $.extend(options,{data:options});
        }
        $.ajax($.extend({
            type: 'POST',
            dataType:'json',
            success:function (data) {
                //列表数据
                var array = [];
                if(options.pagination){
                    array = data.module.list;
                }else{
                    array = data.module;
                }
                var html = "";

                _this.children().remove();
                if(null == array || array.length <= 0){
                    var tdLength = _this.prev().children().children().size();
                    html = "<tr><td align='center' colspan='"+tdLength+"'>暂无数据</td></tr>";
                    $(_this).parent().parent().parent().parent().children(".box-footer.dataTables_paginate").empty();
                }else{
                    var render = template.compile(_this.templateHtml);
                    html = render({list:array,page:(options.data.page==null?1:options.data.page),pageSize:options.pageSize});
                }
                _this.append(html);
                //开启分页功能
                if(options.pagination){
                    initPage(data);
                }
                if(p.callback)
                {
                    p.callback(data);
                }
            }
        },options));
    }
    var pagination;
    var initPage = function (opt) {
        if(pagination){
            pagination.off("page");
        }
        var pageDom = $(_this).parent().parent().parent().parent().children(".box-footer.dataTables_paginate");
        pagination = pageDom.bootpag({
            total: opt.module.pages,
            page: opt.module.pageNum,
            maxVisible: 10,
            firstLastUse: true,
            prev: '上一页',
            next: '下一页',
            first: '首页',
            last: '末页',
            leaps: true
        }).on("page", function (event, num) {
            $('#allChk').attr('checked',false);
        });

        pagination.on("page", function(event, num){
            var opt = $(_this).data("options");
            if(opt){
                options = opt;
            }
            $.extend(options.data,{
                page:num,
                row:options.pageSize
            });
            $(_this).data("options",options);
            postData();
        });
    }
    postData();
}

function reset(f) {
    f.form.reset();
}

function resetForm(f) {
    f.form.reset();
    $(f.form).find("input:hidden").val("");
    $(f.form).find("select").val("").trigger("change");
}


//图片上传方法
var imguploads = function(){
    function imgUpload(data,_this) {
        var id = _this.element.id;
        var rep = JSON.parse(data.xhr.response);
        if (rep.success){
            $("#" + id).attr("src", rep.module.filePath + rep.module.fileName);
            $("#" + id).attr("value", rep.module.fileName);
            $("#" + id).attr("data-val",rep.module.fileName);
        } else {
        }
    }
    return {
        init:function(obj){
            $(obj).dropzone({
                url: "/file/fileUpload",
                maxFiles: 10,
                maxFilesize: 50,
                acceptedFiles: ".png,.jpg",
                dictFileTooBig: "上传文件过大 ({{filesize}}M). 最大可上传: {{maxFilesize}}M.",
                dictInvalidFileType: "图片类型错误，请上传.png,.jpg格式",
                dictResponseError: "错误{{statusCode}}！请稍后重试.",
                error: function(file, message) {
                    bootbox.alert(message);
                },
                success: function (data) {
                    imgUpload(data,this);
                    $(this.element).siblings(".progress").detach();
                },
                uploadprogress:function(file,progress){
                    var processbar = $('<div class="progress-bar progress-bar-warning progress-bar-striped"  role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" > <span class="sr-only">Complete</span> </div>')
                    var dom = $('<div class="progress progress-sm active"  style="position: absolute;bottom: 0;width: 100%;z-index: 10; display: none; margin-bottom: 0;"   ></div>');
                    dom.append(processbar);
                    $(this.element).after(dom);
                    dom.show();
                    processbar.attr('aria-valuenow',progress);
                    processbar.css("width",progress+"%")
                },
                /* uploadprogress:function(a,b,c){
                 /!*layer.load(3);*!/
                 progress(b);

                 },*/
            });
        }
    }
}()

$.prototype.datetimeInit = function(option,startobj,endobj){
    var _this = this;
    var options ={
        'timePicker':false,
        'autoApply': true,
        'singleDatePicker':true,
        'autoUpdateInput': false,
        'timePicker24Hour':true,
        "showDropdowns": true,
        'opens':'center',
        "locale": {
            "format": "YYYY-MM-DD",
            "separator": " 至 ",
            "applyLabel": "确定",
            "cancelLabel": "取消",
            "daysOfWeek": [
                "日",
                "一",
                "二",
                "三",
                "四",
                "五",
                "六"
            ],
            "monthNames": [
                "一月",
                "二月",
                "三月",
                "四月",
                "五月",
                "六月",
                "七月",
                "八月",
                "九月",
                "十月",
                "十一月",
                "十二月"
            ],
        },
    }
    var locale = options.locale;
    if(option &&option.locale&&option.locale.format)
    {
        locale.format=option.locale.format;
    }
    $.extend(options,option);
    $.extend(options.locale,locale);

    _this.daterangepicker(options, function(start, end, label) {
        if(endobj&&startobj)
        {
            _this.val(start.format(options.locale.format)+" 至 "+end.format(options.locale.format))
            startobj.val(start.format(options.locale.format));
            endobj.val(end.format(options.locale.format));
            _this.on('blur',function(){
                if($(this).val() == "")
                {
                    startobj.val("");
                    endobj.val("");
                }
            })
        }else{
            _this.val(start.format(options.locale.format))
        }
    });
}

$.prototype.merSelect = function () {
    var _this = this;
    $.ajax({
        url:"/deviceMgr/getMer",
        type:"POST",
        async:false,
        success:function (data) {
            var val = _this.attr("value");
            var selectArr = [];
            $.each(data,function (i,e) {
                if(val == e["id"]){
                    selectArr.push({
                        id:e["id"],
                        text:e["name"],
                        selected:true
                    });
                }else{
                    selectArr.push({
                        id:e["id"],
                        text:e["name"]
                    });
                }
            });
            $(_this).select2({
                language: 'zh-CN',
                data:selectArr
            });
        }
    });
}

$.prototype.aliPaySelect = function () {
    var _this = this;
    var instance;
    $.ajax({
        url:"/deviceMgr/getAvailableAliPayList",
        type:"POST",
        async:false,
        success:function (data) {
            var val = _this.attr("value");
            var selectArr = [];
            selectArr.push({
                id:"",
                text:"请选择",
                selected:true
            });
            $.each(data,function (i,e) {
                if(val == e["id"]){
                    selectArr.push({
                        id:e["id"],
                        text:e["aliAccount"],
                        selected:true
                    });
                }else{
                    selectArr.push({
                        id:e["id"],
                        text:e["aliAccount"]
                    });
                }
            });
            instance = _this.data('select2');
            if(instance){
                _this.select2('destroy').empty();
            }
            $(_this).select2({
                language: 'zh-CN',
                data:selectArr
            });
        }
    });
}

$.prototype.merchantSelect = function () {
    var _this = this;
    $.ajax({
        url:"/admin/getMerchants",
        type:"POST",
        async:false,
        success:function (data) {
            var val = _this.attr("value");
            var selectArr = [];
            $.each(data,function (i,e) {
                if(val == e["id"]){
                    selectArr.push({
                        id:e["id"],
                        text:e["name"],
                        selected:true
                    });
                }else{
                    selectArr.push({
                        id:e["id"],
                        text:e["name"]
                    });
                }
            });
            $(_this).select2({
                language: 'zh-CN',
                data:selectArr
            });
        }
    });
}

$.prototype.deviceSelect = function () {
    var _this = this;
    $.ajax({
        url:"/alipayMgr/getActivatedDevice",
        type:"GET",
        async:false,
        success:function (data) {
            var val = _this.attr("value");
            var selectArr = [];
            var devices = data;
            $.each(devices,function (i,e) {
                if(val == e["id"]){
                    selectArr.push({
                        id:e["id"],
                        text:e["deviceNo"],
                        selected:true
                    });
                }else{
                    selectArr.push({
                        id:e["id"],
                        text:e["deviceNo"]
                    });
                }
            });
            $(_this).select2({
                language: 'zh-CN',
                data:selectArr
            });
        }
    });
}

$.prototype.bankCardSelect = function () {
    var _this = this;
    $.ajax({
        url:"/bankCardMgr/getCardNumbers",
        type:"GET",
        async:false,
        success:function (data) {
            var val = _this.attr("value");
            var selectArr = [];
            var bankCards = data;
            $.each(bankCards,function (index,element) {
                selectArr.push({
                    id:element,
                    text:element
                });
            });
            $(_this).select2({
                language: 'zh-CN',
                data:selectArr,
                tags:true
            });
        }
    });
}

$.prototype.bankNameSelect = function () {
    var _this = this;
    $.ajax({
        url:"/bankCardMgr/getBankList",
        type:"GET",
        async:false,
        success:function (data) {
            var val = _this.attr("value");
            var selectArr = [];
            var banks = data;
            $.each(banks, function (index,element) {
                selectArr.push({
                    id: element["bankCode"],
                    text: element["bankName"]
                });
            });
            $(_this).select2({
                language: 'zh-CN',
                data: selectArr
            });
        }
    });
}



$.prototype.createSelector = function () {
    var d = {};
    if (this.find("option").length <=0) {
        d = {
            id:0,
            text:"请选择"
        };
    }
    return this.select2({
        minimumResultsForSearch: Infinity,
        data:[d]
    });
};

$.prototype.loadSelection = function(options){
    var $this = this;

    $this.children("option:gt(0)").remove();

    options = $.extend({
        type:"post",
        defaultSelectId:0
    },options);

    $.ajax({
        url:options.url,
        type:options.type,
        data:options.param,
        async:true,
        success:function(data){
            if(data.module && data.module.length > 0){
                for(var i = 0 ; i < data.module.length; i ++){
                    var id = data.module[i][options.module.idName];
                    var text = data.module[i][options.module.textName];
                    $this.append(new Option(text, id));
                }
            }

            $this.val(options.defaultSelectId != null ? options.defaultSelectId : 0).trigger("change");
        }
    });


};

$.prototype.initSelector = function(){
    this.val(null).trigger("change");
}

function secarConfirm(msg,sure,cancel) {
    bootbox.confirm({
        title: "系统提示",
        message: msg,
        buttons: {
            confirm: {
                label: '确定',
                className: 'btn-info'
            },
            cancel: {
                label: '取消',
                className: 'btn-default'
            }
        },
        callback: function (result) {
            if(result){
                if(sure)
                    sure(result);
            }else{
                if(cancel)
                    cancel(result);
            }
        }
    });
}

$.prototype.popimage = function(){
    var _this = this;
    var imgs = [];
    var pic =_this.find('img').length;
    _this.find('img').each(function(index){
        imgs.push($(this));
        $(this).on('click',function(){
            createobj($(this),index);
        })
    })
    var createobj = function(obj,index){
        var url = '';
        if(obj.attr('data-url'))
        {
            url = obj.attr('data-url');
        }else{
            url = obj.attr('src');
        }
        var left = $("<a class='btn btn-sm btn-info' href='javascript:void(0);'> 左转 </a>");
        var right = $("<a class='btn btn-sm btn-info' href='javascript:void(0);'> 右转 </a>");
        var close = $("<a class='oicon oicon-guanbifuzhi pull-right' href='javascript:void(0);'></a>")
        var modal =$("<div class='imgviewlook'><p>  </p><div class='imgview animated fadeIn'  ><img id='popimg' data-index='"+index+"' src='"+ url +"' alt=''/></div></div>");
        if(pic !== 1)
        {
            var next = $("<a class='right carousel-control' href='javascript:void(0);' > <span class='oicon oicon-right glyphicon-chevron-right' ></span><span class='sr-only'>Next</span></a>");
            var prev = $("<a class='left carousel-control' href='javascript:void(0);' > <span class='oicon oicon-left glyphicon-chevron-left' ></span><span class='sr-only'>Prev</span></a>");
        }else{
            var next = $("<div>")
            var prev = $("<div>")
        }

        $("body").append(modal);
        modal.find('p').append(left,right,close);
        modal.find('div.imgview').append(next,prev);
        modal.click(function(e){
            var target = $(e.target);
            if(target.closest("#popimg").length == 0 && target.closest("p").length == 0 && target.closest('.carousel-control').length == 0 ){
                modal.detach();
            }
        });
        next.click(function(){
            modal.detach();
            if(index === imgs.length - 1)
            {
                createobj(imgs[0],0);
            }else{
                createobj(imgs[index+1],index+1);
            }
        })
        prev.click(function(){
            modal.detach();
            if(index === 0)
            {
                createobj(imgs[imgs.length-1],imgs.length-1);
            }else{
                createobj(imgs[index-1],index-1);
            }
        })
        close.click(function(){
            modal.detach();
        })
        left.click(function(){
            rotate(1,1)
        })
        right.click(function(){
            rotate(1,0)
        })
    }
    var rotate = function(obj,arr){
        if(arr==1)
        {arr='left';
        }else{arr='right';}
        if(obj==1)
        {obj ='popimg'}
        var img = document.getElementById(obj);
        if(!img || !arr) return false;
        var n = img.getAttribute('step');
        if(n== null) n=0;
        if(arr=='left'){
            (n==0)? n=3:n--;
        }else if(arr=='right'){
            (n==3)? n=0:n++;
        }
        img.setAttribute('step',n);
        //对IE浏览器使用滤镜旋转
        if(document.all) {
            img.style.filter = 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ n +')';
            //HACK FOR MSIE 8
            switch(n){
                case 0:
                    img.parentNode.style.height = img.height;
                    break;
                case 1:
                    img.parentNode.style.height = img.width;
                    break;
                case 2:
                    img.parentNode.style.height = img.height;
                    break;
                case 3:
                    img.parentNode.style.height = img.width;
                    break;
            }
            // 对现代浏览器写入HTML5的元素进行旋转： canvas
        }else{
            var c = document.getElementById('canvas_'+obj);
            if(c== null){
                img.style.visibility = 'hidden';
                img.style.position = 'absolute';
                c = document.createElement('canvas');
                c.setAttribute("id",'canvas_'+obj);
                img.parentNode.appendChild(c);
            }
            var canvasContext = c.getContext('2d');
            switch(n) {
                default :
                case 0 :
                    c.setAttribute('width', img.width);
                    c.setAttribute('height', img.height);
                    canvasContext.rotate(0 * Math.PI / 180);
                    canvasContext.drawImage(img, 0, 0,img.width,img.height);
                    break;
                case 1 :
                    c.setAttribute('width', img.height);
                    c.setAttribute('height', img.width);
                    canvasContext.rotate(90 * Math.PI / 180);
                    canvasContext.drawImage(img, 0, -img.height,img.width,img.height);
                    break;
                case 2 :
                    c.setAttribute('width', img.width);
                    c.setAttribute('height', img.height);
                    canvasContext.rotate(180 * Math.PI / 180);
                    canvasContext.drawImage(img, -img.width, -img.height,img.width,img.height);
                    break;
                case 3 :
                    c.setAttribute('width', img.height);
                    c.setAttribute('height', img.width);
                    canvasContext.rotate(270 * Math.PI / 180);
                    canvasContext.drawImage(img, -img.width, 0,img.width,img.height);
                    break;
            };
        }
    }
}


$.prototype.mySelect = function(e){
    var dom = this;
    var eValue = ''
    dom.attr('aria-hidden',true);
    dom.addClass('select2-hidden-accessible');
    var options = dom.find('option');
    var tmp = '<div class="filterAtag">'
    options.each(function(e){
        if(this.selected === true){
            tmp = tmp + '<a class="selected" style="cursor: default" data-value="'+this.value+'">' + this.text + '</a>'
        }else{
            tmp = tmp + '<a data-value="'+this.value+'" style="cursor: default">' + this.text + '</a>'
        }
    })

    var option = $(tmp);
    dom.after(option);

    $(document).on('click',$(".filterAtag a"),function(e){
        if(e.target.tagName === "A" && e.target.parentElement.className === 'filterAtag' )
        {
            $(e.target).addClass('selected');
            eValue = $(e.target).attr('data-value');
            dom.val(eValue);
            $(e.target).siblings("a").removeClass();
        }
    })
    dom.change(function(){
        var val = dom.val();
        $(".filterAtag a").each(function(){
            if($(this).attr('data-value') === val)
            {
                $(this).addClass('selected');
                $(this).siblings("a").removeClass();
            }
        })
    })
}
$(document).ajaxComplete(function(event,xhr){
    if (xhr && xhr.responseText && xhr.responseText.indexOf("手机号码")>=0 && xhr.responseText.indexOf("密码")>=0 && xhr.responseText.indexOf("忘记密码？")>=0) {
        window.location.href = "/login";
    }
});

// 毫秒转换为日期
function getDate(str) {
    var date = new Date(str);
    return date.getFullYear() + "/" + getzf((date.getMonth() + 1)) + "/" + getzf(date.getDate());
}

// 补0操作
function getzf(num){
    if(parseInt(num) < 10){
        num = '0'+num;
    }
    return num;
}