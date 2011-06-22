package com.wasome.space_command.util;

import com.wasome.space_command.data.Point;

public class PointUtil {
	/**
	 * (x?,y?)=(x+dcosα, y+dsinα) α = arccos((x? - x)/d) = arcsin((y? - y)/d)
	 * 
	 * @see http://www.physicsforums.com/showpost.php?p=1822287
	 */
	public static Point<Float> rotateAbout(Point<Float> center,
			Point<Float> other, float angleInRadians, boolean CCW) {
		float distance = Point.distanceBetween(center, other);
		float x, y;
		if (distance == 0) {
			x = center.x;
			y = center.y;
		} else {
			float currentAngle = (float) Math.atan2(other.y - center.y, other.x - center.x);
			
			float desiredAngle;
			if(CCW){
				desiredAngle = currentAngle + angleInRadians;
			} else {
				desiredAngle = currentAngle - angleInRadians;
			}
			x = (float) (center.x + distance * Math.cos(desiredAngle));
			y = (float) (center.y + distance * Math.sin(desiredAngle));
		}

		return new Point<Float>(x, y);
	}
}
