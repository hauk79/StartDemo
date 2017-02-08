package hyu.hauk.snake.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by hauk on 17. 1. 31.
 */
public class AssetsLoader {
    private static final String NAME = AssetsLoader.class.getSimpleName();
    private static final String TAG = "\\"+NAME;

    public enum CallerType {
        GameScreen,
        HelpScreen,
        HelpScreen2,
        HelpScreen3,
        HighscoreScreen,
        MainMenuScreen,
        Main,
    }

    public enum RunType {
        Load,
        Unload,
    }
    enum AssetType {
        Texture("com.badlogic.gdx.graphics"),
        Sound("com.badlogic.gdx.audio"),
        BitmapFont("com.badlogic.gdx.graphics.g2d"),
        ;
        String packageName;
        AssetType(String packageName) {
            this.packageName = packageName;
        }

        public Class<?> getType() {
            try {
                return ClassReflection.forName(packageName+ "." +toString());
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
            return Object.class;
        }

    }

    private static HashMap<AssetType, List<String>> map = new HashMap<AssetType, List<String>>();
    static {
        map.put(AssetType.Texture, new ArrayList<String>());
        map.put(AssetType.Sound, new ArrayList<String>());
        map.put(AssetType.BitmapFont, new ArrayList<String>());
    }
    public static void manage(Object object, RunType runType) {
        CallerType caller = Enum.valueOf(CallerType.class, object.getClass().getSimpleName());
        Gdx.app.log(TAG, object.getClass().getSimpleName()+": "+runType);

        switch (caller) {
            case Main:
                queue(Assets.BACKGROUND, AssetType.Texture);
                queue(Assets.BUTTONS, AssetType.Texture);
                queue(Assets.CLICK, AssetType.Sound);
                queue(Assets.SILVER_CURSIVE_32, AssetType.BitmapFont);
                break;
            case MainMenuScreen:
                break;
            case HighscoreScreen:
                break;
            case HelpScreen:
                queue(Assets.HELP1, AssetType.Texture);
                break;
            case HelpScreen2:
                queue(Assets.HELP2, AssetType.Texture);
                break;
            case HelpScreen3:
                queue(Assets.HELP3, AssetType.Texture);
                break;
            case GameScreen:
                queue(Assets.HEAD_DOWN, AssetType.Texture);
                queue(Assets.HEAD_LEFT, AssetType.Texture);
                queue(Assets.HEAD_RIGHT, AssetType.Texture);
                queue(Assets.HEAD_UP, AssetType.Texture);
                queue(Assets.STAIN1, AssetType.Texture);
                queue(Assets.STAIN2, AssetType.Texture);
                queue(Assets.STAIN3, AssetType.Texture);
                queue(Assets.TAIL, AssetType.Texture);

                queue(Assets.BITTEN, AssetType.Sound);
                queue(Assets.EAT, AssetType.Sound);
                break;
        }
        run(runType);
    }

    private static void run(RunType runType) {
        switch (runType) {
            case Load:
                for(AssetType assetType: map.keySet()) {
                    List<String> list = map.get(assetType);
                    Assets.INSTANCE.load(assetType.getType(), list.toArray(new String[list.size()]));
                }
                Assets.INSTANCE.finishLoading();
                break;
            case Unload:
                for(List<String> list: map.values()) {
                    Assets.INSTANCE.unloads(list.toArray(new String[list.size()]));
                }
                break;
        }

        for(List<String> list: map.values()) {
            list.clear();
        }
    }

    private static void queue(String fileName, AssetType assetType) {
        map.get(assetType).add(fileName);
    }
}
