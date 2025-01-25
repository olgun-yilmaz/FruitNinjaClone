package com.olgunyilmaz.fruitninjaclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements InputProcessor {
    // for user input handle
    SpriteBatch batch;
    Texture background;
    Texture apple;
    Texture bill;
    Texture cherry;
    Texture ruby;
    BitmapFont font;

    int lives = 4;
    int score = 0;
    private double current_time;
    private double game_over_time = -1.0f;



    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("ninjabackground.png");
        apple = new Texture("apple.png");
        bill = new Texture("bill.png");
        cherry = new Texture("cherry.png");
        ruby = new Texture("ruby.png");

        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4);


    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        double newTime = TimeUtils.millis() / 1000.0;
        double frameTime = Math.min(newTime - current_time,0.3); // if bigger than 0.3
        System.out.println("frame time : "+frameTime);
        float deltaTime = (float) frameTime;
        System.out.println("delta time "+deltaTime);

        if (lives == 0 && game_over_time == 0f){
            // game over
            game_over_time = current_time;
        }

        if (lives > 0){
            // game mode

            for (int i = 1; i <= lives; i++){
                batch.draw(apple,i*75f,Gdx.graphics.getHeight()-50f,apple.getWidth()/20,apple.getHeight()/20);
            }
        }

        font.draw(batch,"SCORE : 0",30,70);
        font.draw(batch,"Cut to play!",(int)(Gdx.graphics.getWidth()/2.5),Gdx.graphics.getHeight()/2);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
