package net.communitycraft.permissions.commands.player;

import net.cogzmc.core.Core;
import net.cogzmc.core.modular.command.ArgumentRequirementException;
import net.cogzmc.core.modular.command.CommandException;
import net.cogzmc.core.player.CGroup;
import net.cogzmc.core.player.COfflinePlayer;
import net.communitycraft.permissions.commands.general.AbstractPlayerSubCommand;

import java.util.List;

public final class SetGroupCommand extends AbstractPlayerSubCommand {
    protected SetGroupCommand() {
        super("setgroup");
    }

    @Override
    protected boolean needsSecondArgument() {
        return true;
    }

    @Override
    protected boolean validateArgument(String arg) {
        return Core.getPermissionsManager().getGroup(arg) != null;
    }

    @Override
    protected void doAction(COfflinePlayer player, String arg) throws CommandException {
        CGroup group = Core.getPermissionsManager().getGroup(arg);
        if (group == null) throw new ArgumentRequirementException("Invalid group!");
        if (player.isDirectlyInGroup(group)) return;
        List<CGroup> groups = player.getGroups();
        if (groups.size() > 0) {
            for (CGroup group1 : groups) {
                player.removeFromGroup(group1);
            }
        }
        player.addToGroup(group);
    }
}
