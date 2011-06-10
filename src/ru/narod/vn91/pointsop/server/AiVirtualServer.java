package ru.narod.vn91.pointsop.server;

import ru.narod.vn91.pointsop.ai.Ai2Gui_Interface;
import ru.narod.vn91.pointsop.ai.Gui2Ai_Interface;
import ru.narod.vn91.pointsop.gui.GuiController;

/**
 * Чтобы присоединить свой ИИ к Op теперь можно:
 * <br>
 * <br>1) создать класс имплементирующий {@link Gui2Ai_Interface},
 * то есть воспринимающий ходы от человека. (Метод {@link Gui2Ai_Interface#receiveMove(int, int, boolean, boolean, long)}.)
 * <br>2) хранить в этом классе имплементацию {@link Ai2Gui_Interface}.
 * Когда надо вернуть ответ пользователю - обращаемся к этой имплементации,
 * вызываем её метод {@link Ai2Gui_Interface#makeMove(int, int, boolean, double, java.lang.String, long)}, тем самым отправляя информацию человеку
 * <br>3) включить созданный ИИ в работу.
 * Это делается через {@link AiVirtualServer}, надо просто посмотреть пример
 * из SelfishGuiStarted и скопировать. Там 3 строчки.
 * 
 */

public class AiVirtualServer implements ServerInterface, Ai2Gui_Interface {

	GuiController gui;
	Gui2Ai_Interface ai;
	String userSecond;
	
	public AiVirtualServer(GuiController gui,
			String userSecond) {
		this.gui = gui;
		this.userSecond = userSecond;
	}

	public void init() {
		gui.subscribedGame("", this, userSecond, "Me", 10, 10, null, false,
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
			gui.chatReceived(this, "", userSecond, message);
		}
	}

	public void endOfGame() {
		gui.unsubsribedGame(this, "");
	}

	public void connect() {
	}

	public void disconnecttt() {
		throw new UnsupportedOperationException();
//		gui.unsubsribedGame(this, "");
//		gui.serverClosed(this);
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
		gui.unsubsribedGame(this, "");
		gui.serverClosed(this);
		ai.dispose();
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
		return "Me";
	}

	public String getMainRoom() {
		return "";
	}

	public String getServerName() {
		return "human vs. computer game";
	}
}