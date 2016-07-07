package mcarport.business.cms.action;

import java.util.List;
import java.util.Map;

import mcarport.business.cms.dto.PackageInsurOrder;
import mcarport.business.cms.dto.PackageOrder;
import mcarport.business.cms.dto.ReturnData;
import mcarport.business.cms.dto.SearchBean;
import mcarport.business.cms.exception.BusinessException;
import mcarport.business.cms.service.PackageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("package")
public class PackageController {
	
	@Autowired
	PackageService packageService;
	
	@RequestMapping(value="listItem")	
	@ResponseBody
	public ReturnData<List<Map<String,Object>>> ListItem(){
		ReturnData<List<Map<String,Object>>> rd = new ReturnData<List<Map<String,Object>>>();
		
		try {
			
			List<Map<String,Object>> listItem=packageService.queryAllPackageItem();
			rd.setData(listItem);
		} catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}
	
	@RequestMapping(value="newOrder",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> newOrder(@RequestBody PackageOrder order){
		ReturnData<String> rd = new ReturnData<String>();
		
		try {
			packageService.saveOrder(order);
		}catch (BusinessException e){
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}
	
	@RequestMapping(value="newInsurOrder",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<String> newInsurOrder(@RequestBody PackageInsurOrder order){
		ReturnData<String> rd = new ReturnData<String>();
		
		try {
			packageService.saveInsurOrder(order);
		}catch (BusinessException e){
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}
	
	@RequestMapping(value="listOrder",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<List<Map<String,Object>>> listOrder(@RequestBody SearchBean searchData){
		ReturnData<List<Map<String,Object>>>rd = new ReturnData<List<Map<String,Object>>>();
		
		try {
			List<Map<String,Object>> orderList=packageService.queryPackageOrder(searchData);
			rd.setData(orderList);
		}catch (BusinessException e){
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}
	
	@RequestMapping(value="orderDetail",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<List<Map<String,Object>>> orderDetail(long packageId){
		ReturnData<List<Map<String,Object>>>rd = new ReturnData<List<Map<String,Object>>>();
		
		try {
			List<Map<String,Object>> orderList=packageService.packageOrderDetail(packageId);
			rd.setData(orderList);
		}catch (BusinessException e){
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}
	
	@RequestMapping(value="listInsurOrder",method=RequestMethod.POST)	
	@ResponseBody
	public ReturnData<List<Map<String,Object>>> listInsurOrder(@RequestBody SearchBean searchData){
		ReturnData<List<Map<String,Object>>>rd = new ReturnData<List<Map<String,Object>>>();
		
		try {
			List<Map<String,Object>> orderList=packageService.queryInsurOrder(searchData);
			rd.setData(orderList);
		}catch (BusinessException e){
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}

	
	@RequestMapping(value="processOrder")	
	@ResponseBody
	public  ReturnData<String> processOrder(long packgeId,String status){
		 ReturnData<String> rd = new  ReturnData<String>();
		
		try {
			packageService.processOrder( packgeId, status);
		}catch (BusinessException e){
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}
	
	@RequestMapping(value="processInsurOrder")	
	@ResponseBody
	public  ReturnData<String> processInsurOrder(long insurId,String status){
		 ReturnData<String> rd = new  ReturnData<String>();
		
		try {
			packageService.processInsurOrder( insurId, status);
		}catch (BusinessException e){
			rd.setCode(201);
			rd.setMsg(e.getMessage());
		}
		catch (Exception e) {
			rd.setCode(201);
			rd.setMsg("系统繁忙");
			e.printStackTrace();
		}
		return rd;
	}
}
