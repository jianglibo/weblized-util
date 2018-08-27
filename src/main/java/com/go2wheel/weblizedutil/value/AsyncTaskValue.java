package com.go2wheel.weblizedutil.value;

public class AsyncTaskValue {
	
	private Long id;
	
	private String description;
	
	private Object result;
	
	private boolean empty = true;
	
	private Throwable throwable;
	
	public AsyncTaskValue() {}
	
	public AsyncTaskValue(Long id, Object result) {
		this.id = id;
		this.result = result;
		this.empty = false;
	}
	
	public AsyncTaskValue(Long id, Throwable throwable) {
		this.id = id;
		this.setThrowable(throwable);
		this.empty = false;
	}
	
	public static AsyncTaskValue emptyValue() {
		return new AsyncTaskValue();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public AsyncTaskValue withDescription(String description) {
		setDescription(description);
		return this;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

}
