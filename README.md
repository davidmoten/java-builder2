# more-executors
More performant Java Executors.

Tweaks `j.u.c.ThreadPoolExecutor` in particular.

* make six frequently read volatile fields final
* reduce volatile reads (e.g. `ctl.get()` in `addWorker`)

## Benchmark results
The reduced overhead is apparent below (running a lot of do nothing tasks with a single thread pool):

```
Benchmark                                               Mode  Cnt    Score     Error  Units
Benchmarks.executorDoNothingManyTimesSingleThreadJuc   thrpt    5  494.929 ± 130.905  ops/s
Benchmarks.executorDoNothingManyTimesSingleThreadMore  thrpt    5  645.132 ±  66.792  ops/s
```
