package com.go2wheel.weblizedutil.value;

public class IdBinder {
	
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("[id: %s]", getId());
	}
}
