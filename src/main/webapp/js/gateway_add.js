// JavaScript Document
$(function(){
  areaData();	//初始化下拉选择框的小区数据
  siderbarControl("lframe")//设置左侧导航条高度
$("#gateway-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
			var entrance="",exit="";
			var areaId=$("#areaName").val();
			var exitEntrance=$("#exit-entrance").val();
			if(exitEntrance=="1"){
				entrance="1";exit="2";
				}
			else if(exitEntrance=="2"){
				entrance="2";exit="1";
				}
			else{
				entrance="1";exit="1";
				}
		var searchData={areaId:areaId,lotsId:$("#parking-lots-id").val(),gatewayType:$("#gateway-type").val(),entrance:entrance,
						exit:exit,gatewayName:$("#gateway-name").val()};
			 $.ajax({
             type: "post",
             url: "/cms/area/addGateway",
			 data: searchData,
             dataType: "json",
             success: function(data){
				 //console.log(data);
				 var dataCode=data.code;
				 if(dataCode==200){
					 alert("数据添加成功！");
					 getGateWay(areaId);//根据小区id获取此小区的道闸列表
					 }
				else{
					alert(data.msg);
					}
                      }
         });
			//form.submit();
		},
		rules: {
				areaName: {//小区名称
					required:true
				},
				parkingLotsId: {//停车场名称
					required:true
				},
				gatewayType: {//闸道口类型
					required:true
				},
				exitEntrance: {//是否进口
				required:true
				},
				gatewayName:{//闸道口名称
				required:true
				}
			},
			messages: {
				areaName: {
					required: "请选择小区！"
				},
				parkingLotsId: {
					required: "请选择停车场！"
				},
				gatewayType: {
					required: "请选择闸道口类型！",
				},
				exitEntrance: {
					required: "请选择进出口类型！",
				},
				gatewayName: {
					required: "请填写闸道口名称！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });		
	$("#areaName").change(function(){
		var areaId=$(this).val();
		getParkingLotsId(areaId);//根据小区id调用函数获取停车场id 位于common函数中
		getGateWay(areaId);//根据小区id获取此小区的道闸列表
		});
	})

//获取小区
function areaData(){
			 $.ajax({
             type: "get",
             url: "/cms/area/list",
             data: {},		
             dataType: "json",
             success: function(data){
				       var areaId=data[0].area_id;
				        var html = ''; 
                         $.each(data, function(i, area){
                               html += '<option value='+area.area_id+'>'+area.area_name+'</option>';
                         });
                         $('#areaName').append(html);
							getParkingLotsId(areaId);//根据小区id调用函数获取停车场id 位于common函数中
							getGateWay(areaId);//根据小区id获取此小区的道闸列表
                      }
         });
		}
//获取停车场闸道口 根据小区
function getGateWay(areaId){
	//console.log(areaId);
			 $.ajax({
             type: "get",
             url: "/cms/area/getGateWaysByAreaId",
             data: {areaId:areaId},		
             dataType: "json",
             success: function(data){
						//console.log(data);
						var html="";
						$.each(data.data,function(i,dVal){
							var gatewayType=dVal.gateway_type,isEntrance=dVal.is_entrance,isExit=dVal.is_exit;
							if(gatewayType==1){
								gatewayType="停车场进出闸口兼小区进出口";
								}else{
							    gatewayType="停车场进出闸口";
									}
							if(isEntrance==1&&isExit==1){
								exitEntrance="进出口共用";
								}
							else if(isEntrance==1&&isExit==2){
								exitEntrance="进口";
									}
							else if(isEntrance==2&&isExit==1){
								exitEntrance="出口";
								}			
							html += '<tr><td class="ta-l">'+dVal.area_name+'</td><td>'+dVal.parking_lots_name+'</td><td>'+gatewayType+'</td>'
			                        +'<td>'+exitEntrance+'</td><td class="ta-r">'+dVal.gateway_name+'</td></tr>';
							});
						$("#table-a-list").html(html);
                      }
         });
	}