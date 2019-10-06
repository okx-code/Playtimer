package sh.okx.playtimer.options;

public interface PlaytimerOptions {
  /**
   * Gets the threshold to be considered AFK
   * and not receive playtime
   * @return the threshold for considering if a player is AFK
   */
  float getThreshold();

  /**
   * @return the amount of ticks between every time the tracker passively updates
   * @see sh.okx.playtimer.tracker.update.PassiveTrackerUpdateTask
   */
  int getInterval();

  int getPlaytimeLimit();
}
