package dungeonmania.goals.goaltypes;

import dungeonmania.Game;
import dungeonmania.entities.Switch;
import dungeonmania.goals.Goal;

public class BoulderGoal extends Goal {
    public BoulderGoal() {
    }

    @Override
    public boolean subgoalAchieved(Game game) {
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String subgoalToString(Game game) {
        return ":boulders";
    }
}
