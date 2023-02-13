package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.bombs.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity {
    private boolean activated;
    private List<Bomb> bombs = new ArrayList<>();

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        notifyBombs(map);
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            notifyBombs(map);
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
        }
    }

    public boolean isActivated() {
        return activated;
    }

    private void notifyBombs(GameMap map) {
        if (activated) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }
}
