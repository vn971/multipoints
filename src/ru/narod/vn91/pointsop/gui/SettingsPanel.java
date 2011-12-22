package ru.narod.vn91.pointsop.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JColorChooser;
import ru.narod.vn91.pointsop.sounds.Sounds;
import ru.narod.vn91.pointsop.utils.CustomColors;
import ru.narod.vn91.pointsop.utils.Settings;
import ru.narod.vn91.pointsop.utils.Settings.ClickAudibility;

@SuppressWarnings("serial")
public class SettingsPanel extends javax.swing.JPanel {

	private boolean initPhase = true;

	class PointsClickListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Settings.setClickAudibility(e.getActionCommand());
		}
	}
	PointsClickListener pointsClickListener = new PointsClickListener();

	class PrivateChatListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
//			Memory.memory.put("PrivateChat", e.getActionCommand());
		}
	}
	PrivateChatListener privateChatListener = new PrivateChatListener();

	private void setColors() {
		Color p1 = Settings.getPlayer1Color();
		Color p2 = Settings.getPlayer2Color();
		Color back = Settings.getBackgroundColor();
		jButton_P1Color.setBackground(p1);
		jButton_P2Color.setBackground(p2);
		jButton_BackgroundColor.setBackground(back);
		jButton_P1Color.setForeground(CustomColors.getContrastColor(p1));
		jButton_P2Color.setForeground(CustomColors.getContrastColor(p2));
		jButton_BackgroundColor.setForeground(
				CustomColors.getContrastColor(back));
	}

	/** Creates new form SettingsPanel */
	public SettingsPanel() {
		initComponents();
		jRadioButton_PointsClick_InAllGames.setActionCommand(
				ClickAudibility.IN_ALL_GAMES.name());
		jRadioButton_PointsClick_OnlyInMyGames.setActionCommand(
				ClickAudibility.IN_MY_GAMES.name());
		jRadioButton_PointsClick_Nowhere.setActionCommand(
				ClickAudibility.NOWHERE.name());
//		jSlider1.setValueIsAdjusting(true);
		jSlider1.setValue(
				(int)(Settings.getDotWidth()
				* (jSlider1.getMaximum() - jSlider1.getMinimum() + 1)
				+ 0.5));
//		jSlider1.setValueIsAdjusting(false);
		jCheckBox_DrawConnections.setSelected(
				Settings.getDrawConnections());
		setColors();
		jCheckBox_OtherSounds.setSelected(Settings.getOtherSounds());
		{
			Enumeration<AbstractButton> pointClickButtons = buttonGroup_PointClick.getElements();
			String userSetting = Settings.getClickAudibility().name();
			for (Enumeration<AbstractButton> enumerator = pointClickButtons; enumerator.hasMoreElements();) {
				AbstractButton abstractButton = enumerator.nextElement();
				if (abstractButton.getActionCommand().equals(userSetting)) {
					abstractButton.setSelected(true);
				}
				abstractButton.addActionListener(pointsClickListener);
			}
		}
		initPhase = false;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonGroup_PointClick = new javax.swing.ButtonGroup();
    buttonGroup_PrivateChat = new javax.swing.ButtonGroup();
    jPanel_Click = new javax.swing.JPanel();
    jRadioButton_PointsClick_InAllGames = new javax.swing.JRadioButton();
    jRadioButton_PointsClick_OnlyInMyGames = new javax.swing.JRadioButton();
    jRadioButton_PointsClick_Nowhere = new javax.swing.JRadioButton();
    jButton_PointClick = new javax.swing.JButton();
    jPanel_Dots = new javax.swing.JPanel();
    jSlider1 = new javax.swing.JSlider();
    jCheckBox_DrawConnections = new javax.swing.JCheckBox();
    jButton_P1Color = new javax.swing.JButton();
    jButton_P2Color = new javax.swing.JButton();
    jButton_BackgroundColor = new javax.swing.JButton();
    jButton_ResetColors = new javax.swing.JButton();
    jPanel_OtherSounds = new javax.swing.JPanel();
    jCheckBox_OtherSounds = new javax.swing.JCheckBox();

    jPanel_Click.setBorder(javax.swing.BorderFactory.createTitledBorder("звук постановки точек"));

    buttonGroup_PointClick.add(jRadioButton_PointsClick_InAllGames);
    jRadioButton_PointsClick_InAllGames.setSelected(true);
    jRadioButton_PointsClick_InAllGames.setText("во всех играх");
    jRadioButton_PointsClick_InAllGames.setActionCommand("InAllGames");
    jRadioButton_PointsClick_InAllGames.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jRadioButton_PointsClick_InAllGamesStateChanged(evt);
      }
    });

    buttonGroup_PointClick.add(jRadioButton_PointsClick_OnlyInMyGames);
    jRadioButton_PointsClick_OnlyInMyGames.setText("только в моих играх");
    jRadioButton_PointsClick_OnlyInMyGames.setActionCommand("OnlyInMyOwnGames");

    buttonGroup_PointClick.add(jRadioButton_PointsClick_Nowhere);
    jRadioButton_PointsClick_Nowhere.setText("нигде");
    jRadioButton_PointsClick_Nowhere.setActionCommand("Nowhere");

    jButton_PointClick.setText("тест звука");
    jButton_PointClick.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton_PointClickActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel_ClickLayout = new javax.swing.GroupLayout(jPanel_Click);
    jPanel_Click.setLayout(jPanel_ClickLayout);
    jPanel_ClickLayout.setHorizontalGroup(
      jPanel_ClickLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel_ClickLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jRadioButton_PointsClick_InAllGames)
        .addGap(18, 18, 18)
        .addComponent(jRadioButton_PointsClick_OnlyInMyGames)
        .addGap(18, 18, 18)
        .addComponent(jRadioButton_PointsClick_Nowhere)
        .addGap(18, 18, 18)
        .addComponent(jButton_PointClick)
        .addContainerGap(192, Short.MAX_VALUE))
    );
    jPanel_ClickLayout.setVerticalGroup(
      jPanel_ClickLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel_ClickLayout.createSequentialGroup()
        .addGroup(jPanel_ClickLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jRadioButton_PointsClick_InAllGames)
          .addComponent(jRadioButton_PointsClick_OnlyInMyGames)
          .addComponent(jRadioButton_PointsClick_Nowhere)
          .addComponent(jButton_PointClick))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel_Dots.setBorder(javax.swing.BorderFactory.createTitledBorder("настройки игрового поля"));
    // jPanel_Dots.setVisible(false);

    jSlider1.setMajorTickSpacing(3);
    jSlider1.setMaximum(17);
    jSlider1.setMinimum(1);
    jSlider1.setMinorTickSpacing(1);
    jSlider1.setPaintTicks(true);
    jSlider1.setSnapToTicks(true);
    jSlider1.setValue(4);
    jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder("размер точек"));
    jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jSlider1StateChanged(evt);
      }
    });

    jCheckBox_DrawConnections.setText("соединять соседние точки палочками");
    jCheckBox_DrawConnections.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jCheckBox_DrawConnectionsStateChanged(evt);
      }
    });

    jButton_P1Color.setText("игрок 1");
    jButton_P1Color.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton_P1ColorActionPerformed(evt);
      }
    });

    jButton_P2Color.setText("игрок 2");
    jButton_P2Color.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton_P2ColorActionPerformed(evt);
      }
    });

    jButton_BackgroundColor.setText("фон");
    jButton_BackgroundColor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton_BackgroundColorActionPerformed(evt);
      }
    });

    jButton_ResetColors.setText("восстановить цвета");
    jButton_ResetColors.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton_ResetColorsActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel_DotsLayout = new javax.swing.GroupLayout(jPanel_Dots);
    jPanel_Dots.setLayout(jPanel_DotsLayout);
    jPanel_DotsLayout.setHorizontalGroup(
      jPanel_DotsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel_DotsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel_DotsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel_DotsLayout.createSequentialGroup()
            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(jPanel_DotsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(jPanel_DotsLayout.createSequentialGroup()
                .addComponent(jCheckBox_DrawConnections)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addComponent(jButton_P1Color))
              .addComponent(jButton_P2Color)
              .addComponent(jButton_BackgroundColor)))
          .addComponent(jButton_ResetColors, javax.swing.GroupLayout.Alignment.TRAILING))
        .addContainerGap())
    );
    jPanel_DotsLayout.setVerticalGroup(
      jPanel_DotsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel_DotsLayout.createSequentialGroup()
        .addGroup(jPanel_DotsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jCheckBox_DrawConnections)
          .addGroup(jPanel_DotsLayout.createSequentialGroup()
            .addComponent(jButton_P1Color)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton_P2Color)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton_BackgroundColor)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButton_ResetColors)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel_OtherSounds.setBorder(javax.swing.BorderFactory.createTitledBorder("прочие звуки"));

    jCheckBox_OtherSounds.setText("включены?");
    jCheckBox_OtherSounds.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jCheckBox_OtherSoundsStateChanged(evt);
      }
    });

    javax.swing.GroupLayout jPanel_OtherSoundsLayout = new javax.swing.GroupLayout(jPanel_OtherSounds);
    jPanel_OtherSounds.setLayout(jPanel_OtherSoundsLayout);
    jPanel_OtherSoundsLayout.setHorizontalGroup(
      jPanel_OtherSoundsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel_OtherSoundsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jCheckBox_OtherSounds)
        .addContainerGap(614, Short.MAX_VALUE))
    );
    jPanel_OtherSoundsLayout.setVerticalGroup(
      jPanel_OtherSoundsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jCheckBox_OtherSounds)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jPanel_Click, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanel_OtherSounds, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanel_Dots, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel_Click, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel_OtherSounds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel_Dots, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(147, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

	private void jRadioButton_PointsClick_InAllGamesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton_PointsClick_InAllGamesStateChanged
	}//GEN-LAST:event_jRadioButton_PointsClick_InAllGamesStateChanged

	private void jButton_PointClickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PointClickActionPerformed
		Sounds.playMakeMove(true);
	}//GEN-LAST:event_jButton_PointClickActionPerformed

	private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
		if (initPhase == false) {
			Settings.setDotWidth(
					((double) jSlider1.getValue())
							/ jSlider1.getMaximum()
			);
		}
	}//GEN-LAST:event_jSlider1StateChanged

	private void jCheckBox_DrawConnectionsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_DrawConnectionsStateChanged
		Settings.setDrawConnections(
				jCheckBox_DrawConnections.isSelected()
		);
	}//GEN-LAST:event_jCheckBox_DrawConnectionsStateChanged

	private void jButton_P1ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_P1ColorActionPerformed
		Color c = JColorChooser.showDialog(null, "цвет первого игрока",
				Settings.getPlayer1Color());
		if (c != null) {
			Settings.setPlayer1Color(c, true);
			setColors();
		}
	}//GEN-LAST:event_jButton_P1ColorActionPerformed

	private void jButton_P2ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_P2ColorActionPerformed
		Color c = JColorChooser.showDialog(null, "цвет второго игрока",
				Settings.getPlayer2Color());
		if (c != null) {
			Settings.setPlayer2Color(c, true);
			setColors();
		}
	}//GEN-LAST:event_jButton_P2ColorActionPerformed

	private void jButton_BackgroundColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BackgroundColorActionPerformed
		Color c = JColorChooser.showDialog(null, "цвет первого игрока",
				Settings.getBackgroundColor());
		if (c != null) {
			Settings.setBackgroundColor(c, true);
			setColors();
		}

	}//GEN-LAST:event_jButton_BackgroundColorActionPerformed

	private void jButton_ResetColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ResetColorsActionPerformed
		Settings.resetColors();
		setColors();
	}//GEN-LAST:event_jButton_ResetColorsActionPerformed

private void jCheckBox_OtherSoundsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_OtherSoundsStateChanged
	Settings.setOtherSounds(jCheckBox_OtherSounds.isSelected());
}//GEN-LAST:event_jCheckBox_OtherSoundsStateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.ButtonGroup buttonGroup_PointClick;
  private javax.swing.ButtonGroup buttonGroup_PrivateChat;
  private javax.swing.JButton jButton_BackgroundColor;
  private javax.swing.JButton jButton_P1Color;
  private javax.swing.JButton jButton_P2Color;
  private javax.swing.JButton jButton_PointClick;
  private javax.swing.JButton jButton_ResetColors;
  private javax.swing.JCheckBox jCheckBox_DrawConnections;
  private javax.swing.JCheckBox jCheckBox_OtherSounds;
  private javax.swing.JPanel jPanel_Click;
  private javax.swing.JPanel jPanel_Dots;
  private javax.swing.JPanel jPanel_OtherSounds;
  private javax.swing.JRadioButton jRadioButton_PointsClick_InAllGames;
  private javax.swing.JRadioButton jRadioButton_PointsClick_Nowhere;
  private javax.swing.JRadioButton jRadioButton_PointsClick_OnlyInMyGames;
  private javax.swing.JSlider jSlider1;
  // End of variables declaration//GEN-END:variables
}
