package view.owner.functions;

import control.Control;
import view.owner.OwnerMenu;
import representation.utils.Utils;
import view.utils.Form;
import view.utils.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class VehicleRepairDeclaration extends PopupWindow implements ActionListener {
    private final JComboBox vehicleSelector;
    private final JComboBox operationSelector;
    private final String[] datesFormFields = new String[] {"from", "until"};
    private final Form datesForm;
    private JButton submitButton;
    private JButton cancelButton;

    public VehicleRepairDeclaration(OwnerMenu parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setPreferredSize(new Dimension(400, 400));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());
        add(new JLabel("Vehicle Repair Declaration"), BorderLayout.NORTH);

        JPanel declaration = new JPanel();
        declaration.setLayout(new BoxLayout(declaration, BoxLayout.PAGE_AXIS));

        declaration.add(new JLabel("Select car"));
        vehicleSelector = new JComboBox(Control.vehicleManagement().getAvailableVehiclesIds());
        declaration.add(vehicleSelector);

        declaration.add(new JLabel("Operation"));
        operationSelector = new JComboBox(Utils.getOperationTypes());
        declaration.add(operationSelector);

        datesForm = new Form(datesFormFields);
        declaration.add(datesForm);

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
            List<String> dateUncompletedFields = datesForm.getUncompletedFieldNames();
            if (dateUncompletedFields.isEmpty()) {
                String result = Control.vehicleManagement().repair(Objects.requireNonNull(vehicleSelector.getSelectedItem()).toString(), Objects.requireNonNull(operationSelector.getSelectedItem()).toString(), datesForm.getFieldValue(datesFormFields[0]), datesForm.getFieldValue(datesFormFields[1]));
                if (result == null) {
                    exitPopup();
                } else
                    JOptionPane.showMessageDialog(this, result);
            } else {
                JOptionPane.showMessageDialog(this, Utils.generateErrorMessage(dateUncompletedFields));
            }

        }
        exitPopup();
    }
}
