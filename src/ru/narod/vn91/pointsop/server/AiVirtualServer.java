package ru.narod.vn91.pointsop.server;

import ru.narod.vn91.pointsop.ai.Ai2Gui_Interface;
import ru.narod.vn91.pointsop.ai.Gui2Ai_Interface;
import ru.narod.vn91.pointsop.gui.GuiForServerInterface;

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

	GuiForServerInterface gui;
	Gui2Ai_Interface ai;

	public AiVirtualServer(GuiForServerInterface gui) {
		this.gui = gui;
	}

	public void init() {
		gui.updateGameInfo(this, "", "",
			ai.getName(), this.getMyName(),
			39, 32,
			null, null, null, null, null, null, null, null, null, null, null, null);
		gui.subscribedGame(
				this,
				"");
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
		gui.makedMove(this, "", false, x, y, isRed, !isRed
//			, 999, 999
			);
		receiveMessage(message);
	}

	public void receiveMessage(String message) {
		if ((message != null) && (message.equals("") == false)) {
			gui.chatReceived(this, "", ai.getName(), message, null);
		}
	}

	public void endOfGame() {
		gui.unsubscribedRoom(this, "");
		gui.rawError(this, ai.getName() + "закончил игру.");
	}

	public void connect() {
	}

	public void disconnectServer() {
		ai.dispose();
	}

	public void makeMove(String roomName,
			int x,
			int y) {
		ai.receiveMove(x, y, true, true, 3000);
	}

	public void searchOpponent() {
		gui.raw(this, "Not supported");
	}

	public void requestPlay(String gameRoomName) {
		gui.raw(this, "Not supported");
	}

	public void acceptOpponent(String roomName,
			String name) {
		gui.raw(this, "Not supported");
	}

	@Override
	public void rejectOpponent(String roomName, String notWantedOpponent) {
		gui.raw(this, "Not supported");
	}

	public void stopSearchingOpponent() {
		gui.raw(this, "Not supported");
	}

	public void surrender(String roomName) {
		gui.raw(this, "Not supported");
	}

	public void subscribeRoom(String name) {
		gui.raw(this, "Not supported");
	}

	public void unsubscribeRoom(String name) {
		gui.unsubscribedRoom(this, "");
		gui.serverClosed(this);
		ai.dispose();
	}

	public void sendChat(String room,
			String message) {
		gui.raw(this, "Not supported");
	}

	public void sendPrivateMsg(String target,
			String message) {
		gui.raw(this, "Not supported");
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

	@Override
	public int getMaximumMessageLength() {
		return 10;
	}

	@Override
	public String coordinatesToString(Integer xOrNull, Integer yOrNull) {
		if (xOrNull != null && yOrNull != null) {
			return String.format("%02d:%02d", xOrNull, yOrNull);
		} else if (xOrNull != null) {
			return String.format("%02d", xOrNull);
		} else if (yOrNull != null) {
			return String.format("%02d", yOrNull);
		} else {
			return "";
		}
	}
	
	@Override
	public boolean isIncomingYInverted() {
		return false;
	}

	@Override
	public boolean isGuiYInverted() {
		return false;
	}

	@Override
	public boolean isPrivateChatEnabled() {
		return false;
	}

	@Override
	public boolean isPingEnabled() {
		return false;
	}

	@Override
	public boolean isSoundNotifyEnabled() {
		return false;
	}

	@Override
	public void getUserInfo(String user) {
	}
	
}
