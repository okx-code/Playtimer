package sh.okx.playtimer;

import java.util.UUID;

/**
 * A simple interface to access basic methods of playtimer
 */
public interface PlaytimerAccessor {
  /**
   * Get the total amount of ticks played, while not afk.
   * May be capped at a certain amount of time per week.
   */
  int getTicksPlayed(UUID uuid);

  /**
   * Subtract the given amount of ticks from a player's total playtime
   * @param uuid the UUID of the player
   * @param ticks the amount of ticks to subtract
   */
  void subtractTicks(UUID uuid, int ticks);

  default void subtractMinutes(UUID uuid, int minutes) {
    subtractTicks(uuid, minutes * 60 * 20);
  }

  default void subtractHours(UUID uuid, int hours) {
    subtractTicks(uuid, hours * 60 * 60 * 20);
  }

  /**
   * Resets every player's playtime limit to a given value.
   * When players gain playtime, their personal limit will
   * be reduced by the however much playtime they gained.
   * Playtime cannot be gained at a limit of 0.
   *
   * @param limit the playtime limit in ticks
   */
  void setLimit(int limit);
}
