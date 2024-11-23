package gui_controller;

import dao.intefaces.*;
import database.objects.Flat;
import database.objects.Heater;
import database.objects.Task;
import objects.CurrentController;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JPanel {
    public MainWindow(JFrame frame, CurrentController controller, ITaskDao taskDao, IFlatsDao flatsDao, IBuildingDao buildingDao, IHeaterDao heaterDao) {
        StringBuilder currentTasks = new StringBuilder();
        for(Task task : controller.getTasks()){
            currentTasks.append(String.format("Task ID:%d Planned date: %d, Flats:%nID, Flat number, Building address%n",task.getId(), task.getPlannedDate()));
            for(Flat flat : flatsDao.getAllFlatsByTaskId(task.getId())){
                currentTasks.append(String.format("%d, %d, %s%nID, Reading%n", flat.getId(), flat.getNumber(), buildingDao.getBuildingById(flat.getBuilding_id()).getAddress()));
                for(Heater heater : heaterDao.getAllHeaterByFlatId(flat.getId())){
                    currentTasks.append(String.format("%d, %.2f%n",heater.getId() ,heater.getReading()));
                }
            }
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel controllerId = new JLabel("Controller ID: " + controller.getId());
        JLabel controllerName = new JLabel("Controller Name: " + controller.getName());

        JLabel tasks = new JLabel("Current Tasks: ");
        JTextArea tasksArea = new JTextArea();
        tasksArea.setText(currentTasks.toString());
        tasksArea.setEditable(false);
        tasksArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        tasksArea.setWrapStyleWord(true);
        tasksArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(tasksArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(350, 200));

        JButton addHeaterReading = new JButton("Add Heater Reading");
        addHeaterReading.addActionListener(e -> {
            openInputDialogAdd(frame, heaterDao, flatsDao);
            refreshWindow(frame, controller, taskDao, flatsDao, buildingDao, heaterDao);
        });

        JButton updateHeaterReading = new JButton("Update Heater Reading");
        updateHeaterReading.addActionListener(e -> {
            openInputDialogUpdate(frame, heaterDao, flatsDao);
            refreshWindow(frame, controller, taskDao, flatsDao, buildingDao, heaterDao);
        });

        JButton deleteTask = new JButton("Delete Task");
        deleteTask.addActionListener(e -> {
            deleteTasks(frame, controller, taskDao);
            refreshWindow(frame, controller, taskDao, flatsDao, buildingDao, heaterDao);
        });

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            refreshWindow(frame, controller, taskDao, flatsDao, buildingDao, heaterDao);
        });

        add(controllerId);
        add(controllerName);
        add(tasks);
        add(scrollPane);
        add(addHeaterReading);
        add(updateHeaterReading);
        add(deleteTask);
        add(refresh);
    }

    private void refreshWindow(JFrame frame, CurrentController controller, ITaskDao taskDao, IFlatsDao flatsDao, IBuildingDao buildingDao, IHeaterDao heaterDao) {
        controller.refreshData();

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new MainWindow(frame, controller, taskDao, flatsDao, buildingDao, heaterDao));
        frame.revalidate();
        frame.repaint();
    }

    private static void openInputDialogAdd(JFrame parentFrame, IHeaterDao heaterDao, IFlatsDao flatsDao) {
        JDialog dialog = new JDialog(parentFrame, "Add Heater Reading", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parentFrame);

        JLabel labelReading = new JLabel("Heater Reading:");
        JTextField inputReading = new JTextField(10);
        JLabel flatIdLabel = new JLabel("Flat ID:");
        JTextField inputReading2 = new JTextField(10);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String readingInput = inputReading.getText();
                String flatIdInput = inputReading2.getText();
                double reading = Double.parseDouble(readingInput);
                int flatId = Integer.parseInt(flatIdInput);

                if(flatId > flatsDao.getAllFlats().size()){
                    throw new ArrayIndexOutOfBoundsException("There is no flat with this id");
                }
                heaterDao.addHeater(new Heater(reading, flatId));

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

    private static void openInputDialogUpdate(JFrame parentFrame, IHeaterDao heaterDao, IFlatsDao flatsDao){
        JDialog dialog1 = new JDialog(parentFrame, "Update Heater Reading", true);
        dialog1.setLayout(new GridBagLayout());
        dialog1.setSize(400, 300);
        dialog1.setLocationRelativeTo(parentFrame);

        JLabel labelReading = new JLabel("Heater Reading:");
        JTextField inputReading = new JTextField(10);
        JLabel flatIdLabel = new JLabel("Flat ID:");
        JTextField inputReading2 = new JTextField(10);
        JLabel heaterIdLabel = new JLabel("Heater ID:");
        JTextField inputReading3 = new JTextField(10);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String readingInput = inputReading.getText();
                String flatIdInput = inputReading2.getText();
                String heaterIdInput = inputReading3.getText();
                double reading = Double.parseDouble(readingInput);
                int flatId = Integer.parseInt(flatIdInput);
                int heaterId = Integer.parseInt(heaterIdInput);

                if(flatId > flatsDao.getAllFlats().size()){
                    throw new ArrayIndexOutOfBoundsException("There is no flat with this id");
                }
                if(heaterId > heaterDao.getAllHeaters().size()) {
                    throw new ArrayIndexOutOfBoundsException("There is no heater with this id");
                }
                heaterDao.updateHeater(new Heater(reading, flatId), heaterId);

                dialog1.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog1, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(dialog1, "There is no flat or heater with this id","Input Error", JOptionPane.ERROR_MESSAGE);

            }
        });

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(10, 10, 10, 10);

        gbc1.gridx = 0;
        gbc1.gridy = 0;
        dialog1.add(labelReading, gbc1);

        gbc1.gridx = 1;
        dialog1.add(inputReading, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = 1;
        dialog1.add(flatIdLabel, gbc1);

        gbc1.gridx = 1;
        gbc1.gridy = 1;
        dialog1.add(inputReading2, gbc1);

        gbc1.gridx = 0;
        gbc1.gridy = 2;
        dialog1.add(heaterIdLabel, gbc1);

        gbc1.gridx = 1;
        gbc1.gridy = 2;
        dialog1.add(inputReading3, gbc1);

        gbc1.gridx = 1;
        gbc1.gridy = 3;
        dialog1.add(submitButton, gbc1);

        dialog1.setVisible(true);
    }

    private static void deleteTasks(JFrame parentFrame, CurrentController controller, ITaskDao taskDao){
        JDialog dialog = new JDialog(parentFrame, "Add Heater Reading", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parentFrame);

        JLabel labelReading = new JLabel("Task Id:");
        JTextField inputReading = new JTextField(10);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                String taskInput = inputReading.getText();
                int taskId = Integer.parseInt(taskInput);

                for (Task task : taskDao.getTasksByController(controller.getId())) {
                    if(task.getId() == taskId){
                        break;
                    } else {
                        throw new ArrayIndexOutOfBoundsException("There is no task with this id");
                    }
                }

                taskDao.deleteTask(taskId);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(dialog, "There is no task with this id","Input Error", JOptionPane.ERROR_MESSAGE);
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
        gbc.gridy = 1;
        dialog.add(submitButton, gbc);

        dialog.setVisible(true);
    }
}

