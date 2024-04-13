package view.owner.functions.moreoptions;

import view.utils.ParametersPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Other extends ParametersPanel {
    private JTextField queryField;

    public Other() {
        super();
        this.setLayout(new GridLayout(0, 2));

        add(new JLabel("Query: "));
        queryField = new JTextField();
        add(queryField);
    }

    @Override
    public java.util.List<String> getParameters() {
        List<String> values = new ArrayList<>();
        values.add(queryField.getText());
        return values;
    }
}
