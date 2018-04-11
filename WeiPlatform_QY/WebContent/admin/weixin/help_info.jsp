<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script>
    var UrlEncode;
    UrlEncode = function(str) {
        return encodeURIComponent(str).replace(/!/g, '%21').replace(/'/g, '%27').replace(/\(/g, '%28').replace(/\)/g, '%29').replace(/\*/g, '%2A').replace(/%20/g, '+');
    };
    //注入微信配置文件信息，如需调试，请修改debug=true即可开启调试模式
    //请提供应用名称，生产环境的二级域名（例如：***.astrazeneca.com）。我们配置后台。提供key和secret信息。
    document.write('<script type="text/javascript" src="http://eaopen.astrazeneca.com/api/config.js?key=6aafdb7ff3261fd0&secret=61B7C34B9C26ED2AA19291C4F11FA644&debug=false&url=' + UrlEncode(window.location.href) + ' "><\/script>');
    </script>
<title></title>
</head>
<body>
<!-- 图文转发配置信息 -->
<script>
var lineLink = 'http://***.astrazeneca.com/jssdk_test.html';
var imgUrl = 'http://wx-public.qiniudn.com/edoctor.png';
var descContent = "这里是描述";
var shareTitle = '这里是标题';

function ShareMsg() {
    // 2. 分享接口
    // 2.1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口  
    wx.onMenuShareAppMessage({

        title: shareTitle,
        link: lineLink,
        imgUrl: imgUrl,
        desc: descContent,

        trigger: function(res) {
            // alert('用户点击发送给朋友');
        },
        success: function(res) {
            //  alert('已分享');

        },
        cancel: function(res) {
            //  alert('已取消');
        },
        fail: function(res) {
            // alert(JSON.stringify(res));
        }
    });

    // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
    wx.onMenuShareTimeline({
        title: descContent,
        link: lineLink,
        imgUrl: imgUrl,
        desc: descContent,
        trigger: function(res) {
            //  alert('用户点击分享到朋友圈');
        },
        success: function(res) {
            // alert('已分享');
        },
        cancel: function(res) {
            // alert('已取消');
        },
        fail: function(res) {
            // alert(JSON.stringify(res));
        }
    });

};
wx.ready(function() {
    ShareMsg();
    

    wx.hideOptionMenu();

});
</script>
<div style="width:100%;border-bottom: 2px solid #317ecb;color:#000;font-size:18px;margin-bottom:5px;">
	<s:property value="articleEntity.title"/>
</div>
<div style="color:#393939;font-size:1em;margin:5px 0 5px 0;border: 0px solid #317ecb;">
	<s:property value="articleEntity.content" escape="false" />
</div>
<div style="width:100%;border-top: 2px solid #317ecb;color:#000;margin-bottom:5px;">
    阅读:<s:property value="readNumber"/>
</div>
</body>
</html>