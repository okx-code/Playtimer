package sh.okx.playtimer.tracker.update;

import sh.okx.playtimer.tracker.AfkTracker;

public interface PassiveTrackerUpdater {
  /**
   * Called periodically for every player.
   * Will be called synchronously.
   * @param tracker a tracker for a player
   */
  void update(AfkTracker tracker);
}
