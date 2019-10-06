package sh.okx.playtimer.tracker.update;

import org.bukkit.entity.Player;
import sh.okx.playtimer.tracker.AfkTracker;

/**
 * A way to update a player's tracker actively
 */
public interface ActiveTrackerUpdater {
  AfkTracker getTracker(Player player);
}
