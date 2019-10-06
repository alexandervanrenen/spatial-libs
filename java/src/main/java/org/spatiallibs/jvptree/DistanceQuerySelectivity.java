package org.spatiallibs.jvptree;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.eatthepath.jvptree.VPTree;

import org.spatiallibs.jvptree.util.Utilities;
import org.spatiallibs.jvptree.queries.Queries;
import org.spatiallibs.jvptree.index.BuildIndex;
import org.spatiallibs.jvptree.cartesianpoint.CartesianPoint;
import org.spatiallibs.jvptree.cartesianpoint.SimpleCartesianPoint;

@State(Scope.Benchmark)
public class DistanceQuerySelectivity {

	// numPoints in millions
	@Param({"50"})
	private String numPoints;

	// selectivity of a query
	@Param({"0.0001", "0.001", "0.01", "0.1", "1", "10", "100"})
	private String selectivity;

	private List<SimpleCartesianPoint> points;
	private List<SimpleCartesianPoint> distanceQueryPoints;
	private List<Double> distances;
	private VPTree<CartesianPoint, SimpleCartesianPoint> vptree;
	private int idx;

	@Setup
	public void setup() {
		points = Utilities.readBinaryCartesianPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_rides.bin");
		distanceQueryPoints = Utilities.getQueryPoints(System.getProperty("user.dir") + "/resources/query_datasets/projected/taxi_distance_" + selectivity + ".csv");
		distances = Utilities.getDistances(System.getProperty("user.dir") + "/resources/query_datasets/projected/taxi_distance_" + selectivity + ".csv");
		vptree = BuildIndex.buildVpTree(points);
		idx = 0;
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Measurement(iterations = 5, time = 60)
	@Threads(value = 1)
	@Fork(value = 1)
	public void benchmark(Blackhole blackhole) {
		double distance = distances.get(idx);
		SimpleCartesianPoint distanceQueryPoint = distanceQueryPoints.get(idx);
		List<SimpleCartesianPoint> result = Queries.distanceQuery(vptree, distanceQueryPoint, distance);
		blackhole.consume(result);
		idx = (idx + 1) % distanceQueryPoints.size();
	}
}
