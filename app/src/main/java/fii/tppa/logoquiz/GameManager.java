package fii.tppa.logoquiz;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private static final String TAG = "LogoQuiz";
    private static GameManager instance = null;
    private Context context;
    private final String json_path = "logos.json";
    private List<Logo> logos = new ArrayList<Logo>();
    private int maxScore = 0;

    public static GameManager getInstance(Context context) {
        if (instance == null) {
            instance = new GameManager(context);
        }
        return instance;
    }

    public static GameManager getInstanceWithoutContext() {
        return instance;
    }

    private GameManager(Context context) {
        this.context = context;

        LoadLogos();
    }

    public static void Log(String msg) {
        Log.d(TAG, msg);
    }

    private void LoadLogos() {
        try {
            String json = null;
            InputStream dataStream = context.getAssets().open(json_path);
            int size = dataStream.available();
            byte[] buffer = new byte[size];
            int readed = dataStream.read(buffer);
            dataStream.close();
            if (readed != size)
                throw new Exception("Readed size is diferent from avalaible size");
            json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray jsonLogos = new JSONArray(json);

            for (int i = 0; i < jsonLogos.length(); i++) {
                Logo logo = new Logo(
                        jsonLogos.getJSONObject(i).getInt("id"),
                        jsonLogos.getJSONObject(i).getString("name"),
                        jsonLogos.getJSONObject(i).getString("variant"),
                        jsonLogos.getJSONObject(i).getString("hint"),
                        jsonLogos.getJSONObject(i).getString("alternative_1"),
                        jsonLogos.getJSONObject(i).getString("alternative_2"),
                        jsonLogos.getJSONObject(i).getString("alternative_3"),
                        jsonLogos.getJSONObject(i).getString("img_q"),
                        jsonLogos.getJSONObject(i).getString("img_a")
                );
                logos.add(logo);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Failed to load logos database from JSON.", Toast.LENGTH_LONG).show();
            Log(e.toString());
        }
    }

    public GameSession newGameSession() {
        return new GameSession(this.logos);
    }

    private boolean checkExternalMedia() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public boolean saveMaxScore() {
        if (!checkExternalMedia()) {
            Toast.makeText(this.context, "Cannot save score!", Toast.LENGTH_LONG).show();
            return false;
        }

        File root = android.os.Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/Android/data/fii.tppa.logoquiz");

        dir.mkdirs();

        File file = new File(dir, "score");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.print(this.maxScore);
            pw.flush();
            pw.close();
            f.close();
        } catch (IOException e) {
            Toast.makeText(this.context, "Cannot save score!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean loadMaxScore() {
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Android/data/fii.tppa.logoquiz");
        File file = new File(dir, "score");

        this.maxScore = 0;

        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            Scanner s = new Scanner(is);

            if (s.hasNextInt())
                this.maxScore = s.nextInt();
            else {
                this.maxScore = 0;
                Toast.makeText(this.context, "Cannot load score!", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (FileNotFoundException e) {
            this.maxScore = 0;
            Toast.makeText(this.context, "Cannot load score!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return true;
    }

    public boolean updateMaxScore(int newScore)
    {
        if (newScore>maxScore)
        {
            setMaxScore(newScore);
            return saveMaxScore();
        }
        return true;
    }
}
