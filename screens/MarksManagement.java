
package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MarksManagement extends JPanel {

    private final Color navy = new Color(24, 27, 49);
    private final Color purple = new Color(139, 116, 255);
    private final Color background = new Color(246, 245, 252);
    private final Color textColor = new Color(45, 48, 72);

    private JTextField studentIdField;
    private JTextField subjectField;
    private JTextField marksField;
    private JTextField totalField;
    private JLabel resultLabel;

    public MarksManagement() {
        setLayout(new BorderLayout(0, 20));
        setBackground(background);
        setBorder(new EmptyBorder(30, 35, 30, 35));

        // PAGE HEADING
        JLabel title = new JLabel("Marks Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
            "Enter marks and calculate student performance."
        );
        subtitle.setForeground(new Color(125, 125, 150));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(8));
        heading.add(subtitle);

        add(heading, BorderLayout.NORTH);

        // FORM CARD
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        studentIdField = new JTextField();
        subjectField = new JTextField();
        marksField = new JTextField();
        totalField = new JTextField();

        addField(form, gbc, 0, "Student ID", studentIdField);
        addField(form, gbc, 1, "Subject", subjectField);
        addField(form, gbc, 2, "Marks Obtained", marksField);
        addField(form, gbc, 3, "Total Marks", totalField);

        // BUTTONS
        JButton calculateButton = new JButton("Calculate Result");
        calculateButton.setBackground(purple);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        calculateButton.setFocusPainted(false);
        calculateButton.addActionListener(e -> calculateResult());

        JButton clearButton = new JButton("Clear Form");
        clearButton.setFocusPainted(false);
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
        resultLabel = new JLabel("Your result will appear here.");
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
                          int row, String label, JTextField field) {

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(textColor);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        form.add(fieldLabel, gbc);

        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(250, 35));
        form.add(field, gbc);
    }

    private void calculateResult() {

        String studentId = studentIdField.getText().trim();
        String subject = subjectField.getText().trim();
        String marksText = marksField.getText().trim();
        String totalText = totalField.getText().trim();

        if (studentId.isEmpty() || subject.isEmpty()
                || marksText.isEmpty() || totalText.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            double marks = Double.parseDouble(marksText);
            double total = Double.parseDouble(totalText);

            if (total <= 0 || marks < 0 || marks > total) {
                JOptionPane.showMessageDialog(
                    this,
                    "Marks must be between 0 and the total marks.",
                    "Invalid Marks",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            double percentage = (marks / total) * 100;

            String grade;

            if (percentage >= 90) {
                grade = "A+";
            } else if (percentage >= 80) {
                grade = "A";
            } else if (percentage >= 70) {
                grade = "B";
            } else if (percentage >= 60) {
                grade = "C";
            } else if (percentage >= 40) {
                grade = "D";
            } else {
                grade = "F";
            }

            resultLabel.setText(String.format(
                "<html>Student: %s | Subject: %s<br>"
                + "Percentage: %.2f%% | Grade: %s</html>",
                studentId, subject, percentage, grade
            ));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Enter valid numbers for marks and total marks.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        studentIdField.setText("");
        subjectField.setText("");
        marksField.setText("");
        totalField.setText("");
        resultLabel.setText("Your result will appear here.");
    }
}
