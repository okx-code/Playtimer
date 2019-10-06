package sh.okx.playtimer.tracker.update;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sh.okx.playtimer.tracker.AfkTracker;
import sh.okx.playtimer.tracker.TrackerManager;

/**
 * A task to manage passive effects on a player's AFK tracker
 */
@RequiredArgsConstructor
public class PassiveTrackerUpdateTask extends BukkitRunnable {
  private final Set<PassiveTrackerUpdater> updaters = new HashSet<>();
  private final TrackerManager manager;

  public void addUpdater(PassiveTrackerUpdater updater) {
    updaters.add(updater);
  }

  @Override
  public void run() {
    for (Map.Entry<Player, AfkTracker> entry : manager.getTrackers().entrySet()) {
      AfkTracker tracker = entry.getValue();

      for (PassiveTrackerUpdater updater : updaters) {
        updater.update(tracker);
      }
    }
  }
}
