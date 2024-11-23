package gui_manager;

import CurrentManager.CurrentManager;
import dao.intefaces.*;
import database.objects.CostReaders;
import database.objects.Flat;
import database.objects.Manager;
import database.objects.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JPanel{
    public MainWindow(JFrame frame, CurrentManager currentManager, ITaskDao taskDao, IFlatsDao flatsDao, IBuildingDao buildingDao, ICostReadingDao costReadingDao) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        StringBuilder currentFlats = new StringBuilder();
        currentFlats.append("Flat id, number, address, cost:\n");
        for(Flat flat : flatsDao.getAllFlatsByManagerId(currentManager.getId())) {
            currentFlats.append(String.format("%d, %d, %s, %.2f%n", flat.getId(), flat.getNumber(), buildingDao.getBuildingById(flat.getBuilding_id()).getAddress(), flat.getFlat_cost()));
        }

        JLabel managerLabel = new JLabel("Manager ID: " + currentManager.getId());
        JLabel managerName = new JLabel("Name: " + currentManager.getName());
        JScrollPane scrollPane = getjScrollPane(currentFlats);

        JButton setFlatCost = new JButton("Set flat cost");
        setFlatCost.addActionListener(e -> {
            setFlatCost(frame, currentManager, costReadingDao, flatsDao);
            refreshWindow(frame, currentManager, taskDao, flatsDao, buildingDao, costReadingDao);
        });

        JButton addTask = new JButton("Add task");
        addTask.addActionListener(e -> {
            createNewTask(frame, flatsDao, taskDao);
            refreshWindow(frame, currentManager, taskDao, flatsDao, buildingDao, costReadingDao);
        });

        JButton addCostReading = new JButton("Add cost reading");
        addCostReading.addActionListener(e -> {
            createPayment(frame, currentManager, costReadingDao, flatsDao);
            refreshWindow(frame, currentManager, taskDao, flatsDao, buildingDao, costReadingDao);
        });

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            refreshWindow(frame, currentManager, taskDao, flatsDao, buildingDao, costReadingDao);
        });

        add(managerLabel);
        add(managerName);
        add(scrollPane);
        add(setFlatCost);
        add(addTask);
        add(addCostReading);
        add(refresh);
    }

    private static JScrollPane getjScrollPane(StringBuilder currentFlats) {
        JTextArea flatsArea = new JTextArea();
        flatsArea.setText(currentFlats.toString());
        flatsArea.setEditable(false);
        flatsArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        flatsArea.setWrapStyleWord(true);
        flatsArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(flatsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        return scrollPane;
    }

    private void refreshWindow(JFrame frame, CurrentManager manager, ITaskDao taskDao, IFlatsDao flatsDao, IBuildingDao buildingDao, ICostReadingDao costReadingDao) {
        manager.refreshData();

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new MainWindow(frame, manager, taskDao, flatsDao, buildingDao, costReadingDao));
        frame.revalidate();
        frame.repaint();
    }

    private static void setFlatCost(JFrame parentFrame, CurrentManager manager, ICostReadingDao costReadingDao, IFlatsDao flatsDao){
        JDialog dialog = new JDialog(parentFrame, "Update flat cost", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parentFrame);

        JLabel labelReading = new JLabel("Set new flat cost:");
        JTextField inputReading = new JTextField(10);
        JLabel flatIdLabel = new JLabel("Flat ID:");
        JTextField inputReading2 = new JTextField(10);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String costInput = inputReading.getText();
                String flatIdInput = inputReading2.getText();
                double cost = Double.parseDouble(costInput);
                int flatId = Integer.parseInt(flatIdInput);

                if(flatId > flatsDao.getAllFlats().size()){
                    throw new ArrayIndexOutOfBoundsException("There is no flat with this id");
                }
                for(Flat flat : flatsDao.getAllFlatsByManagerId(manager.getId())){
                    if(flatId == flat.getId()){
                        flatsDao.updateFlatCost(cost, flatId);

                        dialog.dispose();
                        return;
                    }
                }
                throw new IllegalArgumentException("No access to this flat");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(dialog, "There is no flat with this id","Input Error", JOptionPane.ERROR_MESSAGE);
            }catch (IllegalArgumentException ex){
                JOptionPane.showMessageDialog(dialog, "No access to this flat","Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(labelReading, gbc);

        gbc.gridx = 1;
        dialog.add(inputReading, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(flatIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        dialog.add(inputReading2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        dialog.add(submitButton, gbc);

        dialog.setVisible(true);
    }

    private static void createNewTask(JFrame parentFrame, IFlatsDao flatsDao, ITaskDao taskDao){
        JDialog dialog = new JDialog(parentFrame, "Create new task", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parentFrame);

        JLabel labelReading = new JLabel("Planned date:");
        JTextField inputReading = new JTextField(10);
        JLabel flatIdLabel = new JLabel("Controller:");
        JTextField inputReading2 = new JTextField(10);
        JLabel flatsLabel = new JLabel("Flats ID(comma separated):");
        JTextArea flatsInput = new JTextArea(5,20);
        flatsInput.setLineWrap(true);
        flatsInput.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(flatsInput);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String dateInput = inputReading.getText();
                String controllerInput = inputReading2.getText();
                String flatsString = flatsInput.getText();
                String[] array = flatsString.split(",\\s*");
                int date = Integer.parseInt(dateInput);
                int controllerId = Integer.parseInt(controllerInput);
                List<Flat> flatIds = new ArrayList<>();
                for(String i : array){
                    flatIds.add(flatsDao.getFlat(Integer.parseInt(i)));
                }

                Task task = new Task(date, controllerId);
                task.setFlats(flatIds);
                taskDao.addTask(task);
                int taskId = taskDao.getLastItemId();
                for(Flat flat : task.getFlats()){
                    flatsDao.updateFlatsForTask(flat.getId(), taskId);
                }

                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(dialog, "There is no flat with this id","Input Error", JOptionPane.ERROR_MESSAGE);

            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(labelReading, gbc);

        gbc.gridx = 1;
        dialog.add(inputReading, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(flatIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        dialog.add(inputReading2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(flatsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        dialog.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        dialog.add(submitButton, gbc);

        dialog.setVisible(true);
    }

    private static void createPayment(JFrame parentFrame, CurrentManager manager, ICostReadingDao costReadingDao, IFlatsDao flatsDao){
        JDialog dialog = new JDialog(parentFrame, "Create new task", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(parentFrame);

        JLabel labelReading = new JLabel("Flat Id:");
        JTextField inputReading = new JTextField(10);


        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try{
                String flatIdInput = inputReading.getText();
                int flatId = Integer.parseInt(flatIdInput);

                for(Flat flat : flatsDao.getAllFlatsByManagerId(manager.getId())){
                    if(flatId == flat.getId()){
                        CostReaders cost = new CostReaders(flatId);
                        costReadingDao.addReading(cost);

                        dialog.dispose();
                        return;
                    }
                }
                throw new IllegalArgumentException("cannot add a reading");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }catch(IllegalArgumentException ex){
                JOptionPane.showMessageDialog(dialog, "Cannot add the flat, doesn't have access", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(labelReading, gbc);

        gbc.gridx = 1;
        dialog.add(inputReading, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        dialog.add(submitButton, gbc);

        dialog.setVisible(true);
    }
}
