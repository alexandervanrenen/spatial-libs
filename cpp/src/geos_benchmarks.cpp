#include <vector>

#include "geos_queries.h"
#include "geos_benchmarks.h"

#include "benchmark/benchmark.h"

int main(int argc, char** argv) {
	static std::vector<std::string> selectivity = {"0.0001", "0.001", "0.01", "0.1", "1", "10", "100"};


	////////////////////////////////////////////////// Range Query ////////////////////////////////////////////////
	//
	///////////////////// STRtree /////////////////////
	//
	// Scale


	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "STRtreeRangeQueryScale/Data-" + std::to_string(j) + "M/Selectivity-" + selectivity[3] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_range, j, selectivity[3])->Threads(1);
	}


	// Selectivity bechnmarks
	for (int j = 0; j < selectivity.size(); j++) {
		std::string benchmark_name = "STRtreeRangeQuerySelectivity/Data-" + std::to_string(50) + "M/Selectivity-" + selectivity[j] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_range, 50, selectivity[j])->Threads(1);
	}

	///////////////////// Quadtree /////////////////////
	//
	// Scale 
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "QuadtreeRangeQueryScale/Data-" + std::to_string(j) + "M/Selectivity-" + selectivity[3] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_range, j, selectivity[3])->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 0; j < selectivity.size(); j++) {
		std::string benchmark_name = "QuadtreeRangeQuerySelectivity/Data-" + std::to_string(50) + "M/Selectivity-" + selectivity[j] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_range, 50, selectivity[j])->Threads(1);
	}

	////////////////////////////////////////////////// Distance Query ////////////////////////////////////////////////
	//
	///////////////////// STRtree In Built Within /////////////////////
	//
	// Scale
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "STRtreeDistanceQueryScale/Data-" + std::to_string(j) + "M/Selectivity-" + selectivity[3] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_distance, j, selectivity[3])->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 0; j < selectivity.size(); j++) {
		std::string benchmark_name = "STRtreeDistanceQuerySelectivity/Data-" + std::to_string(50) + "M/Selectivity-" + selectivity[j] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_distance, 50, selectivity[j])->Threads(1);
	}

/*	
	// Don't register these benchmarks with the framework for users, they are slower and an overhead for reproducibility
    	// Users may uncomment and run them if need be
	// Own within uses faster distance query refinement by not using isWithinDistance in GEOS which makes 6 malloc calls

	///////////////////// STRtree Own Within /////////////////////
	//
	// Scale
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "STRtreeOwnDistanceQueryScale/Data-" + std::to_string(j) + "M/Selectivity-" + selectivity[3] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_distance_own, j, selectivity[3])->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 0; j < selectivity.size(); j++) {
		std::string benchmark_name = "STRtreeOwnDistanceQuerySelectivity/Data-" + std::to_string(50) + "M/Selectivity-" + selectivity[j] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_distance_own, 50, selectivity[j])->Threads(1);
	}
*/

	///////////////////// Quadtree In Built Within /////////////////////
	//
	// Scale
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "QuadtreeDistanceQueryScale/Data-" + std::to_string(j) + "M/Selectivity-" + selectivity[3] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_distance, j, selectivity[3])->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 0; j < selectivity.size(); j++) {
		std::string benchmark_name = "QuadtreeDistanceQuerySelectivity/Data-" + std::to_string(50) + "M/Selectivity-" + selectivity[j] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_distance, 50, selectivity[j])->Threads(1);
	}

/*
	// Don't register these benchmarks with the framework for users, they are slower and an overhead for reproducibility
	// Users may uncomment and run them if need be

	///////////////////// Quadtree Own Within /////////////////////
	//
	// Scale
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "QuadtreeOwnDistanceQueryScale/Data-" + std::to_string(j) + "M/Selectivity-" + selectivity[3] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_distance_own, j, selectivity[3])->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 0; j < selectivity.size(); j++) {
		std::string benchmark_name = "QuadtreeOwnDistanceQuerySelectivity/Data-" + std::to_string(50) + "M/Selectivity-" + selectivity[j] + "%";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_distance_own, 50, selectivity[j])->Threads(1);
	}
*/

	////////////////////////////////////////////////// Join Query ////////////////////////////////////////////////
	//
	/////////////////////// STRTree on Points ////////////////////////
	//
	// Scale bechnmarks
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "STRtreeOnPointsJoinScale/Data-" + std::to_string(j) + "M/Selectivity-289Polygons";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_on_points, j, std::to_string(289))->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 1; j <= 289; j++) {
		if(!(j%50) || j == 289) {
			std::string benchmark_name = "STRtreeOnPointsJoin/Data-50M/Selectivity-" + std::to_string(j) + "Polygons";
			benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_on_points, 50, std::to_string(j))->Threads(1);
		}
	}

	/////////////////////// Quadtree on Points ////////////////////////
	//
	// Scale bechnmarks
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "QuadtreeOnPointsJoin/Data-" + std::to_string(j) + "M/Selectivity-289Polygons";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_on_points, j, std::to_string(289))->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 1; j <= 289; j++) {
		if(!(j%50) || j == 289) {
			std::string benchmark_name = "QuadtreeOnPointsJoin/Data-50M/Selectivity-" + std::to_string(j) + "Polygons";
			benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_on_points, 50, std::to_string(j))->Threads(1);
		}
	}

/*
	// Don't register these benchmarks with the framework for users, they are slower and an overhead for reproducibility
	// Users may uncomment and run them if need be

	/////////////////////// STRTree on Polygons ////////////////////////
	//
	// Scale bechnmarks
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "STRtreeOnPolygonsJoin/Data-" + std::to_string(j) + "M/Selectivity-289Polygons";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_on_polygons, j, std::to_string(289))->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 1; j <= 289; j++) {
		if(j == 1 || !(j%10) || j == 289) {
			std::string benchmark_name = "STRtreeOnPolygonsJoin/Data-50M/Selectivity-" + std::to_string(j) + "Polygons";
			benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_on_polygons, 50, std::to_string(j))->Threads(1);
		}
	}

	/////////////////////// QuadTree on Polygons ////////////////////////
	//
	// Scale bechnmarks
	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "QuadtreeOnPolygonsJoin/Data-" + std::to_string(j) + "M/Selectivity-289Polygons";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_on_polygons, j, std::to_string(289))->Threads(1);
	}

	// Selectivity bechnmarks
	for (int j = 1; j <= 289; j++) {
		if(!(j%50) || j == 289) {
			std::string benchmark_name = "QuadtreeOnPolygonsJoin/Data-50M/Selectivity-" + std::to_string(j) + "Polygons";
			benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_on_polygons, 50, std::to_string(j))->Threads(1);
		}
	}
*/
	benchmark::Initialize(&argc, argv);
	benchmark::RunSpecifiedBenchmarks();
}
