package view.owner.functions.moreoptions;

import representation.utils.Utils;
import view.utils.ParametersPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VehicleCondition extends ParametersPanel {
    private JComboBox vehicleRentConditionSelector;
    private JComboBox vehicleCategorySelector;

    public VehicleCondition() {
        super();
        this.setLayout(new GridLayout(0, 2));

        add(new JLabel("Rent Condition: "));
        vehicleRentConditionSelector = new JComboBox(new String[] {"Available", "Rented"});
        add(vehicleRentConditionSelector);

        add(new JLabel("Vehicle Category: "));
        vehicleCategorySelector = new JComboBox(Utils.getVehicleTypes());
        add(vehicleCategorySelector);
    }

    @Override
    public java.util.List<String> getParameters() {
        List<String> values = new ArrayList<>();
        values.add(Objects.requireNonNull(vehicleRentConditionSelector.getSelectedItem()).toString());
        values.add(Objects.requireNonNull(vehicleCategorySelector.getSelectedItem()).toString());
        return values;
    }
}