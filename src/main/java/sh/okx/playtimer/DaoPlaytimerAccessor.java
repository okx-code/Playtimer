package sh.okx.playtimer;

import java.util.UUID;
import sh.okx.playtimer.database.dao.PlaytimeDao;

public class DaoPlaytimerAccessor implements PlaytimerAccessor {
  private final PlaytimeDao dao;

  public DaoPlaytimerAccessor(PlaytimeDao dao) {
    this.dao = dao;
  }

  @Override
  public int getTicksPlayed(UUID uuid) {
    return dao.getPlaytime(uuid);
  }

  @Override
  public void subtractTicks(UUID uuid, int ticks) {
    dao.subtractPlaytime(uuid, ticks);
  }

  @Override
  public void setLimit(int limit) {
    dao.setLimit(limit);
  }
}
