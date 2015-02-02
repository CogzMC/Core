package net.cogzmc.punishments.types.impl.model;

import lombok.EqualsAndHashCode;
import net.cogzmc.core.player.COfflinePlayer;
import net.cogzmc.punishments.types.HumanFriendlyName;
import net.cogzmc.punishments.types.impl.TargetOnlinesOnly;
import org.bson.types.ObjectId;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@TargetOnlinesOnly
@HumanFriendlyName("Warn")
public final class Warning extends MongoPunishment {
    public Warning(ObjectId objectId, COfflinePlayer target, String message, COfflinePlayer issuer, boolean revoked, Date dateIssued) {
        super(objectId, target, message, issuer, revoked, dateIssued);
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
