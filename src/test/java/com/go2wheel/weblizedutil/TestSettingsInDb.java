package com.go2wheel.weblizedutil;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.go2wheel.weblizedutil.model.KeyValue;

public class TestSettingsInDb extends SpringBaseFort {
	
	@Before
	public void af() {
		clearDb();
		settingsIndb.getSingleValueLc().invalidateAll();
		settingsIndb.getListValueLc().invalidateAll();
	}
	
	/**
	 * order by keyname.
	 */
	@Test
	public void testGetList() {
		KeyValue kv = new KeyValue("a.b.c", "1");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c.d", "2");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c.d.e", "3");
		keyValueDbService.save(kv);
		
		List<String> ss = settingsIndb.getListString("a.b");
		assertThat(ss, contains("1", "2", "3"));
	}
	
	/**
	 * order by keyname.
	 */
	@Test
	public void testGetListKeyNameUnordered() {
		KeyValue kv = new KeyValue("a.b.c", "1");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c.d.e", "2");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c.d", "3");
		keyValueDbService.save(kv);
		List<String> ss = settingsIndb.getListString("a.b");
		assertThat(ss, contains("1", "3", "2"));
	}
	
	/**
	 * order by arraystyle.
	 */
	@Test
	public void testGetListKeyArrayStyle() {
		KeyValue kv = new KeyValue("a.b.c[0]", "1");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c[1]", "2");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c[2]", "3");
		keyValueDbService.save(kv);
		List<String> ss = settingsIndb.getListString("a.b");
		assertThat(ss, contains("1", "2", "3"));
	}
	
	
	/**
	 * When value changed in db should changed in cache.
	 */
	@Test
	public void testUpdateSingleValue() {
		KeyValue kv = new KeyValue("a", "1");
		kv = keyValueDbService.save(kv);
		int i = settingsIndb.getInteger("a");
		assertThat(i, equalTo(1));
		
		kv.setItemValue("2");
		keyValueDbService.save(kv);
		
		i = settingsIndb.getInteger("a");
		assertThat(i, equalTo(2));
	}
	
	/**
	 * when one of the list value changed, all cache should be updated.
	 */
	@Test
	public void testUpdateListValue() {
		KeyValue kv = new KeyValue("a.b.c[0]", "1");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c[1]", "2");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c[2]", "3");
		kv = keyValueDbService.save(kv);
		List<String> ss = settingsIndb.getListString("a.b");
		assertThat(ss, contains("1", "2", "3"));
		
		kv.setItemValue("4");
		keyValueDbService.save(kv);
		
		 ss = settingsIndb.getListString("a.b");
			assertThat(ss, contains("1", "2", "4"));
	}
	
	
	@Test
	public void testByThisOrThat() {
		KeyValue kv = new KeyValue("a.b.c[0]", "1");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c[1]", "2");
		keyValueDbService.save(kv);
		kv = new KeyValue("a.b.c[2]", "3");
		kv = keyValueDbService.save(kv);
		List<String> ss = settingsIndb.getListString("a.b.c_zh", "a.b.c");
		assertThat(ss, contains("1", "2", "3"));
		
	}

	
	
	@Test
	public void testGetString() {
		clearDb();
		String v = settingsIndb.getString("abc");
		assertThat(v, equalTo(""));
	}
	
	@Test
	public void testGetInt() {
		clearDb();
		int v = settingsIndb.getInteger("abc");
		assertThat(v, equalTo(0));
	}
	@Test
	public void testGetStringDefault() {
		clearDb();
		String v = settingsIndb.getString("abc", "hello");
		assertThat(v, equalTo("hello"));
	}
	
	@Test
	public void testGetIntDefault() {
		clearDb();
		int v = settingsIndb.getInteger("abc", 66);
		assertThat(v, equalTo(66));
	}


}
