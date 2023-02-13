package dungeonmania.goals.goaltypes;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.entities.Player;
import dungeonmania.goals.Goal;
import dungeonmania.util.Position;

public class ExitGoal extends Goal {
    public ExitGoal() {
    }

    @Override
    public boolean subgoalAchieved(Game game) {
        Player character = game.getPlayer();
        Position pos = character.getPosition();
        List<Exit> es = game.getMap().getEntities(Exit.class);
        if (es == null || es.size() == 0) return false;
        return es
            .stream()
            .map(Entity::getPosition)
            .anyMatch(pos::equals);
    }

    @Override
    public String subgoalToString(Game game) {
        return ":exit";
    }

}
