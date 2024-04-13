package view.client.functions;

import control.Control;
import view.client.ClientMenu;
import representation.utils.Utils;
import view.utils.DriverPanel;
import view.utils.Form;
import view.utils.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class VehicleRent extends PopupWindow implements ActionListener {
    private final JComboBox vehicleSelector;
    private final String[] vehicleCredentialsFields = new String[]{"date", "days", "hours"};
    private final Form vehicleCredentials;
    private JCheckBox insuranceVehicleCheckbox;
    private final DriverPanel driverPanel;
    private JButton submitButton;
    private JButton cancelButton;

    public VehicleRent(ClientMenu parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Vehicle Rent");
        this.setPreferredSize(new Dimension(300, 800));
        this.setLocation(300, 0);
        this.setLayout(new BorderLayout());
        add(new JLabel("Vehicle Rent"), BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.PAGE_AXIS));

        JPanel vehicleSelect = new JPanel();
        vehicleSelect.setLayout(new GridLayout(0, 1));
        vehicleSelect.add(new JLabel("GENERAL"));
        vehicleSelector = new JComboBox(Control.vehicleManagement().getAvailableVehiclesIds());
        vehicleSelect.add(vehicleSelector);
        form.add(vehicleSelect);

        vehicleCredentials = new Form(vehicleCredentialsFields);
        form.add(vehicleCredentials);

        JPanel insuranceVehicle = new JPanel();
        insuranceVehicle.setLayout(new GridLayout(0, 2));
        insuranceVehicle.add(new JLabel("Insurance Vehicle"));
        insuranceVehicleCheckbox = new JCheckBox();
        insuranceVehicle.add(insuranceVehicleCheckbox);
        form.add(insuranceVehicle);

        driverPanel = new DriverPanel();
        form.add(driverPanel);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        buttons.add(submitButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttons.add(cancelButton);
        add(buttons, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            List<String> vehicleCredentialsUncompletedFieldNames = vehicleCredentials.getUncompletedFieldNames();
            List<String> driverPanelUncompletedFieldNames = driverPanel.getUncompletedFieldNames();
            if (!vehicleCredentialsUncompletedFieldNames.isEmpty()) {
                JOptionPane.showMessageDialog(this, Utils.generateErrorMessage(vehicleCredentialsUncompletedFieldNames));
            } else {
                String result = Control.rentalManagement().rental(Objects.requireNonNull(vehicleSelector.getSelectedItem()).toString(), vehicleCredentials.getFieldValue(vehicleCredentialsFields[0]), vehicleCredentials.getFieldValue(vehicleCredentialsFields[1]), vehicleCredentials.getFieldValue(vehicleCredentialsFields[2]), insuranceVehicleCheckbox.isSelected(), driverPanel.getTIN(), driverPanel.getFirstName(), driverPanel.getLastName(), driverPanel.getBirthday(), driverPanel.getLicenseNumber(), driverPanel.getExpirationDate(), driverPanel.getCanDriveCarValue(), driverPanel.getCanDriveMotorbikeValue());
                JOptionPane.showMessageDialog(this, result);
                if (!result.contains("Invalid"))
                    exitPopup();
            }
        } else {
            exitPopup();
        }
    }
}
