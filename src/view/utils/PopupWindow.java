package view.utils;

import javax.swing.*;

public abstract class PopupWindow extends JFrame implements FunctionalWindow {
    FunctionalWindow parentWindow;

    protected PopupWindow (FunctionalWindow parent) {
        parentWindow = parent;
        disableWindow((JFrame) parentWindow);
    }
    protected void exitPopup() {
        parentWindow.enableWindow((JFrame) parentWindow);
        dispose();
    }

    protected FunctionalWindow getParentWindow() {
        return this.parentWindow;
    }
}
