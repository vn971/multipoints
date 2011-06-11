package com.google.sites.priymakpoints.pointsai.pointsAI_1_07;

import javax.swing.JOptionPane;
import ru.narod.vn91.pointsop.ai.Ai2Gui_Interface;
import ru.narod.vn91.pointsop.ai.Gui2Ai_Interface;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;

public class PointsAIEngine implements Gui2Ai_Interface{
	
	PointsAI pointsAI;
	Ai2Gui_Interface gui;
	SingleGameEngineInterface engine;
	boolean aiColor = false;

	public PointsAIEngine(Ai2Gui_Interface gui,
			int sizeX,
			int sizeY) {
		this.gui = gui;
		this.engine = new SingleGameEngine(sizeX, sizeY);
		
		try{pointsAI=new com.google.sites.priymakpoints.pointsai.pointsAI_1_07.PointsAI();
		}catch(Exception e1){JOptionPane.showMessageDialog(null, "Невозможно запустить ИИ!", "Ошибка", 0);}		
	}
	
	public void init() {
		{
			engine.makeMove(19, 17, true);
			engine.makeMove(18, 16, true);
			engine.makeMove(20, 17, true);
			engine.makeMove(21, 16, true);
			engine.makeMove(18, 17, false);
			engine.makeMove(19, 16, false);
			engine.makeMove(20, 16, false);
			engine.makeMove(21, 17, false);
			
			gui.makeMove(19, 17, true, 0, null, 1);
			gui.makeMove(18, 16, true, 0, null, 1);
			gui.makeMove(20, 17, true, 0, null, 1);
			gui.makeMove(21, 16, true, 0, null, 1);
			gui.makeMove(18, 17, false, 0, null, 1);
			gui.makeMove(19, 16, false, 0, null, 1);
			gui.makeMove(20, 16, false, 0, null, 1);
			gui.makeMove(21, 17, false, 0, null, 1);
			
			pointsAI.makeMove(19,17,true);
			pointsAI.makeMove(18,16,true);
			pointsAI.makeMove(20,17,true);
			pointsAI.makeMove(21,16,true);
			pointsAI.makeMove(18,17,false);
			pointsAI.makeMove(19,16,false);
			pointsAI.makeMove(20,16,false);
			pointsAI.makeMove(21,17,false);
			
			int variant=new java.util.Random().nextInt(6);
			int x=0,y=0;
			switch(variant){
				case 0:{x=17;y=16;break;}
				case 1:{x=21;y=15;break;}
				case 2:{x=18;y=15;break;}
				case 3:{x=19;y=19;break;}
				case 4:{x=20;y=19;break;}
				case 5:{x=22;y=16;break;}
			}
			gui.makeMove(x, y, true, 0, null, 1);
			pointsAI.makeMove(x,y,true);
		}
	}

	public void receiveMove(int x,
			int y,
			boolean isRed,
			boolean toBeAnswered,
			long timeExpected) {
		
		MoveResult moveResult = engine.makeMove(x, y, !isRed);
		pointsAI.makeMove(x,y,!isRed);
		if (moveResult != MoveResult.ERROR) {
			// ai accepted this move
			gui.makeMove(x, y, !isRed, 1, null, 1);
		}

		if ((moveResult != MoveResult.ERROR)
				&& (toBeAnswered)) {
			// return AI answer
			java.awt.Point point=pointsAI.getAIMove();
			int answerX = point.x;
			int answerY = point.y;
			MoveResult result = engine.makeMove(answerX, answerY, isRed);
			answerX = engine.getLastDot().x;
			answerY = engine.getLastDot().y;
			if (result != MoveResult.ERROR) {
				gui.makeMove(answerX, answerY, isRed, 0, null, 1);
			} else {
			}
		}
	}

	public void dispose() {}

	public String getName() {
		return "PointsAI 1.07";
	}

}
