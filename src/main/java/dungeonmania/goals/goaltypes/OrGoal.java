package dungeonmania.goals.goaltypes;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class OrGoal extends ConjunctionGoal {
    public OrGoal(Goal goal1, Goal goal2) {
        super(goal1, goal2);
    }

    @Override
    public boolean subgoalAchieved(Game game) {
        return getGoal1().subgoalAchieved(game) || getGoal2().subgoalAchieved(game);
    }

    @Override
    public String subgoalToString(Game game) {
        return "(" + getGoal1().toString(game) + " OR " + getGoal2().toString(game) + ")";
    }
}
