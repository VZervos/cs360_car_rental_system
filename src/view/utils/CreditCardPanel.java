package view.utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CreditCardPanel extends JPanel {
    private Form form;
    private String[] formFields;

    public CreditCardPanel() {
        this.setPreferredSize(new Dimension(400, 600));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());

        formFields = new String[] {"cardnumber", "cvv", "expirationmonth", "expirationyear", "cardfirstname", "cardlastname"};

        add(new JLabel("Credit Card"), BorderLayout.NORTH);
        form = new Form(formFields);

        add(form, BorderLayout.CENTER);
    }

    public String getCardNumber() {
        return form.getFieldValue(formFields[0]);
    }
    public String getCVV() {
        return form.getFieldValue(formFields[1]);
    }
    public String getExpirationMonth() {
        return form.getFieldValue(formFields[2]);
    }
    public String getExpirationYear() {
        return form.getFieldValue(formFields[3]);
    }
    public String getCardFirstname() {
        return form.getFieldValue(formFields[4]);
    }
    public String getCardLastname() {
        return form.getFieldValue(formFields[5]);
    }
    public List<String> getUncompletedFieldNames() { return form.getUncompletedFieldNames();}
}
