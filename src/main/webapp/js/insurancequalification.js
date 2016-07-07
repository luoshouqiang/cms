// JavaScript Document
$(function(){
	siderbarControl("lframe");
	carInfo.getCarBrand();//获取车辆品牌
	 $("#auditDrivingLicForm").validate({
       submitHandler: function(form) {
       	//var auditobj=$("#auditobj").val();
       	//$("#auditSearch").attr("data",auditobj);//去出分类的值暂存在搜索按钮的data
       //	console.log(auditobj);
   //if(auditobj=="1"){
       		//getAuditPhoneDrivingLicInfo();//手机端上传 调取待审核列表函数
    //   }
    //   	else{
       		getAuditPcDrivingLicInfo();//pc端上传 调取待审核列表函数
      // 	}
		},
		rules: {
				userName: {//用户名字
					//minlength: 2
				},
				phoneNum: {//电话号码
					//isMobile: true
				}
			
			},
			messages: {
				userName: {
					required: "请输入姓名！",
					minlength: "姓名最少为两个字！"
				},
				phoneNum: {
					required: "请输入手机号码！"
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	
	 //分页
	var pageIndex=1;
	$(document).on("click",".page .pageone",function(){
		    var pageSize=10;
			$this=$(this);
			var rePageIndex=page($this);
		
			 if(!rePageIndex){
			 	return;
			 }
			 else{
			 	var judgePcPhone=$("#auditSearch").attr("data");
			 	if(judgePcPhone=="1"){
			 		getAuditPhoneDrivingLicInfo(pageIndex,pageSize);
			 	}
			 	else{
			 		getAuditPcDrivingLicInfo(pageIndex,pageSize);
			 	}
				
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

$(document).on("click",".audit",function(){
	$thisTd=$(this).parent("td");
	var userId=$thisTd.siblings('.userName').attr("userid");
	var picId=$thisTd.siblings('.xsz-img').attr("picid");
	var picUrl=$thisTd.siblings('.xsz-img').attr("picurl");
	var picUrlArr=$thisTd.siblings('.xsz-img').attr("picurlarr");
	var vehicleId=$thisTd.siblings('.xsz-img').attr("vehicleid");
	
	var dataJson={userid:userId,picid:picId,picurl:picUrl,vehicleid:vehicleId};
	console.log(dataJson);

	//var judgePcPhone=$("#auditSearch").attr("data");//获取判断是手机端还是pc端的标记
	var judgePcPhone="2";//获取判断是手机端还是pc端的标记
	if(judgePcPhone=="2"){//物业端
		picUrlArr=picUrlArr.split(",");
		var picUrlLen=picUrlArr.length;
		console.log(picUrlLen);
		var html="";
		for(var i=0;i<picUrlLen;i++){
			console.log(picUrlArr[i]);
			html+='<li>'+
					 '<div class="photo photo-checkbox fl col-6">'+
		               '<div class="pic-box"><a href='+picUrlArr[i]+'><img src='+picUrlArr[i]+' rel='+picUrlArr[i]+' class="pcaudit-xszimg"></div>'+
		            '</div>'+
				  '</li>';
		}
		$("#slideBoxPc").find(".bd ul").html(html);
		//$("#slideBoxPc").slide({mainCell:".bd ul",autoPlay:false});//调用滑动js
		var vehicleId=$thisTd.siblings('.xsz-img').attr("vehicleId");
		$("#pcauditbtn").attr(dataJson);
		getAuditPcInfo(vehicleId);//根据车辆id查询行驶证审核信息
	}
	else{//手机端
	picUrlArr=picUrlArr.split(",");
	var picUrlLen=picUrlArr.length;
	console.log(picUrlLen);
	var html="";
	for(var i=0;i<picUrlLen;i++){
		console.log(picUrlArr[i]);
		/*html+='<li>'+
				'<div class="photo photo-checkbox fl col-6">'+
					'<div class="checkinput-box checkinput-box-sfsh">'+
	              
	                 '<form>'+
	                 '<span class="check-input"><input type="checkbox" name="sfsh" value="" class="sfsh"/><em>审核</em></span>'+
	                 '</form>'+
	               '</div>'+
	                '<div class="clear"></div>'+
	               '<div class="pic-box"><img src='+picUrlArr[i]+' class="audit-xszimg"></div>'+
	               '<div class="clear"></div>'+
	               '<div class="checkinput-box checkinput-box-xsz">'+
	               '<span class="check-title">选正副本：</span>'+
	                 '<form>'+
	                 '<span class="check-input"><input type="radio" name="xszsh" value="VL_ORI" checked="checked" /><em>正本</em></span>'+
	                 '<span class="check-input"><input type="radio" name="xszsh" value="VL_CPY_A"/><em>副本</em></span>'+
	                 '<span class="check-input"><input type="radio" name="xszsh" value="VL_CPY_B"/><em>副本反面</em></span>'+
	                 '</form>'+
	               '</div>'+
	                '<div class="checkinput-box checkinput-box-wt">'+
	               '<span class="check-title">是否通过：</span>'+
	                 '<form>'+
	                 '<span class="check-input"><input type="radio" name="xszwt" value="PASS" checked="checked"/><em>通过</em></span>'+
	                 '<span class="check-input"><input type="radio" name="xszwt" value="NOPASS"/><em>不通过</em></span>'+
	                 '</form>'+
	               '</div>'+
	            '</div>'+
			'</li>';*/
				html+='<li>'+
					 '<div class="photo photo-checkbox fl col-6">'+
		               '<div class="pic-box"><a href='+picUrlArr[i]+'><img src='+picUrlArr[i]+' rel='+picUrlArr[i]+' class="pcaudit-xszimg"></div>'+
		            '</div>'+
				  '</li>';
	}
	$("#slideBoxPhone").find(".bd ul").html(html);
	$("#slideBoxPhone").slide({mainCell:".bd ul",autoPlay:false});//调用滑动js
	$(".agree").attr("disabled","disabled");
	$(".disagree").attr("disabled","disabled");
	$("#auditbtn").attr(dataJson);
	$(".tk-audPhoneDrivinglic").show();
	$(".tk-bg").show();
	 $(".audit-xszimg").imagezoom();
	}
	
});
$(document).on("click","#tk-audPhoneDrivinglic-close",function(){
	$(".tk-audPhoneDrivinglic").hide();
	$(".tk-bg").hide();
});

$(document).on("click","#tk-audPcDrivinglic-close",function(){
	$(".tk-audPcDrivinglic").hide();
	$(".tk-bg").hide();
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
//手机端
$(".auditchoice").click(function(event) {
	$this=$(this);
	$this.removeClass('icon-uncheckbox').addClass('icon-checkbox');
	$this.parent("span").siblings('span').find("i").removeClass('icon-checkbox').addClass('icon-uncheckbox');
	var thisData=$this.attr("data");
    	if(thisData=="1"){
		$(".disagree").attr("disabled","disabled");
		//$(".agree").removeAttr('disabled');
		$("#auditbtn").attr("status","PASS");
		}
		else{
			//$(".agree").attr("disabled","disabled");
			$(".disagree").removeAttr('disabled')
			$("#auditbtn").attr("status","NOPASS");
		}
});
//物业端
$(".pcauditchoice").click(function(event) {
	$this=$(this);
	$this.removeClass('icon-uncheckbox').addClass('icon-checkbox');
	$this.parent("span").siblings('span').find("i").removeClass('icon-checkbox').addClass('icon-uncheckbox');
	var thisData=$this.attr("data");
    	if(thisData=="1"){
		$(".pcdisagree").attr("disabled","disabled");
		$("#pcauditbtn").attr("status","PASS");
		}
		else{
			$(".pcdisagree").removeAttr('disabled')
			$("#pcauditbtn").attr("status","NOPASS");
		}
});
//手机端审核
$("#auditForm").validate({
       submitHandler: function(form) {
       	        console.log(11);
				auditPhoneDrivingLic();
		},
		rules: {
				platNo: {//车牌号
				required:true,
				pNumber:true
				},
				vehicleType:{
					required:true
				},
				owner:{
					required:true,
					maxlength:10
				},
				address:{
					required:true,
					maxlength:50
				},
				useCharacter:{
					required:true
				},
				model:{
					required:true,
					maxlength:20
				},
				vin:{
					required:true,
					maxlength:25
				},
				engineNo:{
					required:true,
					maxlength:15
				},
				registerDate:{
					required:true
				},
				issueDate:{
					required:true
				},
				disagree:{
				required:true,
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


//物业端审核
$("#pcauditForm").validate({
       submitHandler: function(form) {
       	        console.log(11);
				auditPcDrivingLic();
		},
		rules: {
				platNo: {//车牌号
				required:true,
				pNumber:true
				},
				vehicleType:{
					required:true
				},
				owner:{
					required:true,
					maxlength:10
				},
				address:{
					required:true,
					maxlength:50
				},
				useCharacter:{
					required:true
				},
				model:{
					required:true,
					maxlength:20
				},
				vin:{
					required:true,
					maxlength:25
				},
				engineNo:{
					required:true,
					maxlength:15
				},
				registerDate:{
					required:true
				},
				issueDate:{
					required:true
				},
				disagree:{
				required:true,
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
  
	})




function getAuditPhoneDrivingLicInfo(pageIndex,pageSize){//获取用户信息
    var userName=$("#userName").val();
	var phoneNo=$("#phoneNum").val();
	var status=$("#status").val();
	if(pageIndex==null){
		pageIndex=1;
	}
	if (pageSize==null) {
		pageSize=10;
	}
	var searchData={userName:userName,status:status,phone:phoneNo,pageIndex:pageIndex,pageSize:pageSize};
	
	 $.ajax({
             type: "get",
             url: "/cms/vehicle/getUserPics",
             data: searchData,
             dataType: "json",
             success: function(data){
				  $('#table-a-list').html(""); 
				   if(data.code==200){
						if(data.data.total==0){
						$(".page").remove();
					  	alert("没有符合条件的数据！");
					  	return;
					  }
					}
					if(data.code==200){
							var html = ''; 
                         $.each(data.data.datas, function(i, comment){
							 var opr="",trClass="",status="";
							var picurls=comment.picurls;
							var len=picurls.length-1;
							//console.log(picurls);
							var picurl=picurls.substring(1,len).split(",");
							//console.log(picurl);
							var strStatus=comment.picstatus;
							var picstatus=strStatus.split(",");
							status=picstatus[0];
							 if(i%2!=0){
								 trClass="tr-flag";
								 }
 							    if(status=="NEW"){
 									 opr='<a href="javascript:void(0);" class="audit">审核</a>';
 									 status="等待审核";
 								}
 								else if(status=="PASS"){
 									opr='已处理';
 								    status="审核通过";
 								}
 								else{
 									opr='已处理';
 								    status="审核未通过";
 								}
                 	html += '<tr class='+trClass+'><td class="userName ta-l" userid='+comment.user_id+'>'+comment.user_name+'</td>'
                 	+'<td>'+comment.mobile+'</td>'
                 	+'<td class="xsz-img" picurl='+picurl[0]+' picurlarr=\''+picurl+'\'>'
                 	+'<a href=""><img src='+picurl[0]+' class="img-small"/>'
                 	+'<img src='+picurl[0]+' class="img-big"/></a></td>'
                 	+'<td>'+status+'</td>'
               +'<td class="change operation-btn ta-r" >'+opr+'</td></tr>';
                         });
                         $('#table-a-list').html(html);
                         pageList(data.data);//调用设置分页按钮函数
						 siderbarControl("lframe");//设置左侧导航条高度
							 }else{
							 	alert(data.msg);
							 }
                      }
         });
	}
//pc端
function getAuditPcDrivingLicInfo(pageIndex,pageSize){//获取用户信息
    var userName=$("#userName").val();
	var phoneNo=$("#phoneNum").val();
	var status=$("#status").val();
	if(pageIndex==null){
		pageIndex=1;
	}
	if (pageSize==null) {
		pageSize=10;
	}
	var searchData={userName:userName,status:status,phone:phoneNo,type:"2",pageIndex:pageIndex,pageSize:pageSize};
	
	 $.ajax({
             type: "get",
             url: "/cms/vehicle/getVehiclePics",
             data: searchData,
             dataType: "json",
             success: function(data){
				  $('#table-a-list').html(""); 
				   if(data.code==200){
						if(data.data.total==0){
						$(".page").remove();
					  	alert("没有符合条件的数据！");
					  	return;
					  }
					}
					if(data.code==200){
							var html = ''; 
                         $.each(data.data.datas, function(i, comment){
							 var opr="",trClass="",status="";
							var picurls=comment.picurls;
							var len=picurls.length-1;
							var picurl=picurls.substring(1,len).split(",");
							//console.log(picurl[0]);
							var strStatus=comment.picstatus;
							var picstatus=strStatus.split(",");
							   status=picstatus[0];
							 if(i%2!=0){
								 trClass="tr-flag";
								 }
 							    if(status=="NEW"){
 									 opr='<a href="javascript:void(0);" class="audit">审核</a>';
 									 status="等待审核";
 								}
 								else if(status=="PASS"){
 									opr='已处理';
 								    status="审核通过";
 								}
 								else{
 									opr='已处理';
 								    status="审核未通过";
 								}
                 	html += '<tr class='+trClass+'><td class="userName ta-l" userid='+comment.user_id+'>'+comment.user_name+'</td>'
                 	+'<td>'+comment.mobile+'</td>'
                 	+'<td class="xsz-img" picurl='+picurl[0]+' vehicleId='+comment.vehicle_id+' picurlarr=\''+picurl+'\' platno='+comment.plat_no+'>'
                 	+'<a href=""><img src='+picurl[0]+' class="img-small"/>'
                 	+'<img src='+picurl[0]+' class="img-big"/></a></td>'
                 	+'<td>'+status+'</td>'
               +'<td class="change operation-btn ta-r">'+opr+'</td></tr>';
                         });
                         $('#table-a-list').html(html);
                         pageList(data.data);//调用设置分页按钮函数
						 siderbarControl("lframe");//设置左侧导航条高度
							 }else{
							 	alert(data.msg);
							 }
                      }
         });
	}
//点击审核按钮 调用接口 获取信息 显示在输入框中
function getAuditPcInfo(vehicleId){
	var searchData={userName:"",status:"NEW",phone:"",vehicleId:vehicleId,type:"2"};
	 $.ajax({
             type: "get",
             url: "/cms/vehicle/getVehiclePics",
             data: searchData,
             dataType: "json",
             success: function(data){
					if(data.code==200){
						var data=data.data.datas[0];
						console.log(data);
						$("#pcplatNo").val(data.plat_no);
						$("#pcvehicleType").val(data.vehicle_type);
						$("#pcowner").val(data.vehicle_owner);
						$("#pcaddress").val(data.address);
						$("#pcuseCharacter").val(data.use_character);
						$("#pcmodel").val(data.model);
						$("#pcvin").val(data.vin);
						$("#pcengineNo").val(data.engine_no);
						$("#pcregisterDate").val(data.register_date);
						$("#pcissueDate").val(data.issue_date);
						//$("#pcdisagree").val();//不同意时填写的信息
						//$("#pcbrandId").val();
						//$("#pcseriesId").val();
					    //$("#pcmodelId").val();
					    var otherPic=data.others;
					    var len=otherPic.length;
					    var picUrl=otherPic.substring(1,len-1).split(',');
					    //console.log(picUrl);
					    var picUrlLen=picUrl.length;
					    var html='';
					    for(var i=0;i<picUrlLen;i++){
					    	console.log(picUrl[i]);
					    	 html+='<li>'+
						 '<div class="photo photo-checkbox fl col-6">'+
			               '<div class="pic-box"><a href='+picUrl[i]+'><img src='+picUrl[i]+' rel='+picUrl[i]+' class="pcaudit-xszimg"></a></div>'+
			            '</div>'+
					  '</li>';
					    }
						
		
					    $("#slideBoxPc").find(".bd ul").append(html);
					    $("#slideBoxPc").slide({mainCell:".bd ul",autoPlay:false});//调用滑动js
					    $(".pcagree").attr("disabled","disabled");
						$(".pcdisagree").attr("disabled","disabled");
						$(".tk-audPcDrivinglic").show();
						$(".tk-bg").show();
						  $(".pcaudit-xszimg").imagezoom();
                       }
                       else{
                       	alert(data.msg);
                       }
						
                      }
         });
	}

function auditPhoneDrivingLic(){
	var picId=$("#auditbtn").attr("picid");
	var userId=$("#auditbtn").attr("userid");
	var status=$("#auditbtn").attr("status");
	var picUrlZb=$("#auditbtn").attr("picurl");
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
	var brandId=$("#brandId").val();
	var seriesId=$("#seriesId").val();
	var modelId=$("#modelId").val();
	var picLi=$("#slideBoxPhone").find(".bd li");
	console.log(picLi);
	var urlStr="";
	picLi.each(function(i,data) {//循环取得图片
		$this=$(this);
		//var sfsh=$this.find(".sfsh").prop("checked");
		var sfsh=$this.find(".sfsh").prop("checked");
		console.log(sfsh);
		if(sfsh==true){
		var picurl=$this.find(".audit-xszimg").attr("src");
		var type=$this.find("input[name='xszsh']:checked").val();
		var status=$this.find("input[name='xszwt']:checked").val();
		var str=picurl+"?"+"type="+type+"&"+"status="+status;
		urlStr=str+","+urlStr;
		}
	});


		 var dataJson={picId:picId,userId:userId,status:status,platNo:platNo,vehicleType:vehicleType,owner:owner,address:address,useCharacter:useCharacter,
	 	model:model,vin:vin,engineNo:engineNo,registerDate:registerDate,issueDate:issueDate,manualModel:disagree,brandId:brandId,seriesId:seriesId,modelId:modelId,
	 	urls:urlStr};
		
     console.log(dataJson);
	$.ajax({
         type: "post",
         url: "/cms/vehicle/checkPic",
         data: dataJson,
         dataType: "json",
         success: function(data){
			 if(data.code==200){
			 	getAuditPhoneDrivingLicInfo();
			 	alert("审核成功！");
			 	$("#auditForm")[0].reset();
		 		$(".tk-audDrivinglic").hide();
				$(".tk-bg").hide();

			 }
			 else{
			 	alert(data.msg);
			 }
			 }
     });
}

function auditPcDrivingLic(){

	var picLi=$("#slideBoxPc").find(".bd li");
	console.log(picLi);
	var urlStr="";
	picLi.each(function(i,data) {//循环取得图片
		$this=$(this);
		var flag=$this.attr("flag");
		if(flag!="zb"){
			var picurl=$this.find(".pcaudit-xszimg").attr("src");
			urlStr=picurl+","+urlStr;
		}
	});

	var vehicleId=$("#pcauditbtn").attr("vehicleid");
	var status=$("#pcauditbtn").attr("status");
	 var dataJson={vehicleId:vehicleId,status:status,urls:urlStr,type:"2"};
		
		$.ajax({
         type: "post",
         url: "/cms/vehicle/checkPicWithVehicle",
         data: dataJson,
         dataType: "json",
         success: function(data){
			 if(data.code==200){
			 	getAuditPcDrivingLicInfo();
			 	alert("审核成功！");
			 	$("#auditForm")[0].reset();
		 		$(".tk-audDrivinglic").hide();
				$(".tk-bg").hide();

			 }
			 else{
			 	alert(data.msg);
			 }
			 }
     });
}


