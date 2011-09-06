package ru.narod.vn91.pointsop.sounds;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;
import java.util.Map;

import ru.narod.vn91.pointsop.utils.Memory;

public class Sounds {


	private static Map<String,AudioClip> clips = new HashMap<String, AudioClip>();

	private static void playInNewThread(final String fileName) {
		new Thread() {
			public void run() {
				AudioClip audioClip;
				synchronized (clips) {
					if (clips.get(fileName) != null) {
						audioClip = clips.get(fileName);
					} else {
						audioClip = Applet.newAudioClip(
								getClass().getClassLoader().getResource(
									"ru/narod/vn91/pointsop/sounds/" + fileName));
						clips.put(fileName, audioClip);
					}
				}
				audioClip.play();
			};
		}.start();
	}

	public static void playReceiveChat() {
		playInNewThread("receiveChat.wav");
	}

	public static void playSendChat() {
		playInNewThread("sendChat.wav");
	}

	public static void playNameMentioned() {
		playInNewThread("nameMentioned.wav");
	}

	public static void playAlarmSignal() {
		playInNewThread("alarmSignal.wav");
	}

	public static void playMakeMove(boolean isInMyGame) {
		if ((Memory.getClickAudibility() == Memory.ClickAudibility.IN_ALL_GAMES)
				|| (isInMyGame && (Memory.getClickAudibility() == Memory.ClickAudibility.IN_MY_GAMES))) {
			playInNewThread("makeMove.wav");
		}
	}
}
