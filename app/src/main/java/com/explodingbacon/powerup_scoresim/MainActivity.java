package com.explodingbacon.powerup_scoresim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button go, stop;
    TextView playerOneScore, playerTwoScore;
    ViewGroup playerOneRoot, playerTwoRoot;

    GameMaster gameMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go = findViewById(R.id.goButton);
        stop = findViewById(R.id.stopButton);

        playerOneRoot = findViewById(R.id.playerOne);
        playerTwoRoot = findViewById(R.id.playerTwo);
        playerOneScore = findViewById(R.id.playerOneScore);
        playerTwoScore = findViewById(R.id.playerTwoScore);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.partial_player_dash, playerOneRoot, true);
        inflater.inflate(R.layout.partial_player_dash, playerTwoRoot, true);

        gameMaster = new GameMaster(this, playerOneRoot, playerTwoRoot);

        go.setOnClickListener(gameMaster.gameFlowButtonListener);
    }
}
