package fii.tppa.logoquiz;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fii.tppa.logoquiz.utils.DisplayDelay;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private Button ans_1;
    private Button ans_2;
    private Button ans_3;
    private Button ans_4;
    private ImageView logo_viewer;
    private TextView score_label;
    private GameSession session;
    private Drawable btn_default;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        session = GameManager.getInstance(getApplicationContext()).newGameSession();

        bind();

        displayNextLogo();
    }

    @Override
    protected void onPause() {
        super.onPause();

        GameManager.getInstanceWithoutContext().updateMaxScore(session.getScore());
    }

    private void bind() {
        ans_1 = findViewById(R.id.ans_1_b);
        ans_2 = findViewById(R.id.ans_2_b);
        ans_3 = findViewById(R.id.ans_3_b);
        ans_4 = findViewById(R.id.ans_4_b);
        logo_viewer = findViewById(R.id.logo_viewer);
        score_label = findViewById(R.id.score_label);

        ans_1.setOnClickListener(this);
        ans_2.setOnClickListener(this);
        ans_3.setOnClickListener(this);
        ans_4.setOnClickListener(this);

        btn_default = ans_1.getBackground();

        StringBuilder scoreBuilder = new StringBuilder()
                .append("Score: ")
                .append(session.getScore());

        score_label.setText(scoreBuilder.toString());
    }

    public void displayNextLogo() {
        Logo logo = session.getLogo();

        if (logo == null) {
            Toast.makeText(getApplicationContext(), "No more logos", Toast.LENGTH_LONG).show();
            return;
        }

        score_label.setText("Score: " + session.getScore());

        List<String> answers = logo.getPossibleAnswersRandomized();

        ans_1.setBackgroundColor(Color.GRAY);
        ans_2.setBackgroundColor(Color.GRAY);
        ans_3.setBackgroundColor(Color.GRAY);
        ans_4.setBackgroundColor(Color.GRAY);

        ans_1.setText(answers.get(0));
        ans_2.setText(answers.get(1));
        ans_3.setText(answers.get(2));
        ans_4.setText(answers.get(3));

        answered = false;

        showImage(logo.img_q);
    }

    private void showImage(String name)
    {
        int img_id = getApplicationContext().getResources().getIdentifier( name.substring(0, name.length()-4), "drawable", getApplicationContext().getPackageName());
        logo_viewer.setImageResource(img_id);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;

        if (answered) return;

        answered = true;

        if (session.answerIsCorrect(button.getText().toString())) {
            v.setBackgroundColor(Color.GREEN);
            session.increaseScore();
        } else {
            v.setBackgroundColor(Color.RED);
        }

        showImage(session.getCurrentLogo().img_a);

        final Handler handler = new Handler();
        handler.postDelayed(new DisplayDelay(this), 1200);
    }
}
