// JavaScript Document
$(function(){
	 siderbarControl("lframe")//设置左侧导航条高度
$("#serviceorder-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
		var searchData={total:$("#order-number").val(),orderTime:$("#order-time").val(),days:$("#days").val()};
			 $.ajax({
             type: "post",
             url: "/cms/serverOrder/outer/batchGenernateOrders",
			 data: searchData,
             dataType: "json",
             success: function(data){
				// console.log(data);
				  console.log(data);
				 var dataCode=data.code;
				
				 if(dataCode==200){
					 //getServiceOrderList(direction,orderby);
					  alert(data.msg);
					   getServiceOrderList();
					 }
				else{
					alert(data.msg);
					}
                      }
         });
			//form.submit();
		},
		rules: {
				orderNumber: {//服务订单数量
					required:true
				},
				orderTime: {//订单时间
					required:true
				},
				days: {//时间
					required:true
				}
			},
			messages: {
				orderNumber: {
					required: "请输入总数量！"
				},
				orderTime: {
					required: "请选择时间！"
				},
				days: {
					required: "请输入时间！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	
	
		//显示更多订单信息
		$(document).on("click",".more-btn",function(){
			$this=$(this);
			var orderNo=$this.parent("td").siblings(".order-no").text();//获取订单编号
			//console.log(orderNo);
			getMoreServiceOrder(orderNo);
			});
			//关闭显示订单详情信息弹框
		$(document).on("click","#service-ord-ok",function(){
			new hideBombBox({tkbg:".tk-bg",tkbox:".tk-more-serviceord"});
			});
		
		//订单排序
	$(".ord-sort").click(function(){
		$this=$(this);
		var orderby=$this.attr("ordway");
		var direction=$this.attr("direction");
		//console.log(direction);
		if(direction=="NOSORT"){
			//alert(1);
			$this.attr("direction","ASC");
			direction="ASC";
			//direction=$this.attr("direction");
			$this.siblings("th").find("span i").removeClass("icon-arrow-up");
			$this.siblings("th").find("span i").removeClass("icon-arrow-down");
			$this.siblings("th").find("span i").addClass("icon-arrow-updown");
			$this.siblings("th.ord-sort").attr("direction","NOSORT");
			$this.find("span i").removeClass("icon-arrow-updown");
			$this.find("span i").addClass("icon-arrow-up");
				//console.log(direction);
			getServiceOrderList(direction,orderby);
			}
		else if(direction=="ASC"){
			//alert(2);
			direction="DESC";
			$this.attr("direction","DESC");
			$this.siblings("th").find("span i").removeClass("icon-arrow-up");
			$this.siblings("th").find("span i").removeClass("icon-arrow-down");
			$this.siblings("th").find("span i").addClass("icon-arrow-updown");
			$this.siblings("th.ord-sort").attr("direction","NOSORT");
			$this.find("span i").removeClass("icon-arrow-up");
			$this.find("span i").addClass("icon-arrow-down");
				//console.log(direction);
			getServiceOrderList(direction,orderby);
			}
		else if(direction=="DESC"){
			//alert(3);
			direction="ASC"
			$this.attr("direction","ASC");
			$this.siblings("th").find("span i").removeClass("icon-arrow-up");
			$this.siblings("th").find("span i").removeClass("icon-arrow-down");
			$this.siblings("th").find("span i").addClass("icon-arrow-updown");
			$this.siblings("th.ord-sort").attr("direction","NOSORT");
			$this.find("span i").removeClass("icon-arrow-down");
			$this.find("span i").addClass("icon-arrow-up");
				//console.log(direction);
			getServiceOrderList(direction,orderby);
			}
		
		
		
		});
			
		
	
	})

	//获取服务订单列表
function getServiceOrderList(direction,orderby){
  var orderData={startTime:$("#order-time").val(),endTime:$("#order-time").val(),orderBy:orderby,direction:direction};
			 $.ajax({
             type: "post",
             url: "/cms/serverOrder/outer/list",
             //data: orderData,
			 data: JSON.stringify(orderData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
				 //console.log(data);
				   if(data[0]==null){
					  $('#table-a-list').html("");
					 alert("没有符合条件的搜索！");
					 return;
					 }
			//console.log(data);
		
						var html;
						$.each(data,function(i,dVal){
							var orderStatus=dVal.order_status;
							//console.log(orderStatus);
							var opr="";
							var orderSta="";
							//判断是否显示修改按钮
                              var orderStatus=dVal.order_status;
							  if(orderStatus=="等待服务"){
								  opr='<a href="javascript:void(0);" class="modify-btn">修改</a><a href="javascript:void(0);" class="more-btn">更多</a>'
								  }
							  else{
								   opr='<a href="javascript:void(0);" class="more-btn">更多</a>'
								  }
					if(orderStatus=="服务完成"){
							if(i%2==0){
								html+='<tr>'
			+'<td  class="ta-l order-no">'+dVal.order_id+'</td>'
			+'<td>'+dVal.order_name+'</td>'
			+'<td>'+dVal.area_name+'</td>'
			+'<td>'+dVal.user_name+'</td>'
			+'<td>'+dVal.mobile+'</td>'
			+'<td>'+dVal.plat_no+'</td>'
			+'<td>'+dVal.created_time+'</td>'
			+'<td>'+orderStatus+'</td>'
			+'<td class="change operation-btn ta-r">'+opr+'</td></tr>';
			              }	
		  else{
			  html+='<tr class="tr-flag">'
			+'<td  class="ta-l order-no">'+dVal.order_id+'</td>'
			+'<td>'+dVal.order_name+'</td>'
			+'<td>'+dVal.area_name+'</td>'
			+'<td>'+dVal.user_name+'</td>'
			+'<td>'+dVal.mobile+'</td>'
			+'<td>'+dVal.plat_no+'</td>'
			+'<td>'+dVal.created_time+'</td>'
			+'<td>'+orderStatus+'</td>'
			+'<td class="change operation-btn ta-r">'+opr+'</td></tr>';
			  }
			  }
						});
					  $('#table-a-list').html(html);
					  siderbarControl("lframe")//设置左侧导航条高度
                      }
         });
	
}
//点击更多按钮调用获取订单详细信息
function getMoreServiceOrder(orderNo){
	 var orderData={orderId:orderNo};
			 $.ajax({
             type: "post",
             url: "/cms/serverOrder/list",
             //data: orderData,
			 data: JSON.stringify(orderData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
				 var servremark="";
				 var remark=data[0].serv_remark;
				 if(remark==null){
					 servremark="无";
					 }
				 //console.log(data);
				var html="";
					html='<div class="row">'+
                    '<p class="col-6"><strong>订单编号：</strong><span>'+data[0].order_id+'</span></p>'+
                    '<p class="col-6"><strong>订单类型：</strong><span>'+data[0].order_name+'</span></p>'+
                '</div>'+
				 ' <div class="row">'+
                    '<p class="col-6"><strong>小区名称：</strong><span>'+data[0].area_name+'</span></p>'+
                    '<p class="col-6"><strong>用户姓名：</strong><span>'+data[0].user_name+'</span></p>'+
                '</div>'+
               ' <div class="row">'+
                    '<p class="col-6"><strong>电话号码：</strong><span>'+data[0].mobile+'</span></p>'+
                    '<p class="col-6"><strong>车牌号：</strong><span>'+data[0].plat_no+'</span></p>'+
                '</div>'+
                '<div class="row">'+
                   '<p class="col-6"><strong>下单时间：</strong><span>'+data[0].created_time+'</span></p>'+
                   '<p class="col-6"><strong>服务开始时间：</strong><span>'+data[0].serv_start_time+'</span></p>'+
                '</div>'+
                '<div class="row">'+
                     '<p class="col-6"><strong>服务结束时间：</strong><span>'+data[0].serv_end_time+'</span></p>'+
                     '<p class="col-6"><strong>订单状态：</strong><span>'+data[0].order_status+'</span></p>'+
                '</div>'+
                 '<div class="row">'+
                    '<p class="col-6"><strong>订单价格：</strong><span>'+data[0].order_total_price+'</span></p>'+
                    '<p class="col-6"><strong>车位号：</strong><span>'+data[0].parking_code+'</span></p>'+
               ' </div>'+
                  '<div class="row row-remark">'+
                    '<p class="col-12"><strong class="col-2">备注信息：</strong>'+
                    '<span class="col-10">'+servremark+'</span>'+
                    '</p>'+
                '</div>'+
                '<div class="main-bg"></div>'
				
					$("#tk-moreserviceord-main").html(html);
					new showBombBox({tkbg:".tk-bg",tkbox:".tk-more-serviceord"});
					 }
			})
	}



 