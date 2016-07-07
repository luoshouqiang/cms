// JavaScript Document
$(function(){
	siderbarControl("lframe");
	 getPackageList();
	 getUserInfo(getQueryString("userid"));
	 $("#searchform").validate({
       submitHandler: function(form) {
			//alert("submitted!");
			var userId=getQueryString("userid");
			var vehicleId=getQueryString("vehicleid");
			var allPrice=$("#all-price").val();
			var remark=$("#remark").val();
			var itemList=new Array();
			$("#table-a-list").find("tr").each(function(index, el) {
				$this=$(this);
				var itemId=$this.find(".item-id").text();
				var times=$this.find(".pac-tim-td input").val();
				var item={packageItemId:itemId,totalTimes:times};
       	 	    itemList[index]=item;
       	 	    //console.log(itemList[index]);
			});
			var searchData={userId:userId,vehicleId:vehicleId,actualPrice:allPrice,itemList:itemList};
	 $.ajax({
             type: "post",
             url: "/cms/package/newOrder",
             data: JSON.stringify(searchData),
             contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
             	console.log(data);
             	if(data.code==0){
             		alert("添加成功！");
             	}
             	else{
             		alert("添加失败！");
             	}

             }
         })
		},
		rules: {
				allPrice: {//总价
					required:true,
					posNo:true

				},
			},
			messages: {
				allPrice: {
					required: "价格不能为空！",
				}
			},
			 //设置错误信息存放标签
				errorElement: "strong"
    });	

	 $(document).on("blur",".package-times",function(){
	 	$this=$(this);
	 	var packageTime=$this.val();
	 	console.log(packageTime);
	 	var reg=/^[0-9]*$/;
	 	if(reg.test(packageTime)){
		 	var marketPrice=$this.parent("td").siblings('.market-price').html();
		 	console.log(marketPrice);
		 	var packagePrice=parseInt(packageTime)*parseInt(marketPrice);
		 	console.log(packagePrice);
		 	$this.parent("td").siblings('.package-price').html(packagePrice);
		 	$this.siblings('p').hide();
		 	countPrice();
	 	}else{
	 		$this.siblings('p').show();
	 }
	 })
	})

function countPrice(){
	var allPrice=0;
	$(".package-price").each(function(index, el) {
		$this=$(this);
		var price=$this.text();
		if(price){
			allPrice=allPrice+parseInt(price);
		}
	});
	$("#all-price").val(allPrice);
}



function getPackageList(){//获取套餐
	 $.ajax({
             type: "post",
             url: "/cms/package/listItem",
             data: "",
			 //contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){
             			console.log(data);
                         var html = '';
                         $.each(data.data, function(i, data){
                         	html += '<tr class="ta-l"><td class="item-id">'+data.package_item_id+'</td><td>'+data.item_name+'</td><td>'+data.item_provide_type+'</td>'
			                +'<td>'+data.item_cost_price+'</td><td class="market-price">'+data.item_market_price+'</td>'
			                +'<td class="pac-tim-td"><input type="text" class="form-control  package-times" style="width:80px; display:inline;"/>'
			                +'<p style="display:none; font-size:12px; color:red">请输入数字！</p></td>'
			                +'<td class="package-price"></td><td>'+data.remark+'</td></tr>';
                         });
                
                         $('#table-a-list').html(html);
						// siderbarControl("lframe");//设置左侧导航条高度
                      }
         });
	}

function getUserInfo(userId){
	var searchData={userId:userId,areaId:"2"};
	 $.ajax({
             type: "post",
             url: "/cms/areacms/list",
             data: JSON.stringify(searchData),
			 contentType: "application/json; charset=utf-8",
             dataType: "json",
             success: function(data){

             }
         })
}
		

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