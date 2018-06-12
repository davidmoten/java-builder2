# more-executors
More performant Java Executors.

Tweaks `j.u.c.ThreadPoolExecutor` in particular.

* make six frequently read volatile fields final
* reduce volatile reads o(e.g. `ctl.get()` in `addWorker`)
* remove volatile reads and writes of `completedTasks` (and associated unused methods)

## Benchmark results
On Ubuntu 16.04 AWS t2.xlarge there is a 10% throughput boost on a single thread:

```java
Benchmark                                               Mode  Cnt    Score   Error  Units
Benchmarks.executorDoNothingManyTimesSingleThreadJuc   thrpt   10  308.426 ± 2.141  ops/s
Benchmarks.executorDoNothingManyTimesSingleThreadMore  thrpt   10  338.532 ± 1.606  ops/s
```
