# more-executors
More performant Java Executors

Tweaks `ThreadPoolExecutor` in particular.

* make six frequently read volatile fields final
* reduce volatile reads (e.g. `ctl.get()` in `addWorker`)
