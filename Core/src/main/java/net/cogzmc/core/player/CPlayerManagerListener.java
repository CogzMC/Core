package net.cogzmc.core.player;

import lombok.Data;
import net.cogzmc.core.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 * This will talk to any {@link net.cogzmc.core.player.CPlayerManager} and bridge communications between Bukkit and the Core.
 */
@Data
public final class CPlayerManagerListener implements Listener {
    private final CPlayerManager playerManager;

    //no docs
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerLoginEvent event) {
        for (CPlayer cPlayer : playerManager.getOnlinePlayers()) {
            if (cPlayer.getName().equals(event.getPlayer().getName())) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "You are already on this server. Try logging in again!");
                return;
            }
        }
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return; //Prevent whitelist from causing memory leaks.
        Player player = event.getPlayer();
        try {
            playerManager.playerLoggedIn(player, event.getAddress());
        } catch (CPlayerJoinException e) {
            event.setKickMessage(e.getDisconectMessage());
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) playerManager.playerLoggedOut(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Core.getOnlinePlayer(event.getPlayer()).onJoin();
    }

    //no docs
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (playerManager.getCPlayerForPlayer(event.getPlayer()) != null) playerManager.playerLoggedOut(event.getPlayer());
    }

    //no docs
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        if (playerManager.getCPlayerForPlayer(event.getPlayer()) != null) playerManager.playerLoggedOut(event.getPlayer());
    }

//    @EventHandler
//    public void onPlayerTagEvent(AsyncPlayerReceiveNameTagEvent event) {
//        CPlayer onlinePlayer = Core.getOnlinePlayer(event.getNamedPlayer());
//        if (onlinePlayer == null) return;
//        if (onlinePlayer.hasTagName()) event.setTag(onlinePlayer.getTagName());
//        else if (onlinePlayer.hasDisplayName()) event.setTag(onlinePlayer.getDisplayName());
//    }
}
