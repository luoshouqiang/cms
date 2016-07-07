package mcarport.business.cms.dto;


public class Page<T> {

	
	private int  total;
	
	private T datas;
	
	private   int pageIndex=1;
	
	private transient int pageSize=20;
	

	public int getTotal() {
		
		if(this.total%getPageSize()==0) {
			return this.total/getPageSize();
		}else{
			return (this.total/getPageSize())+1;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	

	public T getDatas() {
		return datas;
	}

	public void setDatas(T datas) {
		this.datas = datas;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	
}
