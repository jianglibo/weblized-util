package com.go2wheel.weblizedutil.convert.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.go2wheel.weblizedutil.model.BaseModel;
import com.go2wheel.weblizedutil.model.JobLog;
import com.go2wheel.weblizedutil.model.KeyValue;
import com.go2wheel.weblizedutil.model.ReusableCron;
import com.go2wheel.weblizedutil.model.UserAccount;
import com.go2wheel.weblizedutil.service.JobLogDbService;
import com.go2wheel.weblizedutil.service.KeyValueDbService;
import com.go2wheel.weblizedutil.service.ReusableCronDbService;
import com.go2wheel.weblizedutil.service.UserAccountDbService;

public class Id2Model implements ConverterFactory<String, BaseModel> {
	
	
	@Autowired
	private UserAccountDbService userAccountDbService;
	
	@Autowired
	private ReusableCronDbService reusableCronDbService;
	
	@Autowired
	private KeyValueDbService keyValueDbService;
	
	@Autowired
	private JobLogDbService jobLogDbService;
	

	public <T extends BaseModel> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToModelConverter<T>(targetType);
	}

	private final class StringToModelConverter<T extends BaseModel> implements Converter<String, T> {

		private Class<T> modelType;

		public StringToModelConverter(Class<T> modelType) {
			this.modelType = modelType;
		}

		@SuppressWarnings("unchecked")
		public T convert(String source) {
			if( modelType == UserAccount.class) {
				return (T) userAccountDbService.findById(source);
			} else if (modelType == JobLog.class) {
				return (T) jobLogDbService.findById(source);
			} else if (modelType == KeyValue.class) {
				return (T) keyValueDbService.findById(source);
			} else if (modelType == ReusableCron.class) {
				return (T) reusableCronDbService.findById(source);
			} else {
				return (T) null;
			}
		}
	}

}
