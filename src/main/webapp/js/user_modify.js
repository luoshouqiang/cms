// JavaScript Document
$(function(){
	tab();
	areaData();//获取小区
	carInfo.getCarBrand();//获取车辆品牌
	getUserInfo(getQueryString("userid"),getQueryString("areaid"));//获取小区用户
	
	//车辆所有模拟单选按钮
	$(".checkbox").find("span").click(function(){
		$this=$(this);
		$this.siblings("span").find("i").removeClass("icon-checkbox").addClass("icon-uncheckbox");
		var d=$this.find("i").attr("data");
		$this.parent(".checkbox").attr("data",d);
		$this.find("i").removeClass("icon-uncheckbox");
		$this.find("i").addClass("icon-checkbox");
		});
		
	//修改车牌号
	$(document).on("click",".carno",function(){
		$this=$(this);
		var carplateno_flag=$this.siblings(".carplateno").text();
		
		var userid=$this.attr("userid");
		var areaid=$this.attr("areaid");
		var vehicleid=$this.attr("vehicleid");
		var parkingid=$this.siblings(".carplateno").attr("parkingid");
		var lotsid=$this.attr("lotsid");
		var datajson={userid:userid,areaid:areaid,vehicleid:vehicleid,parkingid:parkingid,lotsid:lotsid}
		$("#changecarno_ok").attr(datajson);
		if(carplateno_flag=="无"){
			alert("没有车位号，无法修改！");
			return;
		}
	
		$.ajax({
             type: "post",
             url: "/cms/areacms/carList",
             data: {userId:userid,areaId:areaid},		
             dataType: "json",
             success: function(data){	
			 //console.log(data);
                        var html = ''; 
                         $.each(data, function(i, carno){
                               html += '<option value='+carno.id+'>'+carno.platNo+'</option>';
                         });
						 if(vehicleid!=0){
							  html+='<option value="0">无</option>'
							 }
                          $("#selectCarno").html(html);	 
					
					  	$(".tk-bg").show();
		               $(".tk_carno").show(); 
                      }
         });
	
		//var carno=$this.find("span em").text();
		//$("#changephoneNum").val(carno);
		});
	$(document).on("click","#changecarno_ok",function(){	
		//$this=$(this);
		//var carNo=$("#changephoneNum").val();
		//var vehicleid=$("#selectCarno").val();
		//var dataVal={areaId:$(".tk-bg").attr("areaId"),vehicleId:vehicleid,parkingId:$(".tk-bg").attr("parkingId")};
		
		$this=$(this);
		var vehicleid=$("#selectCarno").val();
		var userid=$this.attr("userid");
		var areaid=$this.attr("areaid");
		var parkingid=$this.attr("parkingid");
		var lotsid=$this.attr("lotsid");
		var dataVal={areaId:areaid,vehicleId:vehicleid,parkingId:parkingid};
			 $.ajax({
               type: "post",
             url: "/cms/areacms/rebind",
             data: dataVal,		
             dataType: "json",
             success: function(data){	
                   if(data.code==200){
					alert("绑定成功");
					//$(".carno").find("span em").text($("#selectCarno").find("option:selected").text());
					getUserInfo(userid,areaid);//获取小区用户
					$(".tk-bg").hide();
					$(".tk_carno").hide();  
				   }
                   else{
					   alert(data.msg);
					$(".tk-bg").hide();
					$(".tk_carno").hide();  
				   }				   
				
                      }
         });
		});
		$("#quxiao").click(function(){
			$(".tk-bg").hide();
		    $(".tk_carno").hide();  
			});
		
		//修改到期时间
		$(document).on("click",".due-date",function(){	
		//$this=$(this);
		//var carplateno_flag=$this.siblings(".carplateno").text();
		$this=$(this);
		var carplateno_flag=$this.siblings(".carplateno").text();
		
		var userid=$this.siblings(".carno").attr("userid");
		var areaid=$this.siblings(".carno").attr("areaid");
		var vehicleid=$this.siblings(".carno").attr("vehicleid");
		var parkingid=$this.siblings(".carplateno").attr("parkingid");
		
		var datajson={userid:userid,areaid:areaid,vehicleid:vehicleid,parkingid:parkingid}
		//console.log(datajson);
		$("#changedate-ok").attr(datajson);
		
		
		if(carplateno_flag=="无"){
			alert("没有车位号，无法修改！");
			return;
		}
		$(".tk-bg").show();
		$(".tk_date").show(); 
		var date=$this.find("span em").text();
		var carplateno=$this.siblings(".carplateno").text();
		var carparkingid=$this.siblings(".carplateno").attr("data");
		//$(".tk-bg").attr("parkingId",carparkingid);
		$("#plateno_date").val(carplateno);
		$("#changeduedate").val(date);
		});
		
			//选择车辆品牌获得车系
	$(document).on("change","#carBrand",function(){
		var carBrandId=$(this).val();
		carInfo.getCarSeries(carBrandId);
		});
		//选择车系获得车型
	$(document).on("change","#carSeries",function(){
		var carSeriesId=$(this).val();
		carInfo.getCarSeries(carSeriesId);
		});
	
	$("#changedate-ok").click(function(){
		$this=$(this);
		var carNo=$("#changephoneNum").val();
		
		var userid=$this.attr("userid");
		var areaid=$this.attr("areaid");
		var parkingid=$this.attr("parkingid");
		
		var dataVal={areaId:areaid,parkingId:parkingid,dueDate:$(".changedate").val()};
			 $.ajax({
               type: "post",
             url: "/cms/areacms/updateDueTime",
             data: dataVal,		
             dataType: "json",
             success: function(data){
                    if(data==true){
				    alert("修改成功");
					var changedate=$(".changedate").val();
					//console.log(changedate);
					//$(".due-date").find("span em").text(changedate);
					 getUserInfo(userid,areaid);//获取小区用户
					$(".tk-bg").hide();
					$(".tk_date").hide();  
					}
                    else{
						alert("修改失败");
					}					
				
                      }
         });
		});
		$("#quxiao-date").click(function(){
			$(".tk-bg").hide();
		    $(".tk_date").hide();  
			});	
		//修改用户名
		$(document).on("click",".username",function(){
			$this=$(this);
			var userid=$this.attr("userid");
			var areaid=$this.attr("areaid");
			var username=$this.find("span").text();
			
			var datajson={areaid:areaid,userid:userid,username:username}
			$("#change-username-ok").attr(datajson);
			$("#change-username").val(username);
			$(".tk-bg").show();
		    $(".tk_username").show(); 
			});
			
		$("#change-username-ok").click(function(){
			$this=$(this);
			var userid=$this.attr("userid");
			var areaid=$this.attr("areaid");
			var username=$("#change-username").val();
		var dataVal={userId:userid,userName:username};
			 $.ajax({
             type: "post",
             url: "/cms/user/updateBaseUserName",
             data: dataVal,		
             dataType: "json",
             success: function(data){
                  if(data.code==200){
					  alert("修改成功");
					  getUserInfo(userid,areaid);//获取小区用户
					  $(".tk-bg").hide();
		              $(".tk_username").hide(); 
					  }
				  else{
					  alert(data.msg);
					  }
				
			 }
			 });
			});
		$("#quxiao-username").click(function(){
			$(".tk-bg").hide();
		    $(".tk_username").hide();  
			});	
		
		//修改房号
		$(document).on("click",".roomNo",function(){
			$this=$(this);
			var areaId=$this.siblings(".username").attr("areaid");
			var userId=$this.siblings(".username").attr("userid");
			var roomNo=$this.find("span").text();
			
			var datajson={areaId:areaId,roomNo:roomNo,userId:userId}
			$("#change-roomNo-ok").attr(datajson);
			$("#change-roomNo").val(roomNo);
			$(".tk-bg").show();
		    $(".tk-roomNo").show(); 
			});
			
		$("#change-roomNo-form").validate({
			   submitHandler: function(form) {
			var areaId=$("#change-roomNo-ok").attr("areaId");
			var userId=$("#change-roomNo-ok").attr("userId");
			var roomNo=$("#change-roomNo").val();
		var dataVal={areaId:areaId,roomNum:roomNo,userId:userId};
			 $.ajax({
             type: "post",
             url: "/cms/user/updateRoomNum",
             data: dataVal,		
             dataType: "json",
             success: function(data){
                  if(data.code==200){
					  alert("修改成功");
					  getUserInfo(userId,areaid);//获取小区用户
					  $(".tk-bg").hide();
		              $(".tk-roomNo").hide(); 
					  }
				  else{
					  alert(data.msg);
					  }
				
			 }
			 });
				},
				rules: {
						changeRoomNo: {//房号
							required:true,
							isRoomNo:true
						}
					},
					messages: {
						changeRoomNo: {
							required: "请输入房号！"
						}
					},
					 //设置错误信息存放标签
						errorElement: "strong"
						});		
			
			$("#quxiao-roomNo").click(function(){
			$(".tk-bg").hide();
		    $(".tk-roomNo").hide();  
			});	
			
			
		/*$("#change-roomNo-ok").click(function(){
			$this=$(this);
			var areaId=$this.attr("areaId");
			var userId=$this.attr("userId");
			var roomNo=$("#change-roomNo").val();
		var dataVal={areaId:areaId,roomNum:roomNo,userId:userId};
			 $.ajax({
             type: "post",
             url: "/cms/user/updateRoomNum",
             data: dataVal,		
             dataType: "json",
             success: function(data){
                  if(data.code==200){
					  alert("修改成功");
					  new getUserInfo({userid:getQueryString("userid"),areaid:getQueryString("areaid")});//获取小区用户
					  $(".tk-bg").hide();
		              $(".tk-roomNo").hide(); 
					  }
				  else{
					  alert(data.msg);
					  }
				
			 }
			 });
			});*/
			
		//修改车位类型
		$(document).on("click",".parkingspace",function(){
			$this=$(this);
			var areaId=$this.siblings(".username").attr("areaid");
			var userId=$this.siblings(".username").attr("userid");
			var parkingid=$this.siblings(".carplateno").attr("parkingid");
			var datajson={parkingId :parkingid,areaId:areaId,userId:userId}
			$("#change-space-ok").attr(datajson);
			if(parkingid==""){
				alert("没有车位不能修改！");
				return;
				}
			$(".tk-bg").show();
		    $(".tk-parkingspace").show(); 
			});
			

		 //上传行驶证正本
    $(document).on("change","#registrationzb",function(event) {
    	var photoFile=$(this).val();
    	//$("#addcarbtn").attr("disabled","disabled");
    	var userid=$("#addcarbtn").attr("userid");
    	imgUpload(userid);
    });
    //上传行驶证副本
    $(document).on("change","#registrationfb",function(event) {
    	var photoFile=$(this).val();
    	//$("#addcarbtn").attr("disabled","disabled");
    	var userid=$("#addcarbtn").attr("userid");
    	imgUpload(userid);
    });
    //上传行驶证副本反面
    $(document).on("change","#registrationfbfm",function(event) {
    	var photoFile=$(this).val();
    	//$("#addcarbtn").attr("disabled","disabled");
    	var userid=$("#addcarbtn").attr("userid");
    	imgUpload(userid);
    });


		$("#change-space-ok").click(function(){
			$this=$(this);
			var areaId=$this.attr("areaId");
			var userId=$this.attr("userId");
			var parkingId=$this.attr("parkingId");
			var space=$("#select-parkingspace").val();
		var dataVal={parkingId:parkingId,space:space};
			 $.ajax({
             type: "post",
             url: "/cms/parking/updatePakringType",
             data: dataVal,		
             dataType: "json",
             success: function(data){
                  if(data.code==200){
					  alert("修改成功");
					  getUserInfo(userid,areaid);//获取小区用户
					  $(".tk-bg").hide();
		              $(".tk-parkingspace").hide(); 
					  }
				  else{
					  alert(data.msg);
					  }
				
			 }
			 });
			});
		$("#quxiao-parkingspace").click(function(){
			$(".tk-bg").hide();
		    $(".tk-parkingspace").hide(); 
			});
	//选择车辆品牌获得车系
/*	$(document).on("change","#carBrand",function(){
		var carBrandId=$(this).val();
		carInfo.getCarSeries(carBrandId);
		});
		//选择车系获得车型
	$(document).on("change","#carSeries",function(){
		var carSeriesId=$(this).val();
		carInfo.getCarSeries(carSeriesId);
		});*/
	

	//增加车辆
	$("#addcar").click(function(){
		$(".tk-bg").show();
		$(".tk-addcar").show();
		$(".tk-addcar").attr("flag","1");//解决隐藏时点击enter键不通过验证就提交的bug
		});
    $("#tk-addcar-close").click(function() {
    	$(".tk-bg").hide();
		$(".tk-addcar").hide();
		$(".tk-addcar").attr("flag","0");//解决隐藏时点击enter键不通过验证就提交的bug
    });

		/*$("#add-car-form").validate({
			submitHandler: function(form) {
				var flag=$(".tk-addcar").attr("flag");
				console.log(flag);
				if(flag=="0"){
					return;
				}
				else{
					addcar();//调用增加车辆函数
				}	
		},
		rules: {
				platNo: {//车牌号
				required:true,
				pNumber:true
				},
				registration:{
					required:true
				}
			},
			messages: {
				platNo: {
					required: "车牌号不能为空！"
				},
				registration:"行驶证不能为空！"
			},
			 //设置错误信息存放标签
				errorElement: "strong"
		
       
    });*/

    //增加车辆
    $("#add-car-form").validate({
       submitHandler: function(form) {
				addCar();
		},
		rules: {
				platNo: {//车牌号
				required:true,
				pNumber:true
				},
				vehicleType:{
					//required:true
				},
				owner:{
					//required:true,
					maxlength:10
				},
				address:{
					//required:true,
					maxlength:50
				},
				useCharacter:{
					//required:true
				},
				model:{
					//required:true,
					maxlength:20
				},
				vin:{
					//required:true,
					maxlength:25
				},
				engineNo:{
					//required:true,
					maxlength:15
				},
				registerDate:{
					//required:true
				},
				issueDate:{
					//required:true
				},
				disagree:{
				//required:true,
				maxlength:200
				}
			},
			messages: {
				platNo: {
				required: "车牌号不能为空！"
				},
				vehicleType:{
					required:"车辆类型不能为空！"
				},
				owner:{
					required:"所有人不能为空！",
					maxlength:"最大长度为10"
				},
				address:{
					required:"地址不能为空！",
					maxlength:"最大长度为50"
				},
				useCharacter:{
					required:"使用性质不能为空！"
				},
				model:{
					required:"品牌型号不能为空！",
					maxlength:"最大长度为20"
				},
				vin:{
					required:"车辆代号不能为空！",
					maxlength:"最大长度为25"
				},
				engineNo:{
					required:"发动机号不能为空！",
					maxlength:"最大长度为15"
				},
				registerDate:{
					required:"注册日期不能为空！"
				},
				issueDate:{
					required:"发证日期不能为空！"
				},
				disagree:{
				required:"未通过原因不能为空！",
				maxlength:"最大长度为200"
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });

    //取得判断正副本的标记
    $("#tab-title").find("ul li").click(function(event) {
    	$this=$(this);
    	var type=$this.attr("datatype");
    	$("#tab-title").attr("datatype",type);
    });

     //查看车辆信息
    $(document).on('click', '.see-car-info', function() {
    	$this=$(this);
    	var vehicleid=$this.parent("td").siblings('.carno').attr("vehicleid");
    	seeCarInfo(vehicleid);
    });
    $("#tk-carinfo-ok").click(function(event) {
    	$('.tk-bg').hide();
    	$('.tk-car-info').hide();
    });		

		//用户有多个小区时 切换小区
		$(document).on("change","#areaName",function(){
			$this=$(this);
			var areaid=$this.val();
			//console.log(areaid);
			getUserInfo(getQueryString("userid"),areaid);//获取小区用户
			});

		//分页
	var pageIndex=1;
	$(document).on("click",".page .pageone",function(){
		    var pageSize=20;
			$this=$(this);
			var rePageIndex=page($this);
			
			 if(!rePageIndex){
			 	return;
			 }
			 else{

			 	getUserInfo(getQueryString("userid"),$("#areaName").val(),rePageIndex,pageSize);
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


function seeCarInfo(vehicleid){
	 $.ajax({
               type: "get",
             url: "/cms/vehicle/getDetail",
              data: {id:vehicleid},	
             dataType: "json",
             success: function(data){
				 if(data.code==200){
				 	var data=data.data;
				 	var xszPhoto=data.xsz;
				 	var xsz='';
				 	var manualNodel=data.manual_model;
				 	if(xszPhoto!=''){
				 		xsz='<img src="'+xszPhoto+'">'
				 	}
				 	else{
				 		xsz="无";
				 	}
				 	if(manualNodel==null){
				 		manualNodel="无";
				 	}

					var html='<div class="row">'+
                    '<p class="col-6"><strong class="col-5">车牌号：</strong><span>'+data.plat_no+'</span></p>'+
                    '<p class="col-6"><strong class="col-5">车辆品牌：</strong><span>'+data.brand_name+'</span></p>'+
                '</div>'+
                '<div class="row">'+
                    '<p class="col-6"><strong class="col-5">车系：</strong><span>'+data.series_name+'</span></p>'+
                    '<p class="col-6"><strong class="col-5">车型：</strong><span>'+data.model_name+'</span></p>'+
                '</div>'+
                '<div class="row">'+
                    '<p class="col-6"><strong class="col-5">排量：</strong><span>'+data.swept_volume+'</span></p>'+
                '</div>'+
                /*'<div class="row">'+
                    '<p class="col-10 row-xsz"><strong class="col-3" style="">行驶证：</strong><span>'+xsz+'</span></p>'+
                '</div>'+*/
                  '<div class="row row-remark">'+
                    '<p class="col-10"><strong class="col-3">备注：</strong>'+
                    '<span class="col-9">'+manualNodel+'</span>'+
                    '</p>'+
                '</div>'+
                '<div class="main-bg"></div>';
                 $("#tk-carinfo-main").html(html);
			   $('.tk-bg').show();
    	       $('.tk-car-info').show();
					 }	
				 else{
					 alert(data.msg);
					 }		
					
                      }
         });
	


}
	
//获取小区
function areaData(){
	//console.log(2);
	var userid= getQueryString("userid");//获取url参数值
		//var Data={areaInfo:$("#areaName").val()};
			 $.ajax({
               type: "post",
             url: "/cms/areacms/getAreasByUserId",
             data: {userId:userid},		
             dataType: "json",
             success: function(data){
                         var html = ''; 
                         $.each(data, function(i, area){
                               html += '<option value='+area.area_id+'>'+area.area_name+'</option>';
                         });
                         $('#areaName').append(html);
                      }
         });
		}

/*function imgUpload(userId) {
 	//$(".input-file-box").append('<strong>正在上传...</strong>');
 	 $("#xsz-img").attr("src", "../images/xsz_2.jpg");
 	 //$("#registration").attr("disabled","disabled");
 	console.log(userId);
        $.ajaxFileUpload
        (
            {
                url: '/cms/user/upload', //用于文件上传的服务器端请求地址
                type: 'post',
                data:{userId:userId},
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'registration', //文件上传域的ID
                dataType: 'text', //返回值类型 一般设置为json
                success: function (data,status)  //服务器成功响应处理函数
                {

                	var  string=$(data).text();
                	console.log(string);
					var data=eval("("+string+")");
                	if(data.code==200){
		            		 $("#xsz-img").attr("src", data.data);
		            		 $(".input-file-box").find("strong").remove();
		            		 alert("上传成功！");
		            		 $("#addcarbtn").removeAttr('disabled');  
		            		 //$("#registration").removeAttr('disabled');
                		}else{
                			$("#xsz-img").attr("src", "../images/xsz_1.jpg");
                			 $(".input-file-box").find("strong").remove();
                			 // $("#registration").removeAttr('disabled');
                			 alert("上传失败！");
                		}
                }
            }
        )
        return false;
        }*/
 function imgUpload(userId) {
 	//$(".input-file-box").append('<strong>正在上传...</strong>');
 	var type=$("#tab-title").attr("datatype");
 	console.log(type);
 	var imgid="",registration="";
 	if(type=="VL_ORI"){
 		imgid="#xsz-zb-img";
 		registration="registrationzb";
 		$("#addcarbtn").attr("imgtype","VL_ORI");
 	}
 	else if(type=="VL_CPY_A"){
 		imgid="#xsz-fb-img";
 		registration="registrationfb";
 	}
 	else{
 		imgid="#xsz-fbfm-img";
 		registration="registrationfbfm";
 	}
 	console.log(registration);
 	$(imgid).attr("src", "../images/xsz_2.jpg");
        $.ajaxFileUpload
        (
            {
                url: '/cms/user/upload', //用于文件上传的服务器端请求地址
                type: 'post',
                data:{userId:userId,type:type},
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: registration, //文件上传域的ID
                dataType: 'text', //返回值类型 一般设置为json
                success: function (data, status)  //服务器成功响应处理函数
                {
                	var  string=$(data).text();
                	console.log(string);
					var data=eval("("+string+")");
                	if(data.code==200){
		            		 $(imgid).attr("src", data.data.url);
		            		 var picid=$("#addcarbtn").attr("picid");
		            		 if(picid==undefined){
		            		 	 $("#addcarbtn").attr("picid",data.data.id);
		            		 }
		            		 else{
		            		 	 $("#addcarbtn").attr("picid", $("#addcarbtn").attr("picid")+","+data.data.id);
		            		 }
		            		

		            		 $(".input-file-box").find("strong").remove();
		            		 alert("上传成功！");
		            		// $("#addcarbtn").removeAttr('disabled');  
                		}else{
                			 $(".input-file-box").find("strong").remove();
                			 $(imgid).attr("src", "../images/xsz_1.jpg");
                			 alert("上传失败！");
                		}
                }
            }
        )
        return false;
        }

//获取用户信息
function getUserInfo(userId,areaId,pageIndex,pageSize){
	if(pageIndex==null){
		pageIndex=1;
	}
	if (pageSize==null) {
		pageSize=20;
	}
	  $('#table-a-list').html("");
	var areaData={userId:userId,areaId:areaId,pageIndex:pageIndex,pageSize:pageSize};
			 $.ajax({
             type: "post",
             url: "/cms/areacms/list",
             data: JSON.stringify(areaData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
			//console.log(data);
			
			            $("#addcarbtn").attr("userid",data.datas[0].userId);
					    $("#addcarbtn").attr("areaid",data.datas[0].areaId);
                        $("#userName").val(data.datas[0].userName);
						$("#phoneNum").val(data.datas[0].phoneNum);
						//$(".tk-bg").attr("areaId",data[0].areaId);
						//$(".tk-bg").attr("vehicleId",data[0].vehicleId);
						//$(".tk-bg").attr("parkingId",data[0].parkingId);
						var html='';
						$.each(data.datas,function(i,dVal){
							 var plotsName=dVal.plotsName;
							 if(plotsName==null){
								 plotsName="无";
								 }
					 html+='<tr><td class="username ta-l" userid='+dVal.userId+' areaid='+dVal.areaId+'><span><em>'+dVal.userName+'</em><i class="icon-carno-change"></i></td>'
							   +'<td>'+dVal.phoneNum+'</td><td class="roomNo"><span><em>'+dVal.roomNo+'</em><i class="icon-carno-change"></i></td><td>'+plotsName+'</td>'
							   +'<td class="carplateno" parkingid="'+dVal.parkingId+'">'+dVal.parkingNo+'</td>'
			  +'<td class="parkingspace"><span><em>'+dVal.parkingSpace+'</em><i class="icon-carno-change"></i></td><td>'+dVal.parkingType+'</td>'
			  +'<td class="carno" userid="'+dVal.userId+'" vehicleid="'+dVal.vehicleId+'" areaid='+dVal.areaId+' lotsid='+dVal.lotsId+'>'
			  +'<span><em>'+dVal.platNo+'</em><i class="icon-carno-change"></i></span></td>'
              +'<td class="due-date"><span><em>'+dVal.dueDate+'</em><i class="icon-carno-change"></i></td><td class="ta-r">无</td>'
                +'<td class="change operation-btn"><a href="javascript:void(0);" class="see-car-info">查看</a></td></tr>';	
						});
            
					  $('#table-a-list').html(html);
					    pageList(data);//调用设置分页按钮函数 
					    siderbarControl("lframe")//设置左侧导航条高度
                      }
         });
	
}

/*function addcar(){
        var userid=$("#addcarbtn").attr("userid");
		var areaid=$("#addcarbtn").attr("areaid");
		var platNo=$("#platNo").val();
		var brandId=$("#carBrand").val();
		var seriesId=$("#carSeries").val();
	    var modelId=$("#carModels").val();
		var otherInfo=$("#otherInfo").val();
		
		var carown=$(".checkbox").attr("data");
		var swepa=$(".num-number-a").val();
		var swepb=$(".num-number-b").val();
		var sweptvolume=swepa+"."+swepb;
			 $.ajax({
               type: "post",
             url: "/cms/areacms/addVehicle",
             data: {userId:userid,platNo:platNo,sweptVolume:sweptvolume,brandId:brandId,seriesId:seriesId,modelId:modelId,manualModel:otherInfo},		
             dataType: "json",
             success: function(data){ 	
				    getUserInfo(userid,areaid);//获取小区用户
				    $(".tk-bg").hide();
		            $(".tk-addcar").hide();
		            $(".tk-addcar").attr("flag","0");//解决隐藏时点击enter键不通过验证就提交的bug
					if(data.code==200){
							alert("添加成功");  

						}
					else{
						alert(data.msg);
						}	
				
                      }
         });
		 
		

}*/


function addCar(){
	var imgtype=$("#addcarbtn").attr("imgtype");
	/*console.log(imgtype);
	if(imgtype!="VL_ORI"){
		alert("请上传行驶证正本！");
		return;
	}*///取消必传行驶证正本
	var picId=$("#addcarbtn").attr("picid");
	var userId=$("#addcarbtn").attr("userid");
	var platNo=$("#platNo").val();
	var vehicleType=$("#vehicleType").val();
	var owner=$("#owner").val();
	var address=$("#address").val();
	var useCharacter=$("#useCharacter").val();
	var model=$("#model").val();
	var vin=$("#vin").val();
	var engineNo=$("#engineNo").val();
	var registerDate=$("#registerDate").val();
	var issueDate=$("#issueDate").val();
	var disagree=$("#disagree").val();//不同意时填写的信息
	var brandId=$("#carBrand").val();
	var seriesId=$("#carSeries").val();
	var modelId=$("#carModels").val();
    
	 var dataJson={ids:picId,userId:userId,status:"NEW",platNo:platNo,vehicleType:vehicleType,owner:owner,address:address,useCharacter:useCharacter,
	 	model:model,vin:vin,engineNo:engineNo,registerDate:registerDate,issueDate:issueDate,manualModel:disagree,brandId:brandId,seriesId:seriesId,modelId:modelId};
	         console.log(dataJson);

	$.ajax({
         type: "post",
         url: "/cms/user/addVehicle",
         data: dataJson,
         dataType: "json",
         success: function(data){
			 if(data.code==200){
			 	getUserInfo(getQueryString("userid"),$("#areaName").val());
			 	alert("添加成功！");
			 	$("#addcarbtn").removeAttr('picid').removeAttr('imgtype');
			 	$(".xsz-img").attr("src","../images/xsz_1.jpg");
			 	$("#add-car-form")[0].reset();
		 		$(".tk-addcar").hide();
				$(".tk-bg").hide();

			 }
			 else{
			 	$("#addcarbtn").removeAttr('picid').removeAttr('imgtype');
			 	$(".xsz-img").attr("src","../images/xsz_1.jpg");
			 	alert(data.msg);
			 }
			 }
     });
	 
}
	


 
	
