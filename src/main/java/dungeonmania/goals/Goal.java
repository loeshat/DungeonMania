package dungeonmania.goals;

import dungeonmania.Game;

public abstract class Goal {
    public Goal() {
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        return subgoalAchieved(game);
    }

    public abstract boolean subgoalAchieved(Game game);

    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return subgoalToString(game);
    }

    public abstract String subgoalToString(Game game);

}
