package org.spatiallibs.jsi;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.SpatialIndex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spatiallibs.jsi.index.BuildIndex;
import org.spatiallibs.jsi.util.Utilities;
import org.spatiallibs.jsi.queries.Queries;

@State(Scope.Benchmark)
public class DistanceQueryScale {

	// numPoints in millions
	@Param({"10", "15", "20", "25", "30", "35", "40", "45", "50"})
	private String numPoints;

	// selectivity of a query
	@Param({"0.1"})
	private String selectivity;

	private List<Point> points;
	private List<Point> queryPoints;
	private List<Float> distances;
	private SpatialIndex rtree;
	private int index;

	@Setup
	public void setup() {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_rides.bin");
		queryPoints = Utilities.getQueryPoints(System.getProperty("user.dir") + "/resources/query_datasets/projected/taxi_distance_" + selectivity + ".csv");
		distances = Utilities.getDistances(System.getProperty("user.dir") + "/resources/query_datasets/projected/taxi_distance_" + selectivity + ".csv");
		rtree = BuildIndex.buildRtree(points);
		index = 0;
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Measurement(iterations = 5, time = 60)
	@Threads(value = 1)
	@Fork(value = 1)
	public void benchmark(Blackhole bh) {
		Point point = queryPoints.get(index);
		float distance = distances.get(index);
		List<Integer> result = Queries.distanceQuery(rtree, points.size(), points, point, distance);
		bh.consume(result);
		index = (index + 1) % points.size();
	}
}
