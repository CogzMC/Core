/*
 * Copyright (c) 2014.
 * CogzMC LLC USA
 * All Right reserved
 *
 * This software is the confidential and proprietary information of Cogz Development, LLC.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Cogz LLC.
 */

package net.cogzmc.chat.channels;

import net.cogzmc.chat.ChatManager;
import net.cogzmc.core.Core;
import net.cogzmc.core.player.CPlayer;
import net.cogzmc.core.player.CPlayerConnectionListener;
import net.cogzmc.core.player.CPlayerJoinException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.InetAddress;

/**
 * Handles messaging on channels
 * and join and quit events
 * which allow the {@link ChannelManager} to
 * register a {@link org.bukkit.entity.Player} to a {@link net.cogzmc.chat.channels.Channel}.
 * <p/>
 * <p/>
 * Latest Change: Rewrite for Bukkit
 * <p/>
 *
 * @author Jake
 * @since 1/16/2014
 */
public final class ChannelsListener implements Listener, CPlayerConnectionListener {
    private ChannelManager channelManager;

    public ChannelsListener(ChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onChat(AsyncPlayerChatEvent event) {
        if (!ChatManager.getInstance().getChannelManager().isEnabled()) return;
        if (event.isCancelled()) return;
        CPlayer sender = Core.getOnlinePlayer(event.getPlayer());
        ChatManager.getInstance().getChannelManager().sendMessage(sender, event.getMessage());
        event.setCancelled(true);
    }

    @Override
    public void onPlayerLogin(CPlayer player, InetAddress address) throws CPlayerJoinException {
        channelManager.setChannel(player, channelManager.getDefaultChannel());
    }

    @Override
    public void onPlayerDisconnect(CPlayer player) {
        channelManager.removeChannel(player);
    }
}
