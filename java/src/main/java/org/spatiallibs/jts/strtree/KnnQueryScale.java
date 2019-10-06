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
public class KnnQueryScale {

	// numPoints in millions
	@Param({"10", "15", "20", "25", "30", "35", "40", "45", "50"})
	private String numPoints;

	// selectivity of a query
	@Param({"10"})
	private int k;

	private List<Geometry> points;
	private List<Geometry> queryPoints;
	private STRtree strtree;
	private int idx;

	@Setup
	public void setup() {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_rides.bin");
		queryPoints = Utilities.getQueryPoints(System.getProperty("user.dir") + "/resources/query_datasets/projected/knn_points.csv");
		strtree = BuildIndex.buildRtreePoints(points);
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
		Geometry point = queryPoints.get(idx);
		Object[] result = Queries.knnQuery(strtree, point, k);
		blackhole.consume(result);
		idx = (idx + 1) % queryPoints.size();
	}
}
