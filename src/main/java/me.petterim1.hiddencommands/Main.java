package me.petterim1.hiddencommands;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.AvailableCommandsPacket;
import cn.nukkit.plugin.PluginBase;

import java.util.ArrayList;
import java.util.List;

public class Main extends PluginBase implements Listener {

    private boolean ignoreOPs;
    private List<String> hiddenCommands;

    public void onEnable() {
        saveDefaultConfig();
        ignoreOPs = getConfig().getBoolean("ignoreOPs");
        hiddenCommands = getConfig().getStringList("hiddenCommands");
        if (!hiddenCommands.isEmpty()) {
            getServer().getPluginManager().registerEvents(this, this);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSendCommandData(DataPacketSendEvent e) {
        if (e.getPacket() instanceof AvailableCommandsPacket && !(ignoreOPs && e.getPlayer().isOp())) {
            AvailableCommandsPacket pk = (AvailableCommandsPacket) e.getPacket();
            List<String> remove = new ArrayList<>();
            for (String cmd : pk.commands.keySet()) {
                if (hiddenCommands.contains(cmd)) {
                    remove.add(cmd);
                }
            }
            for (String cmd : remove) {
                pk.commands.remove(cmd);
            }
        }
    }
}
