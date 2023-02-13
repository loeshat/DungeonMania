package dungeonmania.goals.goaltypes;

import dungeonmania.goals.Goal;

public abstract class TargetGoal extends Goal {
    private int target;

    public TargetGoal(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }
}
