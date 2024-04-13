package view.client.functions;

import control.Control;
import view.client.ClientMenu;
import view.utils.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VehiclesList extends PopupWindow implements ActionListener {
    private int selectedVehicle;
    private String[] vehiclesList;
    private JButton vehicleSelector;
    private JLabel selectedVehicleTitle;
    private JTextArea vehicleInfoArea;
    private JButton exitButton;

    public VehiclesList(ClientMenu parent) {
        super(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Vehicles List");
        this.setPreferredSize(new Dimension(500, 600));
        this.setLocation(300, 50);
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        reload();

        pack();
        setVisible(true);
    }

    private void reload() {
        vehiclesList = Control.vehicleManagement().getAvailableVehiclesIds();
        add(new JLabel("Vehicles List"));

        vehicleSelector = new JButton("Next vehicle");
        vehicleSelector.addActionListener(this);
        add(vehicleSelector);

        selectedVehicle = -1;
        setupVehiclesList();
        vehicleInfoArea = new JTextArea();
        vehicleInfoArea.setText(getNextVehicleInfo());
        vehicleInfoArea.setEditable(false);
        add(new JScrollPane(vehicleInfoArea));

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        add(exitButton);

        revalidate();
        repaint();
    }

    private void setupVehiclesList() {
        Control.vehicleManagement().updateVehiclesInfo();
        getNextVehicleInfo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vehicleSelector) {
            vehicleInfoArea.setText(getNextVehicleInfo());
        } else if (e.getSource() == exitButton) {
            exitPopup();
        }
    }

    private String getNextVehicleInfo() {
        StringBuilder textToShow = new StringBuilder();
        selectedVehicle++;
        if (selectedVehicle == vehiclesList.length)
            selectedVehicle = 0;
        if (vehiclesList.length == 0) {
            textToShow.append("No vehicles were found.");
            selectedVehicle = -1;
        } else {
            String[][] vehicleInfo = Control.vehicleManagement().getVehicleInfo(vehiclesList[selectedVehicle]);
            for (int i = 0; i<vehicleInfo[1].length; i++) {
                textToShow.append(vehicleInfo[0][i]).append(": ").append(vehicleInfo[1][i]).append("\n");
            }

        }
        return textToShow.toString();
    }
}
