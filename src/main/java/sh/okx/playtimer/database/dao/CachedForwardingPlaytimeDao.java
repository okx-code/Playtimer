package sh.okx.playtimer.database.dao;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CachedForwardingPlaytimeDao implements PlaytimeDao {
  private PlaytimeDao dao;

  private final LoadingCache<UUID, Integer> cache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .build(new CacheLoader<UUID, Integer>() {
        @Override
        public Integer load(UUID request) {
          return dao.getPlaytime(request);
        }
      });

  private final Queue<Runnable> asyncQueue = new LinkedBlockingQueue<>();
  private final Map<UUID, Integer> addMap = new ConcurrentHashMap<>();
  private final Map<UUID, Integer> subtractMap = new ConcurrentHashMap<>();

  public CachedForwardingPlaytimeDao(JavaPlugin plugin, PlaytimeDao dao) {
    this.dao = dao;

    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 400, 400);
  }

  private void save() {
    while(!asyncQueue.isEmpty()) {
      asyncQueue.remove().run();
    }
    doBulk(addMap, dao::addPlaytime);
    doBulk(subtractMap, dao::subtractPlaytime);
  }

  private void doBulk(Map<UUID, Integer> map, BiConsumer<UUID, Integer> consumer) {
    // todo bulk insert
    map.forEach(consumer);
    map.clear();
  }

  @Override
  public void init() {
    dao.init();
  }

  @Override
  public int getPlaytime(UUID uuid) {
    return cache.getUnchecked(uuid);
  }

  @Override
  public void addPlaytime(UUID uuid, int amount) {
    addMap.put(uuid, addMap.getOrDefault(uuid, 0) + amount);
  }

  @Override
  public void subtractPlaytime(UUID uuid, int amount) {
    subtractMap.put(uuid, subtractMap.getOrDefault(uuid, 0) + amount);
  }

  @Override
  public void setLimit(int limit) {
    asyncQueue.add(() -> dao.setLimit(limit));
  }

  public void close() throws SQLException {
    save();
    dao.close();
  }
}
