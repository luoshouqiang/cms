// JavaScript Document
$(function(){
  areaData();	//初始化下拉选择框的小区数据
  siderbarControl("lframe")//设置左侧导航条高度
$("#paring-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
			var areaId=$("#areaName").val();
			var areaName=$("#areaName").find("option:selected").text();
		var searchData={areaId:areaId,lotsName:$("#parkinglots-name").val(),parent:$("#parking-lots-id").val(),desc:$("#parking-lots-desc").val()};
			 $.ajax({
             type: "post",
             url: "/cms/area/addParkingLots",
			 data: searchData,
             dataType: "json",
             success: function(data){
				 //console.log(data);
				 var dataCode=data.code;
				 if(dataCode==200){
					 alert("数据添加成功！");
					  getParkingLotsList(areaId,areaName);//调用获取停车场列表函数
					  getParkingLotsId(areaId);//调用获取停车场函数
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
				parkinglotsName: {//停车场名称
					required:true
				},
				parkingLotsDesc: {//停车场说明
					required:false
				}
			},
			messages: {
				areaName: {
					required: "请选择小区！"
				},
				parkinglotsName: {
					required: "请输入停车场名称！"
				},
				parkingLotsDesc: {
					required: "请输入停车场说明！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });		
	$("#areaName").change(function(){
		var areaId=$(this).val();
		var areaName=$(this).find("option:selected").text();
		getParkingLotsId(areaId);//根据小区id调用函数获取停车场id
		 getParkingLotsList(areaId,areaName);//调用获取停车场列表函数
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
					   var areaName=data[0].area_name;
				        var html = ''; 
                         $.each(data, function(i, area){
                               html += '<option value='+area.area_id+'>'+area.area_name+'</option>';
                         });
                         $('#areaName').append(html);
						 getParkingLotsId(areaId);//调用获取停车场id列表
						 getParkingLotsList(areaId,areaName);//调用获取停车场列表函数
                      }
         });
		}
		
//获取停车场ID函数  重写common中的函数 添加无的选择
function getParkingLotsId(areaId){
	//console.log(areaId);
			 $.ajax({
             type: "get",
             url: "/cms/parking/getAreaPlots",
             data: {areaId:areaId},		
             dataType: "json",
             success: function(data){
						//console.log(data);
						var html="";
						html='<option value="">请选择</option><option value="0">无</option>';
						$.each(data,function(i,plot){
							html+='<option value='+plot.parking_lots_id+'>'+plot.parking_lots_name+'</option>'
							});
						$("#parking-lots-id").html(html);
                      }
         });
	}
	
//获取小区停车场列表
function getParkingLotsList(areaId,areaName){
	//console.log(areaId);
			 $.ajax({
             type: "get",
             url: "/cms/parking/getAreaPlots",
             data: {areaId:areaId},		
             dataType: "json",
             success: function(data){
						//console.log(data);
						var html="";
					   $.each(data,function(i,dVal){
						   var parkingLotsDesc=dVal.parking_lots_desc;
						   if(parkingLotsDesc==null){
							   parkingLotsDesc="无";
							   }
								html += '<tr><td class="ta-l">'+areaName+'</td><td>'+dVal.parking_lots_name+'</td><td>'+dVal.parent_name+'</td>'
			                        +'<td class="ta-r">'+parkingLotsDesc+'</td></tr>';
							});
						$("#table-a-list").html(html);
						siderbarControl();
                      }
         });
	}
	
