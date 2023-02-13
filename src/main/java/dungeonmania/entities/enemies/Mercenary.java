package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     *
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && (player.countEntityOfType(Treasure.class) >= bribeAmount || player.hasSceptre());
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        bribe(player);
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = map.getPlayer();

        if (isAllied() && isAdjacent(player)) {
            // allied merc should occupy player's previous position
            nextPos = ((Entity) player).getPreviousPosition();
            if (nextPos == player.getPosition()) {
                // player hasn't moved, ally shouldn't either
                return;
            }
            if (nextPos.getCardinallyAdjacentPositions().contains(player.getPosition())) {
                // if not, then the player isn't cardinally adjacent, follow dijkstras
                map.moveTo(this, nextPos);
                return;
            }
        }
        // register nextPos (potential move)
        nextPos = map.dijkstraPathFind(getPosition(), map.getPlayer().getPosition(), this);
        map.moveTo(this, nextPos);

    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }

    public boolean isAdjacent(Entity player) {
        Position playerPosition = player.getPosition();
        Position mercPosition = ((Entity) this).getPosition();

        List<Position> positions = mercPosition.getAdjacentPositions();
        return positions.contains(playerPosition);
    }
}
