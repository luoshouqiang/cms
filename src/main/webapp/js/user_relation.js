// JavaScript Document
$(function(){
	areaData();//获取小区
	siderbarControl("lframe")//设置左侧导航条高度
	new ownWay({ownway:".ownway"});//获取拥有方式
	$("#userrelation").validate({
      submitHandler: function(form) {
			//alert("submitted!");
			var searchData={parkingId:$("#parkingNo").attr("datapkid"),userName:$("#userName").val(),phoneNum:$("#phoneNum").val(),
			houseId:$("#roomNo").val(),cardId:$("#idNo").val(),way:$("#relation-selectOwnway").val()};
			 $.ajax({
             type: "post",
             url: "/cms/user/addUser",
             data: searchData,
             dataType: "json",
             success: function(data){
			        var code=data.code;
					if(code=="0"){
					   //$(".mk-user-add").slideUp();
					    var searchData={areaId:$("#areaName").val(),parkingNo:$("#parkingLot").val()};
			            getUserInfo(searchData);
					    new hideBombBox({tkbg:".tk-bg",tkbox:".mk-user-relation"});//调用显示弹框函数
						alert("添加成功");
                      }
					else if(code=="2"){
						//console.log(data);
						//alert("当前用户已经存在车位了");
						var parkingid=$("#parkingNo").attr("datapkid");;
						var roomno=$("#roomNo").val();
						var phonenum=$("#phoneNum").val();
						var idno=$("#idNo").val();
						var username=$("#userName").val();
						var ownway=$("#relation-selectOwnway").val();
						
						$("#uName").val($("#userName").val());//取值放入弹框
						$("#pNum").val($("#phoneNum").val());//取值放入弹框
						
						var dataJson={parkingId:parkingid,roomNo:roomno,phoneNum:phonenum,idNo:idno,userName:username,ownWay:ownway}
						$("#updateuser-ok").attr(dataJson);
						new hideBombBox({tkbg:".tk-bg",tkbox:".mk-user-relation"});//调用隐藏弹框函数
						new showBombBox({tkbg:".tk-bg",tkbox:".tk_ownparking"});//调用隐藏弹框函数
					}
					else if(code=="4"){
						alert("车位已经被占用!");
						}
						}
         });
			//form.submit();
		},
		rules: {
			    roomNo:{
					required:true,
					isRoomNo:true
					},
				userName: {//用户名字
					required: true,
				},
				phoneNum: {//电话号码
				   required: true,
				   isMultiMobile:true
				},
				idNo:{
					required:false,
					isID:true
					}
			},
			messages: {
				roomNo: {
					required: "房号不能为空！",
				},
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
	
	//根据小区和车位搜索
	$(document).on("click","#parkingSearch",function(){
			getUserInfo();
		
		});

    //更新用户（当用户已经有车位的情况下）
	$(document).on("click","#updateuser-ok",function(){
		$this=$(this);
		updateUser($this);
		});
	
	//修改拥有方式 弹框 
	$(document).on("click",".own-way",function(){
		$this=$(this);
		var uid=$this.siblings(".uname").attr("uid");
		var pkid=$this.siblings(".parkingNo").attr("parkingid");
		if(pkid=="无"){
			alert("没有车位号，不能修改");
			return;
			}
		$("#changeownway-ok").attr("uid",uid);
		$("#changeownway-ok").attr("pkid",pkid);
		  	$(".tk-bg").show();
		    $(".tk_ownway").show(); 
		});
		//更改拥有方式 调用修改拥有方式函数 
	$(document).on("click","#changeownway-ok",function(){
		var searchData={userId:$(this).attr("uid"),parkingId:$(this).attr("pkid"),way:$("#selectOwnway").val()};
		 updateOwnway(searchData);
		});
		
	$(".quxiao").click(function(){
			$(".tk-bg").hide();
		    $(".tk").hide();  
			});
	$("#mkuserrelation-close").click(function(){//隐藏添加用户弹框
		new hideBombBox({tkbg:".tk-bg",tkbox:".mk-user-relation"});//调用隐藏弹框函数
		});
			
	//添加信息
	$(document).on("click",".bund-btn",function(){
		$this=$(this);
		var pkno=$this.siblings(".parkingNo").text();
		var pkid=$this.siblings(".parkingNo").attr("parkingid");
		  $("#parkingNo").val(pkno);
		   $("#parkingNo").attr("datapkid",pkid);
		     new showBombBox({tkbg:".tk-bg",tkbox:".mk-user-relation"});//调用显示弹框函数
		  //$(".mk-user-add").slideDown();
		  setTimeout(siderbarControl,400); //设置左侧导航条高度
	})
	
	//解除绑定关系
	$(document).on("click",".unbund-btn",function(){
		$this=$(this);
		var userid=$this.siblings(".uname").attr("uid");
		//var areaid=$this.siblings(".uname").attr("areaId");
		var parkingid=$this.siblings(".parkingNo").attr("parkingid");
		var vehicleid=$this.siblings(".platno").attr("vehicleid");
		//console.log(userid);
		//console.log(parkingid);
		 $.ajax({
             type: "post",
             url: "/cms/user/delUserParking",
             data: {userId:userid,parkingId:parkingid,vehicleId:vehicleid},		
             dataType: "json",
             success: function(data){
				 
				 console.log(data);
				if(data.code==200){
					   alert("解绑成功！");
						var areaid=$("#areaName").val();
					    var parkinglot=$("#parkingLot").val();
					    var searchData={areaId:areaid,parkingNo:parkinglot};
						getUserInfo(searchData);
					}
				else{
					alert(data.msg);
					}
                      }
         });
		
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
			 	getUserInfo(rePageIndex,pageSize);
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

	 

	
//更改拥有方式函数
function updateOwnway(searchData){
			 $.ajax({
             type: "post",
             url: "/cms/user/updateUserParkingRelation",
             data: searchData,		
             dataType: "text",
             success: function(data){
				if(data.msg="修改成功"){
						$(".tk-bg").hide();
		                $(".tk").hide();
					    alert("修改成功！");
						//var areaId=$("#areaName").val();
					   // var parkingLot=$("#parkingLot").val();
					   // var searchData={areaId:areaId,parkingNo:parkingLot};
						getUserInfo();
					}
				else{
					$(".tk-bg").hide();
		            $(".tk").hide();
					alert("修改失败！");
					}
                      }
         });
	}	
	
	
	
	
	
	
//getParkingInfo
function getUserInfo(pageIndex,pageSize){//获取用户信息
	      var areaId=$("#areaName").val();
          var parkingLot=$("#parkingLot").val();
           if(pageIndex==null){
				pageIndex=1;
			}
			if (pageSize==null) {
				pageSize=20;
			}
	      var searchData={areaId:areaId,parkingNo:parkingLot,pageIndex:pageIndex,pageSize:pageSize};
			 $.ajax({
             type: "post",
             url: "/cms/areacms/list",
             data: JSON.stringify(searchData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
			           $('#table-a-list').html(""); 
						 if(data.datas[0]==null){
							 alert("没有符合条件的搜索！");
							 return;
							 }
						var html;
						$.each(data.datas,function(i,dVal){
							var uName=dVal.userName;
							var parkingNo=dVal.parkingNo;
							 var plotsName=dVal.plotsName;
							 var roomNo=dVal.roomNo;
							 if(roomNo==null){roomNo="无";}
							 if(plotsName==null){
								 plotsName="无";
								 }
							if(uName=="无"){
			html+='<tr>'
			+'<td class="uname" uid='+dVal.userId+' areaId='+dVal.areaId+'>'+dVal.userName+'</td>'
			+'<td>'+dVal.phoneNum+'</td>'
			+'<td>'+roomNo+'</td>'
			+'<td>'+plotsName+'</td>'
			+'<td class="parkingNo" parkingid='+dVal.parkingId+'>'+dVal.parkingNo+'</td>'
			+'<td>'+dVal.parkingSpace+'</td>'
			+'<td class="operation">无</td>'
			+'<td class="platno" vehicleid='+dVal.vehicleId+'>'+dVal.platNo+'</td>'
			+'<td class="operation due-date">'+dVal.dueDate+'</td>'
			+'<td class="operation-btn ta-r bund-btn">'
			+'<a href="javascript:void(0);">绑定</a></td>'
                    +'</tr>';
								}
						 else if(uName!="无"&&parkingNo!="无"){
							 html+='<tr>'
			+'<td class="uname" uid='+dVal.userId+' areaId='+dVal.areaId+'>'+dVal.userName+'</td>'
			+'<td>'+dVal.phoneNum+'</td>'
			+'<td>'+roomNo+'</td>'
			+'<td>'+plotsName+'</td>'
			+'<td class="parkingNo" parkingid='+dVal.parkingId+'>'+dVal.parkingNo+'</td>'
			+'<td>'+dVal.parkingSpace+'</td>'
			+'<td class="operation own-way"><span><em>'+dVal.parkingType+'</em><i class="icon-carno-change"></i></span></td>'
			+'<td class="platno" vehicleid='+dVal.vehicleId+'>'+dVal.platNo+'</td>'
			+'<td class="operation due-date">'+dVal.dueDate+'</td>'
			+'<td class="operation-btn ta-r unbund-btn">'
			+'<a href="javascript:void(0);">解绑</a></td>'
                    +'</tr>';
							}
								else{
					html+='<tr>'
			+'<td class="uname" uid='+dVal.userId+' areaId='+dVal.areaId+'>'+dVal.userName+'</td>'
			+'<td>'+dVal.phoneNum+'</td>'
			+'<td>'+roomNo+'</td>'
			+'<td>'+plotsName+'</td>'
			+'<td class="parkingNo" parkingid='+dVal.parkingId+'>'+dVal.parkingNo+'</td>'
			+'<td>'+dVal.parkingSpace+'</td>'
		+'<td class="operation">无</td>'
			+'<td class="platno" vehicleid='+dVal.vehicleId+'>'+dVal.platNo+'</td>'
			+'<td class="operation due-date">'+dVal.dueDate+'</td>'
			+'<td class="operation-btn ta-r">'
			+'<a href="javascript:void(0);" style="color:#000000; text-decoration:none;">无</a></td>'
                    +'</tr>';
									}
						});
					  $('#table-a-list').html(html);
					    pageList(data);//调用设置分页按钮函数
					  siderbarControl("lframe")//设置左侧导航条高度
                      }
         });
	
}

function updateUser($this){
	   $this=$this;
		var searchData={parkingId:$this.attr("parkingId"),userName:$this.attr("userName"),phoneNum:$this.attr("phoneNum"),houseId:$this.attr("roomNo"),cardId:$this.attr("idNo"),
		way:$this.attr("ownWay")};
			 $.ajax({
             type: "post",
             url: "/cms/user/updateUser",
             data: searchData,
             dataType: "json",
             success: function(data){
				  $(".tk-bg").hide();
		          $(".tk").hide(); 
				   //var sData={areaId:$("#areaName").val(),parkingNo:$("#parkingLot").val()};
			         getUserInfo();
				  alert("更新成功！");
				  $(".mk-user-add").slideUp();
			 }
					
         });
	}
 