// JavaScript Document
$(function(){
	siderbarControl("lframe");
	 $("#searchform").validate({
       submitHandler: function(form) {
			//alert("submitted!");
			 getInsurOrder();
			
		},
		rules: {
				userName: {//用户名字
					//minlength: 2
				},
			},
			messages: {
				userName: {
					//required: "请输入姓名！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	
$(document).on("click",".opr-fin",function(){
		$this=$(this);
		$parentTd=$this.parent("td");
		var id=$parentTd.attr("id");
		var dataJson={insurId:id,status:"SERVICE_FINISHED"}
		 $.ajax({
             type: "post",
             url: "/cms/package/processInsurOrder",
             data: dataJson,
			 //contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
             		if(data.code==0){
             			alert("处理成功！");
             			getInsurOrder();
             		}
             }
         })
});
$(document).on("click",".opr-qui",function(){
		$this=$(this);
		$parentTd=$this.parent("td");
		var id=$parentTd.attr("id");
		var dataJson={insurId:id,status:"ORDER_CANCEL"}
		 $.ajax({
             type: "post",
             url: "/cms/package/processInsurOrder",
             data: dataJson,
			 //contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
             		if(data.code==0){
             			alert("处理成功！");
             			getInsurOrder();
             		}
             }
         })
});


	})




function getInsurOrder(){//获取套餐

	 var orderData={userName:$("#userName").val(),phone:$("#phoneNum").val(),startTime:$("#timebegin").val(),endTime:$("#timeend").val(),orderId:$("#orderNo").val(),
  type:$("#servOrderType").val(),status:$("#serviceOrderStatus").val()};
	 $.ajax({
             type: "post",
             url: "/cms/package/listInsurOrder",
             data: JSON.stringify(orderData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
             			if(data.code==0){
                         var html = '';
                         $.each(data.data, function(i, data){
                         	if(data==null){
                         		alert("没有符合条件的搜索！");
                         	}
                         	var status=data.insur_status;
                         	var opr="";
                         	if(status=="未付款"){
                         		opr='<a href="javascript:void(0);" class="opr-fin">完成</a><a href="javascript:void(0);" class="opr-qui">取消</a>';
                         	}
                         	else{
                         		opr="无";
                         	}
	                         			html += '<tr><td class="ta-l">'+data.insur_id+'</td><td>'+data.user_name+'</td><td>'+data.mobile+'</td><td>'+data.area_name+'</td><td>'+data.insur_name+'</td>'
			                +'<td class="package-name">'+data.insur_price+'</td><td class="item-name">'+data.created_time+'</td>'
			                +'<td>'+data.insur_status+'</td>'
			                +'<td>'+data.insur_content+'</td>'
			                +'<td class="operation-btn ta-r" id='+data.insur_id+'>'+opr+'</td></tr>';
                         });
                
                         $('#table-a-list').html(html);
                     }else{
                        alert("查询失败！");
                     }
						 siderbarControl("lframe");//设置左侧导航条高度
                      }
         });
	}

/*
 * 获取url中的参数值
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}	