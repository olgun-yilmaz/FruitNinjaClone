package com.olgunyilmaz.fruitninjaclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2; // yön belirtmek ve iki tane yapıyı kullanmak

public class Fruit {
    public static float radius = 50f;
    public enum Type {
        REGULAR , EXTRA, ENEMY, LIFE
    }

    Type type;
    Vector2 pos;
    Vector2 velocity;

    public boolean isLiving = true;

    Fruit(Vector2 pos, Vector2 velocity){
        this.pos = pos;
        this.velocity = velocity;
        type = Type.REGULAR;
    }

    public boolean isClicked(Vector2 click){
        if (pos.dst2(click) <= radius * radius + 1){ // dst -> distance || dst2 faster than dst
            return true;
        }
        return false;
    }

    public final Vector2 getPos(){
        return pos;
    }

    public boolean outOfScreen(){
        return (pos.y < -2f * radius);
    }

    public void update(float deltaTime){
        velocity.y -= deltaTime * (Gdx.graphics.getHeight()*0.15f);
        velocity.x = deltaTime * Math.signum(velocity.x) * 2f;
        pos.mulAdd(velocity,deltaTime);
    }

}
