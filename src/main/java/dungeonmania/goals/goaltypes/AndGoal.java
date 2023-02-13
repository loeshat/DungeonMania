package dungeonmania.goals.goaltypes;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class AndGoal extends ConjunctionGoal {
    public AndGoal(Goal goal1, Goal goal2) {
        super(goal1, goal2);
    }

    @Override
    public boolean subgoalAchieved(Game game) {
        return getGoal1().subgoalAchieved(game) && getGoal2().subgoalAchieved(game);
    }

    @Override
    public String subgoalToString(Game game) {
        return "(" + getGoal1().toString(game) + " AND " + getGoal2().toString(game) + ")";
    }
}
