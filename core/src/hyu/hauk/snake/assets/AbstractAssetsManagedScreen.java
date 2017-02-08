package hyu.hauk.snake.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

/**
 * Created by hauk on 17. 1. 27.
 */
public abstract class AbstractAssetsManagedScreen extends ScreenAdapter implements AssetsManaged{
    private static final String NAME = AbstractAssetsManagedScreen.class.getSimpleName();
    private static final String TAG = "\\"+NAME;

    @Override
    public final void show() {
        Gdx.app.log(TAG, this.getClass().getSimpleName()+": show()");
        AssetsLoader.manage(this, AssetsLoader.RunType.Load);
        showManaged();
    }

    @Override
    public final void hide() {
        Gdx.app.log(TAG, this.getClass().getSimpleName()+": hide()");
        hideManaged();
        dispose();
    }

    @Override
    public final void dispose() {
        Gdx.app.log(TAG,this.getClass().getSimpleName()+": dispose()");
        disposeManaged();
        AssetsLoader.manage(this, AssetsLoader.RunType.Unload);
    }
}
