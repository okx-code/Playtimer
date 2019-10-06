package sh.okx.playtimer;

import lombok.Data;
import org.bukkit.Location;

/**
 * Records the delta of movements for a specific player move event
 * Specifically, records deltas of: yaw, pitch, x, y and z
 */
@Data
public class DeltaMove {
  private final float deltaYaw;
  private final float deltaPitch;
  private final float deltaX;
  private final float deltaY;
  private final float deltaZ;

  public float getDeltaLook() {
    return deltaYaw + deltaPitch;
  }

  public float getDeltaMove() {
    return deltaX + deltaY + deltaZ;
  }

  public static DeltaMove calculateDelta(Location from, Location to) {
    float deltaYaw = Math.min(Math.min(
        Math.abs(from.getYaw() - to.getYaw()),
        Math.abs((from.getYaw() + 360) - to.getYaw())),
        Math.abs(from.getYaw() - (to.getYaw() + 360)));

    return new DeltaMove(
        deltaYaw,
        Math.abs(from.getPitch() - to.getPitch()),
        (float) Math.abs(to.getX() - from.getX()),
        (float) (to.getY() - from.getY()),
        (float) Math.abs(to.getZ() - from.getZ()));
  }
}
