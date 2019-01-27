package com.xum.zk.test;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞散列集合
 * 
 * @param <V>
 * @exclude
 */
public class LinkedBlockingMap<K, V> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6047657304860175592L;
	private Map<K, BlockingQueue<V>> map = new ConcurrentHashMap<K, BlockingQueue<V>>();

	/**
	 * 获取key对应的value(如果key对应的value值不存在，将阻塞等待)
	 * 
	 * @param key
	 * @return 返回获取后的value值
	 * @throws InterruptedException
	 */
	public V get(K key) throws InterruptedException {
		BlockingQueue<V> queue = initAndGetQueue(key);
		return queue.take();
	}

	/**
	 * 获取key对应的value(如果key对应的value值不存在，将阻塞等待指定的时间)
	 * 
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return 返回获取后的value值
	 * @throws InterruptedException
	 */
	public V get(K key, long timeout, TimeUnit unit) throws InterruptedException {
		BlockingQueue<V> queue = initAndGetQueue(key);
		return queue.poll(timeout, unit);
	}

	/**
	 * 添加指定的key和value至散列集合
	 * 
	 * @param key
	 * @param value
	 * @return 返回添加后的值
	 */
	public V put(K key, V value) {
		BlockingQueue<V> queue = initAndGetQueue(key);
		synchronized (queue) {
			queue.clear();
			queue.add(value);
		}
		return value;
	}

	/**
	 * 删除指定的key对应的value
	 * 
	 * @param key
	 */
	public void remove(K key) {
		map.remove(key);
	}

	/**
	 * 清空
	 * 
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * 判断集合中是否包含指定的key值所对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(K key) {
		// 不能用contantsKey来判断，因为key对应的如果是一个空的LinkedBlockingQueue，那么contantsKey返回true，实际上是不对的
		BlockingQueue<V> v = map.get(key);
		if (v == null || v.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 返回集合长度
	 * 
	 * @return
	 */
	public int size() {
		return map.size();
	}

	/**
	 * @param key
	 * @return
	 */
	private BlockingQueue<V> initAndGetQueue(K key) {
		if (!map.containsKey(key)) {
			synchronized (map) {
				if (!map.containsKey(key)) {
					map.put(key, new LinkedBlockingQueue<V>(1));
				}
			}
		}
		return map.get(key);
	}

	@Override
	public String toString() {
		return map.toString();
	}

}
