package org.spatiallibs.esrigeometry;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.esri.core.geometry.Envelope2D;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.QuadTree;

import org.spatiallibs.esrigeometry.index.BuildIndex;
import org.spatiallibs.esrigeometry.util.Utilities;
import org.spatiallibs.esrigeometry.queries.Queries;

@State(Scope.Benchmark)
public class RangeQueryScale {

	// numPoints in millions
	@Param({"10", "15", "20", "25", "30", "35", "40", "45", "50"})
	private String numPoints;

	// selectivity of a query
	@Param({"0.1"})
	private String selectivity;

	private List<Geometry> points;
	private List<Envelope2D> envs;
	private QuadTree quadtree;
	private int index;

	@Setup
	public void setup() {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_rides.bin");
		envs = Utilities.getEnvelopes(System.getProperty("user.dir") + "/resources/query_datasets/projected/taxi_range_" + selectivity + ".csv");
		quadtree = BuildIndex.buildQuadtree(points, 18);
		quadtree.getElementCount();
		index = 0;
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Measurement(iterations = 5, time = 60)
	@Threads(value = 1)
	@Fork(value = 1)
	public void benchmark(Blackhole bh) {
		Envelope2D env = envs.get(index);
		List<Geometry> result = Queries.rangeQuery(quadtree, env, points);
		bh.consume(result);
		index = (index + 1) % envs.size();
	}
}
