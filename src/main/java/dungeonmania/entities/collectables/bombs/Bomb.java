package dungeonmania.entities.collectables.bombs;

import dungeonmania.util.Position;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Collectable;
import dungeonmania.map.GameMap;

public class Bomb extends Collectable {
    public static final int DEFAULT_RADIUS = 1;
    private BombState state;
    private int radius;

    public Bomb(Position position, int radius) {
        super(position);
        changeBombStateSpawned();
        this.radius = radius;
    }

    public void notify(GameMap map) {
        explode(map);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        state.onOverlap(map, entity, this);
        changeBombStateInventory();
    }

    public void onPutDown(GameMap map, Position p) {
        translate(Position.calculatePositionBetween(getPosition(), p));
        map.addEntity(this);
        changeBombStatePlaced();
        List<Position> adjPosList = getPosition().getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node)
                    .stream()
                    .filter(e -> (e instanceof Switch))
                    .collect(Collectors.toList());
            entities.stream()
                    .map(Switch.class::cast)
                    .forEach(s -> s.subscribe(this, map));
        });
    }

    /**
     * it destroys all entities in diagonally and cardinally adjacent cells, except
     * for the player
     *
     * @param map
     */
    public void explode(GameMap map) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                List<Entity> entities = map.getEntities(new Position(i, j));
                entities = entities.stream()
                        .filter(e -> !(e instanceof Player))
                        .collect(Collectors.toList());
                for (Entity e : entities)
                    map.destroyEntity(e);
            }
        }
    }

    public void changeBombStateInventory() {
        this.state = new InventoryBombState();
    }

    public void changeBombStatePlaced() {
        this.state = new PlacedBombState();
    }

    public void changeBombStateSpawned() {
        this.state = new SpawnedBombState();
    }

}
