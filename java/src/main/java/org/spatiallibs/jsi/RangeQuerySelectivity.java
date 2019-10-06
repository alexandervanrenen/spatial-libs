package org.spatiallibs.jsi;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.SpatialIndex;

import org.spatiallibs.jsi.index.BuildIndex;
import org.spatiallibs.jsi.util.Utilities;
import org.spatiallibs.jsi.queries.Queries;

@State(Scope.Benchmark)
public class RangeQuerySelectivity {

	// numPoints in millions
	@Param({"50"})
	private String numPoints;

	// selectivity of a query
	@Param({"0.0001", "0.001", "0.01", "0.1", "1", "10", "100"})
	private String selectivity;

	private List<Point> points;
	private List<Rectangle> rects;
	private SpatialIndex rtree;
	private int index;

	@Setup
	public void setup() {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_rides.bin");
		rects = Utilities.getEnvelopes(System.getProperty("user.dir") + "/resources/query_datasets/projected/taxi_range_" + selectivity + ".csv");
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
		Rectangle rect = rects.get(index);
		List<Integer> result = Queries.rangeQuery(rtree, rect, points.size(), points);
		bh.consume(result);
		index = (index + 1) % rects.size();
	}
}
