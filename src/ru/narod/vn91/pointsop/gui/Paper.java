package ru.narod.vn91.pointsop.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.JPanel;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.DotType;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract;
import ru.narod.vn91.pointsop.utils.CustomColors;
import ru.narod.vn91.pointsop.utils.Function2;
import ru.narod.vn91.pointsop.utils.Settings;

@SuppressWarnings("serial")
public abstract class Paper extends JPanel {

	static boolean doAnimation = true;
	private SingleGameEngineInterface engine;
	private Function2<Integer, Integer, String> coordinatesFormatter = new Function2<Integer, Integer, String>() {
		@Override
		public String call(Integer a, Integer b) {
			if (a != null && b != null) {
				return String.format("%02d:%02d", a, b);
			} else if (a != null) {
				return String.format("%02d", a);
			} else if (b != null) {
				return String.format("%02d", b);
			} else {
				return "";
			}
		}
	};
	boolean isGuiYInverted;

	Point cursorDot = null;
	int surroundingsAlreadyDrawed = 0;
	private double dotWidth = 0.5;
	private boolean drawConnections = true;
	private int squareSize;
	private double borderXcoeffecient = 0.7;
	private double borderYcoeffecient = 0.7;
	private int offsetX;
	private int offsetY;
	private Color colorRedPoint = new Color(255, 0, 0, 255);
	private Color colorBluPoint = new Color(21, 96, 189, 255);
	private Color colorBackground = new Color(254, 254, 254, 255);
	private Color colorGrid = CustomColors.getMixedColor(
			colorBackground,
			CustomColors.getContrastColor(colorBackground),
			0.75f
	);
	private Color colorPaperBorders = colorGrid;
	private Color colorRedTired =
			CustomColors.getAlphaModifiedColor(colorRedPoint, 128);
	private Color colorBluTired =
			CustomColors.getAlphaModifiedColor(colorBluPoint, 128);
	private Color colorRedEatedBlu = colorBluPoint;
	private Color colorBluEatedRed = colorRedPoint;
	private Color colorRedSurr =
			CustomColors.getAlphaModifiedColor(colorRedPoint, 90);
	private Color colorBluSurr =
			CustomColors.getAlphaModifiedColor(colorBluPoint, 90);
	private Color colorRedCtrlSurr =
			CustomColors.getAlphaModifiedColor(colorBackground, 0);
	private Color colorBluCtrlSurr =
			CustomColors.getAlphaModifiedColor(colorBackground, 0);

	protected void setColors(
			Color p1,
			Color p2,
			Color background) {
		colorRedPoint = p1;
		colorBluPoint = p2;
		colorBackground = background;
		colorGrid = CustomColors.getMixedColor(
				colorBackground,
				CustomColors.getContrastColor(colorBackground),
				0.75f
		);
		colorPaperBorders = colorGrid;
		colorRedTired = CustomColors.getAlphaModifiedColor(colorRedPoint, 128);
		colorBluTired = CustomColors.getAlphaModifiedColor(colorBluPoint, 128);
		colorRedEatedBlu = colorBluPoint;
		colorBluEatedRed = colorRedPoint;
		colorRedSurr = CustomColors.getAlphaModifiedColor(colorRedPoint, 90);
		colorBluSurr = CustomColors.getAlphaModifiedColor(colorBluPoint, 90);
		colorRedCtrlSurr = CustomColors.getAlphaModifiedColor(colorBackground, 0);
		colorBluCtrlSurr = CustomColors.getAlphaModifiedColor(colorBackground, 0);
	}

	public MoveResult makeMove(
			int x,
			int y) {
		return makeMove(false, x, y, !engine.getLastDotColor());
	}

	public MoveResult makeMove(int x, int y, boolean isRed) {
		return makeMove(false, x, y, isRed);
	}

	/**
	 * @param silent true if redrawing is needed
	 * @param x
	 * @param y
	 * @param isRed
	 * @return look SingleGameEngineInterface.makeMove()
	 */
	public MoveResult makeMove(
			boolean silent,
			int x,
			int y,
			boolean isRed) {
//		int previousX = -1, previousY = -1;
		if (engine.getLastDot() != null) {
//			previousX = engine.getLastDot().x;
//			previousY = engine.getLastDot().y;
		}
		SingleGameEngineInterface.MoveResult moveResult = engine.makeMove(
				x, y,
				isRed
		);
		if (silent == false) {
			repaint();
//			Graphics graphics = this.getGraphics();
//			if (moveResult == MoveResult.NOTHING) {
//				if (previousX != -1) {
//					drawPoint(graphics, previousX, previousY);
//				} else {
//				}
//				drawPoint(null, x, y);
//				drawLastDotHint(null);
//			} else if (moveResult == MoveResult.ERROR) {
//			} else {
////				if (previousX != -1) {
////					drawPoint(null, previousX, previousY);
////				}
////				for (int i = surroundingsAlreadyDrawed; i < engine.getSurroundings().size(); i++) {
////					SurroundingAbstract surrounding = engine.getSurroundings().get(i);
////					drawSurrounding(graphics, surrounding);
////				}
////				surroundingsAlreadyDrawed = engine.getSurroundings().size();
////				drawPoint(null, x, y);
////				drawLastDotHint(null);
//				repaint();
		}
		return moveResult;
	}

	int getRedScore() {
		return engine.getRedScore();
	}

	int getBlueScore() {
		return engine.getBlueScore();
	}

	SingleGameEngineInterface getEngine() {
		return engine;
	}

	public Paper() {
		super();
//		super.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
//		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//		{
//			URL url = SelfishGuiStarter.class.getResource("cursor-sight2.png");
//			Image image = new ImageIcon(url).getImage();
//			Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
//					image, new Point(
//					16, 16
//			), "cursor-sight"
//			);
//			super.setCursor(cursor);
//		}
		super.addMouseListener(
				new java.awt.event.MouseAdapter() {

					@Override
					public void mousePressed(java.awt.event.MouseEvent evt) {
						Point humanCoordinates = getHumanCoordinates(evt.getPoint());
						if (humanCoordinates != null) {
							paperClick(humanCoordinates.x, humanCoordinates.y, evt);
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {
						_paperMouseMove(-1, -1, e);
					}
				}
		);

		super.addMouseMotionListener(
				new MouseMotionListener() {

					public void mouseDragged(MouseEvent e) {
					}

					public void mouseMoved(MouseEvent e) {
						if (squareSize > 0) {
							Point humanCoordinates = getHumanCoordinates(e.getPoint());
							if (humanCoordinates != null) {
								_paperMouseMove(humanCoordinates.x, humanCoordinates.y, e);
							}
						}
					}
				}
		);
	}
	
	public void setCoordinatesFormatter(Function2<Integer, Integer, String> coordinatesFormatter) {
		this.coordinatesFormatter = coordinatesFormatter;
	}

	public abstract void paperClick(
			int x,
			int y,
			MouseEvent evt);

	public abstract void paperMouseMove(
			int x,
			int y,
			MouseEvent evt);

	public void _paperMouseMove(
			int x,
			int y,
			MouseEvent evt) {
		Point humanCoordinates = (x == -1) ? null : new Point(x, y);
		if (cursorDot != null && (cursorDot.equals(humanCoordinates))) {
		} else {
			if (cursorDot != null) {
				clearOldCursor();
			}
			cursorDot = humanCoordinates;
			if (cursorDot != null) {
				drawCursor();
			}
		}
		paperMouseMove(x, y, evt);
	}

	public void initPaper(
			int sizeX,
			int sizeY,
			boolean isGuiYInverted) {
		engine = new SingleGameEngine(sizeX, sizeY);
		this.isGuiYInverted = isGuiYInverted;
		repaint();
	}

	void clearOldCursor() {
		if (cursorDot == null) {
			return;
		}
		int x = cursorDot.x, y = cursorDot.y;
		if (engine.getDotType(x, y).isIn(
				DotType.EMPTY, DotType.RED_CTRL,
				DotType.BLUE_CTRL
		) == false) {
			return;
		}
		Graphics graphics = super.getGraphics();
		{
//			float fontSize = squareSize * 0.5f;
//			if (fontSize >= 7) {
				// if FONT < ....
//				Font font = graphics.getFont();
//				font = font.deriveFont(fontSize);
//				graphics.setFont(font);
//				graphics.setColor(Color.BLACK);
//				super.setBackground(Color.ORANGE);
//				((Graphics2D) graphics).setBackground(Color.black);
//				{
//					String string = "" + (x);
//					int stringWidth = graphics.getFontMetrics().stringWidth(
//							string
//					);
//					Point pixel = getPixel(x, -0.1);
//					pixel.translate(-stringWidth / 2, 0);
//					graphics.drawString(string, pixel.x, pixel.y);
//				}
//				{
//					String string = "" + (y);
//					int stringWidth = graphics.getFontMetrics().stringWidth(
//							string
//					);
//					int stringHeight = graphics.getFontMetrics().getHeight();
//					Point pixel = getPixel(.25, y);
//					pixel.translate(-stringWidth, +stringHeight / 2);
//					graphics.drawString(string, pixel.x, pixel.y);
//				}
//			}
		}

		graphics.setColor(colorBackground);
		int pointRadius = (int) (squareSize * dotWidth / 2);
		graphics.drawOval(
				getPixel(x, y).x - pointRadius,
				getPixel(x, y).y - pointRadius,
				2 * pointRadius,
				2 * pointRadius
		);
		graphics.setColor(colorGrid);
		graphics.drawLine(
				getPixel(x, y).x - pointRadius - 1,
				getPixel(x, y).y,
				getPixel(x, y).x + pointRadius + 1,
				getPixel(x, y).y
		);
		graphics.drawLine(
				getPixel(x, y).x,
				getPixel(x, y).y - pointRadius - 1,
				getPixel(x, y).x,
				getPixel(x, y).y + pointRadius + 1
		);
	}

	void drawCursor() {
		if (cursorDot == null) {
			return;
		}
		int x = cursorDot.x, y = cursorDot.y;
		if (engine.getDotType(x, y).isIn(
				DotType.EMPTY, DotType.RED_CTRL,
				DotType.BLUE_CTRL
		) == false) {
			return;
		}
		Graphics graphics = super.getGraphics();
		//BasicStroke basicStroke =
		//		new BasicStroke((float) (squareSize * dotWidth * 0.25),
		//		BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		//((Graphics2D) graphics).setStroke(basicStroke);
		if (engine.getLastDot() == null) {
			graphics.setColor(CustomColors.getContrastColor(colorBackground));
		} else if (engine.getLastDotColor() == true) {
			graphics.setColor(colorBluPoint);
		} else if (engine.getLastDotColor() == false) {
			graphics.setColor(colorRedPoint);
		}

		int ovalRadius = (int) (squareSize * dotWidth / 2);
		graphics.drawOval(
				getPixel(x, y).x - ovalRadius,
				getPixel(x, y).y - ovalRadius,
				2 * ovalRadius,
				2 * ovalRadius
		);
	}

	void drawLastDotHint(Graphics graphics) {
		if (graphics == null) {
			graphics = super.getGraphics();
			if (graphics == null) {
				return;
			}
		}
		if (engine.getLastDot() != null) {
			int x = engine.getLastDot().x, y = engine.getLastDot().y;
			if ((graphics.getClipBounds() != null)
					&& graphics.getClipBounds().intersects(
							getRectangleAroundDot(x, y)) == false) {
				// no intersection
			} else {
				// нарисовать последний ход

				if (doAnimation) {
					new LastMoveDrawer().start();
				} else {
					DotType dotType = engine.getDotType(x, y);
					int pointRadius = (int) (squareSize * dotWidth / 2);
					if (dotType == DotType.RED) {
						// цвет красной заливки
						graphics.setColor(colorRedSurr);
					} else {
						// цвет синей заливки
						graphics.setColor(colorBluSurr);
					}
					int pixelX = getPixel(x, y).x;
					int pixelY = getPixel(x, y).y;
					// нарисовать последний ход
					{
						int innerRadius = pointRadius + 1;
						graphics.drawOval(
								pixelX - innerRadius,
								pixelY - innerRadius,
								innerRadius * 2,
								innerRadius * 2
								);
					}
					{
						int innerRadius = pointRadius + 2;
						graphics.drawOval(
								pixelX - innerRadius,
								pixelY - innerRadius,
								innerRadius * 2,
								innerRadius * 2
								);
					}
				}
			}
		}
	}

	void drawSurrounding(
			Graphics graphics,
			SurroundingAbstract surrounding) {
		if (graphics == null) {
			graphics = super.getGraphics();
			if (graphics == null) {
				return;
			}
		}
		Polygon polygon = new Polygon();
		for (int pathIndex = 0; pathIndex < surrounding.path.size(); pathIndex++) {
			int dotX = surrounding.path.get(pathIndex).x;
			int dotY = surrounding.path.get(pathIndex).y;
			int pixelX = getPixel(dotX, dotY).x;
			int pixelY = getPixel(dotX, dotY).y;
			polygon.addPoint(pixelX, pixelY);
		}
		if ((graphics.getClipBounds() != null)
				&& polygon.getBounds().intersects(graphics.getClipBounds()) == false) {
			return;
		}
		switch (surrounding.type) {
			case BLUE:
				graphics.setColor(colorBluSurr);
				break;
			case RED:
				graphics.setColor(colorRedSurr);
				break;
			case BLUE_CTRL:
				graphics.setColor(colorBluCtrlSurr);
				break;
			case RED_CTRL:
				graphics.setColor(colorRedCtrlSurr);
				break;
		}
		graphics.fillPolygon(polygon);

		switch (surrounding.type) {
			case BLUE:
				graphics.setColor(colorBluPoint);
				break;
			case RED:
				graphics.setColor(colorRedPoint);
				break;
			case BLUE_CTRL:
				graphics.setColor(colorBluCtrlSurr);
				break;
			case RED_CTRL:
				graphics.setColor(colorRedCtrlSurr);
				break;
		}
		Graphics2D graphics2d = (Graphics2D) graphics;
		int lineWidth = (int) (squareSize * dotWidth * 0.125) * 2 + 1;
		BasicStroke basicStroke =
				new BasicStroke(
						lineWidth,
						BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
				);
		graphics2d.setStroke(basicStroke);
		graphics.drawPolygon(polygon);
		graphics2d.setStroke(new BasicStroke());
	}

	void drawConnection(
			Graphics graphics,
			int x1,
			int y1,
			int x2,
			int y2) {
		if (graphics == null) {
			graphics = super.getGraphics();
			if (graphics == null) {
				return;
			}
		}
		if ((graphics.getClipBounds() != null)
				&& (graphics.getClipBounds().intersects(
				getRectangleAroundDot(
						x1,
						y1
				)
		) == false)
				&& (graphics.getClipBounds().intersects(
				getRectangleAroundDot(
						x2,
						y2
				)
		) == false)) {
			return;
		}
		int dx = x2 - x1, dy = y2 - y1;
		DotType type1 = engine.getDotType(x1, y1), type2 = engine.getDotType(
				x2,
				y2
		);

		if ((dx >= -1) && (dx <= 1)
				&& (dy >= -1) && (dy <= 1)
				&& ((dx != 0) || (dy != 0))
				&& (type1 == type2)
				&& ((type1 == DotType.BLUE) || (type1 == DotType.RED))) {
			if (type1 == DotType.RED) {
				graphics.setColor(colorRedPoint);
			} else {
				graphics.setColor(colorBluPoint);
			}
			int lineWidth = (int) (squareSize * dotWidth * 0.125) * 2 + 1;
			BasicStroke basicStroke =
					new BasicStroke(
							lineWidth,
							BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
					);
			((Graphics2D) graphics).setStroke(basicStroke);
			Point pixel1 = getPixel(x1, y1);
			Point pixel2 = getPixel(x2, y2);
			graphics.drawLine(pixel1.x, pixel1.y, pixel2.x, pixel2.y);
			((Graphics2D) graphics).setStroke(new BasicStroke());
		}


	}

	void drawPoint(
			Graphics graphics,
			int x,
			int y,
			boolean paintEmpty) {
		if (graphics == null) {
			graphics = super.getGraphics();
			if (graphics == null) {
				return;
			}
		}
		if ((graphics.getClipBounds() != null)
				&& getRectangleAroundDot(x, y).intersects(
				graphics.getClipBounds()
		) == false) {
			return;
		}

		DotType dotType = engine.getDotType(x, y);
		{
			int pixelX = getPixel(x, y).x;
			int pixelY = getPixel(x, y).y;
			int pointRadius = (int) (squareSize * dotWidth / 2);
			int pointDiameter = 2 * pointRadius - 1;
			int drawX = pixelX - pointRadius + 1;
			int drawY = pixelY - pointRadius + 1;
			switch (dotType) {
				case BLUE:
					graphics.setColor(colorBluPoint);
					graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
					break;
				case BLUE_TIRED:
					graphics.setColor(colorBluTired);
					graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
					break;
				case RED_EATED_BLUE:
					graphics.setColor(colorRedEatedBlu);
					graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
					break;
				case RED:
					graphics.setColor(colorRedPoint);
					graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
					break;
				case RED_TIRED:
					graphics.setColor(colorRedTired);
					graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
					break;
				case BLUE_EATED_RED:
					graphics.setColor(colorBluEatedRed);
					graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
					break;
				default:
					break;
			}

			if (paintEmpty && dotType.isIn(
					DotType.EMPTY, DotType.RED_CTRL,
					DotType.BLUE_CTRL
			)) {
				graphics.setColor(colorBackground);
				graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
				graphics.setColor(colorGrid);
				graphics.drawLine(
						pixelX, pixelY - pointRadius + 1, pixelX,
						pixelY + pointRadius - 1
				);
				graphics.drawLine(
						pixelX - pointRadius + 1, pixelY,
						pixelX + pointRadius - 1, pixelY
				);
			}
		}
	}

	void drawPoint(
			Graphics graphics,
			int x,
			int y) {
		drawPoint(graphics, x, y, false);
	}

	@Override
	public void paint(Graphics graphics) {
		dotWidth = Settings.getDotWidth();
		drawConnections = Settings.getDrawConnections();
		setColors(
				Settings.getPlayer1Color(),
				Settings.getPlayer2Color(),
				Settings.getBackgroundColor()
		);
		if (engine == null) {
			return;
		}
		int engineSizeX = engine.getSizeX();
		int engineSizeY = engine.getSizeY();
		{
			// clearing the paper
			int paperSizeX = this.getWidth();
			int paperSizeY = this.getHeight();
			double squareKoefficientX = paperSizeX / (engineSizeX + .5d + borderXcoeffecient);
			double squareKoefficientY = paperSizeY / (engineSizeY + .5d + borderYcoeffecient);
			squareSize = Math.max(1,
				(int) Math.min(squareKoefficientX, squareKoefficientY));
			offsetX = (int) (paperSizeX - squareSize * (engineSizeX + borderXcoeffecient)) / 2;
			offsetY = (int) (paperSizeY - squareSize * (engineSizeY + borderYcoeffecient)) / 2;
			graphics.setColor(colorBackground);
			graphics.fillRect(
					graphics.getClipBounds().x,
					graphics.getClipBounds().y,
					graphics.getClipBounds().width,
					graphics.getClipBounds().height
			);
		}

		{
			// <drawing-grid>
			graphics.setColor(colorGrid);
			for (int x = 1; x <= engineSizeX; x++) {
				graphics.drawLine(
						getPixel(x, 0.5).x, getPixel(x, 0.5).y, getPixel(
						x,
						engineSizeY + 0.5
				).x, getPixel(x, engineSizeY + 0.5).y
				);
			}
			for (int y = 1; y <= engineSizeY; y++) {
				graphics.drawLine(
						getPixel(0.5, y).x, getPixel(0.5, y).y, getPixel(
						engineSizeX + 0.5, y
				).x,
						getPixel(engineSizeX + 0.5, y).y
				);
			}
			// </drawing-grid>
		}

		if (drawConnections) {
			for (int x = 1; x <= engineSizeX - 1; x++) {
				for (int y = 1; y <= engineSizeY; y++) {
					drawConnection(graphics, x, y, x + 1, y);
				}
			}
			for (int x = 1; x <= engineSizeX; x++) {
				for (int y = 1; y <= engineSizeY - 1; y++) {
					drawConnection(graphics, x, y, x, y + 1);
				}
			}
		}

		((Graphics2D) graphics).setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		);

		for (int x = 1; x <= engineSizeX; x++) {
			for (int y = 1; y <= engineSizeY; y++) {
				// draw DEAD points
				if (engine.getDotType(x, y).isIn(
						DotType.BLUE_EATED_RED,
						DotType.BLUE_TIRED, DotType.RED_EATED_BLUE,
						DotType.RED_TIRED
				)) {
					drawPoint(graphics, x, y);
				}
			}
		}

		for (int x = 1; x <= engineSizeX; x++) {
			for (int y = 1; y <= engineSizeY; y++) {
				// draw DEAD points
				if (engine.getDotType(x, y).isIn(
						DotType.BLUE_EATED_RED,
						DotType.BLUE_TIRED, DotType.RED_EATED_BLUE,
						DotType.RED_TIRED
				)) {
					drawPoint(graphics, x, y);
				}
			}
		}

		for (SurroundingAbstract surrounding : engine.getSurroundings()) {
			drawSurrounding(graphics, surrounding);
		}

		for (int x = 1; x <= engineSizeX; x++) {
			for (int y = 1; y <= engineSizeY; y++) {
				// draw ALIVE points
				if (!engine.getDotType(x, y).isIn(
						DotType.BLUE_EATED_RED,
						DotType.BLUE_TIRED, DotType.RED_EATED_BLUE,
						DotType.RED_TIRED
				)) {
					drawPoint(graphics, x, y);
				}
			}
		}

		drawLastDotHint(graphics);

		{
			// drawing paper borders
			Graphics2D graphics2d = (Graphics2D) graphics;
			BasicStroke basicStroke =
					new BasicStroke(
							(float) (squareSize * dotWidth * 0.10),
							BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND
					);
			graphics2d.setStroke(basicStroke);
			graphics.setColor(colorPaperBorders);
			int x1 = getPixel(0.5, engineSizeY + 0.5).x;
			int y1 = getPixel(0.5, engineSizeY + 0.5).y;
			int x2 = getPixel(engineSizeX + 0.5, 0.5).x;
			int y2 = getPixel(engineSizeX + 0.5, 0.5).y;
			graphics.drawRect(x1, y1, x2 - x1, y2 - y1);
			((Graphics2D) graphics).setStroke(new BasicStroke());
		}

		{
			// <drawing-coordinates>
			float fontSize = squareSize * 0.5f;
			if (fontSize >= 7) {

				Font font = graphics.getFont();
				font = font.deriveFont(fontSize);
				// if FONT < ....
				graphics.setFont(font);
//				graphics.setColor(CustomColors.getContrastColor(Memory.getBackgroundColor()));
				graphics.setColor(Color.BLACK);
				for (int x = 1; x <= engineSizeX; x++) {
					String string = coordinatesFormatter.call(x, null);
//					String string = "" + (x);
					int stringWidth = graphics.getFontMetrics().stringWidth(
							string
					);
					Point pixel = getPixel(x, -0.1);
					pixel.translate(-stringWidth / 2, 0);
					graphics.drawString(string, pixel.x, pixel.y);
				}
				for (int y = 1; y <= engineSizeY; y++) {
					int yGui = isGuiYInverted
							? engineSizeY + 1 - y
							: y;
					String string = coordinatesFormatter.call(null, yGui);
//					String string = "" + (y);
					int stringWidth = graphics.getFontMetrics().stringWidth(
							string
					);
					int stringHeight = graphics.getFontMetrics().getHeight();
					Point pixel = getPixel(.25, y);
					pixel.translate(-stringWidth, +stringHeight / 2);
					graphics.drawString(string, pixel.x, pixel.y);
				}
			}
			// </drawing-coordinates>
		}
	}

//	private int getStringWidth(
//			Graphics graphics,
//			String string) {
//		int result = graphics.getFontMetrics().stringWidth(string);
//		return result;
//	}
//
//	private int getStringHeight(
//			Graphics graphics,
//			String string) {
//		int result = graphics.getFontMetrics().getHeight();
//		if ((string.toLowerCase().equals(string)) && (string.toUpperCase().equals(
//				string
//		))) {
//			result -= 4;
//		} else {
//			result -= 1;
//		}
//		return result;
//	}

	private Point getHumanCoordinates(Point pixel) {
		// we are finding out the position of the click.
		// this is done to easily play with offset constants.
		// ugly, I know.
//		{
//			int bestX = 1, bestY = 1;
//			double bestDistance = Double.POSITIVE_INFINITY;
//			for (int x = 1; x <= engine.getSizeX(); x++) {
//				for (int y = 1; y <= engine.getSizeY(); y++) {
//					if (pixel.distance(getPixel(x, y)) < bestDistance) {
//						bestX = x;
//						bestY = y;
//						bestDistance = (double) (pixel.distance(getPixel(x, y)));
//					}
//				}
//			}
//			if (bestDistance > squareSize) {
//				return new Point(-1, -1);
//			} else {
//				return new Point(bestX, bestY);
//			}
//		}
		int resultX, resultY;
		resultX = (int) (((pixel.x - offsetX) / squareSize) + 0.75 - borderXcoeffecient);
		resultY = (int) ((((double) pixel.y - offsetY) / squareSize) + 0.75);
		resultY = engine.getSizeY() + 1 - resultY;
		if ((resultX > 0) && (resultY > 0)
				&& (resultX <= engine.getSizeX()) && (resultY <= engine.getSizeY())) {
			return new Point(resultX, resultY);
		} else {
			return new Point(-1, -1);
		}

	}

	private Point getPixel(
			double x,
			double y) {
		Point result = new Point();
		y = engine.getSizeY() + 1 - y;
		result.x = (int) (offsetX + squareSize * (x + borderXcoeffecient - 0.25));
		result.y = (int) (offsetY + squareSize * (y - 0.25));
		return result;
	}

	private Rectangle getRectangleAroundDot(
			int x,
			int y) {
		Point pixel = getPixel(x, y);
		int pointRadius = (int) (squareSize * dotWidth / 2);
		return new Rectangle(
				pixel.x - pointRadius, pixel.y - pointRadius,
				pointRadius * 2, pointRadius * 2
		);
	}

	class LastMoveDrawer extends Thread {

		public void run() {
			Graphics graphics = Paper.this.getGraphics();

			long animationTotalTime = 200L;
			long timeOut = new Date().getTime() + animationTotalTime;
			long animationStep = animationTotalTime / 10;

			int x = engine.getLastDot().x, y = engine.getLastDot().y;
			int pixelX = getPixel(x, y).x;
			int pixelY = getPixel(x, y).y;
			int pointRadius = (int) (squareSize * dotWidth / 2);

			for (;;) {
				try {
					new Object().wait(animationStep);
				} catch (Exception e) {
				}
				// totalTime += 20;
				long currentTime = new Date().getTime();
				if (currentTime > timeOut) {
					break;
				}
				float animationPercent = ((float) (timeOut - currentTime))
						/ animationTotalTime;
				if (animationPercent > 1 || animationPercent < 0) {
					animationPercent = 0.5F;
				}
//				graphics.setColor(new Color(
//						animationPercent, animationPercent, animationPercent
//				));
				graphics.setColor(
						CustomColors.getMixedColor(
								// CustomColors.getContrastColor(Memory.getBackgroundColor()),
								// Memory.getBackgroundColor(),
								Color.WHITE,
								Color.BLACK,
								animationPercent));
				// анимация последнего хода
				{
					{
						int innerRadius = pointRadius + 1;
						graphics.drawOval(
								pixelX - innerRadius,
								pixelY - innerRadius,
								innerRadius * 2,
								innerRadius * 2
								);
					}
					{
						int innerRadius = pointRadius + 2;
						graphics.drawOval(
								pixelX - innerRadius,
								pixelY - innerRadius,
								innerRadius * 2,
								innerRadius * 2
								);
					}
				}
			}

			// лучше исправить потом. Здесь можно просто вызвать метод paint()
			// или repaint(), как его там... Если успею то попробую сам.
			// Размер перерисовываемого квадратика для paint() дать просто по размером
			// 1ой точки.


			// Paper.this.repaint(Paper.this.getRectangleAroundDot(x, y));
			// doesn't work because repaint invokes redrawing of the last point.


			graphics.setColor(colorBackground);
			{
				int innerRadius = pointRadius + 1;
				graphics.drawOval(
						pixelX - innerRadius,
						pixelY - innerRadius,
						innerRadius * 2,
						innerRadius * 2
						);
			}
			{
				int innerRadius = pointRadius + 2;
				graphics.drawOval(
						pixelX - innerRadius,
						pixelY - innerRadius,
						innerRadius * 2,
						innerRadius * 2
						);
			}

			((Graphics2D) graphics).setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON
					);

			DotType dotType = engine.getDotType(x, y);
			if (dotType == DotType.RED) {
				// цвет красной заливки
				graphics.setColor(colorRedSurr);
			} else {
				// цвет синей заливки
				graphics.setColor(colorBluSurr);
			}

			{
				int innerRadius = pointRadius + 1;
				graphics.drawOval(
						pixelX - innerRadius,
						pixelY - innerRadius,
						innerRadius * 2,
						innerRadius * 2
						);
			}
			{
				int innerRadius = pointRadius + 2;
				graphics.drawOval(
						pixelX - innerRadius,
						pixelY - innerRadius,
						innerRadius * 2,
						innerRadius * 2
						);
			}
			graphics.setColor(Color.black);
		}
	}

}
