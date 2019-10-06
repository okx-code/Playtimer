package sh.okx.playtimer.map;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import sh.okx.playtimer.tracker.update.ActiveTrackerUpdater;

@RequiredArgsConstructor
public class PlaytimeCommand implements CommandExecutor {
  private final ActiveTrackerUpdater updater;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return false;
    }

    Player player = (Player) sender;
    ItemStack mapItem = new ItemStack(Material.FILLED_MAP);

    MapView map = Bukkit.createMap(player.getWorld());
    map.getRenderers().clear();
    map.addRenderer(new TrackerMapRenderer(updater, args[0]));
    map.setTrackingPosition(false);

    MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
    mapMeta.setMapView(map);
    mapItem.setItemMeta(mapMeta);

    player.getInventory().addItem(mapItem);
    player.sendMessage("You have a map now");
    return true;
  }
}
