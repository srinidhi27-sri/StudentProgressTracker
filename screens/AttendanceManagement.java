
package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AttendanceManagement extends JPanel {

    private final Color navy = new Color(24, 27, 49);
    private final Color purple = new Color(139, 116, 255);
    private final Color background = new Color(246, 245, 252);

    private JTextField studentIdField;
    private JTextField totalClassesField;
    private JTextField attendedClassesField;
    private JComboBox<String> statusBox;
    private JLabel resultLabel;

    public AttendanceManagement() {
        setLayout(new BorderLayout(0, 20));
        setBackground(background);
        setBorder(new EmptyBorder(30, 35, 30, 35));

        // HEADING
        JLabel title = new JLabel("Attendance Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
            "Record attendance and calculate attendance percentage."
        );
        subtitle.setForeground(new Color(125, 125, 150));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(8));
        heading.add(subtitle);

        add(heading, BorderLayout.NORTH);

        // FORM
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        studentIdField = new JTextField();
        totalClassesField = new JTextField();
        attendedClassesField = new JTextField();

        statusBox = new JComboBox<>(new String[]{
            "Present", "Absent", "Late", "Excused"
        });

        addField(form, gbc, 0, "Student ID", studentIdField);
        addField(form, gbc, 1, "Total Classes", totalClassesField);
        addField(form, gbc, 2, "Classes Attended", attendedClassesField);
        addField(form, gbc, 3, "Today's Status", statusBox);

        // BUTTONS
        JButton calculateButton = new JButton("Calculate Attendance");
        calculateButton.setBackground(purple);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);
        calculateButton.addActionListener(e -> calculateAttendance());

        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> clearForm());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttons.setOpaque(false);
        buttons.add(calculateButton);
        buttons.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        // RESULT
        resultLabel = new JLabel("Attendance result will appear here.");
        resultLabel.setForeground(navy);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        gbc.gridy = 5;
        form.add(resultLabel, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(form, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private void addField(JPanel form, GridBagConstraints gbc,
                          int row, String label, JComponent field) {

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(navy);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        form.add(fieldLabel, gbc);

        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(250, 35));
        form.add(field, gbc);
    }

    private void calculateAttendance() {
        String studentId = studentIdField.getText().trim();
        String totalText = totalClassesField.getText().trim();
        String attendedText = attendedClassesField.getText().trim();
        String status = (String) statusBox.getSelectedItem();

        if (studentId.isEmpty() || totalText.isEmpty()
                || attendedText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            int total = Integer.parseInt(totalText);
            int attended = Integer.parseInt(attendedText);

            if (total <= 0 || attended < 0 || attended > total) {
                JOptionPane.showMessageDialog(
                    this,
                    "Enter valid class counts. Attended classes "
                    + "cannot exceed total classes.",
                    "Invalid Attendance",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            double percentage = attended * 100.0 / total;

            String eligibility = percentage >= 75
                ? "Eligible (75% or above)"
                : "Below 75% attendance";

            resultLabel.setText(String.format(
                "<html>Student: %s | Today's Status: %s<br>"
                + "Attendance: %.2f%% | %s</html>",
                studentId, status, percentage, eligibility
            ));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Enter whole numbers for total and attended classes.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        studentIdField.setText("");
        totalClassesField.setText("");
        attendedClassesField.setText("");
        statusBox.setSelectedIndex(0);
        resultLabel.setText("Attendance result will appear here.");
    }
}
