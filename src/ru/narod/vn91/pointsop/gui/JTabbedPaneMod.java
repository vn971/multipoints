package ru.narod.vn91.pointsop.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TabbedPaneUI;

import ru.narod.vn91.pointsop.utils.Function;
import ru.narod.vn91.pointsop.utils.Function0;

public class JTabbedPaneMod {

	JTabbedPane tabbedPane = new JTabbedPane();
	final Color boldColor = Color.GRAY;
	final Color normalColor = tabbedPane.getBackground();

	Map<Component, Function0<Boolean>> closeListeners =
			new HashMap<Component, Function0<Boolean>>();

	public Component getComponent() {
		return Component.class.cast(tabbedPane);
	}

	public synchronized void addTab(
			String title,
			final Component component,
			boolean isCloseable) {
		tabbedPane.addTab(title /*may be ""*/, component);
		TabCloseable tabCloseable = new TabCloseable(title, isCloseable);
		tabCloseable.addCloseListener(new Function<TabCloseable, Void>() {
			@Override
			public Void call(TabCloseable input) {
				Function0<Boolean> closeListener = closeListeners.get(component);
				if (closeListener != null &&
					closeListener.call() == false) {
					// do nothing. Calling listeners is enough.
				} else {
					remove(component);
				}
				return null;
			}
		});
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabCloseable);
	}

	public synchronized void setCloseListener_FalseIfStopClosing(
			Component component,
			Function0<Boolean> closeListener) {
		closeListeners.put(component, closeListener);
	}

	public synchronized boolean contains(Component component) {
		return tabbedPane.indexOfComponent(component) >= 0;
	}

	public synchronized boolean isSelected(Component component) {
		return tabbedPane.getSelectedComponent().equals(component);
	}

	public synchronized void setSelectedComponent(Component component) {
		tabbedPane.setSelectedComponent(component);
	}

	public synchronized void remove(Component component) {
		closeListeners.remove(component);
		tabbedPane.remove(component);
	}

	public synchronized void makeBold(Component component) {
		int tabIndex = tabbedPane.indexOfComponent(component);
		if (tabIndex >= 0 &&
			tabIndex != tabbedPane.getSelectedIndex()) {
			tabbedPane.setBackgroundAt(tabIndex, boldColor);
			// Component panel = tabbedPane.getTabComponentAt(tabIndex);
			// try {
			// TabCloseable tab = TabCloseable.class.cast(panel);
			// String newTitle = getBold(tab.getText());
			// if (newTitle.equals(tab.getText()) == false) {
			// tab.setText(newTitle);
			// }
			// } catch (ClassCastException e) {
			// }
		}
	}

	public synchronized void updateTabText(Component component, String newTitle) {
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