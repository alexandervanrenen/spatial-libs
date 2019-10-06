## Introduction
This repository provides a performance evaluation of five spatial libraries (two in C++, and three in Java): [Google S2] (https://github.com/google/s2geometry), [Geometry Engine, Open Source] (https://github.com/libgeos/geos), [ESRI geometry-api-java] (https://github.com/Esri/geometry-api-java), [JTS Topology Suite] (https://github.com/locationtech/jts), and [Java Spatial Index (JSI)] (https://github.com/aled/jsi). We also compared the libraries with an open sources implementation of Vantage Point Tree, called [jvptree] (https://github.com/jchambers/jvptree). The evaluation was done on the basis of four queries: range query, distance query, kNN query, and a point-in-polygon join query. Majorly there are two benchmarks for each query: scale the dataset with query selectivity fixed to 0.1%, and fix the dataset to 50 million locations and increase the selectivity of the query.

## Getting Started
This benchmark uses several dataset files and query datasets. Please go to setup folder and run `./setup.sh`

The setup script downloads all the datasets (aaproximately 19GB), creates all the symlinks required by the various benchmarks, and also converts the location datasets to binary format for faster loading of input datasets during benchmark runs. Depending on you internet connection, the setup can take anywhere between 25-60 minutes. Minimum memory of 32GB is required, as some libraries have high memory usage for some queries.

Please follow language specific instructions to continue with the setup:

### Java Benchmarks
We use [Java Microbenchmark Harness (JMH)] (https://openjdk.java.net/projects/code-tools/jmh/) for running the benchmarks of the libraries written in Java. The project is packaged using Maven, hence Maven is a requirement. To run the Java based benchmarks, go to the folder java, and:

1. `mvn clean package`
2. `java -jar target/benchmarks.jar`

If you would like to run only a subset of the benchmarks, you can do so as:
* `java -jar target/benchmarks.jar jsi.RangeQueryScale` or
* `java -jar target/benchmarks.jar jsi.RangeQuerySelectivity` or
* `java -jar target/benchmarks.jar jts.kdtree.RangeQueryScale`

etc.

The queries, in general, for each libraries are packaged as `<lib_name>.<query>`.

`<lib_name>` can be one of the following, `esrigeometry`, `jsi`, `jts`, and `jvptree`.

`<queries>` can be one of the following, `RangeQuery`, `DistanceQuery`, `KnnQuery`, and `JoinQuery`. All of the queries have two flavors: `Scale` and `Selectivity` (e.g. `RangeQueryScale` or `RangeQuerySelectivity`) Please note that not all libraries support all queries.
For `jts` there is an extra redirection in the form of `<lib_name>.<index>.<query>`, and the queries and their code is thus modularized based on the indexes. `<index>` can be one of `kdtree`, `quadtree`, and `strtree`.

There are approximately >450 benchmark combinations for all the libraries in Java and can take upto 3 days to execute. Every benchmark runs for at least 5 minutes (to get stable numbers), some may run longer if the queries are more expensive. There is also an additional cost of every benchmark setup phase, which requires loading the input dataset and indexing them.

### C++ Benchmarks
We use [Google Benchmark] (https://github.com/google/benchmark) for running the benchmarks of libraries written in C++. To run the C++ based libraries benchmarks, go to the folder cpp, and:
1. `mkdir build`
2. `cd build`
3. `cmake -DCMAKE_BUILD_TYPE=Release ..`
4. `make -j geos_benchmarks s2_benchmarks`
5. `bin/geos_benchmarks` or `bin/s2_benchmarks`

All the tools/libraries that the benchmarks are dependent on, are automatically downloaded during the build process, compiled and linked with the benchmark binaries. You may (or may not) have to set some environment variables (library dependent) after running cmake, please look at the cmake output to be sure. Library dependencies are not also download automatically currently, and if some dependency is not found, it will be reported in cmake output. Simple `make -j` builds all targets which also includes building library specific tests etc. and may fail if such dependencies are not installed in the system (for e.g. gtest).

If you would like to run only a subset of the benchmarks, you may use `bin/s2_benchmarks --benchmark_list_tests=true` for example to retrieve a list of all benchmarks for the binary. You can then use `--benchmark_filter=<benchmark_name>` with the benchmark binary to run that particular benchmark. For example:

* `bin/s2_benchmarks --benchmark_filter=RangeQueryScale` or
* `bin/s2_benchmarks --benchmark_filter=RangeQuerySelectivity`

etc.

There are approximately >100 benchmarks for all the libraries in C++ and can take upto 1-2 days to execute. The runtime of each benchmark is dynamically determined by Google Benchmark. Again some benchmarks may take longer to stabilize while some may not.

## Disclaimer
The benchmarks were run on Ubuntu 18.04 and use Linux specific commands at various places for I/O etc. Operations on non-Linux operating systems may not work.
