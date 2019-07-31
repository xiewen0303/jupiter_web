var Login = function(){
    var loginsub = function(){
        if(checkInput())
        {
            $(document).off('click','#loginsubmit');
            var adminName = $("#adminName").val().trim();
            var passwd = $("#passwd").val().trim();
            // var vcode = $("#vcode").val().trim();

            $.ajax({
                processData: false,
                type: 'POST',
                url: "/start/login",
                contentType: 'application/json',
                // data: JSON.stringify({adminName: adminName, password: passwd, vcode: vcode}),
                data: JSON.stringify({adminName: adminName, password: passwd}),
                dataType: 'json',
                success: function (data) {
                    if (data.code == 200) {
                        $("#loginsubmit").html("登录中...");
                        $('.form-message').html("");
                        window.location.href = "/start/index";
                    } else {
                        $(document).on('click','#loginsubmit',function(){
                            loginsub();
                        })
                        $('#verifyCodeImg').click();
                        $('.form-message').html(data.msg);
                    }
                },
                error : function (data) {
                    $(document).on('click','#loginsubmit',function(){
                        loginsub();
                    });
                    $('#verifyCodeImg').click();
                    $('.form-message').html(data.msg);
                }
            });
        }
    }
    //输入验证
    var checkInput = function(){
        //验证手机号码是否为空
        var nameObj = $("#adminName");
        var passwdObj = $("#passwd");
        // var vcodeObj = $("#vcode");

        if (nameObj.val().trim() == "") {
            nameObj.addClass("error");
            nameObj.focus();
            return false;
        }else{
            nameObj.removeClass("error");
        }
        //验证用户密码是否为空
        if (passwdObj.val().trim() == "") {
            passwdObj.addClass("error");
            passwdObj.focus();
            return false;
        }else{
            passwdObj.removeClass("error");
        }
        // //验证验证码是否为空
        // if (vcodeObj.val().trim() == "") {
        //     vcodeObj.addClass("error");
        //     vcodeObj.focus();
        //     return false;
        // }else{
        //     vcodeObj.removeClass("error");
        // }
        return true;
    }

    var actions = function(){
        $(document).on('mousedown','.eye',function(){
            $(this).siblings('input').attr('type','text');
            $(this).addClass('eyeopen');
        })
        $(document).on('touchend','.eye',function(){
            $(this).siblings('input').attr('type','password');
            $(this).removeClass('eyeopen');
        })
        $(document).on('touchstart','.eye',function(){
            $(this).siblings('input').attr('type','text');
            $(this).addClass('eyeopen');
        })
        $(document).on('mouseup','.eye',function(){
            $(this).siblings('input').attr('type','password');
            $(this).removeClass('eyeopen');
        })
        $("#phone").focus();
        //提交按钮
        $(document).on('click','#loginsubmit',function(){
            loginsub();
        })
        //键盘enter提交
        $(document).keyup(function (event) {
            if(event.keyCode == 13)
            {
                loginsub();
            }
        });

        $(document).on('blur','input',function(){
            $(this).removeClass("error");
        })
    }

    return {
        init: function(){
            actions();
        }
    };
}()
$(function(){
    Login.init();
})
function changeImage(f){
    f.src = f.src+"?"+Math.random() ;
}