package fii.tppa.logoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button startGame;
    private Button highScore;
    private Button share;
    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        GameManager.getInstance(getApplicationContext());

        bind();
    }

    private void bind() {
        startGame = findViewById(R.id.start_game_b);
        highScore = findViewById(R.id.high_score_b);
        share = findViewById(R.id.share_b);
        logo = findViewById(R.id.logo_i);


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(startGame);
            }
        });

        GameManager.Log("View elements successfully bound");
    }


}
