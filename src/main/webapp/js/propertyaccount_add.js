// JavaScript Document
$(function(){
  areaData();	//初始化下拉选择框的小区数据
  siderbarControl("lframe")//设置左侧导航条高度
$("#virtualparking-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
		var areaId=$("#areaName").val();
		var areaName=$("#areaName").find("option:selected").text();
		var searchData={areaId:areaId,userName:$("#user-name").val(),loginName:$("#login-name").val(),phone:$("#user-phone").val()};
			 $.ajax({
             type: "post",
             url: "/cms/area/AddUserAccount",
			 data: searchData,
             dataType: "json",
             success: function(data){
				 //console.log(data);
				 var dataCode=data.code;
				 if(dataCode==200){
					 console.log(areaName);
					 getPropertyAccount(areaId,areaName);//获取物业账号列表
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
				areaName: {//小区名称
					required:true
				},
				userName: {//物业姓名
					required:true
				},
				loginName: {//登录账号
					required:true
				},
				userPhone: {//物业电话
					required:true,
					isMobile:true
				}
			},
			messages: {
				areaName: {
					required: "请选择小区！"
				},
				userName: {
					required: "请输入用户名！"
				},
				loginName: {
					required: "请输入登录账号！",
				},
				userPhone: {
					required: "请输入电话号码！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	
$("#areaName").change(function(){
		var areaName=$(this).find("option:selected").text();
		var areaId=$(this).val();
		getAreaPinYin(areaName);//根据小区id调用函数获取拼音
		getPropertyAccount(areaId,areaName);//获取物业账号列表
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
				       var areaName=data[0].area_name;
					   var areaId=data[0].area_id;
				        var html = ''; 
                         $.each(data, function(i, area){
                               html += '<option value='+area.area_id+'>'+area.area_name+'</option>';
                         });
                         $('#areaName').append(html);
		                getAreaPinYin(areaName);//根据小区id调用函数获取拼音
						getPropertyAccount(areaId,areaName);//获取物业账号列表
                      }
         });
		}
	
//获取小区的拼音
function getAreaPinYin(areaName){
			 $.ajax({
             type: "post",
             url: "/cms/area/getPinYin",
             data: {areaName:areaName},		
             dataType: "json",
             success: function(data){
					$("#login-name").val(data.data);
                      }
         });
		}

//获取车位，根据小区
function getPropertyAccount(areaId,areaName){
	//console.log(areaId);
	console.log(areaName);
			 $.ajax({
             type: "get",
             url: "/cms/user/getWYUser",
             data: {areaId:areaId},		
             dataType: "json",
             success: function(data){
						console.log(data);
						var html='';
						$.each(data.data,function(i,dVal){
								html += '<tr><td class="ta-l">'+areaName+'</td><td>'+dVal.login_name+'</td><td>'+dVal.user_name+'</td>'
			                        +'<td class="ta-r">'+dVal.mobile+'</td></tr>';
							});
						$("#table-a-list").html(html);
						siderbarControl();
                      }
         });
	}