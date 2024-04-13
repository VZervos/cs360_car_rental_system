package view.owner;

import view.owner.functions.MoreOptions;
import view.owner.functions.VehicleRepairDeclaration;
import view.owner.functions.VehicleSupplement;
import view.utils.FunctionalWindow;
import view.utils.LoggedInSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OwnerMenu extends JFrame implements ActionListener, FunctionalWindow {
    private final JButton vehicleRepairButton;
    private final JButton vehicleSupplyButton;
    private final JButton moreOptionsButton;

    public OwnerMenu() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Owner");
        this.setPreferredSize(new Dimension(400, 400));
        this.setLocation(300, 100);
        this.setLayout(new BorderLayout());
        LoggedInSession logSession = new LoggedInSession(this, true);
        add(logSession, BorderLayout.SOUTH);

        JPanel options = new JPanel();
        options.setLayout(new GridLayout(0, 1));

        vehicleRepairButton = new JButton("Declare vehicle repair");
        vehicleRepairButton.addActionListener(this);
        options.add(vehicleRepairButton);

        vehicleSupplyButton = new JButton("Declare vehicle supplement");
        vehicleSupplyButton.addActionListener(this);
        options.add(vehicleSupplyButton);

        moreOptionsButton = new JButton("More options");
        moreOptionsButton.addActionListener(this);
        options.add(moreOptionsButton);

        add(options);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if (source == vehicleRepairButton) {
            new VehicleRepairDeclaration(this);
        } else if (source == vehicleSupplyButton) {
            new VehicleSupplement(this);
        } else if (source == moreOptionsButton) {
            new MoreOptions(this);
        }
    }
}
