package com.explodingbacon.powerup_scoresim;

import android.view.View;
import android.view.ViewGroup;

public class GameMaster extends Thread {
    PlayerGame playerOne, playerTwo;
    int playerOneScore, playerTwoScore;
    int mySwitchTic, scaleTic, theirSwitchTic, bookshelfTic;
    int playerOneSwitchOne, playerOneSwitchTwo, scaleOne, scaleTwo;
    int playerTwoSwitchOne, playerTwoSwitchTwo, bookshelfOne, bookshelfTwo;
    int powerupCreditsUsedOne, powerupCreditsUsedTwo;

    long endTime;
    
    public GameMaster(MainActivity parent, ViewGroup one, ViewGroup two) {
        playerOne = new PlayerGame(parent, this, one, true);
        playerTwo = new PlayerGame(parent, this, two, false);

        reset();
    }

    private void calculateTics() {
        //TODO: This
    }

    private void reset() {
        endTime = 0;

        playerOne.reset();
        playerTwo.reset();

        playerOneScore = playerTwoScore = 0;
        playerOneSwitchOne = scaleOne = playerTwoSwitchOne = bookshelfOne = 0;
        playerOneSwitchTwo = scaleTwo = playerTwoSwitchTwo = bookshelfTwo = 0;

        powerupCreditsUsedOne = powerupCreditsUsedTwo = 0;
    }

    public void addCrate(PlayerGame player, PlayerGame.Goal goal) {
        if(player.isOne) {
            switch (goal) {
                case MY_SWITCH:
                    if(playerOneSwitchOne < 10)
                        playerOneSwitchOne++;
                    break;
                case SCALE:
                    if(scaleOne < 10)
                        scaleOne++;
                    break;
                case THEIR_SWITCH:
                    if(playerTwoSwitchOne < 10)
                        playerTwoSwitchOne++;
                    break;
                case BOOKSHELF:
                    bookshelfOne++;
            }
        } else {
            switch (goal) {
                case MY_SWITCH:
                    if(playerTwoSwitchTwo < 10)
                        playerTwoSwitchTwo++;
                    break;
                case SCALE:
                    if(scaleTwo < 10)
                        scaleTwo++;
                    break;
                case THEIR_SWITCH:
                    if(playerOneSwitchTwo < 10)
                        playerOneSwitchTwo++;
                    break;
                case BOOKSHELF:
                    bookshelfTwo++;
            }
        }
    }

    public int getPowerupCredits(PlayerGame player) {
        if (player.isOne)
            return bookshelfOne - powerupCreditsUsedOne;
        else
            return bookshelfTwo - powerupCreditsUsedTwo;
    }

    public boolean isActive() {
        return (System.currentTimeMillis() < endTime);
    }
    
    public View.OnClickListener gameFlowButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goButton:
                    endTime = System.currentTimeMillis() + 135000;
                    playerOne.start();
                    playerTwo.start();
                    break;
                case R.id.stopButton:
                    reset();
                    break;
            }
        }
    };
}
