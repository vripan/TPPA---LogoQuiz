package fii.tppa.logoquiz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button startGame;
    private Button share;
    private Button scoreReset;
    private ImageView logo;
    private TextView maxScoreTextView;

    private final int PERMISSIONS_REQ_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        GameManager.getInstance(getApplicationContext());

        requestPermission(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSIONS_REQ_CODE);

        GameManager.getInstance(getApplicationContext()).loadMaxScore();

        bind();
    }

    private void bind() {
        startGame = findViewById(R.id.start_game_b);
        share = findViewById(R.id.share_b);
        scoreReset = findViewById(R.id.reset_b);
        logo = findViewById(R.id.logo_i);

        maxScoreTextView = findViewById(R.id.maxScoreTextView);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGame = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(startGame);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"QuizGame");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "Play this game with your friends. My high score is " + GameManager.getInstanceWithoutContext().getMaxScore());
                startActivity(Intent.createChooser(i,"Share via"));
            }
        });

        scoreReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                GameManager.getInstanceWithoutContext().setMaxScore(0);
                GameManager.getInstanceWithoutContext().saveMaxScore();

                StringBuilder scoreBuilder = new StringBuilder()
                        .append("Max score: ")
                        .append(GameManager.getInstanceWithoutContext().getMaxScore());

                maxScoreTextView.setText(scoreBuilder.toString());
            }
        });

        StringBuilder scoreBuilder = new StringBuilder()
                .append("Max score: ")
                .append(GameManager.getInstanceWithoutContext().getMaxScore());

        maxScoreTextView.setText(scoreBuilder.toString());

        GameManager.Log("View elements successfully bound");
    }

    private void requestPermission(String[] permissions, int reqCode) {
        ArrayList<String> notGrantedPermissions = new ArrayList<String>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                notGrantedPermissions.add(permission);
        }

        if (notGrantedPermissions.size() > 0) {
            String[] arr = new String[notGrantedPermissions.size()];
            arr = notGrantedPermissions.toArray(arr);

            ActivityCompat.requestPermissions(this, arr, reqCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> notGrantedPermissions = new ArrayList<String>();

        switch (requestCode) {
            case PERMISSIONS_REQ_CODE: {
                for (int idx = 0; idx < permissions.length; idx++) {
                    if (grantResults[idx] != PackageManager.PERMISSION_GRANTED)
                        notGrantedPermissions.add(permissions[idx]);
                }
            }
        }

        if (notGrantedPermissions.size() > 0)
            Toast.makeText(getApplicationContext(), "Save feature disabled", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Permissions granted!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        StringBuilder scoreBuilder = new StringBuilder()
                .append("Max score: ")
                .append(GameManager.getInstanceWithoutContext().getMaxScore());

        maxScoreTextView.setText(scoreBuilder.toString());
    }

}
