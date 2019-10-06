package org.spatiallibs.esrigeometry;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.QuadTree;

import org.spatiallibs.esrigeometry.index.BuildIndex;
import org.spatiallibs.esrigeometry.util.Utilities;
import org.spatiallibs.esrigeometry.queries.Queries;

import org.spatiallibs.common.ResourceLoader;

@State(Scope.Benchmark)
public class JoinQueryScale {

	// numPoints in millions
	@Param({"10", "15", "20", "25", "30", "35", "40", "45", "50"})
	private String numPoints;

	// selectivity of a query
	@Param({"289"})
	private String selectivity;

	private List<Geometry> points;
	private List<Geometry> polygons;
	private QuadTree quadtree;

	@Setup
	public void setup() {
		points = Utilities.readBinaryPoints(System.getProperty("user.dir") + 
							"/resources/datasets/projected/binary/java/" + 
							numPoints + "M_rides.bin");
		polygons = Utilities.readPolygons(ResourceLoader.readFileInList(
							System.getProperty("user.dir") + 
							"/resources/datasets/neighborhoods/projected/polygons_" + 
							selectivity + ".csv"));
		quadtree = BuildIndex.buildQuadtree(points, 18);
		quadtree.getElementCount();
	}

	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@Measurement(iterations = 5, time = 60)
	@Threads(value = 1)
	@Fork(value = 1)
	public void benchmark(Blackhole bh) {
		int[] count = Queries.join(quadtree, polygons, points);
		bh.consume(count);
	}
}
