
package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.studenttracker.database.DatabaseConnection;

public class StudentRegistration extends JPanel {

    private JTextField idField, nameField, emailField, phoneField, courseField;
    private JComboBox<String> yearField;

    private final Color navy = new Color(24, 27, 49);
    private final Color purple = new Color(139, 116, 255);
    private final Color textColor = new Color(45, 48, 72);

    public StudentRegistration() {
        setLayout(new BorderLayout(0, 22));
        setBackground(new Color(246, 245, 252));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Register a Student");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(navy);

        JLabel subtitle = new JLabel("Add a student's details to the academic portal.");
        subtitle.setForeground(new Color(125, 125, 150));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(7));
        heading.add(subtitle);
        add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 18, 18));
        form.setOpaque(false);

        idField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        courseField = new JTextField();

        yearField = new JComboBox<>(new String[]{
            "1st Year", "2nd Year", "3rd Year", "4th Year"
        });

        addField(form, "Student ID / Roll Number *", idField);
        addField(form, "Full Name *", nameField);
        addField(form, "Email Address", emailField);
        addField(form, "Phone Number", phoneField);
        addField(form, "Course / Branch *", courseField);
        addField(form, "Academic Year", yearField);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear Form");
        JButton registerButton = new JButton("Register Student");
        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        styleButton(registerButton);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setOpaque(false);
        buttons.add(clearButton);
        buttons.add(registerButton);
        add(buttons, BorderLayout.SOUTH);

        clearButton.addActionListener(e -> clearForm());
        registerButton.addActionListener(e -> saveStudent());
    }

    private void saveStudent() {
        String roll = idField.getText().trim();
        String name = nameField.getText().trim();
        String branch = courseField.getText().trim();
        int semester = yearField.getSelectedIndex() + 1;

        if (roll.isEmpty() || name.isEmpty() || branch.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter Student ID, Full Name, and Course / Branch.",
                "Missing Details", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO students (roll_number, name, branch, semester) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, roll);
            statement.setString(2, name);
            statement.setString(3, branch);
            statement.setInt(4, semester);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Student registered successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();

        } catch (SQLException ex) {
            String message = ex.getMessage() != null
                && ex.getMessage().toLowerCase().contains("unique")
                ? "That Student ID already exists. Please use a different ID."
                : "Could not save student: " + ex.getMessage();

            JOptionPane.showMessageDialog(this, message,
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addField(JPanel form, String label, JComponent field) {
        JPanel group = new JPanel(new BorderLayout(0, 7));
        group.setOpaque(false);

        JLabel caption = new JLabel(label);
        caption.setForeground(textColor);
        caption.setFont(new Font("SansSerif", Font.BOLD, 12));

        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(200, 38));

        group.add(caption, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        form.add(group);
    }

    private void styleButton(JButton button) {
        button.setBackground(purple);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 18, 12, 18));
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        courseField.setText("");
        yearField.setSelectedIndex(0);
    }
}
