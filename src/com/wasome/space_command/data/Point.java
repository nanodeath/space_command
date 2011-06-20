package com.wasome.space_command.data;

public final class Point<T> {
	public final T x, y;
	public Point(T x, T y){
		this.x = x;
		this.y = y;
	}
	
	
	
	@Override
	public String toString() {
		return String.format("Point [x=%s, y=%s]", x, y);
	}



	public static float distanceBetween(Point<Float> pointA, Point<Float> pointB){
		float a = pointB.x - pointA.x;
		float b = pointB.y - pointA.y;
		return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}
}
