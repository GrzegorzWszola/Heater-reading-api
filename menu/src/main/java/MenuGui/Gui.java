package MenuGui;

import org.controller.ControllerClient;
import org.manager.ManagerClient;
import org.tenant.TenantClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Gui extends JFrame {
    public Gui() {
        super("Tenant Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocation(200, 200);
        setLayout(new FlowLayout());
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        JButton button = new JButton("Tenant Client");
        panel.add(button);
        JButton button2 = new JButton("Manager Client");
        panel.add(button2);
        JButton button3 = new JButton("Controller Client");
        panel.add(button3);
        add(panel);

        setVisible(true);

        button.addActionListener(e -> {
            TenantClient.main(new String[]{});
        });

        button2.addActionListener(e -> {
            ManagerClient.main(new String[]{});
        });

        button3.addActionListener(e -> {
            ControllerClient.main(new String[]{});
        });
    }
}
