package com.vonchange.mybatis.common.util.map;

import java.util.Map;


/**
 *支持链式调用的HashMap<String, Object>
 * @author von_change@163.com
 * @date 2015-6-14 下午10:38:53
 */
public class MyHashMap extends HashMap<String, Object> implements Map<String, Object>{


	public MyHashMap() {
	}

	public MyHashMap(Map<String, Object> map) {
		if (null == map) {
			map = new MyHashMap();
		}
		for (Entry<String, Object> entry : map.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
	}
    @Override
	public MyHashMap set(String key, Object value) {
		super.put(key, value);
		return this;
	}
	public MyHashMap setAll(Map<String,Object> map) {
		super.putAll(map);
		return this;
	}
	public MyHashMap put(String key, Object value) {
		return this.set(key, value);
	}
}
