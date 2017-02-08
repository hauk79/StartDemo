package hyu.hauk.snake.assets;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;

/**
 * Created by hauk on 17. 1. 27.
 */
public class Settings {
    private static final String TAG = "\\"+Settings.class.getSimpleName();
    private static final String PREFERENCES = Settings.class.getName();
    private static final String SOUND_ENABLED = "soundEnabled";
    private static final String HIGH_SCORES = "highscores";
    public static boolean soundEnabled = true;
    public static int[] highscrores = new int[] {100,80,50,30,10};
    private static boolean changed = false;

    public static void load(Files files) {
        Gdx.app.log(TAG,"load Settings..");
        Preferences preferences = Gdx.app.getPreferences(PREFERENCES);
        soundEnabled = preferences.getBoolean(SOUND_ENABLED,soundEnabled);
        for(int i=0; i<5; i++) {
            highscrores[i] = preferences.getInteger(HIGH_SCORES+i, highscrores[i]);
        }
    }

    public static void save(Files files) {
        if(changed) {
            Gdx.app.log(TAG, "save Settings..");
            Preferences preferences = Gdx.app.getPreferences(PREFERENCES);
            preferences.putBoolean(SOUND_ENABLED, soundEnabled);
            for (int i = 0; i < 5; i++) {
                preferences.putInteger(HIGH_SCORES + i, highscrores[i]);
            }
            preferences.flush();
            changed = false;
        } else {
            Gdx.app.log(TAG, "Not changed Settings..");
        }
    }

    public static void toggleSoundEnabled() {
        soundEnabled = !soundEnabled;
        changed = true;
    }
    public static void addScore(int score) {
        for(int i=0; i<5; i++) {
            if(highscrores[i]<score) {
                for(int j=4; j>i; j--) highscrores[j] = highscrores[j-1];
                highscrores[i]=score;
                changed = true;
                break;
            }
        }
    }
}
