package com.go2wheel.weblizedutil;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
public class LocaledMessageService {

	@Autowired
	private MessageSource messageSource;

	private Locale locale = Locale.CHINESE;

	public String getMessage(String code, Object... args) {
		try {
			return messageSource.getMessage(code, args, locale);
		} catch (NoSuchMessageException e) {
			return code;
		}
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
