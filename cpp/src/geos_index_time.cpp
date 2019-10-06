#include <vector>

#include "geos/index/strtree/STRtree.h"
#include "geos/index/quadtree/Quadtree.h"

//#include "geos_queries.h"
#include "geos_index.h"
#include "geos_schema.h"
#include "geos_load_dataset.h"
#include "benchmark/benchmark.h"

auto strtree_time = [](benchmark::State& state, int dataset_size) {
	GEOSGeometry points;
	std::unique_ptr<geos::index::strtree::STRtree> strtree = std::make_unique<geos::index::strtree::STRtree>();

	get_points(points, dataset_size);

	while (state.KeepRunning ()) {
		build_strtree(&strtree, points.geometry);
	}

};

auto quadtree_time = [](benchmark::State& state, int dataset_size) {
	GEOSGeometry points;
	std::unique_ptr<geos::index::quadtree::Quadtree> quadtree = std::make_unique<geos::index::quadtree::Quadtree>();

	get_points(points, dataset_size);

	while (state.KeepRunning ()) {
		build_quadtree(&quadtree, points.geometry);
	}

};

int main(int argc, char** argv) {

	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "STRtreeIndexTime/Data-" + std::to_string(j) + "M";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), strtree_time, j)->Threads(1);
	}

	for (int j = 10; j <= 50; j += 5) {
		std::string benchmark_name = "QuadtreeIndexTime/Data-" + std::to_string(j) + "M";
		benchmark::RegisterBenchmark(benchmark_name.c_str(), quadtree_time, j)->Threads(1);
	}

	benchmark::Initialize(&argc, argv);
	benchmark::RunSpecifiedBenchmarks();
}
