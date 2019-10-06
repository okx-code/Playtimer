package sh.okx.playtimer.map;

import com.google.common.collect.EvictingQueue;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import sh.okx.playtimer.tracker.AfkTracker;
import sh.okx.playtimer.tracker.update.ActiveTrackerUpdater;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public class TrackerMapRenderer extends MapRenderer {
  private final ActiveTrackerUpdater updater;
  private final String name;

  private final Map<Player, EvictingQueue<Float>> values = new HashMap<>();
  private final Map<Player, Long> lastTick = new HashMap<>();

  @SuppressWarnings("deprecation")
  @Override
  public void render(MapView map, MapCanvas canvas, Player player) {
    Player p = Bukkit.getPlayer(name);
    if (p == null) {
      return;
    }

    AfkTracker tracker = updater.getTracker(p);
    EvictingQueue<Float> queue = values.computeIfAbsent(p, r -> {
      EvictingQueue<Float> o = EvictingQueue.create(128);
      for (int i = 0; i < 128; i++) {
        o.add(tracker.getTracker());
      }
      return o;
    });

    long time = p.getWorld().getFullTime();
    if (time - lastTick.computeIfAbsent(p, i -> time) > 20) {
      lastTick.put(p, time);
      queue.add(tracker.getTracker());
    }


    Iterator<Float> it = queue.iterator();
    for (int x = 0; x < 128; x++) {
      int pixels = (int) (it.next() * 128);
      for (int y = 0; y < 128; y++) {
        int invY = 128 - y;
        if (y == 64) {
          canvas.setPixel(x, y, MapPalette.BLUE);
        } else if (invY < pixels) {
          canvas.setPixel(x, y, MapPalette.DARK_GREEN);
        } else if (invY == pixels) {
          canvas.setPixel(x, y, MapPalette.LIGHT_GREEN);
        } else {
          canvas.setPixel(x, y, MapPalette.RED);
        }
      }
    }

    canvas.drawText(5, 5, new MinecraftFont(), ((char) 167) + "32;" + name);
  }
}
