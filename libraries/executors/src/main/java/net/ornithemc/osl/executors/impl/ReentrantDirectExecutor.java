package net.ornithemc.osl.executors.impl;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

public class ReentrantDirectExecutor implements Executor {

	private final Queue<Runnable> tasks = new ArrayDeque<>();

	private int runningTasks;

	@Override
	public void execute(Runnable task) {
		this.tasks.offer(task);

		if (this.runningTasks == 0) {
			while (!this.tasks.isEmpty()) {
				this.runTask(this.tasks.poll());
			}
		}
	}

	private void runTask(Runnable task) {
		this.runningTasks++;

		try {
			task.run();;
		} finally {
			this.runningTasks--;
		}
	}
}
