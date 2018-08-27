package com.go2wheel.weblizedutil.value;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.go2wheel.weblizedutil.model.KeyValue;

public class TestKeyValueProperties {
	
	/**
	 * kv => a.b = c
	 */
	@Test
	public void tSingleValue() {
		String key = "a.b";
		KeyValueProperties kvp = new KeyValueProperties();
		
		List<KeyValue> kvs = Arrays.asList(new KeyValue(key, "c"));
		kvp.setKeyvalues(kvs);
		assertThat(kvp.getProperty("a.b"), equalTo("c"));
		
		Optional<KeyValue> kv = kvp.getKeyValue("a.b");
		assertTrue(kv.isPresent());
	}
	
	/**
	 * kv = > a.b = c, when constructing kvp with a prefix, the property key will be chopped.
	 */
	@Test
	public void tPropertyWithPrefix() {
		String key = "a.b";
		List<KeyValue> kvs = Arrays.asList(new KeyValue(key, "c"));
		
		KeyValueProperties kvp = new KeyValueProperties(kvs, "a");
		assertThat(kvp.getProperty("b"), equalTo("c"));
	}
	
	
	@Test
	public void tGetMap() {
		String prefix = "a.b.c";
		List<KeyValue> kvs = Arrays.asList(
				new KeyValue(prefix, "x", "1"),
				new KeyValue(prefix, "y", "2"),
				new KeyValue(prefix, "z", "3")
				);
		KeyValueProperties kvp = new KeyValueProperties(kvs, "a");
		assertThat(kvp.getProperty("b.c.x"), equalTo("1"));
		
		Map<String, String> subKvp = kvp.getRelativeMap("b");
		assertThat(subKvp.size(), equalTo(3));
		String mk =subKvp.keySet().iterator().next(); 
		assertTrue("should start with c..", mk.startsWith("c."));
	}
	
//	menus.groups[0].name=g2
//	menus.groups[0].order=1000
//	menus.groups[0].items[0].name=menu.home
//	menus.groups[0].items[0].order=1
//	menus.groups[0].items[0].path=/
	
	@Test
	public void tOjbectList() {
		String prefix = "menus";
		List<KeyValue> kvs = Arrays.asList(
				new KeyValue("menus.groups[0].name", "g2"),
				new KeyValue("menus.groups[0].order", "1000"),
				new KeyValue("menus.groups[0].items[0].name", "menu.home"),
				new KeyValue("menus.groups[0].items[0].order", "1"),
				new KeyValue("menus.groups[0].items[0].path", "/"),
				new KeyValue("menus.groups[1].name", "g3"),
				new KeyValue("menus.groups[1].order", "1001"),
				new KeyValue("menus.groups[1].items[0].name", "menu.home1"),
				new KeyValue("menus.groups[1].items[0].order", "2"),
				new KeyValue("menus.groups[1.items[0].path", "//")
				);
		KeyValueProperties kvp = new KeyValueProperties(kvs, "");
		
		List<KeyValueProperties> kvl = kvp.getListOfKVP("menus.groups");
		
		assertThat(kvl.size(), equalTo(2));
		
		assertThat(kvl.get(0).getProperty("name"), equalTo("g2"));
		assertThat(kvl.get(1).getProperty("name"), equalTo("g3"));
		
		kvl = kvl.get(0).getListOfKVP("items");
		
		assertThat(kvl.size(), equalTo(1));
		assertThat(kvl.get(0).getProperty("name"), equalTo("menu.home"));
	}
	
	
	@Test
	public void tGetList() {
		String prefix = "a.b";
		List<KeyValue> kvs = Arrays.asList(
				new KeyValue(prefix, "c[0]", "1"),
				new KeyValue(prefix, "c[2]", "3"),
				new KeyValue(prefix, "c[1]", "2"),
				new KeyValue(prefix, "d", "4")
				);
		KeyValueProperties kvp = new KeyValueProperties(kvs, "a.b");
		assertThat(kvp.getProperty("d"), equalTo("4"));
		
		List<String> list = kvp.getRelativeValueList("c");
		assertThat(list.size(), equalTo(3));

		assertThat(list, contains("1", "2", "3"));
	}

}
