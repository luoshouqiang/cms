// JavaScript Document
$(function(){
  areaData();	//初始化下拉选择框的小区数据
  siderbarControl("lframe")//设置左侧导航条高度
$("#virtualparking-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
		var areaId=$("#areaName").val();
		var areaName=$("#areaName").find("option:selected").text();
		var lotsId=$("#parking-lots-id").val();
		var parkingFloor=$("#parking-floor").val();
		var searchData={areaId:areaId,parkingLotsId:lotsId,parkingFloor:parkingFloor,parkingMax:$("#parking-number").val(),type:"2"};
			 $.ajax({
             type: "post",
             url: "/cms/area/AddVirtualParking",
			 data: searchData,
             dataType: "json",
             success: function(data){
				 //console.log(data);
				 var dataCode=data.code;
				 if(dataCode==200){
					 alert("数据添加成功！");
					 getParking(areaId,lotsId,parkingFloor,"v_cbd",areaName);//获取标准车位
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
				parkingFloor: {//停车场楼层
					required:true
				},
				parkingNumber: {//停车场数量
				required:true,
				intNo:true
				}
			},
			messages: {
				areaName: {
					required: "请选择小区！"
				},
				parkingLotsId: {
					required: "请选择停车场！"
				},
				parkingFloor: {
					required: "请选择楼层！",
				},
				parkingNumber: {
					required: "请输入停车场的车位数量！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });		
	//选择小区时调用获取停车场、获取车位函数
	$("#areaName").change(function(){
		 var areaId=$(this).val();
		 var areaName=$(this).find("option:selected").text();
		 var lotsId=$("#parking-lots-id").val();
		 var parkingFloor=$("#parking-floor").val();
		 
		getParkingLotsId(areaId);//根据小区id调用函数获取停车场id
		getParking(areaId,lotsId,parkingFloor,"v_cbd",areaName);//获取标准车位
		});
	//选择停车场时调用获取车位函数
	$("#parking-lots-id").change(function(){
		 var areaId=$("#areaName").val();
		  var areaName=$("#areaName").find("option:selected").text();
		 var lotsId=$(this).val();
		 var parkingFloor=$("#parking-floor").val();
		 getParking(areaId,lotsId,parkingFloor,"v_cbd",areaName);//获取标准车位
		});
		
	//选择停车场时调用获取车位函数
	$("#parking-floor").change(function(){
		 var areaId=$("#areaName").val();
		  var areaName=$("#areaName").find("option:selected").text();
		 var lotsId=$("#parking-lots-id").val();
		 var parkingFloor=$(this).val();
		 getParking(areaId,lotsId,parkingFloor,"v_cbd",areaName);//获取标准车位
		});

	//分页
	var pageIndex=1;
	$(document).on("click",".page .pageone",function(){
		    var pageSize=10;
			$this=$(this);
			var rePageIndex=page($this);
			console.log(rePageIndex);
			 var areaId=$("#areaName").val();
			  var areaName=$("#areaName").find("option:selected").text();
			 var lotsId=$("#parking-lots-id").val();
			 var parkingFloor=$("#parking-floor").val();
			 if(!rePageIndex){
			 	return;
			 }
			 else{
			 	getParking(areaId,lotsId,parkingFloor,"v_cbd",areaName,rePageIndex,pageSize);
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
					   var lotsId=$("#parking-lots-id").val();
					   var parkingFloor=$("#parking-floor").val();
				        var html = ''; 
                         $.each(data, function(i, area){
                               html += '<option value='+area.area_id+'>'+area.area_name+'</option>';
                         });
                         $('#areaName').append(html);
							getParkingLotsId(areaId);//根据小区id调用函数获取停车场id
							
							getParking(areaId,lotsId,parkingFloor,"standard",areaName);//获取标准车位
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