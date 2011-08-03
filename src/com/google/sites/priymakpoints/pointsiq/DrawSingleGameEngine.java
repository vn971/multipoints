package com.google.sites.priymakpoints.pointsiq;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.List;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.DotType;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract;

public class DrawSingleGameEngine{
	
	private int sizeX=20;
	private int sizeY=20;
	Point lastAI=null,lastHuman=null;
	private int offsetX=1;
	private int offsetY=3;
	private int squareSize=16;
	private int pointSize=(int)(squareSize/2);
	
public void setLastAI(int x,int y){if(x==0|y==0)lastAI=null;else lastAI=new Point(x, y);}
public void setLastHuman(int x,int y){if(x==0|y==0)lastHuman=null;else lastHuman=new Point(x, y);}

public void drawPoint(Graphics graphics,int x,int y,Color color){
	int pixelX = getPixel(x, y).x;
	int pixelY = getPixel(x, y).y;
	int pointDiameter = pointSize;
	int drawX = pixelX - pointSize/2;
	int drawY = pixelY - pointSize/2;
	graphics.setColor(color);
	graphics.fillOval(drawX, drawY, pointDiameter-2, pointDiameter-2);
}
	
public void paint(Graphics graphics,SingleGameEngine singleGameEngine){
	graphics.setColor(new Color(225,225,225));
	for (int x = 1; x <= sizeX; x++) 
		graphics.drawLine(getPixel(x, 0.5).x, getPixel(x, 0.5).y, getPixel(x,sizeY + 0.5).x, getPixel(x, sizeY + 0.5).y);
	for (int y = 1; y <= sizeY; y++)
		graphics.drawLine(getPixel(0.5, y).x, getPixel(0.5, y).y, getPixel(sizeX + 0.5, y).x, getPixel(sizeX + 0.5, y).y);
	List<SurroundingAbstract> surroundingsList = singleGameEngine.getSurroundings();
	
	((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	
	for (int x = 1; x <= sizeX; x++) {
		for (int y = 1; y <= sizeY; y++) {
					
			// <drawing-points>
			DotType dotType = singleGameEngine.getDotType(x, y);
			{
				
				// drawing circles
				int pixelX = getPixel(x, y).x;
				int pixelY = getPixel(x, y).y;
				int pointDiameter = pointSize-1;
				int drawX = pixelX - pointSize/2;
				int drawY = pixelY - pointSize/2;
				switch (dotType) {
					case BLUE:graphics.setColor(new Color(21, 96, 189, 255));graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);break;
					case BLUE_TIRED:graphics.setColor(new Color(0, 0, 255, 128));graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);break;
					case RED_EATED_BLUE:graphics.setColor(new Color(21, 96, 189, 255));graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);break;
					case RED:graphics.setColor(new Color(255, 0, 0, 255));graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);break;
					case RED_TIRED:graphics.setColor(new Color(255, 0, 0, 128));graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);break;
					case BLUE_EATED_RED:graphics.setColor(new Color(255, 0, 0, 255));graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);break;
					case BLUE_CTRL:break;
					case RED_CTRL:break;
					default:break;
				}
			}

		}
	}
	
	int pointDiameter = pointSize;
	
	if(lastHuman!=null){
		graphics.setColor(new Color(21, 96, 189, 100));
		int pixelX = getPixel(lastHuman.x, lastHuman.y).x;
		int pixelY = getPixel(lastHuman.x, lastHuman.y).y;
		int drawX = pixelX - pointSize/2;
		int drawY = pixelY - pointSize/2;
		graphics.drawOval(drawX-2, drawY-2, pointDiameter+2, pointDiameter+2);
		graphics.drawOval(drawX-3, drawY-3, pointDiameter+4, pointDiameter+4);
	}
	if(lastAI!=null){
		graphics.setColor(new Color(255, 0, 0, 100));
		int pixelX = getPixel(lastAI.x, lastAI.y).x;
		int pixelY = getPixel(lastAI.x, lastAI.y).y;
		int drawX = pixelX - pointSize/2;
		int drawY = pixelY - pointSize/2;
		graphics.drawOval(drawX-2, drawY-2, pointDiameter+2, pointDiameter+2);
		graphics.drawOval(drawX-3, drawY-3, pointDiameter+4, pointDiameter+4);
	}
	
	{
		// <drawing-surroundings>
		for (int surroundingIndex = 0; surroundingIndex < surroundingsList.size(); surroundingIndex++) {
			SurroundingAbstract surrounding=surroundingsList.get(surroundingIndex);

			Polygon polygon = new Polygon();
			for (int pathIndex = 0; pathIndex < surrounding.path.size(); pathIndex++) {
				int dotX = surrounding.path.get(pathIndex).x;
				int dotY = surrounding.path.get(pathIndex).y;
				int pixelX = getPixel(dotX, dotY).x;
				int pixelY = getPixel(dotX, dotY).y;
				polygon.addPoint(pixelX, pixelY);
			}
			switch (surrounding.type) {
				case BLUE:graphics.setColor(new Color(21, 96, 189, 90));break;
				case RED:graphics.setColor(new Color(255, 0, 0, 90));break;
				case BLUE_CTRL:graphics.setColor(new Color(21, 96, 189, 185));break;
				case RED_CTRL:graphics.setColor(new Color(255, 0, 0, 20));break;
			}
			graphics.fillPolygon(polygon);

			switch (surrounding.type) {
				case BLUE:graphics.setColor(new Color(21, 96, 189, 185));break;
				case RED:graphics.setColor(new Color(255, 0, 0, 255));break;
				case BLUE_CTRL:graphics.setColor(new Color(21, 96, 189, 185));break;
				case RED_CTRL:graphics.setColor(new Color(255, 0, 0, 0));break;
			}
			Graphics2D graphics2d = (Graphics2D) graphics;
			BasicStroke basicStroke=new BasicStroke((float) (3),BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			graphics2d.setStroke(basicStroke);

			graphics.drawPolygon(polygon);
		}
		// </drawing-surroundings>
	}

	{
		// <drawing-coordinates>
		Font font = graphics.getFont();
		font = font.deriveFont((squareSize * 0.5f));
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		for (int x = 1; x <= sizeX; x++) {
			String string = "" + (x);
			int stringWidth = graphics.getFontMetrics().stringWidth(string);
			Point pixel = getPixel(x, sizeY + 1.1);
			pixel.translate(-stringWidth / 2, 0);
			graphics.drawString(string, pixel.x, pixel.y);
		}
		for (int y = 1; y <= sizeY; y++) {
			String string = "" + (y);
			int stringWidth = graphics.getFontMetrics().stringWidth(string);
			int stringHeight = graphics.getFontMetrics().getHeight();
			Point pixel = getPixel(.25, y);
			pixel.translate(-stringWidth, +stringHeight / 2);
			graphics.drawString(string, pixel.x, pixel.y);
		}
		// </drawing-coordinates>
	}
}
	
private Point getPixel(double x, double y) {
	Point result = new Point();
	result.x = (int) (squareSize * (x + offsetX - 0.25));
	result.y = (int) (squareSize * (y + offsetY- 0.25));
	return result;
}
int getStringWidth(Graphics graphics, String string) {int result = graphics.getFontMetrics().stringWidth(string);return result;}

int getStringHeight(Graphics graphics, String string) {
	int result = graphics.getFontMetrics().getHeight();
	if ((string.toLowerCase().equals(string)) && (string.toUpperCase().equals(string))) {
		result -= 4;
	} else {
		result -= 1;
	}
	return result;

}

}
