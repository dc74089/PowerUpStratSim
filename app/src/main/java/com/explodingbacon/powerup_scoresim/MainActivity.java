package com.explodingbacon.powerup_scoresim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button go, stop;
    TextView playerOneScore, playerTwoScore;
    ViewGroup playerOneRoot, playerTwoRoot;

    String scoreDisplayString = "%d/%d";
    String powerupDisplayString = "You have %d powerup credits";

    GameRunner gameRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go = findViewById(R.id.goButton);
        stop = findViewById(R.id.stopButton);
        go.setOnClickListener(runner);
        stop.setOnClickListener(runner);

        playerOneRoot = findViewById(R.id.playerOne);
        playerTwoRoot = findViewById(R.id.playerTwo);
        playerOneScore = findViewById(R.id.playerOneScore);
        playerTwoScore = findViewById(R.id.playerTwoScore);

        gameRunner = new GameRunner(this);

        reset();
    }

    private void reset() {
        gameRunner = null;

        go.setEnabled(false);
        stop.setEnabled(true);

        noAutoButton.setEnabled(true);
        driveAutoButton.setEnabled(true);
        switchAutoButton.setEnabled(true);
        scaleAutoButton.setEnabled(true);

        forceButton.setEnabled(false);
        boostButton.setEnabled(false);
        levitateButton.setEnabled(false);

        mySwitchButton.setEnabled(false);
        scaleButton.setEnabled(false);
        theirSwitchButton.setEnabled(false);
        bookshelfButton.setEnabled(false);

        mySwitchProgress.setProgress(0);
        scaleProgress.setProgress(0);
        theirSwitchProgress.setProgress(0);
        bookshelfProgress.setProgress(0);

        mySwitchTime.setEnabled(true);
        scaleTime.setEnabled(true);
        theirSwitchTime.setEnabled(true);
        bookshelfTime.setEnabled(true);

        mySwitchTime.setText("");
        scaleTime.setText("");
        theirSwitchTime.setText("");
        bookshelfTime.setText("");

        scoreDisplay.setText("");
        powerupDisplay.setText("");
    }
    
    private void prestart() {
        gameRunner = new GameRunner(this);

        go.setEnabled(true);
        stop.setEnabled(true);

        noAutoButton.setEnabled(false);
        driveAutoButton.setEnabled(false);
        switchAutoButton.setEnabled(false);
        scaleAutoButton.setEnabled(false);

        forceButton.setEnabled(false);
        boostButton.setEnabled(false);
        levitateButton.setEnabled(false);

        mySwitchButton.setEnabled(false);
        scaleButton.setEnabled(false);
        theirSwitchButton.setEnabled(false);
        bookshelfButton.setEnabled(false);

        mySwitchProgress.setProgress(0);
        scaleProgress.setProgress(0);
        theirSwitchProgress.setProgress(0);
        bookshelfProgress.setProgress(0);

        mySwitchTime.setEnabled(false);
        scaleTime.setEnabled(false);
        theirSwitchTime.setEnabled(false);
        bookshelfTime.setEnabled(false);

        scoreDisplay.setText(String.format(scoreDisplayString, 0, 0));
        powerupDisplay.setText(String.format(powerupDisplayString, 0));
    }

    private void start() {
        powerupCoord = new PowerupCoordinator();

        go.setEnabled(false);
        stop.setEnabled(true);

        noAutoButton.setEnabled(false);
        driveAutoButton.setEnabled(false);
        switchAutoButton.setEnabled(false);
        scaleAutoButton.setEnabled(false);

        forceButton.setEnabled(false);
        boostButton.setEnabled(false);
        levitateButton.setEnabled(false);

        mySwitchButton.setEnabled(true);
        scaleButton.setEnabled(true);
        theirSwitchButton.setEnabled(true);
        bookshelfButton.setEnabled(true);

        mySwitchTime.setEnabled(false);
        scaleTime.setEnabled(false);
        theirSwitchTime.setEnabled(false);
        bookshelfTime.setEnabled(false);
    }

    private View.OnClickListener runner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == stop) {
                try {
                    gameRunner.finish();
                } catch (NullPointerException ignored) {}
                reset();
            } else if (v == go) {
                gameRunner.setValues(
                        Float.parseFloat(mySwitchTime.getText().toString()),
                        Float.parseFloat(scaleTime.getText().toString()),
                        Float.parseFloat(theirSwitchTime.getText().toString()),
                        Float.parseFloat(bookshelfTime.getText().toString())
                        );

                gameRunner.score += autoPoints;

                gameRunner.go();
                start();
            }
        }
    };

    private View.OnClickListener autoSetter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.noAutoButton:
                    chosenAuto = Auto.NONE;
                    break;
                case R.id.driveAutoButton:
                    chosenAuto = Auto.DRIVE;
                    autoPoints = 5;
                    break;
                case R.id.switchAutoButton:
                    chosenAuto = Auto.SWITCH;
                    autoPoints = 20;
                    break;
                case R.id.scaleAutoButton:
                    chosenAuto = Auto.SCALE;
                    autoPoints = 20;
                    break;
            }
            prestart();
        }
    };
    
    private View.OnClickListener goalSetter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.mySwitchButton:
                    gameRunner.setGoal(Goal.MY_SWITCH);
                    break;
                case R.id.scaleButton:
                    gameRunner.setGoal(Goal.SCALE);
                    break;
                case R.id.theirSwitchButton:
                    gameRunner.setGoal(Goal.THEIR_SWITCH);
                    break;
                case R.id.bookshelfButton:
                    gameRunner.setGoal(Goal.BOOKSHELF);
                    break;
            }
        }
    };

    private View.OnClickListener powerupUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.forceButton:
                    gameRunner.usePowerup(Powerup.FORCE);
                    break;
                case R.id.boostButton:
                    gameRunner.usePowerup(Powerup.BOOST);
                    break;
                case R.id.levitateButton:
                    gameRunner.usePowerup(Powerup.LEVITATE);
            }
        }
    };

    public void draw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mySwitchProgress.setProgress(gameRunner.mySwitchProgress);
                scaleProgress.setProgress(gameRunner.scaleProgress);
                theirSwitchProgress.setProgress(gameRunner.theirSwitchProgress);
                bookshelfProgress.setProgress(gameRunner.bookshelfProgress);

                scoreDisplay.setText(String.format(scoreDisplayString, gameRunner.score, gameRunner.opposingScore));
                powerupDisplay.setText(String.format(powerupDisplayString, gameRunner.powerupCredits));

                mySwitchTime.setText(gameRunner.mySwitchMine + "/" + gameRunner.mySwitchTheirs);
                scaleTime.setText(gameRunner.scaleMine + "/" + gameRunner.scaleTheirs);
                theirSwitchTime.setText(gameRunner.theirSwitchMine + "/" + gameRunner.theirSwitchTheirs);
                bookshelfTime.setText(""+gameRunner.getMatchTime());

                forceButton.setEnabled(gameRunner.powerupCredits > 0);
                boostButton.setEnabled(gameRunner.powerupCredits > 0);
                levitateButton.setEnabled(gameRunner.powerupCredits >= 3);
            }
        });
    }

    public enum Auto {
        NONE, DRIVE, SWITCH, SCALE;
    }
    
    public enum Goal {
        NONE, MY_SWITCH, SCALE, THEIR_SWITCH, BOOKSHELF;
    }

    public enum Powerup {
        FORCE, BOOST, LEVITATE;
    }
}
