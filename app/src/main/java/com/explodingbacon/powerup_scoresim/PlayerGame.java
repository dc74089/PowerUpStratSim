package com.explodingbacon.powerup_scoresim;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

public class PlayerGame extends Thread {
    Button mySwitchButton, scaleButton, theirSwitchButton, bookshelfButton;
    Button forceButton, boostButton, levitateButton;
    ProgressBar mySwitchProgress, scaleProgress, theirSwitchProgress, bookshelfProgress;

    MainActivity.Goal goal = MainActivity.Goal.NONE;
    
    public PlayerGame(ViewGroup viewRoot) {
        mySwitchProgress = viewRoot.findViewById(R.id.mySwitchProgress);
        scaleProgress = viewRoot.findViewById(R.id.scaleProgress);
        theirSwitchProgress = viewRoot.findViewById(R.id.theirSwitchProgress);
        bookshelfProgress = viewRoot.findViewById(R.id.bookshelfProgress);
        mySwitchProgress.setMax(1000);
        scaleProgress.setMax(1000);
        theirSwitchProgress.setMax(1000);
        bookshelfProgress.setMax(1000);
    }
}
