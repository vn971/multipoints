package ru.narod.vn91.pointsop.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.narod.vn91.pointsop.utils.Function;
import ru.narod.vn91.pointsop.utils.Function0;

public class JTabbedPaneMod {

	JTabbedPane tabbedPane = new JTabbedPane();
	final Color boldColor = Color.GRAY;
	final Color normalColor = tabbedPane.getBackground();

	Map<Component, Function0<Boolean>> closeListeners = new HashMap<>();

	public Component getComponent() {
		return Component.class.cast(tabbedPane);
	}

	public synchronized void addTab(
			final String title,
			final Component component,
			final boolean isCloseable) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					tabbedPane.addTab(title /* may be "" */, component);
					TabCloseable tabCloseable = new TabCloseable(title, isCloseable);
					tabCloseable.addCloseListener(new Function<TabCloseable, Void>() {
						@Override
						public Void call(TabCloseable input) {
							try {
								Function0<Boolean> closeListener = closeListeners.get(component);
								if (closeListener == null || closeListener.call() != false) {
									// calling listeners is not enough
									remove(component);
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							return null;
						}
					});
					tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabCloseable);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public synchronized void setCloseListener_FalseIfStopClosing(
			Component component,
			Function0<Boolean> closeListener) {
		closeListeners.put(component, closeListener);
	}

	public synchronized boolean contains(Component component) {
		return tabbedPane.indexOfComponent(component) >= 0;
	}

	public synchronized void setSelectedComponent(final Component component) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					if (tabbedPane.indexOfComponent(component) >= 0) {
						tabbedPane.setSelectedComponent(component);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public synchronized void remove(final Component component) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				closeListeners.remove(component);
				tabbedPane.remove(component);
			}
		});
	}

	public synchronized void makeBold(final Component component) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final int tabIndex = tabbedPane.indexOfComponent(component);
				if (tabIndex >= 0 &&
					tabIndex != tabbedPane.getSelectedIndex() &&
					tabIndex < tabbedPane.getTabCount()) {
					try {
						tabbedPane.setBackgroundAt(tabIndex, boldColor);
					} catch (Exception ex) {
						ex.printStackTrace(); // SWING is buggy? I have lot's of exceptions here... :-/
					}
				}
			}
		});
	}

	public synchronized void updateTabText(
			final Component component, final String newTitle) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int tabIndex = tabbedPane.indexOfComponent(component);
				if (tabIndex >= 0) {
					Component tabPanel = tabbedPane.getTabComponentAt(tabIndex);
					// try {
					TabCloseable tab = TabCloseable.class.cast(tabPanel);
					tab.setText(newTitle);
					// } catch (ClassCastException e) {
					// }
				}
			}
		});
	}

	public JTabbedPaneMod() {
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setFocusable(false);
		tabbedPane.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				// FIXME need to fix if using Nimbus LNF
				tabbedPane.setBackgroundAt(selectedIndex, normalColor);

				// Component panel = tabbedPane.getTabComponentAt(selectedIndex);
				// try {
				// TabCloseable tab = TabCloseable.class.cast(panel);
				// String s = tab.getText();
				// s = getNotBold(s);
				// tab.setText(s);
				// } catch (ClassCastException ex) {
				// } catch (NullPointerException ex) {
				// }
			}
		});
	}
}
