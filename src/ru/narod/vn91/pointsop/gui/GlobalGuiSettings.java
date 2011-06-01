/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.narod.vn91.pointsop.gui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author vasya
 */
public class GlobalGuiSettings {

	static final SimpleAttributeSet playerNameIncoming = new SimpleAttributeSet();
	static final SimpleAttributeSet playerNameOutgoing = new SimpleAttributeSet();
	static final SimpleAttributeSet chatIncoming = new SimpleAttributeSet();
	static final SimpleAttributeSet chatOutgoing = new SimpleAttributeSet();
	static SimpleAttributeSet playerNameRed;
	static SimpleAttributeSet playerNameBlue;
	static final SimpleAttributeSet serverNotice = new SimpleAttributeSet();

	static {
		Color myColor = new Color(4, 135, 12);
		playerNameIncoming.addAttribute(StyleConstants.CharacterConstants.Bold,
				true);
		StyleConstants.setForeground(playerNameOutgoing, myColor);
		playerNameOutgoing.addAttribute(StyleConstants.CharacterConstants.Bold,
				true);
		playerNameRed = new SimpleAttributeSet(playerNameIncoming);
		playerNameBlue = new SimpleAttributeSet(playerNameIncoming);

		StyleConstants.setForeground(serverNotice, Color.GRAY);
		StyleConstants.setForeground(chatOutgoing, myColor);
//		StyleConstants.setBackground(serverNotice, Color.BLACK);
	}
	static SimpleDateFormat myTimeFormat = new SimpleDateFormat("HH:mm:ss");
	static SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy.MM.dd");

	private static String getHtmlColor(int inputByte) {
		String hex = Integer.toHexString(inputByte);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex;
	}

	static String getHtmlColor(Color javaColor) {

		return "#"
				+ getHtmlColor(javaColor.getRed())
				+ getHtmlColor(javaColor.getGreen())
				+ getHtmlColor(javaColor.getBlue());
	}
}
