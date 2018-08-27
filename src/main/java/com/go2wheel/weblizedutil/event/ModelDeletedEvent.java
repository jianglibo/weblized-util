package com.go2wheel.weblizedutil.event;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import com.go2wheel.weblizedutil.model.BaseModel;

public class ModelDeletedEvent<T extends BaseModel> implements ResolvableTypeProvider {
	
	private T model;
	
    public ModelDeletedEvent(T model) {
        this.model = model;
    }


	@Override
	public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(),
                ResolvableType.forInstance(model));
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}
}
