import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

public class SignUpForm extends JFrame {
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JButton clearButton;
    private JLabel passwordStrengthLabel;

    public SignUpForm() {
        setTitle("Sign Up Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        panel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        panel.add(fullNameField);

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        passwordStrengthLabel = new JLabel();
        panel.add(passwordStrengthLabel);

        submitButton = new JButton("Sign Up");
        clearButton = new JButton("Clear");
        panel.add(clearButton);
        panel.add(submitButton);

        SignUpActionListener listener = new SignUpActionListener(this);
        submitButton.addActionListener(listener);
        clearButton.addActionListener(listener);
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String password = new String(passwordField.getPassword());
                passwordStrengthLabel.setText(getPasswordStrength(password));
            }
        });

        add(panel);
        pack();
        setVisible(true);
    }

    public JTextField getFullNameField() {
        return fullNameField;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JLabel getPasswordStrengthLabel() {
        return passwordStrengthLabel;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    private String getPasswordStrength(String password) {
        if (password.length() < 8) return "Weak";
        if (password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[0-9].*") && password.matches(".*[@,#,$,%].*$")) {
            return "Strong";
        }
        return "Medium";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUpForm());
    }

    private class SignUpActionListener implements ActionListener {
        private SignUpForm form;

        public SignUpActionListener(SignUpForm form) {
            this.form = form;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String fullName = form.getFullNameField().getText();
            String username = form.getUsernameField().getText();
            String email = form.getEmailField().getText();
            String phone = form.getPhoneField().getText();
            String password = new String(form.getPasswordField().getPassword());

            if (e.getSource() == form.getClearButton()) {
                clearFields();
            } else if (e.getSource() == form.getSubmitButton()) {
                if (validateFields(fullName, username, email, phone, password)) {
                    JOptionPane.showMessageDialog(form, "Sign up successful!\nWelcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // TODO: Save to your database using JDBC implemented in a Singleton Class
                }
            }
        }

        private boolean validateFields(String fullName, String username, String email, String phone, String password) {
            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(form, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (username.length() < 5) {
                JOptionPane.showMessageDialog(form, "Username must be at least 5 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(form, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isValidPhone(phone)) {
                JOptionPane.showMessageDialog(form, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(form, "Password must be at least 8 characters long, contain uppercase and lowercase letters, numbers, and special characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        private void clearFields() {
            form.getFullNameField().setText("");
            form.getUsernameField().setText("");
            form.getEmailField().setText("");
            form.getPhoneField().setText("");
            form.getPasswordField().setText("");
            form.getPasswordStrengthLabel().setText("");
        }

        private boolean isValidEmail(String email) {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pat = Pattern.compile(emailRegex);
            return pat.matcher(email).matches();
        }

        private boolean isValidPhone(String phone) {
            String phoneRegex = "\\d{10}";
            Pattern pat = Pattern.compile(phoneRegex);
            return pat.matcher(phone).matches();
        }

        private boolean isValidPassword(String password) {
            if (password.length() < 8) return false;
            String upperCaseChars = "(.*[A-Z].*)";
            if (!password.matches(upperCaseChars)) return false;
            String lowerCaseChars = "(.*[a-z].*)";
            if (!password.matches(lowerCaseChars)) return false;
            String numbers = "(.*[0-9].*)";
            if (!password.matches(numbers)) return false;
            String specialChars = "(.*[@,#,$,%].*$)";
            return password.matches(specialChars);
        }
    }
}
