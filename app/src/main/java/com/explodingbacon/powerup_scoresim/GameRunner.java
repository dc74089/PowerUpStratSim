package com.explodingbacon.powerup_scoresim;

import android.util.Log;

public class GameRunner extends Thread {
    final Object lock;
    MainActivity parent;

    Long finishTime;
    Boolean shouldFinish = false;

    Integer mySwitchProgress, scaleProgress, theirSwitchProgress, bookshelfProgress;
    Integer mySwitchTic, scaleTic, theirSwitchTic, bookshelfTic;
    Integer mySwitchMine, mySwitchTheirs, scaleMine, scaleTheirs, theirSwitchMine, theirSwitchTheirs;
    Integer powerupCredits = 0, score = 0, opposingScore = 0;

    MainActivity.Goal goal = MainActivity.Goal.NONE;

    public GameRunner(MainActivity parent) {
        this.lock = new Object();
        this.parent = parent;
        mySwitchProgress = scaleProgress = theirSwitchProgress = bookshelfProgress = 0;

        mySwitchMine = scaleMine = theirSwitchMine = 0;
        mySwitchTheirs = scaleTheirs = theirSwitchTheirs = 0;
    }

    public void setValues(float mySwitchTime, float scaleTime, float theirSwitchTime, float bookshelfTime) {
        mySwitchTic = Math.round(100 / mySwitchTime);
        scaleTic = Math.round(100 / scaleTime);
        theirSwitchTic = Math.round(100 / theirSwitchTime);
        bookshelfTic = Math.round(100 / bookshelfTime);
    }

    public void setGoal(MainActivity.Goal goal) {
        this.goal = goal;
    }

    public void usePowerup(MainActivity.Powerup powerup) {
        switch (powerup) {
            case FORCE:
                //TODO: This
                break;
            case BOOST:
                //TODO: This
                break;
            case LEVITATE:
                score += 30;
                powerupCredits -= 3;
                break;
        }
    }

    public void mainLoop() {
        Log.v("GameRunner", "Main Loop");
        //Increment Goal
        switch (goal) {
            case MY_SWITCH:
                mySwitchProgress += mySwitchTic;

                scaleProgress = 0;
                theirSwitchProgress = 0;
                bookshelfProgress = 0;
                break;
            case SCALE:
                scaleProgress += scaleTic;

                mySwitchProgress = 0;
                theirSwitchProgress = 0;
                bookshelfProgress = 0;
                break;
            case THEIR_SWITCH:
                theirSwitchProgress += theirSwitchTic;

                mySwitchProgress = 0;
                scaleProgress = 0;
                bookshelfProgress = 0;
                break;
            case BOOKSHELF:
                bookshelfProgress += bookshelfTic;

                mySwitchProgress = 0;
                scaleProgress = 0;
                theirSwitchProgress = 0;
                break;
        }

        //Handle finished goal
        if (mySwitchProgress >= 1000) {
            if(mySwitchMine < 10) mySwitchMine += 1;
            mySwitchProgress = 0;
            
        } else if (scaleProgress >= 1000) {
            if(scaleMine < 10) scaleMine += 1;
            scaleProgress = 0;
            
        } else if (theirSwitchProgress >= 1000) {
            if(theirSwitchMine < 10) theirSwitchMine += 1;
            theirSwitchProgress = 0;
            
        } else if (bookshelfProgress >= 1000) {
            powerupCredits++;
            bookshelfProgress = 0;
        }
        
        parent.draw();
    }

    private void doScore() {
        if (mySwitchMine > mySwitchTheirs) {
            score += 2;
        }

        if(scaleMine > scaleTheirs) {
            score += 2;
        } else if (scaleTheirs > scaleMine) {
            opposingScore += 2;
        }

        if(theirSwitchTheirs > theirSwitchMine) {
            opposingScore += 2;
        }

        Log.v("GameRunner", "Score: " + score);
    }

    public long getMatchTime() {
        return Math.round((finishTime - System.currentTimeMillis()) / 1000);
    }

    public void go() {
        finishTime = System.currentTimeMillis() + 135000;
        this.start();
    }

    public void finish() {
        shouldFinish = true;
    }

    @Override
    public void run() {
        Log.d("GameRunner", "Running");
        Long lastScoringTime = 0L;
        while (System.currentTimeMillis() < finishTime && !shouldFinish) {
            mainLoop();
            if(System.currentTimeMillis() - lastScoringTime >= 1000) {
                doScore();
                lastScoringTime = System.currentTimeMillis();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        finish();
    }
}
