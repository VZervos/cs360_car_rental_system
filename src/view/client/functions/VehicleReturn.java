package view.client.functions;

import control.Control;
import view.client.ClientMenu;
import view.utils.Form;
import view.utils.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class VehicleReturn extends PopupWindow implements ActionListener {
    private String selectedVehicle;
    private final JComboBox vehicleSelector;
    private final String[] currentTimeFormFields = new String[]{"date", "hour of day"};
    private final Form currentTimeForm;
    private final JLabel extraFee;
    private JButton returnButton;
    private JButton cancelButton;

    public VehicleReturn(ClientMenu parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Return vehicle");
        this.setPreferredSize(new Dimension(400, 400));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());
        add(new JLabel("Return vehicle"), BorderLayout.NORTH);

        JPanel returnStatement = new JPanel();
        returnStatement.setLayout(new GridLayout(0, 1));

        returnStatement.add(new JLabel("Select car"));
        vehicleSelector = new JComboBox(Control.rentalManagement().getVehiclesRentedByCustomer(Control.getCurrentCustomer().getCId()));
        vehicleSelector.addActionListener(this);
        returnStatement.add(vehicleSelector);

        currentTimeForm = new Form(currentTimeFormFields);
        returnStatement.add(currentTimeForm);

        extraFee = new JLabel();
        returnStatement.add(extraFee);

        updateReturnCost();

        add(returnStatement, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        returnButton = new JButton("Return");
        returnButton.addActionListener(this);
        buttons.add(returnButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttons.add(cancelButton);
        add(buttons, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }

    private void updateReturnCost() {
        selectedVehicle = Objects.requireNonNull(vehicleSelector.getSelectedItem()).toString();
        Control.rentalManagement().updateVehicleReturnSelectedVehicle(selectedVehicle);
        extraFee.setText("Extra fee: " + Control.rentalManagement().getVehicleReturnExtraCostAndHours(currentTimeForm.getFieldValue(currentTimeFormFields[0]), currentTimeForm.getFieldValue(currentTimeFormFields[1])));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnButton) {
            String result = Control.rentalManagement().vehicleReturn(currentTimeForm.getFieldValue(currentTimeFormFields[0]), currentTimeForm.getFieldValue(currentTimeFormFields[1]));
            JOptionPane.showMessageDialog(this, result);
            if (!result.contains("Invalid")) {
                exitPopup();
                ((ClientMenu) getParentWindow()).destroy();
                new ClientMenu();
                dispose();
            }
        } else if (e.getSource() == cancelButton) {
            exitPopup();
        } else if (e.getSource() == vehicleSelector) {
            updateReturnCost();
        }
    }
}
