package view.client;

import control.Control;
import view.client.functions.*;
import view.utils.FunctionalWindow;
import view.utils.LoggedInSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientMenu extends JFrame implements ActionListener, FunctionalWindow {
    private boolean hasRentedCars;
    private final JButton viewVehiclesButton;
    private final JButton vehicleRentButton;
    private JButton vehicleDamageButton;
    private JButton accidentButton;
    private JButton vehicleReturnButton;
    private JPanel options;

    public ClientMenu() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Client");
        this.setPreferredSize(new Dimension(400, 400));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());
        LoggedInSession logSession = new LoggedInSession(this, false);
        add(logSession, BorderLayout.SOUTH);

        options = new JPanel();
        options.setLayout(new GridLayout(0, 1));

        viewVehiclesButton = new JButton("View available vehicles");
        viewVehiclesButton.addActionListener(this);
        options.add(viewVehiclesButton);

        vehicleRentButton = new JButton("Rent a vehicle");
        vehicleRentButton.addActionListener(this);
        options.add(vehicleRentButton);

        updateMenu();

        Runnable checkerThread = () -> {
            while (true) {
                try {
                    updateMenu();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread rentalChecker = (new Thread(checkerThread));
        rentalChecker.start();

        add(options);

        pack();
        setVisible(true);
    }

    private void updateMenu() {
        Boolean newValue = Control.userManagement().hasRentedCars();
        if (newValue != hasRentedCars) {
            hasRentedCars = newValue;

            if (vehicleDamageButton != null)
                options.remove(vehicleDamageButton);
            vehicleDamageButton = new JButton("Submit vehicle damage");
            vehicleDamageButton.addActionListener(this);
            options.add(vehicleDamageButton);

            if (accidentButton != null)
                options.remove(accidentButton);
            accidentButton = new JButton("Submit accident");
            accidentButton.addActionListener(this);
            options.add(accidentButton);

            if (vehicleReturnButton != null)
                options.remove(vehicleReturnButton);
            vehicleReturnButton = new JButton("Return a vehicle");
            vehicleReturnButton.addActionListener(this);
            options.add(vehicleReturnButton);

            options.revalidate();
            options.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if (source == accidentButton) {
            new VehicleAccidentStatement(this);
        } else if (source == vehicleDamageButton) {
            new VehicleDamageStatement(this);
        } else if (source == vehicleRentButton) {
            new VehicleRent(this);
        } else if (source == vehicleReturnButton) {
            new VehicleReturn(this);
        } else if (source == viewVehiclesButton) {
            new VehiclesList(this);
        }
    }

    public void destroy() {
        dispose();
    }
}
