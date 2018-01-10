package com.explodingbacon.powerup_scoresim;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayerGame extends Thread {
    Button mySwitchButton, scaleButton, theirSwitchButton, bookshelfButton;
    Button forceButton, boostButton, levitateButton;
    ProgressBar mySwitchProgressBar, scaleProgressBar, theirSwitchProgressBar, bookshelfProgressBar;
    TextView mySwitchScore, scaleScore, theirSwitchScore, bookshelfScore, powerupCreditDisplay;
    int mySwitchProgress, scaleProgress, theirSwitchProgress, bookshelfProgress;

    MainActivity parent;
    GameMaster gameMaster;

    Goal goal = Goal.NONE;

    public final Boolean isOne;
    
    public PlayerGame(MainActivity parent, GameMaster gameMaster, ViewGroup viewRoot, Boolean isOne) {
        this.parent = parent;
        this.gameMaster = gameMaster;
        this.isOne = isOne;

        mySwitchProgressBar = viewRoot.findViewById(R.id.mySwitchProgress);
        scaleProgressBar = viewRoot.findViewById(R.id.scaleProgress);
        theirSwitchProgressBar = viewRoot.findViewById(R.id.theirSwitchProgress);
        bookshelfProgressBar = viewRoot.findViewById(R.id.bookshelfProgress);

        mySwitchButton = viewRoot.findViewById(R.id.mySwitchButton);
        scaleButton = viewRoot.findViewById(R.id.scaleButton);
        theirSwitchButton = viewRoot.findViewById(R.id.theirSwitchButton);
        bookshelfButton = viewRoot.findViewById(R.id.bookshelfButton);
        mySwitchButton.setOnClickListener(goalSwitcher);
        scaleButton.setOnClickListener(goalSwitcher);
        theirSwitchButton.setOnClickListener(goalSwitcher);
        bookshelfButton.setOnClickListener(goalSwitcher);

        forceButton = viewRoot.findViewById(R.id.forceButton);
        boostButton = viewRoot.findViewById(R.id.boostButton);
        levitateButton = viewRoot.findViewById(R.id.levitateButton);
        forceButton.setOnClickListener(powerupListener);
        boostButton.setOnClickListener(powerupListener);
        levitateButton.setOnClickListener(powerupListener);

        mySwitchScore = viewRoot.findViewById(R.id.mySwitchScore);
        scaleScore = viewRoot.findViewById(R.id.scaleScore);
        theirSwitchScore = viewRoot.findViewById(R.id.theirSwitchScore);
        bookshelfScore = viewRoot.findViewById(R.id.bookshelfScore);
        powerupCreditDisplay = viewRoot.findViewById(R.id.powerupCreditDisplay);
    }

    public void reset() {
        mySwitchProgress = scaleProgress = theirSwitchProgress = bookshelfProgress = 0;

        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                forceButton.setEnabled(false);
                boostButton.setEnabled(false);
                levitateButton.setEnabled(false);

                mySwitchButton.setEnabled(false);
                scaleButton.setEnabled(false);
                theirSwitchButton.setEnabled(false);
                bookshelfButton.setEnabled(false);

                mySwitchProgressBar.setMax(1000);
                scaleProgressBar.setMax(1000);
                theirSwitchProgressBar.setMax(1000);
                bookshelfProgressBar.setMax(1000);

                mySwitchProgressBar.setProgress(0);
                scaleProgressBar.setProgress(0);
                theirSwitchProgressBar.setProgress(0);
                bookshelfProgressBar.setProgress(0);
            }
        });
    }

    private void mainLoop() {
        //Increment Goal
        switch (goal) {
            case MY_SWITCH:
                mySwitchProgress += gameMaster.mySwitchTic;

                scaleProgress = 0;
                theirSwitchProgress = 0;
                bookshelfProgress = 0;
                break;
            case SCALE:
                scaleProgress += gameMaster.scaleTic;

                mySwitchProgress = 0;
                theirSwitchProgress = 0;
                bookshelfProgress = 0;
                break;
            case THEIR_SWITCH:
                theirSwitchProgress += gameMaster.theirSwitchTic;

                mySwitchProgress = 0;
                scaleProgress = 0;
                bookshelfProgress = 0;
                break;
            case BOOKSHELF:
                bookshelfProgress += gameMaster.bookshelfTic;

                mySwitchProgress = 0;
                scaleProgress = 0;
                theirSwitchProgress = 0;
                break;
        }

        //Handle finished goal
        if (mySwitchProgress >= 1000) {
            gameMaster.addCrate(this, Goal.MY_SWITCH);
            mySwitchProgress = 0;
        } else if (scaleProgress >= 1000) {
            gameMaster.addCrate(this, Goal.SCALE);
            scaleProgress = 0;
        } else if (theirSwitchProgress >= 1000) {
            gameMaster.addCrate(this, Goal.THEIR_SWITCH);
            theirSwitchProgress = 0;
        } else if (bookshelfProgress >= 1000) {
            gameMaster.addCrate(this, Goal.BOOKSHELF);
            bookshelfProgress = 0;
        }
    }

    private void draw() {
        parent.runOnUiThread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                mySwitchProgressBar.setProgress(mySwitchProgress);
                scaleProgressBar.setProgress(scaleProgress);
                theirSwitchProgressBar.setProgress(theirSwitchProgress);
                bookshelfProgressBar.setProgress(bookshelfProgress);

                mySwitchButton.setEnabled(goal != Goal.MY_SWITCH);
                scaleButton.setEnabled(goal != Goal.SCALE);
                theirSwitchButton.setEnabled(goal != Goal.THEIR_SWITCH);
                bookshelfButton.setEnabled(goal != Goal.BOOKSHELF);

                forceButton.setEnabled(gameMaster.getPowerupCredits(PlayerGame.this) > 0);
                boostButton.setEnabled(gameMaster.getPowerupCredits(PlayerGame.this) > 0);
                levitateButton.setEnabled(gameMaster.getPowerupCredits(PlayerGame.this) >= 3);

                powerupCreditDisplay.setText(String.format("You have %d powerup credits",
                        gameMaster.getPowerupCredits(PlayerGame.this)));
            }
        });
    }

    View.OnClickListener goalSwitcher = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mySwitchButton:
                    goal = Goal.MY_SWITCH;
                    break;
                case R.id.scaleButton:
                    goal = Goal.SCALE;
                    break;
                case R.id.theirSwitchButton:
                    goal = Goal.THEIR_SWITCH;
                    break;
                case R.id.bookshelfButton:
                    goal = Goal.BOOKSHELF;
                    break;
            }
        }
    };

    @Override
    public void run() {
        while (gameMaster.isActive()) {
            mainLoop();
            draw();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    View.OnClickListener powerupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: THIS
        }
    };

    public enum Goal {
        NONE, MY_SWITCH, SCALE, THEIR_SWITCH, BOOKSHELF;
    }
}
