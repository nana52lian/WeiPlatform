var swidth = window.screen.width;
var shight = window.screen.height;

$(document).ready(function () {
    _showpopup($('.drop_panel'), $('#h_name_list'));
    _showpopup($('#client_drop'), $('#client_drop_list'));
    _showpopup($('#publish'), $('#site_publish'));
    _showmousedown($('.drop_btn'), $('#menu_drop_list'));
    _showpopup($('#drop_btn_new'), $('#news_drop_list'));
    _showpopup($('#drop_btn_import'), $('#import_drop_list'));
    _showmousedown($('.d_card'), $('.d_card_list'));
    _showmousedown($('#left_drop'), $('#left_drop_list'));
    _showpopup($('#drop_filter'), $('#drop_filter_list'));

    SetLayout();
});

$(window).resize(function () {
    SetLayout();
});
function closeWindow(id){
	$('#'+id).window('close');
}
 
/*点击弹出窗口的关闭按钮*/
function cancleWindow(id){
	$('#'+id).window('close');
	$('#'+id).find("input").val("");
}
function _showpopup(lr_systembtn, lr_menu) {
    lr_systembtn.mouseenter(function () {
        t_delay = setTimeout(function () {
            lr_menu.fadeIn("slow");
        }, 200);
    });
    lr_systembtn.mouseleave(function () {
        clearTimeout(t_delay);
        lr_menu.fadeOut("slow");
    });
};

function _showmousedown(lr_systembtn, lr_menu) {

    lr_systembtn.mousedown(function () {

        t_delay = setTimeout(function () {

            lr_menu.fadeToggle("slow");

        }, 200);
    });
};

function _toggleClass(obj) {
    if ($(obj).css("display") == "block") {
        $(obj).fadeOut();
    } else {
        $(obj).fadeIn();
    }
}

//�ı���ʽ��ͬʱ��ؼ���ֵ--����
function _toggleClassWithLabel(obj1, obj2, val1) {
    if ($(obj1).css("display") == "block") {
        $(obj1).fadeOut();
        $(obj2).html(val1);
    } else {
        $(obj1).fadeIn();
    }
}

function _toggleClassWithTextBox(obj1, obj2, val1) {
    if ($(obj1).css("display") == "block") {
        $(obj1).fadeOut();
        $(obj2).val(val1);
    } else {
        $(obj1).fadeIn();
    }
}

//�����
function SetLayout() {

    var screen = swidth + "x" + shight;

    $("#left").css("height", window.document.body.parentNode.clientHeight - 93);
    $("#left_menu_panel").css("height", $("#left").height() - 66);
    $("#right").css("height", window.document.body.parentNode.clientHeight - 133);
    $("#right").css("width", window.document.body.parentNode.clientWidth - 250);
    $("#client_right").css("height", window.document.body.parentNode.clientHeight - 133);
    $("#client_right").css("width", window.document.body.parentNode.clientWidth - 200);
    $(".client_list").css("width", window.document.body.parentNode.clientWidth - 600);
    $("#content_left").css("width", window.document.body.parentNode.clientWidth - 350);
    $("#content_left").css("height", window.document.body.parentNode.clientHeight);
    $("#content_body").css("height", window.document.body.parentNode.clientHeight);
    $(".chance_left").css("width", window.document.body.parentNode.clientWidth - 600);
    $("#task_left").css("width", window.document.body.parentNode.clientWidth - 700);
}


//����cookie -- ����
function setCookie(c_name, value, expiredays) {
    var exdate = new Date()
    exdate.setDate(exdate.getDate() + expiredays)
    document.cookie = c_name + "=" + escape(value) +
    ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}

//��ȡcookie -- ����
function getCookie(c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=")
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1
            c_end = document.cookie.indexOf(";", c_start)
            if (c_end == -1) c_end = document.cookie.length
            return unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return ""
}

/*****���� added 2013-05-28 Start *****/
//���ı����ý���ʱ���õķ���
function focusEvent(obj, dVal) {
    if (obj.value == dVal)
        obj.value = "";
}
//���ı���ʧȥ����ʱ���õķ���
function blurEvent(obj, dVal) {
    if (obj.value == "")
        obj.value = dVal;
}

//����ǰ�ؼ���ֵ�����Ӧ�ķ������ؼ�
function getVal(oTxt, sTxt) {
    $("#ctl00_ContentPlaceHolder_" + sTxt).val(oTxt.value);
}
/*****2013-05-28 End*****/

//��������label
function dropLabel() {
    $('.son_ul').hide(); //��ʼul����
    //$('.select_box span').hover(function () { //����ƶ�����
    //    alert();
    //    $(this).parent().find('ul.son_ul').slideDown();  //�ҵ�ul.son_ul��ʾ
    //    $(this).parent().find('li').hover(function () { $(this).addClass('hover') }, function () { $(this).removeClass('hover') }); //li��hoverЧ��
    //    $(this).parent().hover(function () { },
    //        function () {
    //            $(this).parent().find("ul.son_ul").slideUp();
    //        }
    //    );
    //}, function () { });
    $('.select_box span').toggle(//ѭ���������

        function () {
            $(this).parent().find('ul.son_ul').slideDown();//�ҵ�ul.son_ul��ʾ
            $(this).parent().find('li').hover(function () { $(this).addClass('hover') }, function () { $(this).removeClass('hover') }); //li��hoverЧ��
        },
        function () {
            $(this).parent().find('ul.son_ul').slideUp();//�ҵ�ul.son_ul��ʾ
        }
    );
    $('ul.son_ul li').click(function () {
        $(this).parents('li').find('span').html($(this).html());
        $(this).parents('li').find('ul').slideUp();
    });
}

//��֤�Ƿ���С�� -- ����
function isDouble(event) {
    if (event === undefined) event = window.event;
    if (event.keyCode) {
        if ((event.keyCode > 47 && event.keyCode < 58) || event.keyCode === 46) {
            return event.keyCode;
        } else {
            return false;
        }
    } else {
        if ((event.which > 47 && event.which < 58) || event.which === 46) {
            return event.which;
        } else {
            return false;
        }
    }
}

//��֤�Ƿ��Ǵ�ŵ�С�� -- ����
function isNegative(event) {
    if (event === undefined) event = window.event;
    if (event.keyCode) {
        if ((event.keyCode > 47 && event.keyCode < 58) || event.keyCode === 46 || event.keyCode === 45) {
            return event.keyCode;
        } else {
            return false;
        }
    } else {
        if ((event.which > 47 && event.which < 58) || event.which === 46 || event.which === 45) {
            return event.which;
        } else {
            return false;
        }
    }
}

//��֤�Ƿ�������
function isInterger(event) {
    if (event === undefined) event = window.event;
    if (event.keyCode) {
        if ((event.keyCode > 47 && event.keyCode < 58) || event.keyCode === 46) {
            return event.keyCode;
        } else {
            return false;
        }
    } else {
        if ((event.which > 47 && event.which < 58) || event.which === 46) {
            return event.which;
        } else {
            return false;
        }
    }
}

//С���
function xed(obj, sType) {
    var oDiv = document.getElementById(obj);
    if (sType == 'show') { oDiv.style.display = 'block'; }
    if (sType == 'hide') { oDiv.style.display = 'none'; }
}

//���ÿ�ݼ�
function hotKey(event) {
    if (event === undefined) event = window.event;
    if (event.keyCode) {
        if ((event.keyCode > 47 && event.keyCode < 58) || event.keyCode === 46) {
            return event.keyCode;
        } else {
            return false;
        }
    } else {
        if ((event.which > 47 && event.which < 58) || event.which === 46) {
            return event.which;
        } else {
            return false;
        }
    }
}

//���ÿؼ��Ƿ����
function setControlEnable(v, obj, myid) {
    var o = $(obj).parent().parent();
    if ($(obj).val().toUpperCase().indexOf(v) >= 0) {
        var mo = o.find(myid);
        mo.attr("readonly", "readonly");
        mo.val("0");
    } else {
        o.find(myid).removeAttr("readOnly");
    }
}

//����������
function floatDiv(o) {
    $(window).scroll(function () {
        var offsetTop = $(window).scrollTop() + $(window).height() - 80 - 60 + "px";
        $("#" + o).animate({ top: offsetTop }, { duration: 500, queue: false });
    });
}



var common = {};

common.changtab = function (hide1, show1, show2, hide2) {
    $(hide1).removeClass().addClass("normal");
    $(show1).removeClass().addClass("active");
    $(hide2).hide();
    $(show2).show();

}

var slideFunc = function (obj, tableid) {
    $(obj).parent().next(tableid).slideToggle('normal');
}

//���ά���Ŀ�ݼ����÷���
function dataKeyEvent(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];//�¼�
    var thisElement = e.srcElement || e.target;//�¼�Դ

    //ִ�м��̲���
    if (e && e.keyCode == 113) { // �� F2 ִ�б༭

        var $top = $(thisElement).closest('.fwxx_01');                                      //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var edit = $($top).find('.editbtn');                                                //�༭��ť
        if (typeof ($(edit).html()) != 'undefined') {
            $(edit).click();
        } else {
            var $div_temp = $(thisElement).closest('.fwxx_01');                             //Repeater�°�����б��div
            if ($div_temp.attr('id') == "div_fwk") {                                        //�������ؼ���div��id����
                var $topper = $(thisElement).closest('body');
                var divtemp = $($topper).find($div_temp);
                if ($(divtemp).css('display') == 'block') {
                    $($topper).find('#ctl00_ContentPlaceHolder_btnSaveService').click();    //���沢������Ӱ�ť
                }
            }
        }
    }
    if (e && e.keyCode == 115) { // �� F4 ִ��ɾ��
        var $top = $(thisElement).closest('.fwxx_01');                                      //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var del = $($top).find('.delbtn');                                                  //ɾ��ť
        if (typeof ($(del).html()) == 'undefined') return;
        $(del).click();
    }
    if (e && e.keyCode == 114) { // �� F3 
        e.returnValue = false;
        e.preventDefault();
        e.stopPropagation();
        var $topper = $(thisElement).closest('body');
        var $divtemp = $('#div_fwk');                                                       //�������ؼ���div��id����
        var divtemp = $($topper).find($divtemp);
        if ($(divtemp).css('display') == 'block') {
            cancelAdd();
            var $last_track_05 = $('.fwxx_01').eq(-2);                                      //Repeater�°�����б��div
            $last_track_05.find('.txtCode').focus();                                        //Ϊһ����֪��ݻ�ȡ����
        } else {
            displayAdd();
        }
    }
}

//�����������ճ̵Ŀ�ݼ����÷���
function quoteBrandKeyEvent(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];//�¼�
    var thisElement = e.srcElement || e.target;//�¼�Դ

    if (e && e.keyCode == 8) { // �� Backspace ִ�б༭
        $('.service').remove();
    }

    //ִ�м��̲���
    if (e && e.keyCode == 113) { // �� F2 ִ�б༭
        var $top = $(thisElement).closest('.quote_mt');                                      //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var edit = $($top).find('.editbtn');                                                //�༭��ť
        if (typeof ($(edit).html()) != 'undefined') {
            $(edit).click();
        } else {
            var $div_temp = $(thisElement).closest('.quote_mt');                            //Repeater�°�����б��div
            if ($div_temp.attr('id') == "div_gen") {                                        //�������ؼ���div��id����
                var $topper = $(thisElement).closest('body');
                var divtemp = $($topper).find($div_temp);
                if ($(divtemp).css('display') == 'block') {
                    $($topper).find('#ctl00_ContentPlaceHolder_btnSaveDate').click();       //���沢������Ӱ�ť
                }
            }
        }
    }
    if (e && e.keyCode == 13) { // �� Enter ִ�б༭
        try {
            e.returnValue = false;//IE ��ֹ�¼�ִ��
            e.preventDefault();//FF ��ֹ�¼�ִ��
        } catch (ex) { }
        var $top = $(thisElement).closest('.quote_mt');                                     //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var edit = $($top).find('.editbtn');                                                //�༭��ť
        if (typeof ($(edit).html()) != 'undefined') {
            $(edit).click();
        } else {
            var $div_temp = $(thisElement).closest('.quote_mt');                            //Repeater�°�����б��div
            if ($div_temp.attr('id') == "div_gen") {                                        //�������ؼ���div��id����
                var $topper = $(thisElement).closest('body');
                var divtemp = $($topper).find($div_temp);
                if ($(divtemp).css('display') == 'block') {
                    $($topper).find('#ctl00_ContentPlaceHolder_btnSaveDate').click();       //���沢������Ӱ�ť
                }
            }
        }
    }
    if (e && e.keyCode == 115) { // �� F4 ִ��ɾ��
        var $top = $(thisElement).closest('.quote_mt');                                     //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var del = $($top).find('.delbtn');                                                  //ɾ��ť
        if (typeof ($(del).html()) == 'undefined') return;
        $(del).click();
    }
    if (e && e.keyCode == 114) { // �� F3 
        e.returnValue = false;
        e.preventDefault();  
        e.stopPropagation();  
        var $topper = $(thisElement).closest('body');
        var $divtemp = $('#div_gen');                                                       //�������ؼ���div��id����
        var divtemp = $($topper).find($divtemp);
        if ($(divtemp).css('display') == 'block') {
            cancelAdd();
            var $last_track_05 = $('.quote_mt').eq(-2);                                     //Repeater�°�����б��div
            $last_track_05.find('.txtSDExplanation').focus();                               //Ϊһ����֪��ݻ�ȡ����
        } else {
            displayAdd();
        }
    }
}

//���������г��ѡ���Ʊ�����ѵĿ�ݼ����÷���
function quoteListKeyEvent(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];//�¼�
    var thisElement = e.srcElement || e.target;//�¼�Դ

    //ִ�м��̲���
    if (e && e.keyCode == 113) { // �� F2 ִ�б༭
        var $top = $(thisElement).closest('.quoteListRep');                                 //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var edit = $($top).find('.editbtn');                                                //�༭��ť
        if (typeof ($(edit).html()) != 'undefined') {
            $(edit).click();
        } else {
            var $div_temp = $(thisElement).closest('.quoteListRep').children();             //Repeater�°�����б��div
            if ($div_temp.attr('id') == "div_gen") {                                        //�������ؼ���div��id����
                var $topper = $(thisElement).closest('body');
                var divtemp = $($topper).find($div_temp);
                if ($(divtemp).css('display') == 'block') {
                    $($topper).find('#ctl00_ContentPlaceHolder_btnSave').click();           //���沢������Ӱ�ť
                }
            }
        }
    }
    if (e && e.keyCode == 13) { // �� Enter ִ�б༭
        try {
            e.returnValue = false;//IE ��ֹ�¼�ִ��
            e.preventDefault();//FF ��ֹ�¼�ִ��
        } catch (ex) { }
        var $top = $(thisElement).closest('.quoteListRep');                                 //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var edit = $($top).find('.editbtn');                                                //�༭��ť
        if (typeof ($(edit).html()) != 'undefined') {
            $(edit).click();
        } else {
            var $div_temp = $(thisElement).closest('.quoteListRep').children();             //Repeater�°�����б��div
            if ($div_temp.attr('id') == "div_gen") {                                        //�������ؼ���div��id����
                var $topper = $(thisElement).closest('body');
                var divtemp = $($topper).find($div_temp);
                if ($(divtemp).css('display') == 'block') {
                    $($topper).find('#ctl00_ContentPlaceHolder_btnSave').click();           //���沢������Ӱ�ť
                }
            }
        }
    }
    if (e && e.keyCode == 115) { // �� F4 ִ��ɾ��
        var $top = $(thisElement).closest('.quoteListRep');                                 //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var del = $($top).find('.delbtn');                                                  //ɾ��ť
        if (typeof ($(del).html()) == 'undefined') return;
        $(del).click();
    }
    if (e && e.keyCode == 114) { // �� F3 
        e.returnValue = false;
        e.preventDefault();
        e.stopPropagation();
        var $topper = $(thisElement).closest('body');
        var $divtemp = $('#div_gen');                                                       //�������ؼ���div��id����
        var divtemp = $($topper).find($divtemp);
        if ($(divtemp).css('display') == 'block') {
            cancelAdd();
            var $last_track_05 = $('.quoteListRep').eq(-2);                                 //Repeater�°�����б��div
            $last_track_05.find('.service_name').focus();                                   //Ϊһ����֪��ݻ�ȡ����
        } else {
            displayAdd();
        }
    }
}

//���������л���ѵĿ�ݼ����÷���
function quoteActivityKeyEvent(event) {
    var e = event || window.event || arguments.callee.caller.arguments[0];//�¼�
    var thisElement = e.srcElement || e.target;//�¼�Դ

    //ִ�м��̲���
    if (e && e.keyCode == 113) { // �� F2 ִ�б༭
        var $top = $(thisElement).closest('.hdgl_02');                                      //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var edit = $($top).find('.editbtn');                                                //�༭��ť
        if (typeof ($(edit).html()) != 'undefined') {
            $(edit).click();
        } else {
            var $div_temp = $(thisElement).closest('.hdgl_02');                             //Repeater�°�����б��div
            if ($div_temp.attr('id') == "div_hdgl") {                                       //�������ؼ���div��id����
                var $topper = $(thisElement).closest('body');
                var divtemp = $($topper).find($div_temp);
                if ($(divtemp).css('display') == 'block') {
                    $($topper).find('#ctl00_ContentPlaceHolder_btnSave').click();           //���沢������Ӱ�ť
                }
            }
        }
    }
    if (e && e.keyCode == 115) { // �� F4 ִ��ɾ��
        var $top = $(thisElement).closest('.hdgl_02');                                      //Repeater�°�����б��div
        if (typeof ($top.html()) == 'undefined') return;
        var del = $($top).find('.delbtn');                                                  //ɾ��ť
        if (typeof ($(del).html()) == 'undefined') return;
        $(del).click();
    }
    if (e && e.keyCode == 114) { // �� F3 
        e.returnValue = false;
        e.preventDefault();
        e.stopPropagation();
        var $topper = $(thisElement).closest('body');
        var $divtemp = $('#div_hdgl');                                                       //�������ؼ���div��id����
        var divtemp = $($topper).find($divtemp);
        if ($(divtemp).css('display') == 'block') {
            cancelAdd();
            var $last_track_05 = $('.hdgl_02').eq(-2);                                      //Repeater�°�����б��div
            $last_track_05.find('.city').focus();                                           //Ϊһ����֪��ݻ�ȡ����
        } else {
            displayAdd();
        }
    }
}

/**
 * 序列化form对象
 * @param form
 * @returns {___anonymous12931_12932}
 */
function serializeObject(form){
    var o = {};
    $.each(form.serializeArray(),function(index){
    	var v = this['value'];
        if(o[this['name']]){
            o[this['name']] = o[this['name']]+","+v;
        } else {
            if(v!=""){
                o[this['name']] = v;
            }
        }
    });
    return o;
}
