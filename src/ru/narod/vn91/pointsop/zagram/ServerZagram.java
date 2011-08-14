package ru.narod.vn91.pointsop.zagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import org.json.JSONException;
//import org.json.JSONObject;
import ru.narod.vn91.pointsop.gui.GameRoom;
import ru.narod.vn91.pointsop.gui.GuiForServerInterface;
import ru.narod.vn91.pointsop.gui.Paper;
import ru.narod.vn91.pointsop.server.ServerInterface;

public class ServerZagram implements ServerInterface {

    private static final String MsgLoginFailed = "ustawOpis";
    private static final String MsgLoginOK = "ok.zalogowanyNaSerwer";
    private static final String MsgPlayerList = "opisyGraczy";
    private static final String MsgLoginStatus = "infoZserwera";
    private static final String MsgYourCurrentTable = "opisStolu";
    private static final String MsgGameRoomsList = "opisyStolow";
    private static final String MsgFreePlayerList = "opisyWolnych";
    private static final String MsgDefaultGameChatMessages = "noweWpisy";
    private static final String MsgDefaultRoomName = "graZkomputerem";
    
    GuiForServerInterface gui;
    ArrayList<Room> subscribedRooms = new ArrayList<Room>();
    Map<String, Room> string2room = new HashMap<String, Room>();
    Map<String, Room.Player> string2player = new HashMap<String, Room.Player>();
    int lastUpdatedRoomIndex = -1;
    String playerNickname = "from_PointsOP";
    String playerSecretID = "fsgkde";
    //String playerSecretID = "--удалено Васей Новиковым:) Иначе можно будет зайти под этим именем. В будущем это не должно быть константой а должно генериться";
    Thread threadMain;
    int updateInterval = 1000;

    Runnable serverUpdater = new Runnable() {

        public synchronized void run() {
            while (updateInterval > 0) {
                ++lastUpdatedRoomIndex;
                if (lastUpdatedRoomIndex >= subscribedRooms.size()) {
                    lastUpdatedRoomIndex = (subscribedRooms.isEmpty()) ? -1 : 0;
                }
                if (lastUpdatedRoomIndex >= 0) {
                    updateRoom(subscribedRooms.get(lastUpdatedRoomIndex));
                }
                try {
                    this.wait(updateInterval);
                } catch (InterruptedException ex) {
                }
            }
        }
    };

    private synchronized void authorize() {
        String loginInfo =
                getLinkContent("http://zagram.org/auth.py?co=loguj&opisGracza="
                + playerNickname + "&idGracza=" + playerSecretID);
        if (loginInfo.equals(MsgLoginOK)) {
            //gui.receiveServerInfo("logged in");
        }

    }

    private synchronized void updateRoom(Room room) {
        // periodical clean-up of players list needed.. For example, when the room index is 0.

        // enter needed room
        getLinkContent(
                "http://zagram.org/a.kropki?playerID="
                + playerSecretID + "&co=zmienStol&newTable=graZkomputerem");
        // should be fixed! currently = game with the computer


        // CHECK IF THE DATA IS FROM THE NEEDED ROOM!!!!!!!!!!

        // get "news"
        String updateInfo = getLinkContent(
                "http://zagram.org/a.kropki?idGracza="
                + playerSecretID + "&co=pobierz&howManyLinesOfChat="
                + room.numberOfChatMessagesRed
                + "&writes=nie");
        if (updateInfo.substring(0, 4).equals("ok.{")) {
            updateInfo = updateInfo.substring(3);
            JSONObject json;
            try {
                json = new JSONObject(updateInfo);
                if (json.get(MsgLoginStatus).equals(MsgLoginFailed)) {
                    authorize();
                } else {
                    {
                        // game rooms
                        room.roomList.clear();
                        ArrayList<String> roomNameList = decodeJSONArray(json.getString(MsgGameRoomsList));
                        for (int i = 0; i < roomNameList.size(); i++) {
                            String s = roomNameList.get(i);
                            Room roomFromList = string2room.get(s);
                            if (roomFromList == null) {
                                roomFromList = new Room(null, s);
                                string2room.put(s, roomFromList);
                                String[] opponents = s.split(" - ");
                                roomFromList.player1 = opponents[0];
                                roomFromList.player2 = opponents[1];
                            }
                            room.roomList.add(roomFromList);
                        }
                    }
                    {
                        // player list
                        room.playerList.clear();
                        ArrayList<String> playerList = decodeJSONArray(json.getString(MsgPlayerList));
                        for (int i = 0; i < playerList.size(); i++) {
                            String s = playerList.get(i);
                            Room.Player player = string2player.get(s);
                            if (player == null) {
                                player = room.new Player(s);
                                string2player.put(s, player);
                            }
                            player.isFree = false;
                            room.playerList.add(player);
                        }
                    }
                    {
                        // free player list
                        ArrayList<String> freePlayerList = decodeJSONArray(json.getString(MsgFreePlayerList));
                        for (int i = 0; i < freePlayerList.size(); i++) {
                            String s = freePlayerList.get(i);
                            Room.Player player = string2player.get(s);
                            if (player == null) {
                                throw new IndexOutOfBoundsException("player doesn't exist (getting list of free users)");
                            }
                            player.isFree = true;
                        }
                    }
                    {
                        ArrayList<String> chatUpdate = decodeJSONArray(json.getString(MsgDefaultGameChatMessages));
                        for (int i = 0; i < chatUpdate.size(); i++) {
                            String chatString = chatUpdate.get(i);
                            int bOpenStart = chatString.indexOf("<b>");
                            int bOpenEnd = bOpenStart + 3;
                            int bCloseStart = chatString.indexOf("<\\/b>");
                            int bCloseEnd = bCloseStart + 5;
                            String playerName = chatString.substring(bOpenEnd, bCloseStart);
                            String msg = chatString.substring(bCloseEnd, chatString.length());
                            Room.Player player = string2player.get(playerName);
                            if (player == null) {
                                player = room.new Player(playerName);
                                string2player.put(playerName, player);
                            }
                            Room.ChatMessage chatMsg = room.new ChatMessage(player, msg);
                            room.chat.add(chatMsg);
                        }
                        room.numberOfChatMessagesRed += chatUpdate.size();
                    }
                    gui.updateRoom(room);
                }
            } catch (JSONException ex) {
                System.out.println(ex.toString());
            }

        }
    }

    private synchronized String getLinkContent(String link) {
        System.out.println(link);
        String result = "";
        try {
            URL url;
            URLConnection urlConn;
            InputStreamReader inStream;
            url = new URL(link);
            urlConn = url.openConnection();
            inStream = new InputStreamReader(
                    urlConn.getInputStream());
            BufferedReader buff = null;
            buff = new BufferedReader(inStream);

            while (true) {
                String nextLine;
                nextLine = buff.readLine();
                if (nextLine != null) {
                    result += nextLine;
                } else {
                    break;
                }
            }
        } catch (MalformedURLException e) {
//            gui.receiveServerText("Please check the URL:" + e);
        } catch (IOException e1) {
//            gui.receiveServerText("Can't read  from the Internet: " + e1);
        }
        return result;
    }

    public synchronized void sendChat(Room room, String msg) {
        String newMessage = msg;
        newMessage = newMessage.replace(" ", "%20");
        System.out.println(newMessage);
        getLinkContent(
                "http://zagram.org/a.kropki?idGracza="
                + playerSecretID + "&co=dodajWpis&table="
                + room.serverName + "&newMsgs=" + newMessage);
    }

    public synchronized Room subscribeRoom(String roomName) {

        Room room = new Room(this, roomName);
        subscribedRooms.add(room);
        lastUpdatedRoomIndex = subscribedRooms.size() - 1;
        room.hasChat = true;
        room.hasGame = false;
        room.hasPlayerList = true;
        room.hasRoomList = true;
        string2room.put(roomName, room);
        if (threadMain == null) {
            System.out.println("thread started");
            threadMain = new Thread(serverUpdater);
            threadMain.start();
        }
        return room;
    }

    public synchronized void subscribeDefaultRoom() {
        System.out.println(MsgDefaultRoomName);
        subscribeRoom(MsgDefaultRoomName);
    }

    public synchronized void unsubscribeRoom(Room room) {
//        string2room ...
        //if the last - stop the main thread
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private ArrayList<String> decodeJSONArray(String in) {
        if (in.length() >= 4) {
            in = in.substring(2, in.length() - 2);
            String[] array = in.split("\",\"");
            ArrayList<String> result = new ArrayList<String>();
            for (int i = 0; i < array.length; i++) {
                result.add(array[i]);
            }
            return result;

        } else {
            return new ArrayList<String>();
        }
    }

    
    public ServerZagram(
			String server,
			GuiForServerInterface gui,
			String myName
	) {
    	playerNickname="pop_"+myName;
    	this.gui=gui;
    }

   
	public void connect() {
        String loginInfo =
            getLinkContent("http://zagram.org/a.kropki?co=guestLogin&idGracza="
            + playerSecretID + "&opis=" + playerNickname + "&lang=ru");
        if (loginInfo.equals(MsgLoginOK)) {
        //gui.receiveServerInfo("logged in");
        }
	}
	
	public void disconnecttt() {}
	public void searchOpponent() {}
	public void requestJoinGame(String gameRoomName) {}
	public void acceptOpponent(String roomName, String newOpponent) {}
	public void stopSearchingOpponent() {}
	public void makeMove(String roomName, int x, int y) {}
	public void surrender(String roomName) {}
//	public void subscribeRoom(String name) {}
	public void unsubscribeRoom(String name) {}
	public void sendChat(String room, String message) {}
	public void sendPrivateMsg(String target, String message) {}
	public String getMyName() {return null;	}
	public String getMainRoom() {return null;}
	public String getServerName() {return null;}
    	
}