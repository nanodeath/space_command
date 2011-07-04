package com.wasome.space_command;

import java.util.Comparator;

/**
 * Sort entities by their z-index. In the case of a tie, the order is consistent
 * but undefined and subject to change.
 * 
 * @author max
 * 
 */
public class ZIndexEntityComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity arg0, Entity arg1) {
		float z0 = arg0.getZIndex(), z1 = arg1.getZIndex();
		if (z0 < z1) {
			return -1;
		} else if (z0 > z1) {
			return 1;
		}
		if (!arg0.equals(arg1)) {
			if (arg0.entityId < arg1.entityId) {
				return -1;
			} else if (arg0.entityId > arg1.entityId) {
				return 1;
			} else {
				throw new RuntimeException("Different objects with the same entityId? What?!");
			}
		}
		return 0;
	}
}
