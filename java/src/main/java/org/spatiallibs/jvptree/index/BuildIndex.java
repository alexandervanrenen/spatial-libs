package org.spatiallibs.jvptree.index;

import java.util.List;

import com.eatthepath.jvptree.VPTree;

import org.spatiallibs.jvptree.cartesianpoint.CartesianPoint;
import org.spatiallibs.jvptree.cartesianpoint.CartesianDistanceFunction;
import org.spatiallibs.jvptree.cartesianpoint.SimpleCartesianPoint;

public class BuildIndex {

	public static VPTree<CartesianPoint, SimpleCartesianPoint> buildVpTree(List<SimpleCartesianPoint> points) {
		VPTree vptree = new VPTree<CartesianPoint, SimpleCartesianPoint>(new CartesianDistanceFunction());
		vptree.addAll(points);
		return vptree;
	}
}
