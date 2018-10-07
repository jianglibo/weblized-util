package com.go2wheel.weblizedutil.valueprovider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.stereotype.Service;

import com.go2wheel.weblizedutil.SettingsInDb;
import com.go2wheel.weblizedutil.annotation.ObjectFieldIndicator;
import com.go2wheel.weblizedutil.annotation.ShowPossibleValue;
import com.go2wheel.weblizedutil.model.ReusableCron;
import com.go2wheel.weblizedutil.service.ReusableCronDbService;
import com.go2wheel.weblizedutil.util.ObjectUtil;

@Service
public class SharedValueProviderMethods {

	
	@Autowired
	private SettingsInDb settingsInDb;

	@Autowired
	private ReusableCronDbService reusableCronDbService;

	public List<CompletionProposal> getOstypeProposals(String input) {
		return settingsInDb.getListString(SettingsInDb.OSTYPE_PREFIX).stream().map(CompletionProposal::new).collect(Collectors.toList());
	}

	public Object filedHasAnnotation(CompletionContext completionContext, MethodParameter parameter,
			List<Class<? extends Annotation>> al) {
		ObjectFieldIndicator sv = parameter.getParameterAnnotation(ObjectFieldIndicator.class);
		if (sv != null) {
			Field fd = getCurrentField(completionContext, parameter, sv);
			if (fd != null) {
				for (Class<? extends Annotation> t : al) {
					Object an = fd.getAnnotation(t);
					if (an != null) {
						return an;
					}
				}
			}
		}
		return null;
	}
	
	public List<CompletionProposal> getCronProposals() {
		List<ReusableCron> crons = reusableCronDbService.findAll();
		return crons.stream().map(o -> o.toListRepresentation()).map(CompletionProposal::new)
				.collect(Collectors.toList());
	}

	public List<CompletionProposal> getOriginValue(CompletionContext completionContext, MethodParameter parameter,
			String input) {
		ObjectFieldIndicator sv = parameter.getParameterAnnotation(ObjectFieldIndicator.class);
		List<String> candicates = new ArrayList<>();
		if (sv != null) {
			Field fd = getCurrentField(completionContext, parameter, sv);
			if (fd != null) {
				Class<?> c = sv.objectClass();
			}
		}
		return candicates.stream().map(CompletionProposal::new).collect(Collectors.toList());
	}

	private Field getCurrentField(CompletionContext completionContext, MethodParameter parameter,
			ObjectFieldIndicator sv) {
		Method md = parameter.getMethod();
		/**
		 * Because ObjectFieldValueProvider usually has a @see ShowPossibleValue
		 * annoation which list all of possible filed names. So by getting the value of
		 * --field which already entered we can get the field name of the target object,
		 * then get the field information by java reflecting.
		 */
		Optional<Parameter> pvOp = Arrays.stream(md.getParameters())
				.filter(p -> p.getAnnotation(ShowPossibleValue.class) != null).findAny();
		if (pvOp.isPresent()) {
			String pn = "--" + pvOp.get().getName(); // --field os
			List<String> words = completionContext.getWords();
			int len = words.size();
			for (int i = 0; i < len; i++) {
				String w = words.get(i);
				if (w.equals(pn) && i + 1 < len) {
					String fn = words.get(i + 1);
					Optional<Field> fdOp = ObjectUtil.getField(sv.objectClass(), fn);
					if (fdOp.isPresent()) {
						return fdOp.get();
					}
				}
			}
		}
		return null;
	}

}
