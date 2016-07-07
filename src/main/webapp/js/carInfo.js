// JavaScript Document

var carInfo={
	getCarBrand:function(){//获取车辆品牌
	 $.ajax({
             type: "get",
             url: "/cms/vehicle/getBrands",
             data: {},		
             dataType: "json",
             success: function(data){
						//console.log(data);
						if(data.code==200){
							  var html = ''; 
							 $.each(data.data, function(i, brand){
								   html += '<option value='+brand.brand_id+'>'+brand.brand_name+'</option>';
							 });
							 $('#carBrand').html(html);
							 carInfo.getCarSeries(data.data[0].brand_id);
						}
						else{
							alert(data.msg);
							}
                      }
         });
	
	
	},
	getCarSeries:function(carBarndId){//获取车系
	 $.ajax({
             type: "get",
             url: "/cms/vehicle/getSeriesByBrandId",
             data: {id:carBarndId},		
             dataType: "json",
             success: function(data){
						if(data.code==200){
						  var html = ''; 
                         $.each(data.data, function(i,series){
                               html += '<option value='+series.series_id+'>'+series.series_name+'</option>';
                         });
                         $('#carSeries').html(html);
						 carInfo.getCarModel(data.data[0].series_id);
						 }
						else{
							alert(data.msg);
							}
                      }
         });
	
	
	},
	getCarModel:function(carSeriesId){//获取车型
	 $.ajax({
             type: "get",
             url: "/cms/vehicle/getModelBySeriesId",
             data: {id:carSeriesId},		
             dataType: "json",
             success: function(data){
						console.log(data);
						if(data.code==200){
						  var html = ''; 
                         $.each(data.data, function(i, model){
                              html += '<option value='+model.model_id+'>'+model.model_name+'</option>';
                         });
                         $('#carModels').html(html);
						}
						else{
							alert(data.msg);
							}
                      }
         });
	
	
	}
	
}