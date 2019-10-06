package sh.okx.playtimer.options;

/**
 * A developer implementation of {@link PlaytimerOptions}, returning static values.
 */
public class DevPlaytimerOptions implements PlaytimerOptions {
  @Override
  public float getThreshold() {
    return 0.5f;
  }

  @Override
  public int getInterval() {
    return 20;
  }

  @Override
  public int getPlaytimeLimit() {
    return 10000;
  }
}
