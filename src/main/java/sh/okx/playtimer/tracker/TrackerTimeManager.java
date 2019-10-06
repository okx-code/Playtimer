package sh.okx.playtimer.tracker;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import sh.okx.playtimer.database.dao.PlaytimeDao;
import sh.okx.playtimer.tracker.update.ActiveTrackerUpdater;

/**
 * Stores a player's non-AFK time into the database
 */
public class TrackerTimeManager {
  private final ActiveTrackerUpdater updater;
  private final PlaytimeDao dao;
  private final float threshold;

  private final int intervalLength;
//  /**
//   * A map to represent how many intervals have passed since a player
//   * last had their time added to the database
//   */
//  private final Map<UUID, Integer> intervals = new HashMap<>();

  public TrackerTimeManager(ActiveTrackerUpdater updater, PlaytimeDao dao, Plugin plugin, float threshold, int intervalLength) {
    this.updater = updater;
    this.dao = dao;
    this.threshold = threshold;
    this.intervalLength = intervalLength;

    Bukkit.getScheduler().runTaskTimer(plugin, this::addInterval, intervalLength, intervalLength);
//    Bukkit.getScheduler().runTaskTimer(plugin, this::saveAll, intervalLength * 100, intervalLength * 100);
  }

  private void addInterval() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      UUID uuid = player.getUniqueId();
      if (updater.getTracker(player).getTracker() >= threshold) {
        dao.addPlaytime(uuid, intervalLength);
//        intervals.put(uuid, intervals.getOrDefault(uuid, 0) + intervalLength);
      }
    }
  }

//  private void saveAll() {
//    intervals.forEach(dao::addPlaytime);
//    intervals.clear();
//  }
}
