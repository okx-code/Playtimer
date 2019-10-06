package sh.okx.playtimer.tracker;

import java.util.function.Function;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import sh.okx.playtimer.PlaytimerPlugin;
import sh.okx.playtimer.tracker.update.ActiveTrackerUpdater;
import sh.okx.playtimer.tracker.update.PassiveTrackerUpdateTask;
import sh.okx.playtimer.tracker.update.PassiveTrackerUpdater;
import sh.okx.playtimer.tracker.update.PassiveTrackerUpdaterListener;

public class TrackerRegisterEvent extends Event {
  private static final HandlerList handlers = new HandlerList();

  private final PlaytimerPlugin plugin;
  private final PassiveTrackerUpdateTask passiveTrackerTask;
  private final ActiveTrackerUpdater activeTrackerUpdater;

  public TrackerRegisterEvent(PlaytimerPlugin plugin, PassiveTrackerUpdateTask passiveUpdater,
      ActiveTrackerUpdater activeTrackerUpdater) {
    this.plugin = plugin;
    this.passiveTrackerTask = passiveUpdater;
    this.activeTrackerUpdater = activeTrackerUpdater;
  }

  public void registerPassiveTracker(PassiveTrackerUpdater tracker) {
    passiveTrackerTask.addUpdater(tracker);
  }

  public void registerPassiveTrackerListener(JavaPlugin plugin, PassiveTrackerUpdaterListener listener) {
    plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    registerPassiveTracker(listener);
  }

  public void registerActiveTracker(JavaPlugin plugin, Function<ActiveTrackerUpdater, Listener> listener) {
    plugin.getServer().getPluginManager().registerEvents(listener.apply(activeTrackerUpdater), plugin);
  }

  public PlaytimerPlugin getPlugin() {
    return plugin;
  }

  public PassiveTrackerUpdateTask getPassiveTrackerTask() {
    return passiveTrackerTask;
  }

  public ActiveTrackerUpdater getActiveTrackerUpdater() {
    return activeTrackerUpdater;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
