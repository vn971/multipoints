package ru.narod.vn91.pointsop.sounds;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;
import java.util.Map;

import ru.narod.vn91.pointsop.utils.Settings;

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
		if (Settings.getOtherSounds()) {
			playInNewThread("receiveChat.wav");
		}
	}

	public static void playSendChat() {
		if (Settings.getOtherSounds()) {
			playInNewThread("sendChat.wav");
		}
	}

	public static void playNameMentioned() {
		if (Settings.getOtherSounds()) {
			playInNewThread("nameMentioned.wav");
		}
	}

	public static void playAlarmSignal() {
		if (Settings.getOtherSounds()) {
			playInNewThread("alarmSignal.wav");
		}
	}

	public static void playMakeMove(boolean isInMyGame) {
		if ((Settings.getClickAudibility() == Settings.ClickAudibility.IN_ALL_GAMES)
				|| (isInMyGame && (Settings.getClickAudibility() == Settings.ClickAudibility.IN_MY_GAMES))) {
			playInNewThread("makeMove.wav");
		}
	}
}
