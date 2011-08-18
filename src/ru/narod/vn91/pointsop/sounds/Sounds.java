package ru.narod.vn91.pointsop.sounds;

import java.applet.Applet;
import java.applet.AudioClip;

import ru.narod.vn91.pointsop.utils.Memory;

public class Sounds {

	static private AudioClip audioMakeMove;
	static private AudioClip audioAlarmSignal;
	static private AudioClip nameMentioned;
	static private AudioClip receiveChat;
	static private AudioClip sendChat;

	private void playInNewThread(final AudioClip audioClip) {
		if (audioClip != null) {
			new Thread() {

				@Override
				public void run() {
					audioClip.play();
				}
			}.start();
		} else {
			System.out.println("audioClip is null:( ");
		}
	}

	public void playReceiveChat() {
		if (receiveChat == null) {
			// lazy creation
			receiveChat = Applet.newAudioClip(getClass().getClassLoader().
					getResource("ru/narod/vn91/pointsop/sounds/receiveChat.wav"));
		}
		playInNewThread(receiveChat);
	}

	public void playSendChat() {
		if (sendChat == null) {
			// lazy creation
			sendChat = Applet.newAudioClip(getClass().getClassLoader().
					getResource("ru/narod/vn91/pointsop/sounds/sendChat.wav"));
		}
		playInNewThread(sendChat);
	}

	public void playNameMentioned() {
		if (nameMentioned == null) {
			// lazy creation
			nameMentioned = Applet.newAudioClip(getClass().getClassLoader().
					getResource(
					"ru/narod/vn91/pointsop/sounds/nameMentioned.wav"));
		}
		playInNewThread(nameMentioned);
	}

	public void playAlarmSignal() {
		if (audioAlarmSignal == null) {
			// lazy creation
			audioAlarmSignal = Applet.newAudioClip(getClass().getClassLoader().
					getResource("ru/narod/vn91/pointsop/sounds/alarmSignal.wav"));
		}
		playInNewThread(audioAlarmSignal);
	}

	public void playMakeMove(boolean isInMyGame) {
		if (audioMakeMove == null) {
			// lazy creation
			audioMakeMove = Applet.newAudioClip(getClass().getClassLoader().
					getResource("ru/narod/vn91/pointsop/sounds/makeMove.wav"));
		}
		if ((Memory.getClickAudibility() == Memory.ClickAudibility.IN_ALL_GAMES)
				|| (isInMyGame && (Memory.getClickAudibility() == Memory.ClickAudibility.IN_MY_GAMES))) {
			playInNewThread(audioMakeMove);
		}

//		Object oncePer2Minutes = new Object();
//		synchronized (oncePer2Minutes) {
//			while (true) {
//				// do something
//				try {
//					oncePer2Minutes.wait(100000);
//				} catch (InterruptedException ex) {
//				}
//			}
//		}
	}
}
