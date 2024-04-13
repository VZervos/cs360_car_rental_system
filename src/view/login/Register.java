package view.login;

import control.Control;
import representation.utils.Utils;
import view.utils.CreditCardPanel;
import view.utils.Form;
import view.utils.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Register extends PopupWindow implements ActionListener {
    private Form form;
    private final String[] formFields = new String[] {"tax identification number", "firstname", "lastname", "email", "birthday", "address", "postal code", "username", "password"};

    private CreditCardPanel creditCardPanel;
    private JButton registerButton;
    private JButton cancelButton;

    public Register(Login parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setPreferredSize(new Dimension(500, 1000));
        this.setLocation(300, 0);
        this.setLayout(new BorderLayout());


        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.PAGE_AXIS));

        add(new JLabel("Register"), BorderLayout.NORTH);
        form = new Form(formFields);
        fields.add(form, BorderLayout.CENTER);

        creditCardPanel = new CreditCardPanel();
        fields.add(creditCardPanel);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(registerButton);
        buttons.add(cancelButton);
        add(buttons, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            List<String> mainUncompletedFields = form.getUncompletedFieldNames();
            List<String> creditCardUncompletedFields = creditCardPanel.getUncompletedFieldNames();
            if (!mainUncompletedFields.isEmpty()) {
                JOptionPane.showMessageDialog(this, Utils.generateErrorMessage(mainUncompletedFields));
            } else if (!creditCardUncompletedFields.isEmpty()) {
                JOptionPane.showMessageDialog(this, Utils.generateErrorMessage(creditCardUncompletedFields));
            } else {
                String result = Control.userManagement().registerCustomer(form.getFieldValue(formFields[0]), form.getFieldValue(formFields[1]), form.getFieldValue(formFields[2]), form.getFieldValue(formFields[3]), form.getFieldValue(formFields[4]), form.getFieldValue(formFields[5]), form.getFieldValue(formFields[6]), form.getFieldValue(formFields[7]), form.getFieldValue(formFields[8]), creditCardPanel.getCardNumber(), creditCardPanel.getCVV(), creditCardPanel.getExpirationMonth(), creditCardPanel.getExpirationYear(), creditCardPanel.getCardFirstname(), creditCardPanel.getCardLastname());
                if (result == null)
                    exitPopup();
                else
                    JOptionPane.showMessageDialog(this, result);
            }
        } else {
            exitPopup();
        }
    }
}
