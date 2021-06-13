package de.ngloader.timer.core.timer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.ngloader.timer.api.timer.Timer;
import de.ngloader.timer.api.timer.TimerManager;

public class ImplTimerManager implements TimerManager {

	private final Set<Timer> timers = new HashSet<>();

	@Override
	public void run() {
		for (Timer timer : this.timers) {
			try {
				if (timer.isEnabled()) {
					timer.run();
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.timers.remove(timer);
			}
		}
	}

	@Override
	public boolean addTimer(Timer timer) {
		return this.timers.add(timer);
	}

	@Override
	public boolean removeTimer(Timer timer) {
		return this.timers.remove(timer);
	}

	@Override
	public Set<Timer> getTimers() {
		return Collections.unmodifiableSet(this.timers);
	}
}