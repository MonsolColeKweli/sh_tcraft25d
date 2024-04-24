package com.sebs.shtcraft_25d;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sebs.shtcraft_25d.Renderer.DrawCallCollector;

import glm.vec._2.Vec2;

public class Zombie extends WorldEntity
{
	private static Image zombieImage;
	private static Image getZombieImage()
	{
		if (Zombie.zombieImage == null)
		{
			Zombie.zombieImage = Utils.loadImage("demo.jpeg");
		}
		
		return Zombie.zombieImage;
	}
	
	private Vec2 prev;
	private Vec2 dir;
	private final double tTravelGoal = 3.0;
	private double t = 0;
	private State state;
	
	Zombie(Vec2 start)
	{	
		super(Zombie.getZombieImage(), new Vec2(start), 1.0);
		
		this.prev = new Vec2(0.0, 0.0);
		this.dir = new Vec2((Math.random() * 2.0) - 1.0, (Math.random() * 2.0) - 1.0).normalize();
		this.t = tTravelGoal - 0.00001;
		this.state = State.Travel;
		
	}
	
	private Vec2 calculateCurrentPos()
	{
		Vec2 workingPos = new Vec2(this.prev);
		
		workingPos.add(new Vec2(this.dir).mul((float)this.t));
		
		return workingPos;
	}
	
	@Override public String toString()
	{		
		return String.format("Zombie @ %s", this.calculateCurrentPos().toString());
	}
	
	@Override
	public void tick(double deltaTime)
	{
		switch (this.state)
		{
		case Randomize:
			this.prev = this.calculateCurrentPos();
			
			this.dir = new Vec2((Math.random() - 0.5), (Math.random() - 0.5)).normalize();

			this.t = 0.0;
			break;
		case Travel:
			this.t += deltaTime;
		}
		
		
		
		switch (this.state)
		{
		case Randomize:
			this.state = State.Travel;
			break;
			
		case Travel:
			if (this.t > tTravelGoal)
			{
				this.state = State.Randomize;
			}
			else
			{
				this.state = State.Travel;
			}
			break;
		}
		

		
	}

	@Override
	public void draw(DrawCallCollector d)
	{					
		Vec2 pos = this.calculateCurrentPos();
		
		d.drawFilledRectangleWorld(
				pos.x,
				pos.y,
				1,
				this.edgeLength,
				this.edgeLength,
				Color.RED);
	}
	
	private enum State
	{
		Randomize,
		Travel
	}

	@Override
	protected ColissionType getColissionType()
	{
		return ColissionType.Thick;
	}
}

