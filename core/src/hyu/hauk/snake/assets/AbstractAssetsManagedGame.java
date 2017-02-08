package hyu.hauk.snake.assets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * Created by hauk on 17. 2. 1.
 */
public abstract class AbstractAssetsManagedGame extends Game implements AssetsManaged{
    private static final String NAME = AbstractAssetsManagedGame.class.getSimpleName();
    private static final String TAG = "\\"+NAME;

    @Override
    public final void create() {
        Gdx.app.log(TAG, this.getClass().getSimpleName()+": create()");
        AssetsLoader.manage(this, AssetsLoader.RunType.Load);
        showManaged();
    }

    @Override
    public final void showManaged() {
        createManaged();
    }

    protected abstract void createManaged();

    @Override
    public final void hideManaged() {
        Gdx.app.log(TAG,"hideManaged()");
        // need not implement
    }

    @Override
    public final void dispose() {
        super.dispose();
        Gdx.app.log(TAG,this.getClass().getSimpleName()+": dispose()");
        disposeManaged();
        AssetsLoader.manage(this, AssetsLoader.RunType.Unload);
        Assets.INSTANCE.clear();
    }

    @Override
    public void disposeManaged() {
        Gdx.app.log(TAG,"disposeManaged()");
    }
}
