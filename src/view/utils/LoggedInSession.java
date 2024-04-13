package view.utils;

import view.login.Login;

import javax.swing.*;
import java.awt.*;

public class LoggedInSession extends JPanel {
    private final JFrame parentWindow;

    public LoggedInSession(JFrame parent, boolean asOwner) {
        super();
        setLayout(new GridLayout(0, 1));
        parentWindow = parent;

        add(new JLabel("Logged in as " + (asOwner ? "Owner" : "Client")));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            new Login();
            parentWindow.dispose();
        });
        add(logoutButton);

        setVisible(true);
    }

}
