package gui_tenant;

import dao.intefaces.IHeaterDao;
import logger.FileLogger;

import javax.swing.*;
import java.awt.*;

public class UserLogin extends JPanel {
    private FileLogger fileLogger = new FileLogger("application.log");

    public UserLogin(JFrame frame,IHeaterDao heaterDao ,InputCallback callback) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel idLabel = new JLabel("Enter your flat id: ");
        JTextField idField = new JTextField(10);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(e -> {
            try {
                String userInput = idField.getText();
                if (userInput.isEmpty()) {
                    throw new IllegalArgumentException("String is empty");
                }
                int id = Integer.parseInt(userInput);
                if(heaterDao.getAllHeaterByFlatId(id).isEmpty()) {
                    throw new IllegalArgumentException("There is no heater with that flat id");
                }
                callback.onInputReceived(id);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                fileLogger.logSevere("Invalid input: " + ex.getMessage());
            }
        });

        add(idLabel);
        add(idField);
        add(submitButton);
    }

    public interface InputCallback {
        void onInputReceived(int id);
    }
}
