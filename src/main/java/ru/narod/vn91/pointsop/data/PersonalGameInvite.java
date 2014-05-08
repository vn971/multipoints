package ru.narod.vn91.pointsop.data;

public class PersonalGameInvite {
	
	public String masterRoomId = "";
	public final String targetUserId;

	public Integer sizeX = 30, sizeY = 30;
	public Boolean isRated = false;
	public final FirstPlayerRule firstPlayerRule;
	public Integer handicapRed = 0;
	public Integer instantWin = 0;
	public Boolean manualEnclosings = false;
	public Boolean stopEnabled = true;
	public Boolean isEmptyScored = false;

	public Boolean isRedFirst = true;
	
	public enum FirstPlayerRule {
		INITIATOR, TARGET, UNASSIGNED,
	}

	public PersonalGameInvite(String masterRoomId, String targetUserId,
			Integer sizeX, Integer sizeY, Boolean isRated,
			FirstPlayerRule firstPlayerRule, Integer handicapRed, Integer instantWin,
			Boolean manualEnclosings, Boolean stopEnabled, Boolean isEmptyScored,
			Boolean isRedFirst) {
		super();
		this.masterRoomId = masterRoomId;
		this.targetUserId = targetUserId;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.isRated = isRated;
		this.firstPlayerRule = firstPlayerRule;
		this.handicapRed = handicapRed;
		this.instantWin = instantWin;
		this.manualEnclosings = manualEnclosings;
		this.stopEnabled = stopEnabled;
		this.isEmptyScored = isEmptyScored;
		this.isRedFirst = isRedFirst;
	}
}
