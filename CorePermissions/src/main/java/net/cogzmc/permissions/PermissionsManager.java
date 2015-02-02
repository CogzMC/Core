package net.cogzmc.permissions;

import lombok.Getter;
import net.cogzmc.core.Core;
import net.cogzmc.core.modular.ModularPlugin;
import net.cogzmc.core.modular.ModuleMeta;
import net.cogzmc.core.player.CGroup;
import net.cogzmc.core.player.CPermissionsManager;
import net.cogzmc.permissions.command.PermissionsCommand;

@ModuleMeta(
        name = "Permissions Manager",
        description = "Provides commands for the permissions plugin, along with creating the default groups and assigning people groups on default."
)
public final class PermissionsManager extends ModularPlugin {
    @Getter private static PermissionsManager instance;

    @Override
    public void onModuleEnable() {
        instance = this;
        //Create default group
        CPermissionsManager permissionsManager = Core.getPermissionsManager();
        if (permissionsManager.getGroups().size() == 0) {
            CGroup defaultGroup = permissionsManager.createNewGroup("Default");
            defaultGroup.setPermission("test.permission", true);
            permissionsManager.saveGroup(defaultGroup);
        }
        registerCommand(new PermissionsCommand());
        if (Core.getNetworkManager() != null) Core.getNetworkManager().registerNetCommandHandler(new PermissionsReloadNetCommandHandler(), PermissionsReloadNetCommand.class);
    }
}
