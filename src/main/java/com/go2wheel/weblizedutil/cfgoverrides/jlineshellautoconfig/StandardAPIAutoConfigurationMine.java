package com.go2wheel.weblizedutil.cfgoverrides.jlineshellautoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.ValueProvider;

import com.go2wheel.weblizedutil.valueprovider.CronStringValueProvider;
import com.go2wheel.weblizedutil.valueprovider.DbTableNameProvider;
import com.go2wheel.weblizedutil.valueprovider.DefaultValueProvider;
import com.go2wheel.weblizedutil.valueprovider.FileValueProviderMine;
import com.go2wheel.weblizedutil.valueprovider.JobKeyProvider;
import com.go2wheel.weblizedutil.valueprovider.KeyValueValueProvider;
import com.go2wheel.weblizedutil.valueprovider.ObjectFieldValueProvider;
import com.go2wheel.weblizedutil.valueprovider.OstypeProvider;
import com.go2wheel.weblizedutil.valueprovider.PossibleValueProvider;
import com.go2wheel.weblizedutil.valueprovider.SQLCandiatesValueProvider;
import com.go2wheel.weblizedutil.valueprovider.TemplateValueProvider;
import com.go2wheel.weblizedutil.valueprovider.TriggerKeyProvider;
import com.go2wheel.weblizedutil.valueprovider.UserAccountValueProvider;

/**
 * copy from {@link StandardAPIAutoConfiguration}
 * @author jianglibo@gmail.com
 *
 */

@Configuration
public class StandardAPIAutoConfigurationMine {

	@Bean
	public ValueProvider keyValueValueProvider() {
		return new KeyValueValueProvider();
	}
	
	@Bean
	public ValueProvider candidatesFromSQLProvider() {
		return new SQLCandiatesValueProvider();
	}
	
	
	@Bean
	public ValueProvider ostypeValueProvider() {
		return new OstypeProvider();
	}
	
	@Bean
	public ValueProvider cronStringValueProvider() {
		return new CronStringValueProvider();
	}

	@Bean
	public ValueProvider userAccountValueProvider() {
		return new UserAccountValueProvider();
	}
	
	@Bean
	public ValueProvider tableNameValueProvider() {
		return new DbTableNameProvider();
	}

	@Bean
	public ValueProvider fileValueProvider() {
		return new FileValueProviderMine();
	}
	
	
	@Bean
	public ValueProvider TemplateProvider() {
		return new TemplateValueProvider();
	}

	
	@Bean
	public ValueProvider objectFieldValueProvider() {
		return new ObjectFieldValueProvider();
	}

	@Bean
	public ValueProvider defaultValueProvider() {
		return new DefaultValueProvider();
	}
	
	@Bean
	public ValueProvider possibleValueProvider() {
		return new PossibleValueProvider();
	}
	

	@Bean
	public ValueProvider triggerKeyProvider() {
		return new TriggerKeyProvider();
	}

	@Bean
	public ValueProvider jobKeyProvider() {
		return new JobKeyProvider();
	}

}
