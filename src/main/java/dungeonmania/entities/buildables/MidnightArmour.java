package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class MidnightArmour extends Buildable {
    // unlimited durability
    private int durability;
    private double attackBoost;
    private double defenceBoost;

    public MidnightArmour(double attackBoost, double defenceBoost) {
        super(null);
        this.attackBoost = attackBoost;
        this.defenceBoost = defenceBoost;
    }

    public double getDefenceBoost() {
        return defenceBoost;
    }

    public double getAttackBoost() {
        return attackBoost;
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                attackBoost,
                defenceBoost,
                0,
                0));
    }

    @Override
    public int getDurability() {
        // unlimited durability
        return durability;
    }

}
