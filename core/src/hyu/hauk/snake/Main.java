package hyu.hauk.snake;

import com.badlogic.gdx.Gdx;
import hyu.hauk.snake.assets.AbstractAssetsManagedGame;
import hyu.hauk.snake.assets.Settings;
import hyu.hauk.snake.screens.MainMenuScreen;

public class Main extends AbstractAssetsManagedGame {
	private static final String NAME = Main.class.getSimpleName();
	private static final String TAG = "\\"+NAME;

	@Override
	public void createManaged () {
		Gdx.app.log(TAG,"Create Game(Test: AssetLoader)");
		Settings.load(Gdx.files);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void disposeManaged() {
		Gdx.app.log(TAG,"dispose()");
	}
}
