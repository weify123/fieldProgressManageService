package com.autoyol.field.progress.manage.util;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.*;

public class CollectionUtil<T> {

	/**
	 * 并集
	 *
	 * @param field1
	 * @param field2
	 * @param type
	 * @return
	 */
	public T[] Union(T[] field1, T[] field2, Class<T> type) {
		Set<T> set = new HashSet<T>();
		if (field2 != null) {
			set.addAll(Arrays.asList(field2));
		}
		if (field1 != null) {
			for (T f1 : field1) {
				if (!set.contains(f1)) {
					set.add(f1);
				}
			}
		}
		return toArray(set, type);
	}

	/**
	 * 交集
	 *
	 * @param field1
	 * @param field2
	 * @param type
	 * @return
	 */
	public T[] intersection(T[] field1, T[] field2, Class<T> type) {
		Set<T> set = new HashSet<T>();
		Set<T> ret = new HashSet<T>();
		if (field2 != null) {
			set.addAll(Arrays.asList(field2));
		}
		if (field1 != null) {
			for (T f1 : field1) {
				if (set.contains(f1)) {
					ret.add(f1);
				}
			}
		}
		return toArray(ret, type);
	}

	@SuppressWarnings("unchecked")
	private T[] toArray(Collection<T> c, Class<T> type) throws NullPointerException{
		if(type == null){
			throw new NullPointerException("Collection Class is null !");
		}
		T[] array = (T[]) Array.newInstance(type, c.size());
		Iterator<T> iterator = c.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			array[i++] = iterator.next();
		}
		return array;
	}


	  public List<List<T>> divideIntoGroups(List<T> list, int groupSize) {
	        if (CollectionUtils.isEmpty(list)) {
	            return null;
	        }

	        List<List<T>> groups = new ArrayList<List<T>>();

	        List<T> group = null;
	        int totalSize = list.size();

	         for (int i = 0; i < totalSize; i++) {
	             if (i % groupSize == 0) {
	                 group = new ArrayList<T>();
	                 groups.add(group);
	             }
	             group.add(list.get(i));
	         }
	         return groups;
	     }

}
