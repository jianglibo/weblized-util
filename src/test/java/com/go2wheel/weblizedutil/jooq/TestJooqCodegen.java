package com.go2wheel.weblizedutil.jooq;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.junit.Before;
import org.junit.Test;

public class TestJooqCodegen {

	
	public static final String JOOQ_CONFIG_FILE = "/jooq-config.xml";
	
	@Before
	public void be() throws IOException {
	}
	
	@Test
	public void codegen() throws Exception {
		Properties p = new Properties();
		try (InputStream is = Files.newInputStream(Paths.get("gradle.properties"))) {
			p.load(is);
		}
//		String jdbcUrl = p.getProperty("flyway.url");
//		String user = p.getProperty("flyway.user");
//		String pwd = p.getProperty("flyway.password");
//		String driver = p.getProperty("driver");
		try (InputStream in = TestJooqCodegen.class.getResourceAsStream(JOOQ_CONFIG_FILE)) {
			Configuration cfg = GenerationTool.load(in);
			GenerationTool.generate(cfg);
		}
//		cfg.setJdbc(new Jdbc().withUrl(jdbcUrl).withUser(user).withPassword(pwd).withDriver(driver));
		
	}
}
