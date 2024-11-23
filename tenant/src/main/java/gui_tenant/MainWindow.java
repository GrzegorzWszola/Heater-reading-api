package gui_tenant;

import dao.intefaces.ICostReadingDao;
import database.objects.CostReaders;
import database.objects.Heater;
import objectUtils.Tenant;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JPanel {
    public MainWindow(JFrame frame, Tenant tenant, ICostReadingDao costReadingDao) {
        StringBuilder listOfReadings = new StringBuilder();
        StringBuilder listOfUnpaid = new StringBuilder();
        StringBuilder listOfPaid = new StringBuilder();
        int i = 1;
        for (Heater heater : tenant.getHeaterList()){
            listOfReadings.append(String.format("%d: %.2f%n", i, heater.getReading()));
            i++;
        }
        i = 1;
        for (CostReaders reading : tenant.getIsUnpaid()){
            listOfUnpaid.append(String.format("%d: %.2fzl%n", i, reading.getToPay()));
            i++;
        }
        i = 1;
        for (CostReaders reading : tenant.getIsPaid()){
            listOfPaid.append(String.format("%d: %.2fzl%n", i, reading.getToPay()));
            i++;
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel currentId = new JLabel("Current ID: " + tenant.getFlatId());
        JLabel heaterReading = new JLabel("Heaters Readings:");
        JTextArea heaterReadingArea = new JTextArea();
        heaterReadingArea.setText(listOfReadings.toString());
        heaterReadingArea.setEditable(false);
        heaterReadingArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        heaterReadingArea.setWrapStyleWord(true);
        heaterReadingArea.setLineWrap(true);

        JLabel isUnpaidLabel = new JLabel("Unpaid: ");
        JTextArea isUnpaid = new JTextArea();
        isUnpaid.setText(listOfUnpaid.toString());
        isUnpaid.setEditable(false);
        isUnpaid.setAlignmentX(Component.CENTER_ALIGNMENT);
        isUnpaid.setWrapStyleWord(true);
        isUnpaid.setLineWrap(true);

        JLabel isPaidLabel = new JLabel("Paid: ");
        JTextArea isPaid = new JTextArea();
        isPaid.setText(listOfPaid.toString());
        isPaid.setEditable(false);
        isPaid.setAlignmentX(Component.CENTER_ALIGNMENT);
        isPaid.setWrapStyleWord(true);
        isPaid.setLineWrap(true);

        JButton payButton = new JButton("Pay All");
        payButton.addActionListener(e -> {
            costReadingDao.markAsPaid(tenant.getFlatId());
            refreshWindow(frame, tenant, costReadingDao);
        });

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            refreshWindow(frame, tenant, costReadingDao);
        });

        currentId.setAlignmentX(Component.CENTER_ALIGNMENT);
        heaterReading.setAlignmentX(Component.CENTER_ALIGNMENT);
        heaterReadingArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        isPaid.setAlignmentX(Component.CENTER_ALIGNMENT);
        isUnpaidLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        payButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currentId);
        add(heaterReading);
        add(new JScrollPane(heaterReadingArea));
        add(isUnpaidLabel);
        add(isUnpaid);
        add(isPaidLabel);
        add(isPaid);
        add(payButton);
        add(refresh);
    }

    private void refreshWindow(JFrame frame, Tenant tenant, ICostReadingDao costReadingDao) {
        tenant.refreshData();

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new MainWindow(frame, tenant, costReadingDao));
        frame.revalidate();
        frame.repaint();
    }
}
