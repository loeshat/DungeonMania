package dungeonmania.entities.playerState;

import dungeonmania.entities.Player;

public abstract class PlayerState {
    private Player player;

    PlayerState(Player player) {
        this.player = player;
    }

    public boolean isInvincible() {
        return getClass().equals(InvincibleState.class);
    };

    public boolean isInvisible() {
        return getClass().equals(InvisibleState.class);
    };

    public Player getPlayer() {
        return player;
    }

    public void transitionInvisible() {
        player.changeState(new InvisibleState(player));
    };

    public void transitionInvincible() {
        player.changeState(new InvincibleState(player));
    };

    public void transitionBase() {
        player.changeState(new BaseState(player));
    };
}
