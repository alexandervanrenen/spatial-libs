package org.spatiallibs.jts.kdtree;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.index.kdtree.KdTree;

import org.spatiallibs.jts.index.BuildIndex;
import org.spatiallibs.jts.util.Utilities;
import org.spatiallibs.jts.queries.Queries;

import org.spatiallibs.common.ResourceLoader;

@State(Scope.Benchmark)
public class JoinQuerySelectivity {

	// numPoints in millions
	@Param({"50"})
	private String numPoints;

	// selectivity of a query
	@Param({"50","100","150","200","250","289"})
	private String selectivity;

	private List<Geometry> points;
	private List<Geometry> polygons;
	private List<PreparedGeometry> preparedPolygons;

	private KdTree kdtreePoints;

	@Setup
	public void setup() {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + "/resources/datasets/projected/binary/java/" + numPoints + "M_rides.bin");
		polygons = Utilities.readPolygons(ResourceLoader.readFileInList(System.getProperty("user.dir") + "/resources/datasets/neighborhoods/projected/polygons_" + selectivity + ".csv"));
		preparedPolygons = BuildIndex.buildPreparedGeometries(polygons);
		kdtreePoints = BuildIndex.buildKdtree(points);
		kdtreePoints.isEmpty();
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Measurement(iterations = 5, time = 60)
	@Threads(value = 1)
	@Fork(value = 1)
	public void kdtreeOnPointsJoinBenchmark(Blackhole blackhole) {
		int[] count = Queries.kdtreeOnPointsJoin(kdtreePoints, points, polygons, preparedPolygons);
		blackhole.consume(count);
	}
}
