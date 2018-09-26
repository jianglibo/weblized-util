package com.go2wheel.weblizedutil.job;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.go2wheel.weblizedutil.SpringBaseFort;
import com.go2wheel.weblizedutil.service.QuartzCronBuilderService;
import com.go2wheel.weblizedutil.value.QuartzCronBuilderContext;
import com.go2wheel.weblizedutil.value.QuartzCronField;

public class TestQuartzCronBuilderService extends SpringBaseFort {
	
	
	@Autowired
	private QuartzCronBuilderService quartzCronBuilderService;
	
	@Test
	public void tInit() {
		QuartzCronBuilderContext qcc = quartzCronBuilderService.getContext(Locale.CANADA_FRENCH);
		List<QuartzCronField> fields = qcc.getCronFields();
		
		assertThat(fields.size(), equalTo(7));
		
		fields.stream().forEach(f -> {
			assertNotNull(f.getIname());
			assertNotNull(f.getUname());
			
			assertTrue(f.getIname().length() > 0);
			assertTrue(f.getUname().length() > 0);
		});
	}

}
