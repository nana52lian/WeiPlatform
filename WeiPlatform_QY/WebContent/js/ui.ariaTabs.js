/*
 * jQuery UI ariaTabs 1.0
 * Download by http://sc.xueit.com
 * Copyright (c) 2009 Felix Nagel for Namics (Deustchland) GmbH
 * Licensed under Creative Commens Attribution-Share Alike 3.0 Unported (http://creativecommons.org/licenses/by-sa/3.0/)
 * 
 *   Depends: 	ui.core.js
 *   			ui.tabs.js
 *  			ui.sortable.js (optional, see tabs docu)
 
 USAGE:::::::::::::
* Take a look in the html file or the (german) pdf file delivered with this example
* Simply add the js file uner the regular ui.tabs.js script tag
* Supports all options, methods and callbacks of the original widget
* sortable tabs are accessable but the sortable functionality as it is provided by the ui.sortable widget doesnt support ARIA 
 
 */
(function($) {
	$.fn.extend($.ui.tabs.prototype,{
	
		_original_init: $.ui.tabs.prototype._init,
		// when widget is initiated
		_init: function() {
			var self = this, options = this.options;			
			// fire original function
			self._original_init();			
			// ARIA
			self.element.attr("role", "application");
			self.list.attr("role", "tablist");	
			// init aria atrributes for each panel and anchor
			for (var x = 0; x < self.anchors.length; x++) {
				self._ariaInit(x);
			}		
			
			// keyboard
			self.element.keydown( function(event){
				switch (event.keyCode) {
					case $.ui.keyCode.RIGHT: 
						event.preventDefault();
						self.select(options.selected+1);
						break;
					case $.ui.keyCode.DOWN: 
						event.preventDefault();
						self.select(options.selected+1);
						break;
					case $.ui.keyCode.UP: 
						event.preventDefault();
						self.select(options.selected-1);
						break;
					case $.ui.keyCode.LEFT: 
						event.preventDefault();
						self.select(options.selected-1);
						break;
					case $.ui.keyCode.END: 
						event.preventDefault();
						self.select(self.anchors.length-1);
						break;
					case $.ui.keyCode.HOME: 
						event.preventDefault();
						self.select(0);
						break;				
				}
			});	
		},
		
		_original_load: $.ui.tabs.prototype.load,
		// called whenever tab is called but if option collapsible is set | fired once at init for the chosen tab
		load: function(index) {	
			// hide all unselected
			for (var x = 0; x < this.anchors.length; x++) {			
				// anchors
				this._ariaSet(x, false);
				// remove ARIA live settings
				if($.data(this.anchors[x], 'href.tabs')) {
					$(this.panels[x])
						.removeAttr("aria-live")
						.removeAttr("aria-busy");
				}
			};	
			// is remote? set ARIA states 
			if($.data(this.anchors[index], 'href.tabs')) {
				$(this.panels[index])
					.attr("aria-live", "polite")
					.attr("aria-busy", "true");
			}		
			// fire original function
			this._original_load(index);			
			// is remote? end ARIA busy
			if($.data(this.anchors[index], 'href.tabs')) {
				$(this.panels[index])
					.attr("aria-busy", "false");
			}			
			// set state for the activated tab
			this._ariaSet(index, true);
		},
		
		// sets aria states for single tab and its panel
		_ariaSet: function(index, state) {		
			var tabindex = (state) ? 0 : -1;
			// set ARIA state for loaded tab
			$(this.anchors[index])
				.attr("tabindex", tabindex)
				.attr("aria-selected", state)						
			// set ARIA state for loaded tab
			$(this.panels[index])
				.attr("aria-hidden", !state)
				.attr("aria-expanded", state);
		},
		
		// sets all attributes when plugin is called or if tab is added
		_ariaInit: function(index) {
			var self = this;
			// get widget generated ID of the panel
			var panelId = $(this.panels[index]).attr("id");		
			// ARIA anchors and li's
			$(this.anchors[index])
				.attr("role", "tab")
				.attr("aria-controls", panelId)
				.attr("id", panelId+"-tab")				
			// set li to presentation role
			.parent().attr("role", "presentation");				
			// ARIA panels aka content wrapper
			$(this.panels[index])
				.attr("role", "tabpanel")
				.attr("aria-labelledby", panelId+"-tab");				
			// if collapsible, set event to toggle ARIA state
			if (this.options.collapsible) {
				$(this.anchors[index]).bind(this.options.event, function(event) {
					// get class to negate it to set states correctly when panel is collapsed
					self._ariaSet(index, !$(self.panels[index]).hasClass("ui-tabs-hide"));
				});
			}
		},		
		
		_original_add: $.ui.tabs.prototype.add,
		// called when a tab is added
		add: function(url, label, index) {
			// fire original function
			this._original_add(url, label, index);
			// ARIA			
			this.element
				.attr("aria-live", "polite")
				.attr("aria-relevant","additions");
			
			// if no index is defined tab should be added at the end of the tab list
			if (index) {
				this._ariaInit(index);
				this._ariaSet(index, false);
			} else {
				this._ariaInit(this.anchors.length-1);
				this._ariaSet(this.anchors.length-1, false);
			}			
		},
		
		_original_remove: $.ui.tabs.prototype.remove,
		// called when a tab is removed
		remove: function(index) {
			// fire original function
			this._original_remove(index);	
			// ARIA
			this.element
				.attr("aria-live", "polite")
				.attr("aria-relevant","removals");
		},		
		
		_original_destroy: $.ui.tabs.prototype.destroy,
		// removes all the setted attributes
		destroy: function() {
			var self = this, options = this.options;
			// fire original function
			this._original_destroy();	
			// remove ARIA attribute
			// wrapper element
			self.element
				.removeAttr("role")
				.removeAttr("aria-live")
				.removeAttr("aria-relevant");
			// ul element
			self.list.removeAttr("role");		
			for (var x = 0; x < self.anchors.length; x++) {
				// tabs
				$(self.anchors[x])
					.removeAttr("aria-selected")
					.removeAttr("aria-controls")
					.removeAttr("role")
					.removeAttr("id")
					.removeAttr("tabindex")
				// remove presentation role of the li element
				.parent().removeAttr("role");
				// tab panels
				$(self.panels[x])
					.removeAttr("aria-hidden")
					.removeAttr("aria-expanded")
					.removeAttr("aria-labelledby")
					.removeAttr("aria-live")
					.removeAttr("aria-busy")
					.removeAttr("aria-relevant")
					.removeAttr("role");
			}
		}		
	});
})(jQuery); 
