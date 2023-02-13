package dungeonmania.entities;

import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    public static final int DEFAULT_MOVEMENT_FACTOR = 2;
    private int movementFactor;

    public SwampTile(Position position, int movementFactor) {
        super(position);
        this.movementFactor = movementFactor;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            return;
        }
        if (entity instanceof Mercenary) {
            // detect ally or not
            Mercenary merc = (Mercenary) entity;
            if (merc.isAllied() && merc.isAdjacent(map.getPlayer())) {
                // allied, adjacent entities are not affected by swamp
                entity.removeStuck();
                return;
            }
        }
        if (entity.getStuck() == 0 && entity.getPreviousPosition() != entity.getPosition()) {
            entity.setStuck(movementFactor);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public int getMovementFactor() {
        return movementFactor;
    }
}
