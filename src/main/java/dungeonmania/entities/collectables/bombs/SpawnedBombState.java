package dungeonmania.entities.collectables.bombs;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;

public class SpawnedBombState extends BombState {

    public SpawnedBombState() {
        super();
    }

    @Override
    public void onOverlap(GameMap map, Entity entity, Bomb b) {
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(b))
                return;
            map.getEntities(Switch.class).stream().forEach(s -> s.unsubscribe(b));
            map.destroyEntity(b);
        }
    }
}
