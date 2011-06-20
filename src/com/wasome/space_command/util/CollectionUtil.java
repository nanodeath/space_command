package com.wasome.space_command.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.Predicate;

final public class CollectionUtil {
	/**
	 * Partition a collection into a number of different sets. If you provide n
	 * predicates, the return set will have n+1 sets, where the last set is
	 * everything that didn't match any previous predicate. Predicates are
	 * evaluated in the order they're passed into the method, and each element
	 * will show up in the return value exactly once.
	 * 
	 * @param <T>
	 * @param collection
	 * @param pred
	 * @return List of lists, where the first list represents the values
	 *         matching the first partition, second list matching the second
	 *         list, etc.
	 */
	public static final <T> List<List<T>> partition(Collection<T> collection,
			Predicate... pred) {
		List<List<T>> ret = new ArrayList<List<T>>(pred.length + 1);
		for(int i = 0; i < pred.length + 1; i++){
			ret.add(new LinkedList<T>());
		}
		for (T element : collection) {
			boolean accountedFor = false;
			for (int i = 0; i < pred.length; i++) {
				if (pred[i].evaluate(element)) {
					accountedFor = true;
					ret.get(i).add(element);
					break;
				}
			}
			if (!accountedFor) {
				ret.get(pred.length).add(element);
			}
		}
		return ret;
	}
}
