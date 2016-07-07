// JavaScript Document
$(function(){
	 getServiceOrderType(".serv-order-type");//获取服务订单类型
	 getServiceOrderStatus(".serv-Order-Status");//获取服务订单状态
	siderbarControl("lframe");//设置左侧导航条高度
	//提交修改信息
	$("#userrelation").validate({
       submitHandler: function(form) {
			//alert("submitted!");
			var searchData={orderId:$("#change-parking-no").val(),status:$("#change-service-status").val(),
			servProv:$("#change-service-prov").val(),servProvNo:$("#change-service-provno").val(),
			startTime :$("#change-timebegin").val(),endTime :$("#change-timeend").val(),remark:$("#change-remark").val()};
			//console.log(searchData);
			 $.ajax({
             type: "post",
             url: "/cms/serverOrder/executeOrder",
             data: searchData,
             dataType: "json",
             success: function(data){
			     if(data.code=="200"){
					 alert("修改成功！");
					 new hideBombBox({tkbg:".tk-bg",tkbox:".mk-user-serviceorder"});
					 getServiceOrderList();
					 }
				 else{
					 alert("修改失败！");
					 }
						}
         });
			//form.submit();
		},
		rules: {
				userName: {//用户名字
					required: true,
				},
				phoneNum: {//电话号码
				   required: true,
				   isMultiMobile:true
				}
			},
			messages: {
				userName: {
					required: "用户名不能为空！",
				},
				phoneNum: {
					required: "手机号不能为空,可填写多个，中间用逗号隔开！"
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });
	//提交修改信息结束
	
	
	
	
	
	//搜索服务订单
	$(document).on("click","#serviceOrderSearch",function(){
			getServiceOrderList();
		});
	
		//修改订单信息
		$(document).on("click",".modify-btn",function(){
			new showBombBox({tkbg:".tk-bg",tkbox:".mk-user-serviceorder"});
			$this=$(this);
			var orderNo=$this.parent("td").siblings(".order-no").text();//获取订单编号
			$("#change-parking-no").val(orderNo);//把订单编号放入弹框
			});
		//关闭修改订单信息弹框
		$(document).on("click","#mkuserserviceorder-close",function(){
			new hideBombBox({tkbg:".tk-bg",tkbox:".mk-user-serviceorder"});
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
		//分页
	var pageIndex=1;
	$(document).on("click",".page .pageone",function(){
           var orderNoOrderby=$(".order-no").attr("ordway");
		   var orderNoDirection=$(".order-no").attr("direction");
		   var orderDateOrderby=$(".order-date").attr("ordway");
		   var orderDateDirection=$(".order-date").attr("direction");
		   var direction='',orderby='';
		   if(orderNoDirection!="NOSORT"){
		     	direction=orderNoDirection;
		    	orderby=orderNoOrderby;
		   }
		   else{
		   	   	direction=orderDateDirection;
		   	    orderby=orderDateOrderby;
		   }
            console.log(direction);
            console.log(orderby);
		    var pageSize=20;
			$this=$(this);
			var rePageIndex=page($this);
			
			 if(!rePageIndex){
			 	return;
			 }
			 else{
			getServiceOrderList(direction,orderby,rePageIndex,pageSize);
			 }
	});

	function page($this){
			var pageData=$this.attr("data");
			var pageTotal=parseInt($('.page').attr("total"));
			var nowPageIndex=parseInt($('.page').attr("pageIndex"));
			if(pageData=="pre"){
				if(pageIndex==1){
					alert("当前已经是第一页！");
					return;
				}
				else{
					pageIndex=parseInt(pageIndex)-1;
				}
			}
			else if(pageData=='next'){
				if(nowPageIndex==pageTotal){
						alert("当前已经是最后一页！");
						return;
					}else{
				pageIndex=parseInt(pageIndex)+1;
				}
			}
			else if(pageData=='current'){
				return;
			}
			else{
				pageIndex=pageData;
			}
	      return pageIndex;
}
	
	    })

	//获取服务订单列表
function getServiceOrderList(direction,orderby,pageIndex,pageSize){
	if(pageIndex==null){
		pageIndex=1;
	}
	if (pageSize==null) {
		pageSize=20;
	}
	var pageIndex=parseInt(pageIndex);
  var orderData={userName:$("#userName").val(),roomNo:$("#roomNo").val(),phone:$("#phoneNum").val(),parkingCode:$("#parkingNo").val(),
  startTime:$("#timebegin").val(),endTime:$("#timeend").val(),orderId:$("#orderNo").val(),platNo:$("#platNo").val(),
  servProv:$("#servProv").val(),servProvNo:$("#servProvNo").val(),type:$("#servOrderType").val(),status:$("#serviceOrderStatus").val(),
  orderBy:orderby,direction:direction,pageIndex:pageIndex,pageSize:pageSize};
  //console.log(orderData);
			 $.ajax({
             type: "post",
             url: "/cms/serverOrder/list",
             //data: orderData,
			 data: JSON.stringify(orderData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
				 console.log(data);
				   if(data.datas[0]==null){
					  $('#table-a-list').html("");
					 alert("没有符合条件的搜索！");
					 return;
					 }
			//console.log(data);
		
						var html;
						$.each(data.datas,function(i,dVal){
							var orderStatus=dVal.order_status;
							//console.log(orderStatus);
							var opr="";
							var orderSta="";
							var roomNo=dVal.roomno;
							if(roomNo==null){roomNo="无";}
							//判断是否显示修改按钮
                              var orderStatus=dVal.order_status;
							  if(orderStatus=="等待服务"){
								  opr='<a href="javascript:void(0);" class="modify-btn">修改</a><a href="javascript:void(0);" class="more-btn">更多</a>'
								  }
							  else{
								   opr='<a href="javascript:void(0);" class="more-btn">更多</a>'
								  }
								
							if(i%2==0){
								html+='<tr>'
			+'<td  class="ta-l order-no">'+dVal.order_id+'</td>'
			+'<td>'+dVal.order_name+'</td>'
			+'<td>'+dVal.area_name+'</td>'
			+'<td>'+dVal.user_name+'</td>'
			+'<td>'+dVal.mobile+'</td>'
			+'<td>'+roomNo+'</td>'
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
			+'<td>'+roomNo+'</td>'
			+'<td>'+dVal.plat_no+'</td>'
			+'<td>'+dVal.created_time+'</td>'
			+'<td>'+orderStatus+'</td>'
			+'<td class="change operation-btn ta-r">'+opr+'</td></tr>';
			  }
						});
					  $('#table-a-list').html(html);
					    pageList(data);//调用设置分页按钮函数
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
             	//console.log(data.datas[0]);
             	var data=data.datas;
				 var servremark="";
				 var remark=data[0].serv_remark;
				 if(remark==null){
					 remark="无";
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
                    '<span class="col-10">'+remark+'</span>'+
                    '</p>'+
                '</div>'+
                '<div class="main-bg"></div>'
				
					$("#tk-moreserviceord-main").html(html);
					new showBombBox({tkbg:".tk-bg",tkbox:".tk-more-serviceord"});
					 }
			})
	}

//获取服务订单类型函数	
function getServiceOrderType(servOrderType){
	var servOrderType=$(servOrderType);//服务订单类型的类名
	 $.ajax({
	 type: "get",
	 url: "/cms/serverOrder/getTypes",
	 data: {},		
	 dataType: "json",
	 success: function(data){
		 //console.log(data[0]);
				 var html = '';
				   html+='<option value="">全部</option>';
				 $.each(data,function(i){
					 	for (var key in data[i]) {
						//console.log(key);
						//console.log(data[i][key]);
					   html += '<option value='+key+'>'+data[i][key]+'</option>';
					    }
					 }) 
				   servOrderType.html(html);
			  }
	 })
	
	}
	
//获取服务订单状态函数	
function getServiceOrderStatus(servOrderStatus){
	var servOrderStatus=$(servOrderStatus);//服务订单类型的类名
	 $.ajax({
	 type: "get",
	 url: "/cms/serverOrder/getStatus",
	 data: {},		
	 dataType: "json",
	 success: function(data){
		 //console.log(data[0]);
				 var html = '';
				  html+='<option value="">全部</option>';
				 $.each(data,function(i){
					 	for (var key in data[i]) {
						//console.log(key);
						//console.log(data[i][key]);
					   html += '<option value='+key+'>'+data[i][key]+'</option>';
					    }
					 }) 
				   servOrderStatus.html(html);
			  }
	 })
	
	}




 