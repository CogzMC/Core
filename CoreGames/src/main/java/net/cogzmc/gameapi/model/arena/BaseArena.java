package net.cogzmc.gameapi.model.arena;

import lombok.Getter;
import org.bukkit.World;

abstract class BaseArena implements Arena {
    @Getter private World world;
    @Getter private boolean loaded;

    abstract World createOrLoadWorld();

    public void load() {
        if (loaded) throw new IllegalStateException("You cannot load when we have already loaded!");
        world = createOrLoadWorld();
        loaded = true;
    }
}
