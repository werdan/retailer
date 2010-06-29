/**
 * jQuery Resettable - A plugin function to make form fields reset to their initial values when left blank
 * Copyright (c) 2010 Ben Byrne - ben(at)fireflypartners(dot)com | http://www.fireflypartners.com
 * Dual licensed under MIT and GPL.
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * Date: 03/12/2010
 * @author Ben Byrne
 * @version 0.2.0
 *
 */

(function($) {

  $.fn.resettable = function ( ) {
  
    fields = new Array();
  
    this.each(function() {
  
      //check to make sure the element has a name attribute. If not, do nothing!
      //if (!jQuery(this).attr("name")) return true;
    
      jQuery(this).data("original", jQuery(this).val()) ;
      
      jQuery(this).focus(function() {
        if ( jQuery(this).val() == jQuery(this).data("original") ) 	jQuery(this).val("");   
      }).blur(function() {
    		if (jQuery(this).val() == "") jQuery(this).val( jQuery(this).data("original") );
      });
      
    });
    return this;
  }

})(jQuery);