package de.lucalabs.fairylights.main.fastener.accessor;

import de.lucalabs.fairylights.main.fastener.FastenerType;
import de.lucalabs.fairylights.main.fastener.PlayerFastener;
import net.minecraft.world.entity.player.Player;

public final class PlayerFastenerAccessor extends EntityFastenerAccessor<Player> {
    public PlayerFastenerAccessor() {
        super(Player.class);
    }

    public PlayerFastenerAccessor(final PlayerFastener fastener) {
        super(Player.class, fastener);
    }

    @Override
    public FastenerType getType() {
        return FastenerType.PLAYER;
    }
}
