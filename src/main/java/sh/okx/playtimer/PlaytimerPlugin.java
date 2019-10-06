package sh.okx.playtimer;

import java.sql.SQLException;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import sh.okx.playtimer.database.dao.CachedForwardingPlaytimeDao;
import sh.okx.playtimer.database.dao.MySqlPlaytimeDao;
import sh.okx.playtimer.database.dao.PlaytimeDao;
import sh.okx.playtimer.database.pool.ConnectionPool;
import sh.okx.playtimer.database.pool.ConnectionPoolMySql;
import sh.okx.playtimer.map.PlaytimeCommand;
import sh.okx.playtimer.options.DevPlaytimerOptions;
import sh.okx.playtimer.options.PlaytimerOptions;
import sh.okx.playtimer.tracker.TrackerManager;
import sh.okx.playtimer.tracker.TrackerTimeManager;

public class PlaytimerPlugin extends JavaPlugin {
  @Getter
  private PlaytimeDao playtimeDao;
  private PlaytimerAccessor api;
  private PlaytimerOptions options;
  private ConnectionPool pool;

  @Override
  public void onEnable() {
    this.options = new DevPlaytimerOptions();
    this.pool = new ConnectionPoolMySql("localhost", 3306, "minecraft", "root", "root");
    this.playtimeDao = new CachedForwardingPlaytimeDao(this, new MySqlPlaytimeDao(pool, options.getPlaytimeLimit()));
    this.api = new DaoPlaytimerAccessor(playtimeDao);

    playtimeDao.init();

    TrackerManager trackerManager = new TrackerManager(options, this);

    getCommand("playtime").setExecutor(new PlaytimeCommand(trackerManager));

    TrackerTimeManager timeManager = new TrackerTimeManager(trackerManager, playtimeDao, this, options.getThreshold(), 20);
  }

  @Override
  public void onDisable() {
    try {
      playtimeDao.close();
      pool.close();
    } catch (SQLException e) {
      getLogger().log(Level.SEVERE, "Could not close connections", e);
    }
  }

  public PlaytimerAccessor getApi() {
    return api;
  }
}
