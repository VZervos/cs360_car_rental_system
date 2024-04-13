package view.utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DriverPanel extends JPanel {
    private Form form;
    private final String[] formFields = new String[] {"Tax Identification Number", "firstname", "lastname", "birthday", "licensenumber", "expirationdate"};

    private JCheckBox canDriveCarCheckbox;
    private JCheckBox canDriveMotorbikeCheckbox;

    public DriverPanel() {
        this.setPreferredSize(new Dimension(400, 600));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.PAGE_AXIS));
        add(new JLabel("Driver License"), BorderLayout.NORTH);
        form = new Form(formFields);
        fields.add(form);

        JPanel capabilities = new JPanel();
        capabilities.setLayout(new GridLayout(0, 2));

        capabilities.add(new JLabel("Can drive car: "));
        canDriveCarCheckbox = new JCheckBox();
        capabilities.add(canDriveCarCheckbox);

        capabilities.add(new JLabel("Can drive motorbike: "));
        canDriveMotorbikeCheckbox = new JCheckBox();
        capabilities.add(canDriveMotorbikeCheckbox);

        fields.add(capabilities);

        add(fields, BorderLayout.CENTER);
    }

    public String getTIN() {
        return form.getFieldValue(formFields[0]);
    }

    public String getFirstName() {
        return form.getFieldValue(formFields[1]);
    }

    public String getLastName() {
        return form.getFieldValue(formFields[2]);
    }

    public String getBirthday() {
        return form.getFieldValue(formFields[3]);
    }

    public String getLicenseNumber() {
        return form.getFieldValue(formFields[4]);
    }

    public String getExpirationDate() {
        return form.getFieldValue(formFields[5]);
    }

    public boolean getCanDriveCarValue() {
        return canDriveCarCheckbox.isSelected();
    }

    public boolean getCanDriveMotorbikeValue() {
        return canDriveMotorbikeCheckbox.isSelected();
    }
    public List<String> getUncompletedFieldNames() { return form.getUncompletedFieldNames();}
}
