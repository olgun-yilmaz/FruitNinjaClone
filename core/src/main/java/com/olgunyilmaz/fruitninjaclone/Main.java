package com.olgunyilmaz.fruitninjaclone;

import static java.lang.Math.abs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

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

    Random random = new Random();
    Array<Fruit> fruitArray = new Array<>();

    int lives = 0;
    int score = 0;
    int highScore;
    float genCounter = 0.0f;
    private final float startGenSpeed = 1.1f;
    float genSpeed = startGenSpeed;
    int maxDimension;
    private double current_time;
    private double game_over_time = -1.0f;
    DataSaver dataSaver;



    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("ninjabackground.png");
        apple = new Texture("apple.png");
        bill = new Texture("bill.png");
        cherry = new Texture("cherry.png");
        ruby = new Texture("ruby.png");

        dataSaver = new DataSaver();
        highScore = dataSaver.getData();

        maxDimension = Math.max(Gdx.graphics.getHeight(),Gdx.graphics.getWidth()); //Landspace or portrait?
        Fruit.radius = maxDimension / 30f;

        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3);


    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        double newTime = TimeUtils.millis() / 1000.0;
        double frameTime = Math.min(newTime - current_time,0.3); // if bigger than 0.3
        float deltaTime = (float) frameTime;
        current_time = newTime;


        if (lives == 0 && game_over_time == 0f){
            // game over
            game_over_time = current_time;
        }

        if (lives > 0){
            // game mode

            genSpeed -= deltaTime *0.015f;
            if (score > highScore){
                updateHighScore();
            }


            if (genCounter <= 0){
                genCounter = genSpeed;
                addItem();
            }else{
                genCounter -= deltaTime;
            }

            for (int i = 1; i <= lives; i++){
                batch.draw(apple,i*75f,Gdx.graphics.getHeight()-50f,apple.getWidth()/20,apple.getHeight()/20);
            }

            for (Fruit fruit : fruitArray){
                fruit.update(deltaTime);

                Texture texture = apple ; // default case


                switch (fruit.type){
                    case LIFE :
                        texture = bill;
                        break;
                    case ENEMY:
                        texture = ruby;
                        break;
                    case EXTRA:
                        texture = cherry;
                        break;
                }

                batch.draw(texture,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);

            }

            boolean holdLives = false;
            Array <Fruit> toRemove = new Array<>();
            for (Fruit fruit : fruitArray){
                if (fruit.outOfScreen()){
                    toRemove.add(fruit);

                    if (fruit.isLiving && fruit.type == Fruit.Type.REGULAR){ // if apple
                        lives --;
                        holdLives = true;
                        break;
                    }
                }
            }

            if (holdLives){
                for (Fruit fruit : fruitArray){
                    fruit.isLiving = false; // if any fruit falls off the fruit set, don't reduce the lives.
                }
            }

            for (Fruit fruit : toRemove){
                fruitArray.removeValue(fruit,true);
            }

        }else{
            font.draw(batch,"Cut to play!",(int)(Gdx.graphics.getWidth()/2.5),Gdx.graphics.getHeight()/2);
        }

        font.draw(batch,"SCORE : "+score+"  ",(float) (Gdx.graphics.getWidth()/1.5),Gdx.graphics.getHeight()-25);
        font.draw(batch,"HIGH SCORE : "+highScore,(float) (Gdx.graphics.getWidth()/1.25),Gdx.graphics.getHeight()-25);
        batch.end();
    }

    private void addItem(){
        float pos = random.nextFloat() * maxDimension;
        float positionOffset = abs(random.nextFloat() - .5f);
        Fruit item = new Fruit(new Vector2(pos,-Fruit.radius), new Vector2(
            (Gdx.graphics.getWidth()*.5f)*positionOffset,(Gdx.graphics.getHeight()*.5f)));

        float type = random.nextFloat();
        if (type > 0.98){ // bill ->%2
            item.type = Fruit.Type.LIFE;
        }else if(type > 0.88){ //cherry -> % 10
            item.type = Fruit.Type.EXTRA;
        }else if(type > 0.75){ // enemy -> % 13
            item.type = Fruit.Type.ENEMY;
        }

        fruitArray.add(item);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

    }

    private void updateHighScore(){
        dataSaver.saveData(score);
        highScore = score;
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
    public boolean touchDragged(int screenX, int screenY, int pointer) { // cut
        if (lives <= 0 && current_time - game_over_time > 2f){
            // menu mode
            game_over_time = 0f;
            score = 0;
            lives = 4;
            genSpeed = startGenSpeed;
            fruitArray.clear();
        }else{
            // game mode
            Array <Fruit> toRemove = new Array<>();
            Vector2 pos = new Vector2(screenX,Gdx.graphics.getHeight()-screenY);
            int plus_score = 0;


            for (Fruit fruit : fruitArray){
                if (fruit.isClicked(pos)){
                    toRemove.add(fruit);

                    System.out.println("distance : "+pos.dst2(fruit.pos));
                    System.out.println("distance : "+fruit.isClicked(pos));
                    System.out.println("distance : "+Fruit.radius*Fruit.radius + 1);


                    switch (fruit.type){
                        case REGULAR:
                            plus_score ++;
                            break;
                        case LIFE :
                            lives ++;
                            break;
                        case ENEMY:
                            lives --;
                            break;
                        case EXTRA:
                            plus_score += 2;
                            score ++;
                            break;
                    }
                }
            }
            score += plus_score * plus_score;

            for (Fruit fruit : toRemove){
                fruitArray.removeValue(fruit,true);
            }

        }
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
