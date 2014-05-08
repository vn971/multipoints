package ru.narod.vn91.pointsop.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

import ru.narod.vn91.pointsop.utils.Function;

@SuppressWarnings("serial")
public class TabCloseable extends JPanel {

	final CloseButton closeButton;
	final JLabel label;
	Set<Function<TabCloseable, Void>> closeListenerSet =
			new LinkedHashSet<Function<TabCloseable, Void>>();

	public TabCloseable(String title, boolean isCloseable) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		super.setBorder(BorderFactory.createEmptyBorder(
			2, 0, 0, 0));
		super.setOpaque(false);
		// FIXME need to fix if using Nimbus LNF

		closeButton = new CloseButton();
		label = new JLabel(title);

		super.add(label);
		if (isCloseable) {
			label.setBorder(BorderFactory.createEmptyBorder(
				0, 0, 0, 6));
			super.add(closeButton);
		}
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void addCloseListener(Function<TabCloseable, Void> closeListener) {
		closeListenerSet.add(closeListener);
	}

	private class CloseButton extends JButton {
		int size = 15;

		public CloseButton() {
			setUI(new BasicButtonUI());
			setPreferredSize(new Dimension(size, size));
			setFocusable(false);
			setOpaque(false);
			setBorder(BorderFactory.createEtchedBorder());

			setBorderPainted(false);
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					CloseButton.this.setBorderPainted(true);
				}

				public void mouseExited(MouseEvent e) {
					CloseButton.this.setBorderPainted(false);
				}
			});

			super.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (Function<TabCloseable, Void> closeListener : closeListenerSet) {
						closeListener.call(TabCloseable.this);
					}
				}
			});
		}

		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setStroke(new BasicStroke(2));
			// FIXME need to fix if using Nimbus LNF
			g2.setColor(super.getModel().isRollover() ? Color.PINK : Color.BLACK);
			int margin = size / 3;
			g2.drawLine(
				margin, margin,
				super.getWidth() - margin - 1,
				super.getHeight() - margin - 1);
			g2.drawLine(
				super.getWidth() - margin - 1,
				margin, margin,
				super.getHeight() - margin - 1);
			g2.dispose();
		}

		public void updateUI() {
			// ignore
		}
	}
}
