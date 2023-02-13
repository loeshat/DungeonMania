package dungeonmania.goals.goaltypes;

import dungeonmania.goals.Goal;

public abstract class ConjunctionGoal extends Goal {
    private Goal goal1;
    private Goal goal2;

    public ConjunctionGoal(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public Goal getGoal1() {
        return goal1;
    }

    public Goal getGoal2() {
        return goal2;
    }
}
