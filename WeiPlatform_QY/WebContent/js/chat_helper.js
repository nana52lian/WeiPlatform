// 日期格式化
// yyyy-MM-dd hh:mm:ss
// hh:mm:ss
Date.prototype.format = function(format){ 
	var o = { 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S" : this.getMilliseconds() //millisecond 
	};

	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
}

//获取当前时间
function getShortDate() {
	var now = new Date(); 
	var nowStr = now.format("hh:mm:ss"); 
	return nowStr;
}

//获取当前时间
function getLongDate() {
	var now = new Date(); 
	var nowStr = now.format("yyyy-MM-dd hh:mm:ss"); 
	return nowStr;
}

//open window.
function openWindow(tPage, win_name, tWidth, tHeight) {
	var windowWidth = window.screen.width;
    var windowHeight = window.screen.height;
    var curLeft = windowWidth/2 - tWidth/2;
    var curTop = windowHeight/2 - tHeight/2;
	var win = window.open(tPage,win_name,'toolbar=no,scrollbars=no,menubar=no,resizable=no,status=no,location=no,width=' + tWidth + ',height=' + tHeight + ',top=' + curTop + ',left=' + curLeft);
	win.focus();
	return win;
}