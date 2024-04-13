package view.utils;

import javax.swing.*;

public interface FunctionalWindow {

    default void enableWindow(JFrame window) {
        window.setEnabled(true);
        window.setVisible(true);
    }

    default void disableWindow(JFrame window) {
        window.setEnabled(false);
        window.setVisible(false);
    }
}
