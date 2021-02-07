package com.edibeozkul.taptap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class TapTap extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture cat;
	Texture rock1;
	Texture rock2;
	Texture rock3;

	int score = 0;
	int scoredRock = 0;
	int gameState = 0;
	float catX = 0;
	float catY = 0;
	float velocity = 0;
	float distance = 0;
	float rockVelocity= 7;
	float gravity = 0.3f;

	Random random;
	BitmapFont scoreFont;
	BitmapFont gameOverFont;
	BitmapFont font;
	Circle catCircle;
	//ShapeRenderer shapeRenderer;

	int numberOfRocks = 4;
	float [] rockX = new float[numberOfRocks];
	float [] rockOffSet = new float[numberOfRocks];
	float [] rockOffSet2 = new float[numberOfRocks];
	float [] rockOffSet3 = new float[numberOfRocks];

	Circle [] rockCircles;
	Circle [] rockCircles2;
	Circle [] rockCircles3;



	@Override
	public void create () {
		//shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		background = new Texture("background.png");
		cat = new Texture("cat.png");

		rock1 = new Texture("rock.png");
		rock2 = new Texture("rock.png");
		rock3 = new Texture("rock.png");

		distance = Gdx.graphics.getWidth() / 4;
		random = new Random();

		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.WHITE);
		scoreFont.getData().setScale(5);

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(3);

		gameOverFont = new BitmapFont();
		gameOverFont.setColor(Color.WHITE);
		gameOverFont.getData().setScale(5);

		catX = Gdx.graphics.getWidth() / 5;
		catY = Gdx.graphics.getHeight() / 3;

		catCircle = new Circle();
		rockCircles = new Circle[numberOfRocks];
		rockCircles2 = new Circle[numberOfRocks];
		rockCircles3 = new Circle[numberOfRocks];

		for (int i = 0; i < numberOfRocks; i++){
			rockOffSet[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);
			rockOffSet2[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);
			rockOffSet3[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);

			rockX[i] = Gdx.graphics.getWidth() - rock1.getWidth() / 2 + i * distance;

			rockCircles[i] = new Circle();
			rockCircles2[i] = new Circle();
			rockCircles3[i] =  new Circle();
		}

	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {
			if (rockX[scoredRock] < Gdx.graphics.getWidth() / 5) {
				score++;
				if (scoredRock < numberOfRocks - 1) {
					scoredRock++;
				} else {
					scoredRock = 0;
				}
			}
			if (Gdx.input.justTouched()) { ;
				velocity = -7;
			}

			for (int i = 0; i < numberOfRocks; i++) {
				if (rockX[i] < 0) {
					rockX[i] = rockX[i] + numberOfRocks * distance;
					rockOffSet[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);
					rockOffSet2[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);
					rockOffSet3[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);
				} else {
					rockX[i] = rockX[i] - rockVelocity;
				}
				batch.draw(rock1, rockX[i], Gdx.graphics.getHeight() / 2 + rockOffSet[i], Gdx.graphics.getWidth() / 12, Gdx.graphics.getHeight() / 8);
				batch.draw(rock2, rockX[i], Gdx.graphics.getHeight() / 2 + rockOffSet2[i], Gdx.graphics.getWidth() / 12, Gdx.graphics.getHeight() / 8);
				batch.draw(rock3, rockX[i], Gdx.graphics.getHeight() / 2 + rockOffSet3[i], Gdx.graphics.getWidth() / 12, Gdx.graphics.getHeight() / 8);

				rockCircles[i] = new Circle(rockX[i] + Gdx.graphics.getWidth() / 24, Gdx.graphics.getHeight() / 2 + rockOffSet[i] + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 24);
				rockCircles2[i] = new Circle(rockX[i] + Gdx.graphics.getWidth() / 24, Gdx.graphics.getHeight() / 2 + rockOffSet2[i] + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 24);
				rockCircles3[i] = new Circle(rockX[i] + Gdx.graphics.getWidth() / 24, Gdx.graphics.getHeight() / 2 + rockOffSet3[i] + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 24);

			}

			if (catY > 0) {
				velocity = velocity + gravity;
				catY = catY - velocity;
			} else {
				gameState = 2;
			}

		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
			} else if (gameState == 2) {
				gameOverFont.draw(batch, "Game Over!", 900, 700);
				font.draw(batch, " Tap To Play Again!", 900, 600);
				if (Gdx.input.justTouched()) {
					gameState = 1;
					catY = Gdx.graphics.getHeight() / 3;
					for (int i = 0; i < numberOfRocks; i++) {
						rockOffSet[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);
						rockOffSet2[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);
						rockOffSet3[i] = (random.nextFloat() - 0.7f) * (Gdx.graphics.getHeight() - 100);

						rockX[i] = Gdx.graphics.getWidth() - rock1.getWidth() / 2 + i * distance;

						rockCircles[i] = new Circle();
						rockCircles2[i] = new Circle();
						rockCircles3[i] = new Circle();
					}
					velocity = 0;
					score = 0;
					scoredRock = 0;
				}
			}

			batch.draw(cat, catX, catY, Gdx.graphics.getWidth() / 14, Gdx.graphics.getHeight() / 8);
			scoreFont.draw(batch, String.valueOf(score), 70, 1000);
			batch.end();
			catCircle.set(catX + Gdx.graphics.getWidth() / 28, catY + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 28);

			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.BLACK);
			//shapeRenderer.circle(catCircle.x, catCircle.y, catCircle.radius);

			for (int i = 0; i < numberOfRocks; i++) {
				//shapeRenderer.circle(rockX[i] + Gdx.graphics.getWidth() / 24, Gdx.graphics.getHeight() / 2 + rockOffSet[i] + Gdx.graphics.getHeight() / 16,Gdx.graphics.getWidth() / 24);
				//shapeRenderer.circle(rockX[i] + Gdx.graphics.getWidth() / 24, Gdx.graphics.getHeight() / 2 + rockOffSet2[i] + Gdx.graphics.getHeight() / 16,Gdx.graphics.getWidth() / 24);
				//shapeRenderer.circle(rockX[i] + Gdx.graphics.getWidth() / 24, Gdx.graphics.getHeight() / 2 + rockOffSet3[i] + Gdx.graphics.getHeight() / 16,Gdx.graphics.getWidth() / 24);
				if (Intersector.overlaps(catCircle, rockCircles[i]) || Intersector.overlaps(catCircle, rockCircles2[i]) || Intersector.overlaps(catCircle, rockCircles3[i])) {
					gameState = 2;
				}
			}
			//shapeRenderer.end();
		}

	
	@Override
	public void dispose () {

	}
}
