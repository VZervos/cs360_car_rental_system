package view.client.functions;

import control.Control;
import view.client.ClientMenu;
import representation.utils.Utils;
import view.utils.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class VehicleDamageStatement extends PopupWindow implements ActionListener {
    private final JComboBox vehicleSelector;
    private JButton submitButton;
    private JButton cancelButton;

    public VehicleDamageStatement(ClientMenu parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Vehicle Damage Statement");
        this.setPreferredSize(new Dimension(400, 400));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());
        add(new JLabel("Vehicle Damage Statement"), BorderLayout.NORTH);

        JPanel declaration = new JPanel();
        declaration.setLayout(new GridLayout(0, 1));

        declaration.add(new JLabel("Select car"));
        vehicleSelector = new JComboBox(Control.rentalManagement().getVehiclesRentedByCustomer(Control.getCurrentCustomer().getCId()));
        declaration.add(vehicleSelector);

        add(declaration);

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
            String newVehicle = Control.vehicleManagement().vehicleDamage(Objects.requireNonNull(vehicleSelector.getSelectedItem()).toString());
            JOptionPane.showMessageDialog(this, newVehicle);
        }
        exitPopup();
    }
}
