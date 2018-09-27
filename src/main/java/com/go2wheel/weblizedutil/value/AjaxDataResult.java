package com.go2wheel.weblizedutil.value;

import java.util.List;

import com.google.common.collect.Lists;

public class AjaxDataResult<T> implements AjaxResult {

	private List<T> data = Lists.newArrayList();
	
	private AjaxResultMeta meta;
	
	public AjaxDataResult() {
		
	}
	
	public AjaxDataResult(T singleData) {
		this.data.add(singleData);
	}
	
	public AjaxDataResult(List<T> listData) {
		this.data = listData;
	}
	
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	@SuppressWarnings("unchecked")
	public void addObject(Object o) {
		this.data.add((T) o);
	}
	
	public AjaxResultMeta getMeta() {
		return meta;
	}

	public void setMeta(AjaxResultMeta meta) {
		this.meta = meta;
	}

	public static class AjaxResultMeta {

		private long total;

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}
	}
	
}
