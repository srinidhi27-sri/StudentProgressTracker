
package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentRegistration extends JPanel {

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField courseField;
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

        JLabel subtitle = new JLabel(
            "Add a student's details to the academic portal."
        );
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

        addField(form, "Student ID *", idField);
        addField(form, "Full Name *", nameField);
        addField(form, "Email Address", emailField);
        addField(form, "Phone Number", phoneField);
        addField(form, "Course / Branch", courseField);
        addField(form, "Academic Year", yearField);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear Form");
        JButton registerButton = new JButton("Register Student");

        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        styleButton(registerButton);

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );
        buttons.setOpaque(false);
        buttons.add(clearButton);
        buttons.add(registerButton);
        add(buttons, BorderLayout.SOUTH);

        clearButton.addActionListener(e -> clearForm());

        registerButton.addActionListener(e -> {
            if (idField.getText().trim().isEmpty()
                    || nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please enter Student ID and Full Name.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Form validation successful!\n"
                + "Database saving will be connected later.",
                "Student Registration",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }

    private void addField(
            JPanel form, String label, JComponent field) {

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
