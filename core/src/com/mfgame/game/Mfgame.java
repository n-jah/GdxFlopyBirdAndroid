package com.mfgame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Mfgame extends ApplicationAdapter {
    public Random random;
    SpriteBatch batch;
    Texture background,dizzy;
    Texture man[];
    int manState = 0;
    int pause = 0;
    float gravity = 0.2f;
    float velocity = 0;
    int manY = 0;
    Rectangle manRect;
    ArrayList<Integer> coinXs = new ArrayList<Integer>();
    ArrayList<Integer> coinYs = new ArrayList<Integer>();
    Texture coin;
    int coinCount;
    ArrayList<Integer> bombXs = new ArrayList<Integer>();
    ArrayList<Integer> bombYs = new ArrayList<Integer>();
    //rect
    ArrayList<Rectangle> coinRectangle = new ArrayList<Rectangle>();
    ArrayList<Rectangle> bombRectangle = new ArrayList<Rectangle>();
    Texture bomb;
    int bombCount;
    int score = 0;
    int gameState = 0;
    BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4];
        man[0] = new Texture("frame-1.png");
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");
        dizzy = new Texture("dizzy-1.png");
		manY = ((int) Gdx.graphics.getHeight() / 2);

        coin = new Texture("coin.png");
        bomb = new Texture("bomb.png");
        random = new Random();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);


    }

    public void makeCoin() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        coinYs.add((int) height);
        coinXs.add(Gdx.graphics.getWidth());
        //puts it in right of screen
    }

    public void makeBomb() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bombYs.add((int) height);
        bombXs.add(Gdx.graphics.getWidth());
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (gameState == 1) {
            //it's live game

            //it's like a delay
            if (coinCount < 100) {
                coinCount++;
            } else {
                coinCount = 0;
                makeCoin();
            }

            if (bombCount < 300) {
                bombCount++;
            } else {
                bombCount = 0;
                makeBomb();
            }

            coinRectangle.clear();
//this for move coins
            for (int i = 0; i < coinXs.size(); i++) {
                batch.draw(coin, coinXs.get(i), coinYs.get(i));
                coinXs.set(i, coinXs.get(i) - 5);
                coinRectangle.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
            }
            bombRectangle.clear();
//lets move bombs also
            for (int i = 0; i < bombXs.size(); i++) {
                batch.draw(bomb, bombXs.get(i), bombYs.get(i));
                bombXs.set(i, bombXs.get(i) - 7);
                bombRectangle.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));

            }

            if (Gdx.input.justTouched()) {
                velocity = -13;
            }
            if (pause < 10) {
                pause++;
            } else {
                pause = 0;
                if (manState < 3) {
                    manState++;

                } else {
                    manState = 0;
                }
            }
            velocity += gravity;
            manY -= velocity;
            if (manY <= 0) {
                manY = 0;
            }
        }
        if (gameState == 0) {
            //hold
			if (Gdx.input.justTouched()){
				gameState =1;

			}

        }
        if (gameState == 2) {
			//game over
			if (Gdx.input.justTouched()){
				gameState =1;
				manY = Gdx.graphics.getHeight()/2;
				score = 0;
				velocity = 0 ;
				coinXs.clear();
				coinYs.clear();
				bombYs.clear();
				bombXs.clear();
				bombRectangle.clear();
				coinRectangle.clear();
				coinCount = 0;
				bombCount = 0;
			}

		}


		if (gameState == 2){

			batch.draw(dizzy, (Gdx.graphics.getWidth() / 2) - man[manState].getWidth() / 2, manY);
		}else {
			batch.draw(man[manState], (Gdx.graphics.getWidth() / 2) - man[manState].getWidth() / 2, manY);

		}

		manRect = new Rectangle((Gdx.graphics.getWidth() / 2) - man[manState].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getHeight());

			for (int i = 0; i < coinRectangle.size(); i++) {
				if (Intersector.overlaps(manRect, coinRectangle.get(i))) {
					score++;
					coinYs.remove(i);
					coinXs.remove(i);
					coinRectangle.remove(i);
					//to hide it
					break;
				}

			}

			for (int i = 0; i < bombRectangle.size(); i++) {
				if (Intersector.overlaps(manRect, bombRectangle.get(i))) {
					gameState = 2;
				}

			}




			font.draw(batch, String.valueOf(score), 100, 200);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
