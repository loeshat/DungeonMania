package dungeonmania.battles;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

/**
 * Entities extend this class can do battles
 */
public abstract class BattleableEntity extends Entity {
    private BattleStatistics battleStatistics;

    public BattleableEntity(Position position, double health, double attack,
                            double defence, double attackMagnifier, double damageReducer) {
        super(position);
        this.battleStatistics = new BattleStatistics(
            health,
            attack,
            defence,
            attackMagnifier,
            damageReducer);
    }

    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public double getHealth() {
        return getBattleStatistics().getHealth();
    }

    public void setHealth(double health) {
        getBattleStatistics().setHealth(health);
    }
}
