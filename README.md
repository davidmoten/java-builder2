# more-executors
More performant Java Executors.

Tweaks `j.u.c.ThreadPoolExecutor` in particular.

* make six frequently read volatile fields final
* reduce volatile reads (e.g. `ctl.get()` in `addWorker`)

## TODO
* JMH benchmarks against `j.u.c.Executors`.
