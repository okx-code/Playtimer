package sh.okx.playtimer.tracker;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
class ListenerPlayerJoin implements Listener {
  private final TrackerManager manager;

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    manager.addNewTracker(e.getPlayer());
  }
}
