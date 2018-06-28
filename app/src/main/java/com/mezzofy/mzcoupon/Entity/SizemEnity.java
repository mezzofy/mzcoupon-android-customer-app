package com.mezzofy.mzcoupon.Entity;

import java.io.Serializable;

public class SizemEnity implements Serializable {
	private Integer pagesize;
	private Integer resultsize;
	private Integer totalsize;
	
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getResultsize() {
		return resultsize;
	}
	public void setResultsize(Integer resultsize) {
		this.resultsize = resultsize;
	}
	public Integer getTotalsize() {
		return totalsize;
	}
	public void setTotalsize(Integer totalsize) {
		this.totalsize = totalsize;
	}

	
}