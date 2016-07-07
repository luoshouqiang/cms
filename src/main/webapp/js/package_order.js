// JavaScript Document
$(function(){
	siderbarControl("lframe");
	 $("#searchform").validate({
       submitHandler: function(form) {
			//alert("submitted!");
			 getPackageOrder();
			
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

$(document).on("click",".opr-qui",function(){
		$this=$(this);
		$parentTd=$this.parent("td");
		var id=$parentTd.attr("id");
		var dataJson={packgeId:id,status:"ORDER_CANCEL"}
		 $.ajax({
             type: "post",
             url: "/cms/package/processOrder",
             data: dataJson,
			 //contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
             		if(data.code==0){
             			alert("处理成功！");
             			getPackageOrder();
             		}
             }
         })
});

$(document).on("click",".opr-det",function(){
		var id=$(this).parent("td").attr("id");
	 $.ajax({
              type: "post",
             url: "/cms/package/orderDetail ",
             data:{packageId:id},
             dataType: "json",
             success: function(data){
             	console.log(data);
             		var dataList=data.data[0].detailList;
             		console.log(dataList);
             		var html='';
             		for(i in dataList){
             				html += '<tr><td class="">'+dataList[i].item_name+'</td><td>'+dataList[i].total_times+'</td><td>'+dataList[i].used_times+'</td></tr>'	 
             		}
             		    $('#tk-table-list').html(html);
         		    	$(".tk-bg").show();
						$(".tk-table").show();
             		}
            
         })
})

$(document).on('click', '.tk-ok', function(event) {
		$(".tk-bg").hide();
		$(".tk-table").hide();
});


	})




function getPackageOrder(){//获取套餐

	 var orderData={userName:$("#userName").val(),phone:$("#phoneNum").val(),startTime:$("#timebegin").val(),endTime:$("#timeend").val(),orderId:$("#orderNo").val(),
  type:$("#servOrderType").val(),status:$("#serviceOrderStatus").val()};
	 $.ajax({
             type: "post",
             url: "/cms/package/listOrder",
             data: JSON.stringify(orderData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
             			if(data.code==0){
                         	if(data.data.length==0){
                         		alert("没有符合条件的搜索！");
                         	}
                         var html = "";
                         var opr="";
                         $.each(data.data, function(i, data){
                         	if(data.package_status=="未付款"){
                         		opr='<a href="javascript:void(0);" class="opr-qui">取消</a><a href="javascript:void(0);" class="opr-det">详情</a>';
                         	}
                         	else{
                         		opr='无';
                         	}
	                         			html += '<tr><td class="ta-l">'+data.package_id+'</td><td>'+data.user_name+'</td><td>'+data.mobile+'</td><td>'+data.area_name+'</td>'
			                +'<td class="package-name" packageid='+data.package_id+'>'+data.package_name+'</td><td>'+data.package_price+'</td>'
			                +'<td>'+data.actual_price+'</td><td>'+data.created_time+'</td><td>'+data.package_status+'</td>'	              
			                +'<td class="operation-btn ta-r" id='+data.package_id+'>'+opr+'</td></tr>';
	                    
                         
                         });
                            $('#table-a-list').html(html);
                }
                else{
                	alert("搜索失败！");
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