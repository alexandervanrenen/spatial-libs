package org.spatiallibs.jsi;
  
import java.util.List;

import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.SpatialIndex;

import org.spatiallibs.jsi.index.BuildIndex;
import org.spatiallibs.jsi.util.Utilities;

import org.openjdk.jol.info.GraphLayout;

public class IndexSize {

	private static List<Point> points;
	private static SpatialIndex rtree;

	public static void index_size(String dataset, String numPoints) {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_" + dataset + ".bin");
		rtree = BuildIndex.buildRtree(points);
		System.out.println(GraphLayout.parseInstance(rtree).toFootprint());
	}

	public static void main(String[] args) {
		index_size(args[0], args[1]);
	}
}
