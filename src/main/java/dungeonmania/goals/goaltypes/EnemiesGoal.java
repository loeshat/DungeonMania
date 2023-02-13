package dungeonmania.goals.goaltypes;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemiesGoal extends TargetGoal {
    public EnemiesGoal(int target) {
        super(target);
    }

    @Override
    public boolean subgoalAchieved(Game game) {
        return game.getEnemiesDestroyed() >= getTarget()
                && game.getMap().getEntities(ZombieToastSpawner.class).size() == 0;
    }

    @Override
    public String subgoalToString(Game game) {
        return ":enemies";
    }
}
