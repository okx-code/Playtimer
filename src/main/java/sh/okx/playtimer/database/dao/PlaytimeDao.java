package sh.okx.playtimer.database.dao;

import java.sql.SQLException;
import java.util.UUID;

public interface PlaytimeDao {
  void init();

  /**
   * Retrieves the total playtime of a player in ticks
   *
   * @param uuid the player's unique id
   * @return the playtime of a player
   */
  int getPlaytime(UUID uuid);

  /**
   * Adds the given amount to a player's playtime.
   * if a player's playtime is not stored yet,
   * {@code defaultLimitRemaining} refers to the total playtime
   * a player can gain before it is is reset.
   *
   * @param uuid   the player's unique id
   * @param amount the amount of playtime to add in ticks
   */
  void addPlaytime(UUID uuid, int amount);

  /**
   * Subtracts the amount from a player's playtime
   * Make sure to check that the player's current playtime
   * is not 0 and is greater or equal to the {@code amount},
   * as this method makes no checks of its own.
   *
   * @param uuid   the player's unique id
   * @param amount the amount to subtract in ticks
   */
  void subtractPlaytime(UUID uuid, int amount);

  /**
   * Resets every player's playtime limit to a given value.
   * When players gain playtime, their personal limit will
   * be reduced by the however much playtime they gained.
   * Playtime cannot be gained at a limit of 0.
   *
   * @param limit the playtime limit
   */
  void setLimit(int limit);

  default void close() throws SQLException {
  }
}
