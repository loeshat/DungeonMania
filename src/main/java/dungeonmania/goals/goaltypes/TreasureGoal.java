package dungeonmania.goals.goaltypes;

import dungeonmania.Game;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;

public class TreasureGoal extends TargetGoal {
    public TreasureGoal(int target) {
        super(target);
    }

    @Override
    public boolean subgoalAchieved(Game game) {
        int totalCollectedTreasure = game.getMap().getEntities(Treasure.class).size()
                + game.getMap().getEntities(SunStone.class).size();
        return game.getInitialTreasureCount() - totalCollectedTreasure >= getTarget();
    }

    @Override
    public String subgoalToString(Game game) {
        return ":treasure";
    }
}
