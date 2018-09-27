package com.go2wheel.weblizedutil.service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.go2wheel.weblizedutil.SettingsInDb;
import com.go2wheel.weblizedutil.controller.QuartzCronExpressController;
import com.go2wheel.weblizedutil.value.PreDefinedCronPattern;
import com.go2wheel.weblizedutil.value.QuartzCronBuilderContext;
import com.go2wheel.weblizedutil.value.QuartzCronField;
import com.google.common.collect.Lists;

@Service
public class QuartzCronBuilderService {
	
	public static final String CRON_EXAMPLES_PREFIX = "app.quartzcron.examples.";
	
	public static final String CRON_PATTERN_NAME_PREFIX = "app.quartzcron.pattern.name";
	public static final String CRON_PATTERN_INAME_PREFIX = "app.quartzcron.pattern.iname.";
	public static final String CRON_PATTERN_V_PREFIX = "app.quartzcron.pattern.v.";
	
	public static final String CRON_FIELD_IMESSAGE_PREFIX = "quartz.cron.fieldname.iname.";
	public static final String CRON_FIELD_UMESSAGE_PREFIX = "quartz.cron.fieldname.uname.";
	
	public static final String CRON_TEMPLATE_A_PREFIX = "quartz.cron.template.all";
	public static final String CRON_TEMPLATE_S_PREFIX = "quartz.cron.template.specified";
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SettingsInDb settingsInDb;
	
	@PostConstruct
	public void post() {
		String curName = "period_seconds";
		settingsInDb.getString(CRON_PATTERN_NAME_PREFIX + "[0]", curName);
		settingsInDb.getString(CRON_PATTERN_INAME_PREFIX + curName, "每隔几秒钟执行");
		settingsInDb.getString(CRON_PATTERN_V_PREFIX + curName, "0/5 * * * * ? *");
		
		curName = "period_minutes";
		settingsInDb.getString(CRON_PATTERN_NAME_PREFIX + "[1]", curName);
		settingsInDb.getString(CRON_PATTERN_INAME_PREFIX + curName, "每隔几分钟执行");
		settingsInDb.getString(CRON_PATTERN_V_PREFIX + curName, "1 3/5 * * * ? *");
		
		curName = "period_hours";
		settingsInDb.getString(CRON_PATTERN_NAME_PREFIX + "[2]", curName);
		settingsInDb.getString(CRON_PATTERN_INAME_PREFIX + curName, "每隔几小时执行");
		settingsInDb.getString(CRON_PATTERN_V_PREFIX + curName, "2 3 1/2 * * ? *");
		
		curName = "period_day_of_month";
		settingsInDb.getString(CRON_PATTERN_NAME_PREFIX + "[3]", curName);
		settingsInDb.getString(CRON_PATTERN_INAME_PREFIX + curName, "每隔几天执行");
		settingsInDb.getString(CRON_PATTERN_V_PREFIX + curName, "3 33 22 */1 * ? *");
	}
	
	private String getMessage(String defaultValue, String mn, Locale locale) {
		return messageSource.getMessage(mn, new Object[] {}, defaultValue, locale);
	}
	
	public QuartzCronBuilderContext getContext(Locale locale) {
		QuartzCronBuilderContext qcc = new QuartzCronBuilderContext();
		qcc.setAllTemplate(getMessage("", CRON_TEMPLATE_A_PREFIX, locale));
		qcc.setSpecifiedTemplate(getMessage("", CRON_TEMPLATE_S_PREFIX, locale));
		qcc.setCronFields(getFieldDefinitions(locale));
		qcc.setPatterns(getPredefinedCronPattern(locale));
		qcc.setMaxValueNumber(settingsInDb.getInteger("quartz.cron.max-value-number", 30));
		qcc.setNextTimeLabel(getMessage("Next Execute Time", "quartz.cron.template.nextimelabel", locale));
		qcc.setNextTimeUrl(settingsInDb.getString( "quartz.cron.nextimeUrl", QuartzCronExpressController.MAPPING_PATH + "/next"));
		return qcc;
	}
	
	// app.quartzcron.pattern.name[0] = period_seconds
	// app.quartzcron.pattern.iname.period_seconds_zh
	private List<PreDefinedCronPattern> getPredefinedCronPattern(Locale locale) {
		List<String> ptns = settingsInDb.getListString(CRON_PATTERN_NAME_PREFIX);
		return ptns.stream().map(pt -> {
			PreDefinedCronPattern pdc = new PreDefinedCronPattern(pt);
			String inamek = CRON_PATTERN_INAME_PREFIX + pt;
			String iname = settingsInDb.getStringByKeyaThenKeyb(inamek + "_" + locale.getLanguage(), inamek);
			if (iname.isEmpty()) {
				pdc.setIname(inamek + " does'nt exists.");
			} else {
				pdc.setIname(iname);
			}
			
			String cvaluek = CRON_PATTERN_V_PREFIX + pt;
			String cvalue = settingsInDb.getString(cvaluek);
			pdc.setCronValue(cvalue);
			return pdc;
		}).collect(Collectors.toList());
		
	}
	
	private List<QuartzCronField> getFieldDefinitions(Locale locale) {
		List<QuartzCronField> fieldDefinitions = Lists.newArrayList();
		
		QuartzCronField f = new QuartzCronField("seconds", true, "0-59", ", - * /");
		fieldDefinitions.add(f);
		
//		app.quartzcron.examples.seconds_zh[0]
		String k = CRON_EXAMPLES_PREFIX + f.getName();
		List<String> examples = settingsInDb.getListString(k + "_" + locale.getLanguage(), k);
		f.setExamples(examples);
		
		String mn = CRON_FIELD_IMESSAGE_PREFIX + f.getName();
		f.setIname(getMessage(f.getName(), mn, locale));
		String mn1 = CRON_FIELD_UMESSAGE_PREFIX + f.getName();
		f.setUname(getMessage(f.getName(), mn1, locale));
		

		f = new QuartzCronField("minutes", true, "0-59", ", - * /");
		fieldDefinitions.add(f);
		
		k = CRON_EXAMPLES_PREFIX + f.getName();
		examples = settingsInDb.getListString(k + "_" + locale.getLanguage(), k);
		f.setExamples(examples);

		mn = CRON_FIELD_IMESSAGE_PREFIX + f.getName();
		f.setIname(getMessage(f.getName(), mn, locale));
		mn1 = CRON_FIELD_UMESSAGE_PREFIX + f.getName();
		f.setUname(getMessage(f.getName(), mn1, locale));

		f = new QuartzCronField("hours", true, "0-23", ", - * /");
		fieldDefinitions.add(f);
		
		k = CRON_EXAMPLES_PREFIX + f.getName();
		examples = settingsInDb.getListString(k + "_" + locale.getLanguage(), k);
		f.setExamples(examples);

		mn = CRON_FIELD_IMESSAGE_PREFIX + f.getName();
		f.setIname(getMessage(f.getName(), mn, locale));
		mn1 = CRON_FIELD_UMESSAGE_PREFIX + f.getName();
		f.setUname(getMessage(f.getName(), mn1, locale));
		
		f = new QuartzCronField("dayOfMonth", true, "1-31", ", - * / ? L W");
		fieldDefinitions.add(f);
		
		k = CRON_EXAMPLES_PREFIX + f.getName();
		examples = settingsInDb.getListString(k + "_" + locale.getLanguage(), k);
		f.setExamples(examples);

		mn = CRON_FIELD_IMESSAGE_PREFIX + f.getName();
		f.setIname(getMessage(f.getName(), mn, locale));
		mn1 = CRON_FIELD_UMESSAGE_PREFIX + f.getName();
		f.setUname(getMessage(f.getName(), mn1, locale));
		
		f = new QuartzCronField("month", true, "1-12 or JAN-DEC", ", - * /");
		fieldDefinitions.add(f);
		k = CRON_EXAMPLES_PREFIX + f.getName();
		examples = settingsInDb.getListString(k + "_" + locale.getLanguage(), k);
		f.setExamples(examples);

		mn = CRON_FIELD_IMESSAGE_PREFIX + f.getName();
		f.setIname(getMessage(f.getName(), mn, locale));
		mn1 = CRON_FIELD_UMESSAGE_PREFIX + f.getName();
		f.setUname(getMessage(f.getName(), mn1, locale));
		
		f = new QuartzCronField("dayOfWeek", true, "1-7 or SUN-SAT", ", - * / ? L #");
		fieldDefinitions.add(f);
		k = CRON_EXAMPLES_PREFIX + f.getName();
		examples = settingsInDb.getListString(k + "_" + locale.getLanguage(), k);
		f.setExamples(examples);

		mn = CRON_FIELD_IMESSAGE_PREFIX + f.getName();
		f.setIname(getMessage(f.getName(), mn, locale));
		mn1 = CRON_FIELD_UMESSAGE_PREFIX + f.getName();
		f.setUname(getMessage(f.getName(), mn1, locale));
		
		f = new QuartzCronField("year", false, "1970-2099", ", - * /");
		fieldDefinitions.add(f);
		
		k = CRON_EXAMPLES_PREFIX + f.getName();
		examples = settingsInDb.getListString(k + "_" + locale.getLanguage(), k);
		f.setExamples(examples);
		
		mn = CRON_FIELD_IMESSAGE_PREFIX + f.getName();
		f.setIname(getMessage(f.getName(), mn, locale));
		mn1 = CRON_FIELD_UMESSAGE_PREFIX + f.getName();
		f.setUname(getMessage(f.getName(), mn1, locale));
		
		return fieldDefinitions;
	}

}
