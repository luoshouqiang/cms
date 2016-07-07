// JavaScript Document
$(function(){
  siderbarControl("lframe")//设置左侧导航条高度
$("#area-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
		var searchData={areaName:$("#area-name").val(),contact:$("#contact").val(),contactPhone:$("#contact-phone").val()/*,areaDesc:$("#area-desc").val()*/};
			 $.ajax({
             type: "post",
             url: "/cms/area/AddAreas",
			 data: searchData,
             dataType: "json",
             success: function(data){
				 //console.log(data);
				 var dataCode=data.code;
				 if(dataCode==200){
					alert(data.data);
					 }
				else{
					alert(data.msg);
					}
                      }
         });
			//form.submit();
		},
		rules: {
				areaName: {//小区名字
					required:true
				},
				contact: {//联系人
					required:true
				},
				contactPhone: {//联系电话
					required:true,
					isMobile:true
				},
				areaDesc: {//小区介绍
					required:false
				}
			},
			messages: {
				areaName: {
					required: "请输入小区名称！"
				},
				contact: {
					required: "请输入联系人！"
				},
				contactPhone: {
					required: "请输入联系方式！",
				},
				areaDesc: {
					required: "请输入小区简介！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	
	})