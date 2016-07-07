// JavaScript Document
$(function(){
  siderbarControl("lframe")//设置左侧导航条高度
$("#area-add-form").validate({
       submitHandler: function(form) {
			//alert("submitted!");
		var searchData={insurName:$("#insur-name").val(),userId:getQueryString("userid"),vehicleId:getQueryString("vehicleid"),insurPrice:$("#insur-price").val(),
		insurContent:$("#insur-desc").val()};
			 $.ajax({
             type: "post",
             url: "/cms/package/newInsurOrder",
			 data: JSON.stringify(searchData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
				 //console.log(data);
				 var dataCode=data.code;
				 if(dataCode==0){
					alert("添加成功！");
					 }
				else{
					alert("添加失败！");
					}
                      }
         });
			//form.submit();
		},
		rules: {
				insurName: {//保险名称
					required:true
				},
				userName: {//用户名称
					required:true
				},
				insurPrice: {//保险价格
					required:true,
					intNo:true
				},
				insurDesc: {//保险内容
					required:false
				}
			},
			messages: {
				insurName: {
					required: "请输入保险名称！"
				},
				userName: {
					required: "请输入用户名称！"
				},
				insurPrice: {
					required: "请输入保险价格！",
				},
				insurDesc: {
					required: "请输入保险内容！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	
	})

/*
 * 获取url中的参数值
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}	