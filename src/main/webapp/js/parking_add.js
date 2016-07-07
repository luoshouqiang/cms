// JavaScript Document
$(function(){
  areaData();	//初始化下拉选择框的小区数据
  siderbarControl("lframe")//设置左侧导航条高度
$("#paring-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
		var searchData={areaId:$("#areaName").val(),parkingLotsId:$("#parking-lots-id").val(),
		              parkingCode:$("#parking-code").val(),parkingSpace:$("#parking-space").val(),parkingType:$("#parking-type").val(),
					   parkingFloor:$("#parking-floor").val(),parkingLocation:$("#parking-location").val()};
			 $.ajax({
             type: "post",
             url: "/cms/parking/addAreaParking",
			 data: searchData,
             //data: JSON.stringify(searchData),
			// contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
				 //console.log(data);
				 var dataCode=data.code;
				 if(dataCode==200){
					 alert("数据添加成功！");
					 }
				else{
					alert(data.msg);
					}
                      }
         });
			//form.submit();
		},
		rules: {
				parkingLotsId: {//停车场id
					required:true
				},
				parkingCode: {//车位号
					required:true
				},
				parkingSpace: {//车位尺寸
					required:true
				},
				parkingType: {//车位类型
					required:true
				},
				parkingFloor: {//车位楼层
					required:true
				}
			},
			messages: {
				parkingLotsId: {
					required: "请选择停车场！"
				},
				parkingCode: {
					required: "请输入车位号！"
				},
				parkingSpace: {
					required: "请选择车位尺寸！",
				},
				parkingType: {
					required: "请选择车位类型！",
				},
				parkingFloor: {
					required: "请选择车位楼层！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	
	$("#areaName").change(function(){
		var areaId=$(this).val();
		getParkingLotsId(areaId);//根据小区id调用函数获取停车场id
		});
		
	})

//获取重写了common中的areaData函数
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
						 getParkingLotsId(areaId);//根据小区id调用函数获取停车场id
                      }
         });
		}
	
//获取停车场ID函数
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
						html='<option value="">请选择</option>';
						$.each(data,function(i,plot){
							html+='<option value='+plot.parking_lots_id+'>'+plot.parking_lots_name+'</option>'
							});
						$("#parking-lots-id").html(html);
                      }
         });
	}