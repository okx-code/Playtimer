package sh.okx.playtimer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_14_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_14_R1.PacketPlayInFlying;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class PacketAnalyser {
  private float yaw;
  private float pitch;

  private final PlaytimerPlugin plugin;

  public void init() {
    ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, Client.getInstance()) {
      @Override
      public void onPacketReceiving(PacketEvent event) {
        PacketType type = event.getPacketType();
        if (type == Client.TAB_COMPLETE
            || type == Client.KEEP_ALIVE
            || type == Client.POSITION) {
          return;
        }
        PacketContainer packet = event.getPacket();
        String msg = type.name() + " ";
        if (type == Client.BLOCK_DIG) {
          PacketPlayInBlockDig p = (PacketPlayInBlockDig) packet.getHandle();
          msg += "status=" + s(p.d().ordinal()) + " l=" + " face=" + f(p.c().ordinal());
        } else if (type == Client.LOOK || type == Client.POSITION_LOOK) {
          PacketPlayInFlying look = (PacketPlayInFlying) packet.getHandle();
          float yaw = look.a(0) % 360;
          float pitch = look.b(0);

          float delta = Math.abs(PacketAnalyser.this.yaw - yaw) + Math.abs(PacketAnalyser.this.pitch - pitch);
          PacketAnalyser.this.yaw = yaw;
          PacketAnalyser.this.pitch = pitch;
          msg += "delta=" + delta + " yaw=" + yaw + " pitch=" + pitch;
        }
        Bukkit.broadcastMessage(msg);
      }
    });
  }

  private double r(double d) {
    return Math.round(d * 10) / 10D;
  }

  private String s(int i) {
    switch (i) {
      case 0:
        return "dig start";
      case 1:
        return "dig cancel";
      case 2:
        return "dig finish";
      case 3:
        return "drop stack";
      case 4:
        return "drop";
      case 5:
        return "shoot arrow / finish eating";
      case 6:
        return "swap item";
      default:
        throw new IllegalStateException("Unexpected value: " + i);
    }
  }

  private String f(int b) {
    switch (b) {
      case 0:
        return "bottom";
      case 1:
        return "top";
      case 2:
        return "north";
      case 3:
        return "south";
      case 4:
        return "west";
      case 5:
        return "east";
      default:
        throw new IllegalStateException("Unexpected value: " + b);
    }
  }
}
