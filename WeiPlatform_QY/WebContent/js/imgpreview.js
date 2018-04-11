/*
 * Image preview script 
 * powered by jQuery (http://www.jquery.com)
 * 
 * written by Alen Grakalic (http://cssglobe.com)
 * 
 * for more info visit http://cssglobe.com/post/1695/easiest-tooltip-and-image-preview-using-jquery
 *
 */
 
this.imagePreview = function(){	
	/* CONFIG */
		
		xOffset = 10;
		yOffset = 30;
		
		tmpXOffset = 0;
		tmpYOffset = 0;
		
		// these 2 variable determine popup's distance from the cursor
		// you might want to adjust to get the right result
		
	/* END CONFIG */
	$("img.preview").hover(function(e){		
		if (e.pageY > 500) {
			tmpYOffset = 180;			
		} else {
			tmpYOffset = 180;
		}
		if (e.pageX > 600) {
			tmpXOffset = 200;			
		} else {
			tmpXOffset = 0;
		}
		this.t = this.title;
		this.title = "";	
		var c = (this.t != "") ? "<br/>" + this.t : "";
		$("body").append("<p id='preview'><img src='"+ this.src +"' alt='Image preview' width='140px'/>"+ c +"</p>");								 
		$("#preview")
			.css("top",(e.pageY - xOffset - tmpYOffset) + "px")
			.css("left",(e.pageX + yOffset - tmpXOffset) + "px")
			.fadeIn("fast");						
    },
	function(){
		this.title = this.t;	
		$("#preview").remove();
    });	
	$("img.preview").mousemove(function(e){
		if (e.pageY > 500) {
			tmpYOffset = 180;			
		} else {
			tmpYOffset = 180;
		}
		if (e.pageX > 600) {
			tmpXOffset = 200;			
		} else {
			tmpXOffset = 0;
		}
		$("#preview")
			.css("top",(e.pageY - xOffset - tmpYOffset) + "px")
			.css("left",(e.pageX + yOffset - tmpXOffset) + "px");
	});			
};


// starting the script on page load
//$(document).ready(function(){
//	imagePreview();
//});