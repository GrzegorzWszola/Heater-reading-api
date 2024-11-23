package gui_controller;

import dao.intefaces.IControllerDao;
import logger.FileLogger;

import javax.swing.*;
import java.awt.*;

public class ControlerInput extends JPanel {
    private FileLogger fileLogger = new FileLogger("application.log");

    public ControlerInput(JFrame frame, IControllerDao controllerDao, InputCallback callback) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel inputLabel = new JLabel("Enter your id: ");
        JTextField idField = new JTextField(10);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            try {
                String userInput = idField.getText();
                if (userInput.isEmpty()) {
                    throw new IllegalArgumentException("String is empty");
                }
                int id = Integer.parseInt(userInput);
                if(controllerDao.getController(id) == null) {
                    throw new IllegalArgumentException("Controller not found");
                }
                callback.onInputReceived(id);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                fileLogger.logSevere("Invalid input: " + ex.getMessage());
            }
        });

        add(inputLabel);
        add(idField);
        add(submitButton);
    }

    public interface InputCallback {
        void onInputReceived(int id);
    }
}
