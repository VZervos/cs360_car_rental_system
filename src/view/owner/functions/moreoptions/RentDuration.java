package view.owner.functions.moreoptions;

import representation.utils.Utils;
import view.utils.ParametersPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RentDuration extends ParametersPanel {
    private final JComboBox vehicleCategorySelector;
    private final JComboBox durationTypeSelector;

    public RentDuration() {
        super();
        this.setLayout(new GridLayout(0, 2));

        add(new JLabel("Vehicle Category: "));
        vehicleCategorySelector = new JComboBox(Utils.getVehicleTypes());
        add(vehicleCategorySelector);

        add(new JLabel("Rent Condition: "));
        durationTypeSelector = new JComboBox(new String[] {"Maximum", "Minimum", "Average"});
        add(durationTypeSelector);
    }

    @Override
    public java.util.List<String> getParameters() {
        List<String> values = new ArrayList<>();
        values.add(Objects.requireNonNull(vehicleCategorySelector.getSelectedItem()).toString());
        values.add(Objects.requireNonNull(durationTypeSelector.getSelectedItem()).toString());
        return values;
    }
}
