package com.explodingbacon.powerup_scoresim;

public class PowerupCoordinator {
    PlayerGame playerOne, playerTwo;
    Boolean oneUsedForce, oneUsedBoost, oneUsedLevitate;
    Boolean twoUsedForce, twoUsedBoost, twoUsedLevitate;
    
    public PowerupCoordinator(PlayerGame p1, PlayerGame p2) {
        this.playerOne = p1;
        this.playerTwo = p2;
        
        oneUsedForce = oneUsedBoost = oneUsedLevitate = false;
        twoUsedForce = twoUsedBoost = twoUsedLevitate = false;
    }

    public boolean use(PlayerGame player, Powerup powerup) {
        //TODO: This

        return true;
    }

    public enum Powerup {
        FORCE, BOOST, LEVITATE;
    }
}
