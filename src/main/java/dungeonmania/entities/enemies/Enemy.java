package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleableEntity;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends BattleableEntity {

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER), health, attack, 0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.playerBattle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
        g.incrementEnemiesDestroyed();
    }

    public void move(Game game) {
        randomMovement(game);
    }

    public Position randomMovement(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Random randGen = new Random();
        List<Position> pos = getPosition().getCardinallyAdjacentPositions();
        pos = pos
                .stream()
                .filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = getPosition();
            map.moveTo(this, nextPos);
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
            map.moveTo(this, nextPos);
        }
        return nextPos;
    }
}
