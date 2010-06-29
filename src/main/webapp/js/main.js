jQuery(document).ready(function() {
        jQuery("input.form-field").focus(function(){
        	jQuery(this).addClass("selected");
        });
        jQuery("input.form-field").blur(function(){
        	jQuery(this).removeClass("selected");
        });
        
        //Animating buttons
        jQuery(".column-button").hover(
         	function(){
				jQuery(this).addClass("hoverover");
        	},
         	function(){
				jQuery(this).removeClass("hoverover");
        	}    	
        );        
        jQuery("#company-button, #dev-button").click(
         	function(){
//                 _gaq.push(['_trackEvent','Keep in touch','Newsletter']);
				//Identify another button id
				anotherButtonId = "company-button";
				if (anotherButtonId == jQuery(this).attr("id")) {
					anotherButtonId = "dev-button";
				}
         		//Another form and show: show button and hide form
         		jQuery("#" + anotherButtonId).show().next().hide();
         		//This button and show: hide button and show form; remove class identifier
         		jQuery(this).hide().next().show();
         		jQuery(this).removeClass("hoverover");
        	}
        );                
        
});
 