/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2003-2009 Frederico Caldeira Knabben
 *
 * == BEGIN LICENSE ==
 *
 * Licensed under the terms of any of the following licenses at your
 * choice:
 *
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 *
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 *
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * == END LICENSE ==
 *
 * Common objects and functions shared by all pages that compose the
 * File Browser dialog window.
 */

// Automatically detect the correct document.domain (#1919).
(function()
{
	var d = document.domain ;

	while ( true )
	{
		// Test if we can access a parent property.
		try
		{
			var test = window.top.opener.document.domain ;
			break ;
		}
		catch( e )
		{}

		// Remove a domain part: www.mytest.example.com => mytest.example.com => example.com ...
		d = d.replace( /.*?(?:\.|$)/, '' ) ;

		if ( d.length == 0 )
			break ;		// It was not able to detect the domain.

		try
		{
			document.domain = d ;
		}
		catch (e)
		{
			break ;
		}
	}
})() ;

function AddSelectOption( selectElement, optionText, optionValue )
{
	var oOption = document.createElement("OPTION") ;

	oOption.text	= optionText ;
	oOption.value	= optionValue ;

	selectElement.options.add(oOption) ;

	return oOption ;
}

var oConnector	= window.parent.oConnector ;
var oIcons		= window.parent.oIcons ;


function StringBuilder( value )
{
    this._Strings = new Array( value || '' ) ;
}

StringBuilder.prototype.Append = function( value )
{
    if ( value )
        this._Strings.push( value ) ;
}

StringBuilder.prototype.ToString = function()
{
    return this._Strings.join( '' ) ;
}

var req;                    //定义变量，用来创建xmlhttprequest对象    

//产生不重复的随机数   
var rn = Math.ceil(Math.random() * 1000000);   
var rnch = rn;   

function rndnum() {   
  while (rn == rnch) rn = Math.ceil(Math.random() * 1000000);   
  rnch = rn;   
  return rn;   
}   

//删除文件,Ajax开始   
function deleteFile(file) {   
  var url = "FCKdel_file.jsp?filePath=" + escape(file) + "&UD=" + rndnum();                  //要请求的服务端地址      
  if (window.XMLHttpRequest)                                                                  //非IE浏览器及IE7(7.0及以上版本)，用xmlhttprequest对象创建      
  {   
      req = new XMLHttpRequest();   
  }   
  else if (window.ActiveXObject)                                                              //IE(6.0及以下版本)浏览器用activexobject对象创建,如果用户浏览器禁用了ActiveX,可能会失败.      
  {   
      req = new ActiveXObject("Microsoft.XMLHttp");   
  }   
     
  if (req)                                                                                    //成功创建xmlhttprequest   
  {   
      req.open("GET", url, true);                                                             //与服务端建立连接(请求方式post或get，地址,true表示异步)      
      req.onreadystatechange = callback;                                                      //指定回调函数      
      req.send(null);                                                                         //发送请求      
  }   
}   

function callback() {       
  Refresh();      //刷新一下    
}  

