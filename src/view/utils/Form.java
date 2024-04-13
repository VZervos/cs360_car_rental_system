package view.utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Form extends JPanel {
    HashMap<String, JTextField> formFields;

    public Form(String[] formFieldsIn) {
        super();
        formFields = new HashMap<>();
        for (String field : formFieldsIn)
            addField(field);
        this.setLayout(new GridLayout(0, 1));
    }

    public void addField(String fieldName) {
        add(new JLabel(fieldName.toUpperCase()));
        JTextField field = new JTextField();
        add(field);
        formFields.put(fieldName, field);
    }
    private JTextField getField(String fieldName) {
        return formFields.get(fieldName);
    }
    public String getFieldValue(String fieldName) { return getField(fieldName).getText();}

    public List<String> getUncompletedFieldNames() {
        List<String> uncompletedFields = new ArrayList<>();
        for (String fieldName : formFields.keySet()) {
            JTextField field = formFields.get(fieldName);
            String fieldValue = field.getText();
            if (fieldValue.isBlank()) {
                uncompletedFields.add(fieldName);
            }
        }
        return uncompletedFields;
    }
}
