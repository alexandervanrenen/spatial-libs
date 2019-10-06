package org.spatiallibs.jts.strtree;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.strtree.STRtree;

import org.spatiallibs.jts.index.BuildIndex;
import org.spatiallibs.jts.util.Utilities;
import org.spatiallibs.jts.queries.Queries;

@State(Scope.Benchmark)
public class DistanceQueryScale {

	// numPoints in millions
	@Param({"10", "15", "20", "25", "30", "35", "40", "45", "50"})
	private String numPoints;

	// selectivity of a query
	@Param({"0.1"})
	private String selectivity;

	private List<Geometry> points;
	private List<Geometry> circles;
	private List<Double> distances;
	private Object[] obj;
	private STRtree strtree;
	private int idx;

	@Setup
	public void setup() {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_rides.bin");
		obj = Utilities.getQueryPointsAndDistances(System.getProperty("user.dir") + "/resources/query_datasets/projected/taxi_distance_" + selectivity + ".csv");
		circles = (List<Geometry>) obj[0];
		distances = (List<Double>) obj[1];
		strtree = BuildIndex.buildRtree(points);
		strtree.size();
		idx = 0;
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Measurement(iterations = 5, time = 60)
	@Threads(value = 1)
	@Fork(value = 1)
	public void benchmark(Blackhole blackhole) {
		Geometry circle = circles.get(idx);
		double distance = distances.get(idx);
		List<Object> result = Queries.distanceQueryRtree(strtree, circle, points, distance);
		blackhole.consume(result);
		idx = (idx + 1) % circles.size();
	}
}
