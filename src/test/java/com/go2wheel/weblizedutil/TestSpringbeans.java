package com.go2wheel.weblizedutil;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Import(com.go2wheel.weblizedutil.TestSpringbeans.Tcc.class)
public class TestSpringbeans extends SpringBaseFort {

	@Autowired
	private Tenv tv3;

	@Autowired
	@Qualifier("amc")
	private Mc amc;

	/**
	 * By default, Spring will be searching for an associated thread pool
	 * definition: either a unique org.springframework.core.task.TaskExecutor bean
	 * in the context, or an java.util.concurrent.Executor bean named "taskExecutor"
	 * otherwise. If neither of the two is resolvable, a
	 * org.springframework.core.task.SimpleAsyncTaskExecutor will be used to process
	 */
	@Test
	public void tAsyncExecutor() {
		String[] executors = applicationContext.getBeanNamesForType(TaskExecutor.class);
		String[] jexecutors = applicationContext.getBeanNamesForType(Executor.class);
		String[] sexecutors = applicationContext.getBeanNamesForType(SimpleAsyncTaskExecutor.class);
		
		assertThat(executors.length, equalTo(0));
		assertThat(jexecutors.length, equalTo(0));
		assertThat(sexecutors.length, equalTo(0));
	}
	
	@Test
	public void thymeLeafResolver() {
		String[] executors = applicationContext.getBeanNamesForType(ITemplateResolver.class);
		Map<String, ITemplateResolver> beans = applicationContext.getBeansOfType(ITemplateResolver.class);
		assertThat(executors.length, equalTo(2));
		assertThat(beans.size(), equalTo(2));
		
		String[] viewresovlers = applicationContext.getBeanNamesForType(ViewResolver.class);
		assertThat(viewresovlers.length, equalTo(4));
	}

	@Test
	public void t() {
		Tenv tv1 = applicationContext.getBean("abean", Tenv.class);
		Tenv tv2 = applicationContext.getBean("bbean", Tenv.class);
		assertThat(tv1, equalTo(tv2));
		assertThat(tv2, equalTo(tv3));
	}
	
	@Test
	public void testLocaleResolver() {
		LocaleResolver lr = applicationContext.getBean(LocaleResolver.class);
		
		assertNotNull(lr);
		
	}

	
	@Test
	public void testConvertService() {
		Map<String, ConversionService> sc = applicationContext.getBeansOfType(ConversionService.class);
		assertTrue("should contain mvcConversionService", sc.containsKey("mvcConversionService"));
		assertTrue("should contain shellConversionService", sc.containsKey("shellConversionService"));
	}

	@TestConfiguration
	public static class Tcc {
		@Bean(name = { "abean", "bbean" })
		public Tenv tenv() {
			return new Tenv();
		}

		@Bean(name = "amc")
		public Mc mc() {
			return new Mc();
		}

		@Bean(name = "bmc")
		public Mc mc1() {
			return new Mc();
		}
	}

	public static class Mc {
		private int i = 0;

		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}
	}

	public static class Tenv {

		@Autowired
		private Environment env;

		private String oneStr = "hello";

		public List<String> getAslist() {
			String base = "ppp.aslist";
			List<String> values = new ArrayList<>();
			int i = 0;
			String v;
			while ((v = env.getProperty("ppp.aslist[" + i + "]")) != null) {
				values.add(v);
				i++;
			}
			return values;
		}

		public String getOneStr() {
			return oneStr;
		}

		public void setOneStr(String oneStr) {
			this.oneStr = oneStr;
		}

	}

}
