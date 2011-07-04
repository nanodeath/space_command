package com.wasome.space_command.util;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.wasome.space_command.Entity;

public class ZIndexComparatorTest {

	private Entity e1, e2, e3, e3b;

	@Before
	public void setupEntities(){
		e1 = new Entity(){};
		e1.setZIndex(0);
		e2 = new Entity(){};
		e2.setZIndex(1);
		e3 = new Entity(){};
		e3.setZIndex(2);
		e3b = new Entity(){};
		e3b.setZIndex(2);
	}
	
	@Test
	public void initiallyOrdered() {
		Set<Entity> set = new TreeSet<Entity>(new ZIndexEntityComparator());
		set.add(e1);
		set.add(e2);
		set.add(e3);
		Iterator<Entity> it = set.iterator();
		assertEquals(e1, it.next());
		assertEquals(e2, it.next());
		assertEquals(e3, it.next());
	}
	
	@Test
	public void initiallyUnordered(){
		Set<Entity> set = new TreeSet<Entity>(new ZIndexEntityComparator());
		set.add(e3);
		set.add(e1);
		set.add(e2);
		Iterator<Entity> it = set.iterator();
		assertEquals(e1, it.next());
		assertEquals(e2, it.next());
		assertEquals(e3, it.next());
	}

	@Test
	public void sameZ(){

		Set<Entity> set = new TreeSet<Entity>(new ZIndexEntityComparator());
		set.add(e3);
		set.add(e1);
		set.add(e3b);
		Iterator<Entity> it = set.iterator();
		assertEquals(e1, it.next());
		assertEquals(e3, it.next());
		assertEquals(e3b, it.next());
	}
}
