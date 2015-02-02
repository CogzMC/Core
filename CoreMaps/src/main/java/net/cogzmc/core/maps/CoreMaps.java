package net.cogzmc.core.maps;

import lombok.Getter;
import net.cogzmc.core.Core;
import net.cogzmc.core.modular.ModularPlugin;
import net.cogzmc.core.modular.ModuleMeta;
import net.cogzmc.core.player.mongo.CMongoDatabase;

@ModuleMeta(
        name = "CoreMaps",
        description = "Manages maps for a network based, or single server."
)
public final class CoreMaps extends ModularPlugin {
    @Getter private static CoreMaps instance;
    @Getter private CMongoMapManager mapManager;

    @Override
    protected void onModuleEnable() throws Exception {
        if (!(Core.getInstance().getCDatabase() instanceof CMongoDatabase)) throw new IllegalStateException("THis is a mongo bean in a strange world!");
        mapManager = new CMongoMapManager(((CMongoDatabase) Core.getInstance().getCDatabase()));
        instance = this;
    }

    @Override
    protected void onModuleDisable() throws Exception {
        for (CMap cMap : mapManager.getLoadedMaps()) {
            if (cMap.isLoaded()) cMap.unload();
            if (cMap.getZipFileHandle() != null && cMap.getZipFileHandle().exists()) cMap.getZipFileHandle().delete();
        }
    }
}
