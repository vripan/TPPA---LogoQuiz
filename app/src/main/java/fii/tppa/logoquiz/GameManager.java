package fii.tppa.logoquiz;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final String TAG = "LogoQuiz";
    private static GameManager instance = null;
    private Context context;
    private final String json_path = "logos.json";
    private List<Logo> logos = new ArrayList<Logo>();

    public static GameManager getInstance(Context context) {
        if (instance == null) {
            instance = new GameManager(context);
        }
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

}
