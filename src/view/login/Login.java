package view.login;

import control.Control;
import view.client.ClientMenu;
import view.owner.OwnerMenu;
import view.utils.FunctionalWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Login extends JFrame implements ActionListener, FunctionalWindow {

    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Login() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setPreferredSize(new Dimension(400, 400));
        this.setLocation(300, 100);
        this.setLayout(new GridLayout(0, 1));

        add(new JLabel("Login"));
        // TODO: reset to hints
//        usernameField = new JTextField("Enter username. (owner/<client username>)");
//        passwordField = new JTextField("Enter password. (o123/<client password>)");
        usernameField = new JTextField("CUSTOMER1");
        passwordField = new JTextField("0000");
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        add(usernameField);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void disableWindow(JFrame frame) {
        usernameField.setText("");
        passwordField.setText("");
        frame.setVisible(false);
        frame.setEnabled(false);
    }

    @Override
    public void enableWindow(JFrame frame) {
        usernameField.setText("Enter username. (owner/client)");
        passwordField.setText("Enter password. (o123/c123");
        frame.setEnabled(true);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (e.getSource() == loginButton) {
            if (Objects.equals(username, "owner") && Objects.equals(password, "o123")) {
                new OwnerMenu();
                dispose();
            } else if (Control.userManagement().login(username, password)) {
                new ClientMenu();
                dispose();
            } else {
                passwordField.setText("Wrong password or username.");
            }
        } else {
            disableWindow(this);
            new Register(this);
        }
    }
}
