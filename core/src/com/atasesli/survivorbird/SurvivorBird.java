package com.atasesli.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	Random random;
	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture bee1,bee2,bee3;
	float birdX;
	float birdY;
	Integer bee1Y;
	Integer bee2Y;
	Integer bee3Y;
	int gameState = 0;
	int birdVelocity;
	int beeVelocity = 5;
	int numberOfBees = 4;
	int score = 0;
	int scoredEnemy = 0;
	float gravity = 0.1f;
	float[] beeX = new float[numberOfBees];
	float distance = 0;
	Circle birdCircle;
	Circle[] beeCircles1;
	Circle[] beeCircles2;
	Circle[] beeCircles3;
	BitmapFont scoreText,gameOverText;
	ShapeRenderer shapeRenderer;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("mainImages/background.png");
		bird = new Texture("mainImages/bird.png");
		bee1 = new Texture("mainImages/bee.png");
		bee2 = new Texture("mainImages/bee.png");
		bee3 = new Texture("mainImages/bee.png");
		distance = Gdx.graphics.getWidth() / 2;
		random = new Random();
		birdX = Gdx.graphics.getWidth() / 3 - bird.getHeight() / 2;
		birdY = Gdx.graphics.getHeight() / 3;
		birdCircle = new Circle();
		beeCircles1 = new Circle[numberOfBees];
		beeCircles2 = new Circle[numberOfBees];
		beeCircles3 = new Circle[numberOfBees];
		scoreText = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(4);
		gameOverText = new BitmapFont();
		gameOverText.setColor(Color.WHITE);
		gameOverText.getData().setScale(4);
		shapeRenderer = new ShapeRenderer();
		bee1Y = (int) (random.nextFloat() * (Gdx.graphics.getHeight() - bee1.getHeight()));
		bee2Y = (int) (random.nextFloat() * (Gdx.graphics.getHeight() - bee2.getHeight()));
		bee3Y = (int) (random.nextFloat() * (Gdx.graphics.getHeight() - bee3.getHeight()));
		for (int i = 0; i < numberOfBees; i++){
			beeX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;
			beeCircles1[i] = new Circle();
			beeCircles2[i] = new Circle();
			beeCircles3[i] = new Circle();
		}
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(bird,birdX,birdY
				,Gdx.graphics.getWidth() / 15
				,Gdx.graphics.getHeight() / 10);
		if (gameState == 1){
			if (beeX[scoredEnemy] < birdX){
				score++;
				if (scoredEnemy < 3){
					scoredEnemy++;
				}
				else {
					scoredEnemy = 0;
				}
			}
			if (Gdx.input.justTouched()){
				birdVelocity = -13;
			}
			for (int i = 0; i < numberOfBees; i++){
				if (beeX[i] < -bee1.getWidth()){
					beeX[i] += numberOfBees * distance;
				}
				else {
					beeX[i] -= beeVelocity;
				}
				updateBees();
			}
			if (birdY > 0) {
				birdVelocity++;
				birdY -= birdVelocity;
			}
			else {
				gameState = 2;
			}
		}
		else if (gameState == 0){
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		} else if (gameState == 2){
			gameOverText.draw(batch,"Game Over!",100,Gdx.graphics.getHeight() / 2);
			if (Gdx.input.justTouched()){
				gameState = 1;
				birdY = Gdx.graphics.getHeight() / 3;
				for (int i = 0; i < numberOfBees; i++){
					beeX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;
					beeCircles1[i] = new Circle();
					beeCircles2[i] = new Circle();
					beeCircles3[i] = new Circle();
				}
				birdVelocity = 0;
				scoredEnemy = 0;
				score = 0;
			}
		}
		scoreText.draw(batch,String.valueOf(score),100,200);
		batch.end();
		birdCircle.set(birdX + Gdx.graphics.getWidth() / 30,birdY + Gdx.graphics.getHeight() / 20,Gdx.graphics.getWidth() / 37);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		shapeRenderer.end();
		for (int i = 0; i < numberOfBees; i++){
			if (Intersector.overlaps(birdCircle,beeCircles1[i])
			||  Intersector.overlaps(birdCircle,beeCircles2[i])
			||  Intersector.overlaps(birdCircle,beeCircles3[i])){
				gameState = 2;
			}
		}
	}
	public void setBees(){
		double heightCount;
		heightCount = Math.floor(Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() / 15));
		ArrayList<Integer> heights = new ArrayList<Integer>();
		int sum = 0;
		for (int i = 0; sum < Gdx.graphics.getHeight(); i++){
			sum += heightCount;
			heights.add(sum);
		}
		Collections.shuffle(heights);
		bee1Y = heights.get(0);
		bee2Y = heights.get(1);
		bee3Y = heights.get(2);
	}
	public void updateBees(){
		float deltaTime = Gdx.graphics.getDeltaTime();
		for (int i = 0; i < numberOfBees; i++) {
			beeX[i] -= beeVelocity * deltaTime;
			beeCircles1[i].setPosition(beeX[i] + Gdx.graphics.getWidth() / 30, bee1Y + Gdx.graphics.getHeight() / 20);
			beeCircles2[i].setPosition(beeX[i] + Gdx.graphics.getWidth() / 30, bee2Y + Gdx.graphics.getHeight() / 20);
			beeCircles3[i].setPosition(beeX[i] + Gdx.graphics.getWidth() / 30, bee3Y + Gdx.graphics.getHeight() / 20);
			batch.draw(bee1,beeX[i],bee1Y,Gdx.graphics.getWidth() / 15,Gdx.graphics.getHeight() / 10);
			batch.draw(bee2,beeX[i],bee2Y, Gdx.graphics.getWidth() / 15,Gdx.graphics.getHeight() / 10);
			batch.draw(bee3,beeX[i],bee3Y,Gdx.graphics.getWidth() / 15,Gdx.graphics.getHeight() / 10);
			if (beeX[i] < -bee1.getWidth() / 2) {
				beeX[i] += numberOfBees * distance;
				setBees();
			}
			if (Intersector.overlaps(birdCircle, beeCircles1[i]) || Intersector.overlaps(birdCircle, beeCircles2[i]) || Intersector.overlaps(birdCircle, beeCircles3[i])) {
				gameState = 2;
			}
		}
	}
	@Override
	public void dispose () {
	}
}
