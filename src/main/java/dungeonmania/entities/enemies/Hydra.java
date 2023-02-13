package dungeonmania.entities.enemies;

import dungeonmania.util.Position;

public class Hydra extends ZombieToast {
    public static final double DEFAULT_HEALTH_INCREASE_RATE = 1.0;
    public static final double DEFAULT_HEALTH_INCREASE_AMOUNT = 5.0;

    private double healthIncreaseRate = DEFAULT_HEALTH_INCREASE_RATE;
    private double healthIncreaseAmount = DEFAULT_HEALTH_INCREASE_AMOUNT;

    public Hydra(Position position, double health, double attack, double rate, double amount) {
        super(position, health, attack);
        this.healthIncreaseRate = rate;
        this.healthIncreaseAmount = amount;
    }

    public double getHealthIncreaseRate() {
        return healthIncreaseRate;
    }

    public double getHealthIncreaseAmount() {
        return healthIncreaseAmount;
    }
}
