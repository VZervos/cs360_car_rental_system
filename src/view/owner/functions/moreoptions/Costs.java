package view.owner.functions.moreoptions;

import representation.utils.Utils;
import view.utils.ParametersPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Costs extends ParametersPanel {
    private JTextField fromDateField;
    private JTextField untilDateField;

    public Costs() {
        super();
        this.setLayout(new GridLayout(0, 2));

        add(new JLabel("From: "));
        fromDateField = new JTextField();
        add(fromDateField);

        add(new JLabel("Until: "));
        untilDateField = new JTextField();
        add(untilDateField);
    }

    @Override
    public java.util.List<String> getParameters() {
        List<String> values = new ArrayList<>();
        values.add(fromDateField.getText());
        values.add(untilDateField.getText());
        return values;
    }
}
