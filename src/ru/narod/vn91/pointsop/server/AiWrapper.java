package ru.narod.vn91.pointsop.server;

import ru.narod.vn91.pointsop.ai.Ai2Gui_Interface;
import ru.narod.vn91.pointsop.ai.Gui2Ai_Interface;
import ru.narod.vn91.pointsop.gui.GuiController;

public class AiWrapper implements ServerInterface, Ai2Gui_Interface {

	GuiController gui;
	Gui2Ai_Interface ai;

	public AiWrapper(GuiController gui) {
		this.gui = gui;
	}

	public void init() {
		gui.subscribedGame("", this, "you", "ai", 10, 10, null, false,
				null, true, true);
		ai.init();
	}

	public void setAi(Gui2Ai_Interface ai) {
		this.ai = ai;
	}

	public void makeMove(int x,
			int y,
			boolean isRed,
			double aiThinksOfHisPosition,
			String message,
			long timeTook) {
		gui.makedMove(this, "", false, x, y, isRed);
		receiveMessage(message);
	}

	public void receiveMessage(String message) {
		if ((message != null) && (message.equals("") == false)) {
			gui.chatReceived(this, "", "ai", message);
		}
	}

	public void connect() {
	}

	public void disconnecttt() {
		gui.unsubsribedGame(this, "");
		gui.serverClosed(this);
	}

	public void makeMove(String roomName,
			int x,
			int y) {
		ai.receiveMove(x, y, true, true, 3000);
	}

	public void searchOpponent() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void requestJoinGame(String gameRoomName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void acceptOpponent(String roomName,
			String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void stopSearchingOpponent() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void surrender(String roomName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void subscribeRoom(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void unsubscribeRoom(String name) {
		ai.dispose();
		gui.unsubsribedGame(this, "");
		gui.serverClosed(this);
	}

	public void sendChat(String room,
			String message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sendPrivateMsg(String target,
			String message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getMyName() {
		return "you";
	}

	public String getMainRoom() {
		return "";
	}

	public String getServerName() {
		return "human vs. computer game";
	}
}
