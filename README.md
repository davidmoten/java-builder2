# more-executors
More performant Java Executors

Tweaks `ThreadPoolExecutor` in particular.

* make five frequently read volatile fields final
* reduce volatile reads (e.g. `ctl.get()` in `addWorker`)
* `Worker.completedTasks` is `volatile` but is not incremented safely
