package dungeonmania.entities.collectables.bombs;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public abstract class BombState {

    public BombState() {
    }

    public void onOverlap(GameMap map, Entity entity, Bomb b) {
        return;
    }
}
