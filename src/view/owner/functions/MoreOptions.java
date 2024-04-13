package view.owner.functions;

import control.Control;
import view.owner.OwnerMenu;
import view.owner.functions.moreoptions.*;
import view.utils.ParametersPanel;
import view.utils.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MoreOptions extends PopupWindow implements ActionListener {
    private int querySelected = -1;
    private final String[] queryOptions = new String[]{"Vehicle condition", "Rent condition", "Rent duration", "Income", "Costs", "Most wanted vehicle", "Other"};
    private JButton querySelector;
    private JLabel selectedQueryTitle;
    private ParametersPanel queryParametersPanel;
    private JTextArea resultsArea;
    private JButton submitButton;
    private JButton cancelButton;

    public MoreOptions(OwnerMenu parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("More Options");
        this.setPreferredSize(new Dimension(300, 350));
        this.setLocation(300, 100);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        add(new JLabel("More Options"));

        querySelector = new JButton("Next query");
        querySelector.addActionListener(this);
        add(querySelector);

        selectedQueryTitle = new JLabel(queryOptions[0]);
        add(selectedQueryTitle);

        queryParametersPanel = generateParametersPanel();
        add(queryParametersPanel);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        add(new JScrollPane(resultsArea));

        JPanel buttons = new JPanel();
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        buttons.add(submitButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttons.add(cancelButton);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == querySelector){
            remove(queryParametersPanel);
            queryParametersPanel = generateParametersPanel();
            add(queryParametersPanel, 3);
            revalidate();
            repaint();
        }else if (e.getSource() == submitButton) {
                String[] result = Control.vehicleManagement().moreOptions(querySelected, (queryParametersPanel.getParameters()));
                if (result[0].contains("No results"))
                    JOptionPane.showMessageDialog(this, result[0]);
                else {
                    showResults(result);
                }
        } else
            exitPopup();
    }

    private void showResults(String[] results) {
        StringBuilder textToShow = new StringBuilder();
        for (String s : results) {
            textToShow.append(s + "\n");
        }
        resultsArea.setText(textToShow.toString());
    }

    private ParametersPanel generateParametersPanel() {
        if (querySelected == 6)
            querySelected = -1;
        selectedQueryTitle.setText(queryOptions[++querySelected]);
        ParametersPanel newPanel = null;
        switch (querySelected) {
            case 0:
                newPanel = new VehicleCondition();
                break;
            case 1:
                newPanel = new RentCondition();
                break;
            case 2:
                newPanel = new RentDuration();
                break;
            case 3:
                newPanel = new Income();
                break;
            case 4:
                newPanel = new Costs();
                break;
            case 5:
                newPanel = new MostWantedVehicle();
                break;
            case 6:
                newPanel = new Other();
        }
        if (queryParametersPanel != null) {

        }
        return newPanel;
    }
}
