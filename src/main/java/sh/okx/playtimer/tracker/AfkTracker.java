package sh.okx.playtimer.tracker;

import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Tracker associated with a player to determine their AFK status
 */
public class AfkTracker {
  /**
   * The associated player object for this tracker;
   */
  @Getter
  private final Player player;
  /**
   * A tracker on a scale of 0 to 1 where 0 is AFK and 1 is active player input
   */
  private float tracker = 1;
  /**
   * A flag to determine if a player is certainly AFK,
   * such as when they ride a minecart.
   * If set, the main tracker does not change.
   */
  private boolean afk = false;

  public AfkTracker(Player player, float tracker) {
    this.player = player;
    this.tracker = tracker;
  }

  public boolean isAfk() {
    return afk;
  }

  public void setAfk(boolean afk) {
    this.afk = afk;
  }

  public float getTracker() {
    return afk ? 0 : tracker;
  }

  /**
   * Sets the tracker value to the given value
   * @param tracker the value to update the tracker with
   * @param force whether to bypass checking if the player has the AFK flag
   */
  public void setTracker(float tracker, boolean force) {
    if (force || !isAfk()) {
      this.tracker = Math.max(0, Math.min(1, tracker));
    }
  }

  public void setTracker(float tracker) {
    setTracker(tracker, false);
  }

  public void subtractTracker(float value) {
   setTracker(tracker - value);
  }

  public void addTracker(float value) {
    setTracker(tracker + value);
  }
}

