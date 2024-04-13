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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Objects;

public class VehicleSupplement extends PopupWindow implements ActionListener, ItemListener {
    JPanel vehicleInfo;
    private JComboBox vehicleSelector;
    private String[] generalFormFields = new String[]{"brand", "model", "color", "autonomy in kilometers", "rental cost", "insurance cost"};

    private Form generalInfoForm;
    String[] carSpecificFormFields = new String[]{"plateNumber", "type", "passengersLimit"};
    String[] vehicleSpecificFormFields = new String[]{"plateNumber or vehiclecode"};
    private Form vehicleSpecificInfoForm;
    private JButton submitButton;
    private JButton cancelButton;

    public VehicleSupplement(OwnerMenu parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Vehicle Supplement");
        this.setPreferredSize(new Dimension(300, 600));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());
        add(new JLabel("Vehicle Supplement"), BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.PAGE_AXIS));

        JPanel vehicleSelect = new JPanel();
        vehicleSelect.add(new JLabel("VEHICLE TYPE"));
        vehicleSelector = new JComboBox(Utils.getVehicleTypes());
        vehicleSelector.addItemListener(this);
        vehicleSelect.add(vehicleSelector);
        form.add(vehicleSelect);

        vehicleInfo = new JPanel();
        form.add(generateForm(Objects.requireNonNull(vehicleSelector.getSelectedItem()).toString()));
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

    private JPanel generateForm(String vehicleType) {

        vehicleInfo.removeAll();
        vehicleInfo.setLayout(new BoxLayout(vehicleInfo, BoxLayout.PAGE_AXIS));

        generalInfoForm = new Form(generalFormFields);
        vehicleInfo.add(generalInfoForm);

        JPanel vehicleSpecificInfo = new JPanel();
        if (Objects.equals(vehicleType, "Car")) {
            vehicleSpecificInfoForm = new Form(carSpecificFormFields);
        } else {
            vehicleSpecificInfoForm = new Form(vehicleSpecificFormFields);
        }
        vehicleSpecificInfo.add(vehicleSpecificInfoForm);
        vehicleInfo.add(vehicleSpecificInfo);

        vehicleInfo.revalidate();
        vehicleInfo.repaint();

        return vehicleInfo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            List<String> generalUncompletedFields = generalInfoForm.getUncompletedFieldNames();
            List<String> vehicleUncompletedFields = vehicleSpecificInfoForm.getUncompletedFieldNames();
            if (!generalUncompletedFields.isEmpty()) {
                JOptionPane.showMessageDialog(this, Utils.generateErrorMessage(generalUncompletedFields));
            } else if (!vehicleUncompletedFields.isEmpty()) {
                JOptionPane.showMessageDialog(this, Utils.generateErrorMessage(vehicleUncompletedFields));
            } else {
                String vehicleType = Objects.requireNonNull(vehicleSelector.getSelectedItem()).toString();
                String result;
                if (Objects.equals(vehicleType, "Car"))
                    result = Control.vehicleManagement().supply(vehicleType, vehicleSpecificInfoForm.getFieldValue(carSpecificFormFields[0]), generalInfoForm.getFieldValue(generalFormFields[0]), generalInfoForm.getFieldValue(generalFormFields[1]), generalInfoForm.getFieldValue(generalFormFields[2]), generalInfoForm.getFieldValue(generalFormFields[3]), generalInfoForm.getFieldValue(generalFormFields[4]), generalInfoForm.getFieldValue(generalFormFields[5]), vehicleSpecificInfoForm.getFieldValue(carSpecificFormFields[1]), vehicleSpecificInfoForm.getFieldValue(carSpecificFormFields[2]));
                else
                    result = Control.vehicleManagement().supply(vehicleType, vehicleSpecificInfoForm.getFieldValue(vehicleSpecificFormFields[0]), generalInfoForm.getFieldValue(generalFormFields[0]), generalInfoForm.getFieldValue(generalFormFields[1]), generalInfoForm.getFieldValue(generalFormFields[2]), generalInfoForm.getFieldValue(generalFormFields[3]), generalInfoForm.getFieldValue(generalFormFields[4]), generalInfoForm.getFieldValue(generalFormFields[5]), null, null);
                if (result == null)
                    exitPopup();
                else
                    JOptionPane.showMessageDialog(this, result);
            }
        } else {
            exitPopup();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == vehicleSelector) {
            generateForm(Objects.requireNonNull(vehicleSelector.getSelectedItem()).toString());
        }
    }
}
