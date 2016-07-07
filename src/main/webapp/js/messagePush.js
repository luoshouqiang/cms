// JavaScript Document
$(function(){
  siderbarControl("lframe")//设置左侧导航条高度
$("#messagePushForm").validate({
       submitHandler: function(form) {
       	$(".tk-bg").show();
       	$("#tkConfirm").show();
		},
		rules: {
				messageTitle: {//标题
					required:true
				},
				messageContent: {//内容
					required:true
				}
			},
			messages: {
				messageTitle: {
					required: "请填写标题！"
				},
				messageContent: {
					required: "请填写推送内容！"
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	

	$("#pushConfirm").click(function(){
		var searchData={title:$("#messageTitle").val(),content:$("#messageContent").val()};
		 $.ajax({
         type: "post",
         url: "/cms/msg/pushAll",
		 data: searchData,
         //data: JSON.stringify(searchData),
		 //contentType: "application/json; charset=utf-8",
         dataType: "json",
         success: function(data){
			 var dataCode=data.code;
			 if(dataCode==0){
				 alert("消息推送成功！");
				  	$(".tk-bg").hide();
       				$("#tkConfirm").hide();
				 }
			else{
				alert(data.msg);
				}
                  }
     });
	});
	$("#pushCancle").click(function(){
		$(".tk-bg").hide();
   		$("#tkConfirm").hide();
	});
})