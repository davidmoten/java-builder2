# more-executors
More performant Java Executors.

Tweaks `j.u.c.ThreadPoolExecutor` in particular.

* make six frequently read volatile fields final
* reduce volatile reads o(e.g. `ctl.get()` in `addWorker`)
* remove volatile reads and writes of `completedTasks` (and associated unused methods)

## Benchmark results
The theoretical benefits are so far lost in the noise so far I'm afraid!`
