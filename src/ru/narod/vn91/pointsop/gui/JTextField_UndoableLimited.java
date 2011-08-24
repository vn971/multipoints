package ru.narod.vn91.pointsop.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
class JTextField_UndoableLimited extends PlainDocument {
		private int limit;
		JTextField jTextField;

	JTextField_UndoableLimited(JTextField jTextField, int limit) {
		super();
		this.limit = limit;
		this.jTextField = jTextField;
		final UndoManager undo = new UndoManager();

		// Listen for undo and redo events
		addUndoableEditListener(new UndoableEditListener() {

			public void undoableEditHappened(UndoableEditEvent evt) {
				undo.addEdit(evt.getEdit());
			}
		});

		// Create an undo action and add it to the text component
		jTextField.getActionMap().put("Undo",
					new AbstractAction("Undo") {

						public void actionPerformed(ActionEvent evt) {
							try {
								if (undo.canUndo()) {
									undo.undo();
								}
							} catch (CannotUndoException ignored) {
							}
						}
					});

		// Bind the undo action to control-Z
		jTextField.getInputMap().put(
					// KeyStroke.getKeyStroke("control Z"),
					KeyStroke.getKeyStroke(KeyEvent.VK_Z,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
					"Undo");

		// Create a redo action and add it to the text component
		jTextField.getActionMap().put("Redo",
					new AbstractAction("Redo") {

						public void actionPerformed(ActionEvent evt) {
							try {
								if (undo.canRedo()) {
									undo.redo();
								}
							} catch (CannotRedoException ignored) {
							}
						}
					});

		// Bind the redo action to control-Y
		jTextField.getInputMap().put(
					KeyStroke.getKeyStroke(KeyEvent.VK_Y,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
					"Redo");
	}

		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			if (str == null) {
				return;
			}
			if ((getLength() + str.length()) > limit) {
				str = str.substring(0, limit - getLength());
			}
			super.insertString(offset, str, attr);
		}
	}