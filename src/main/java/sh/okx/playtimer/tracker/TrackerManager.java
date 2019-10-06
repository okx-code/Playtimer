package sh.okx.playtimer.tracker;

import java.util.Map;
import java.util.WeakHashMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import sh.okx.playtimer.PlaytimerPlugin;
import sh.okx.playtimer.options.PlaytimerOptions;
import sh.okx.playtimer.tracker.update.ActiveTrackerUpdater;
import sh.okx.playtimer.tracker.update.PassiveTrackerUpdateTask;
import sh.okx.playtimer.tracker.update.PassiveTrackerUpdaterListener;

/**
 * Manages the AFK tracker of players
 * @see AfkTracker
 */
public class TrackerManager implements ActiveTrackerUpdater {
  @Getter
  private final PlaytimerOptions options;
  private final PlaytimerPlugin plugin;

  @Getter
  private Map<Player, AfkTracker> trackers = new WeakHashMap<>();

  public TrackerManager(PlaytimerOptions options, PlaytimerPlugin plugin) {
    this.options = options;
    this.plugin = plugin;

    registerListeners();
    registerUpdaters();
  }

  public void registerListeners() {
    PluginManager pm = plugin.getServer().getPluginManager();

    pm.registerEvents(new ListenerPlayerJoin(this), plugin);
  }

  public void registerUpdaters() {
    PassiveTrackerUpdateTask passive = new PassiveTrackerUpdateTask(this);
    passive.runTaskTimer(plugin, options.getInterval(), options.getInterval());

    Bukkit.getScheduler().runTask(plugin,
        () -> Bukkit.getPluginManager().callEvent(new TrackerRegisterEvent(plugin, passive, TrackerManager.this)));
  }

  private void registerPassiveUpdater(PassiveTrackerUpdateTask task, PassiveTrackerUpdaterListener updater) {
    plugin.getServer().getPluginManager().registerEvents(updater, plugin);
    task.addUpdater(updater);
  }

  private AfkTracker createNewTracker(Player player) {
    return new AfkTracker(player, options.getThreshold());
  }

  public void addNewTracker(Player player) {
    // afk tracker should start at the afk threshold
    trackers.put(player, createNewTracker(player));
  }

  @Override
  public AfkTracker getTracker(Player player) {
    return trackers.computeIfAbsent(player, this::createNewTracker);
  }
}
