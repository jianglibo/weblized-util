package com.go2wheel.weblizedutil.value;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.go2wheel.weblizedutil.model.KeyValue;

public class KeyValueProperties extends Properties {
	
	private String prefix = "";
	
	private List<KeyValue> keyvalues;
	
	private KeyValueProperties next;

	public void setNext(KeyValueProperties next) {
		this.next = next;
	}
	public KeyValueProperties getNext() {
		return next;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public KeyValueProperties() {}

	public KeyValueProperties(List<KeyValue> kvs, String prefix) {
		super();
		this.prefix = prefix;
		parseKvs(kvs);
	}

	private void parseKvs(List<KeyValue> kvs) {
		this.keyvalues = kvs;
		int len = prefix.length() == 0 ? 0 : prefix.length() + 1;
		kvs.forEach(kv -> put(kv.getItemKey().substring(len), kv.getItemValue()));
	}
	
	public Optional<KeyValue> getKeyValue(String relativeKey) {
		String key = prefix.isEmpty() ? relativeKey : prefix + "." + relativeKey;
		return keyvalues.stream().filter(kv -> key.equals(kv.getItemKey())).findAny();
	}

	public List<String> getRelativeValueList(String relativePrefix) {
		final String kp = relativePrefix + "[";
		return keySet().stream().map(Objects::toString).filter(k -> k.startsWith(kp))
				.map(k -> getProperty(k))
				.collect(Collectors.toList());
	}

	public Map<String, String> getRelativeMap(String relativePrefix) {
		final int pl = relativePrefix.length() + 1;
		return keySet().stream().map(Objects::toString).filter(k -> k.startsWith(relativePrefix))
				.collect(Collectors.toMap(k -> k.substring(pl), this::getProperty));
	}
	
	@Override
	public String getProperty(String key) {
		String v =  super.getProperty(key);
		if (v == null && next != null) {
			return next.getProperty(key);
		}
		return v;
	}
	
	@Override
	public String getProperty(String key, String defaultValue) {
		String v = getProperty(key);
		if (v == null) {
			return defaultValue;
		}
		return v;
	}
	
	public Integer getInteger(String key) {
		String v = getProperty(key);
		if (v == null) {
			return 0;
		} else {
			return Integer.parseInt(v);
		}
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<KeyValue> getKeyvalues() {
		return keyvalues;
	}

	public void setKeyvalues(List<KeyValue> keyvalues) {
		parseKvs(keyvalues);
	}
	public List<KeyValueProperties> getListOfKVP(String relativePrefix) {
		final Pattern kp = Pattern.compile(relativePrefix + "\\[\\(d+)\\](.*$)");
		
		Map<String, List<IdxAndKv>> idToKvs =  keySet().stream().map(Objects::toString)
				.map(k -> {
					Matcher m = kp.matcher(k);
					if (m.matches()) {
						String nk = m.group(2);
						if (nk.length() > 0) {
							if (nk.charAt(0) == '.') {
								nk = nk.substring(2);
							}
							return new IdxAndKv(m.group(1), new KeyValue(m.group(2), getProperty(k)));
						}
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(idk -> idk.getIdx()));
		
	}
	
	protected static class IdxAndKv {
		private String idx;
		private KeyValue kv;
		
		public IdxAndKv(String idx, KeyValue kv) {
			super();
			this.idx = idx;
			this.kv = kv;
		}
		public String getIdx() {
			return idx;
		}
		public void setIdx(String idx) {
			this.idx = idx;
		}
		public KeyValue getKv() {
			return kv;
		}
		public void setKv(KeyValue kv) {
			this.kv = kv;
		}
	}
	
}
