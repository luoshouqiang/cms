// JavaScript Document

jQuery.extend( {  
    reg : function(x) {  
        return new RegExp("^[0-9]\\d*$").test(x);  
    },
    amount : function(obj, mode) { 
        var x=obj.val(); 
        if (this.reg(parseInt(x))) {  
            if (mode) {  
                x++;  
				//console.log(x);
            } else {  
                x--;  
            }  
        } else {  
            obj.val(0);  
        }  
        return x;  
    },  
    reduce : function(obj) {//减
		var obj= $(obj).siblings("input");
		var min=parseInt(obj.attr("datamin"));  
        var x = this.amount(obj, false);  
        if (parseInt(x) >= min) {  
            obj.val(x);  
        } else {  
            obj.val(0);  
        }  
    },  
    add : function(obj) {//加
		var obj= $(obj).siblings("input");
		var max=parseInt(obj.attr("datamax")); 
        var x = this.amount(obj, true);  
        if (parseInt(x) <= parseInt(max)) {  
            obj.val(x);  
        } else {  
            obj.val(max == 0 ? 0 : max);  
        }  
    },  
    modify : function(obj) {//修改
		var obj=$(obj)  
        var x = parseInt(obj.val());
		//console.log(parseInt(x));  
       var max=parseInt(obj.attr("datamax")); 
	   var min=parseInt(obj.attr("datamin")); 
        if (!this.reg(x)) {  
            obj.val(0);  
            return;  
        }
		else{
				 if (x < min) {  
					obj.val(min);  
				} else if (x > max) {  
					obj.val(max == 0 ? 0 : max);  
				}else{
					obj.val(x);
				}
			} 
    },
	tip:function(obj,tipcon){//提示
		
		
		}
		
});  