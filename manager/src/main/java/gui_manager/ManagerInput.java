package gui_manager;

import dao.intefaces.IManagerDao;
import logger.FileLogger;

import javax.swing.*;
import java.awt.*;


public class ManagerInput extends JPanel {
    FileLogger fileLogger = new FileLogger("application.log");

    public ManagerInput(JFrame frame, IManagerDao managerDao, InputCallback callback) {
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
                if(managerDao.getManager(id) == null) {
                    throw new IllegalArgumentException("Manager not found");
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
