package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Sceptre extends Buildable {

    private int mindControlDuration;
    private int durability;

    public Sceptre(int mindControlDuration, int durability) {
        super(null);
        this.durability = durability;
        this.mindControlDuration = mindControlDuration;
    }

    public int getMindControlDuration() {
        return mindControlDuration;
    }

    public void reduceMindControlDuration() {
        mindControlDuration--;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
                0,
                0,
                0,
                0,
                0));
    }

    @Override
    public void use(Game game) {
        durability--;
    }

    public boolean notBroken() {
        if (mindControlDuration > 0) {
            return true;
        }
        return false;
    }

}
