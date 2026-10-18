
package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProgressReports extends JPanel {

    private final Color navy = new Color(24, 27, 49);
    private final Color purple = new Color(139, 116, 255);
    private final Color background = new Color(246, 245, 252);

    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextField marksField;
    private JTextField totalMarksField;
    private JTextField attendanceField;
    private JTextArea reportArea;

    public ProgressReports() {
        setLayout(new BorderLayout(0, 20));
        setBackground(background);
        setBorder(new EmptyBorder(30, 35, 30, 35));

        // HEADING
        JLabel title = new JLabel("Progress Reports");
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
            "Generate a student academic performance summary."
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
        form.setBorder(new EmptyBorder(20, 22, 20, 22));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        studentIdField = new JTextField();
        studentNameField = new JTextField();
        marksField = new JTextField();
        totalMarksField = new JTextField();
        attendanceField = new JTextField();

        addField(form, gbc, 0, "Student ID", studentIdField);
        addField(form, gbc, 1, "Student Name", studentNameField);
        addField(form, gbc, 2, "Marks Obtained", marksField);
        addField(form, gbc, 3, "Total Marks", totalMarksField);
        addField(form, gbc, 4, "Attendance (%)", attendanceField);

        JButton generateButton = new JButton("Generate Report");
        generateButton.setBackground(purple);
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.addActionListener(e -> generateReport());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttons.setOpaque(false);
        buttons.add(generateButton);
        buttons.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        // REPORT PREVIEW
        reportArea = new JTextArea(7, 30);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBackground(new Color(250, 249, 255));
        reportArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        reportArea.setText("Your generated report will appear here.");

        JScrollPane reportScroll = new JScrollPane(reportArea);
        reportScroll.setBorder(BorderFactory.createTitledBorder(
            "REPORT PREVIEW"
        ));

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);
        center.add(reportScroll, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    private void addField(JPanel form, GridBagConstraints gbc,
                          int row, String label, JTextField field) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(navy);
        fieldLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        form.add(fieldLabel, gbc);

        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(240, 30));
        form.add(field, gbc);
    }

    private void generateReport() {
        String id = studentIdField.getText().trim();
        String name = studentNameField.getText().trim();

        if (id.isEmpty() || name.isEmpty()
                || marksField.getText().trim().isEmpty()
                || totalMarksField.getText().trim().isEmpty()
                || attendanceField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            double marks = Double.parseDouble(marksField.getText().trim());
            double total = Double.parseDouble(
                totalMarksField.getText().trim()
            );
            double attendance = Double.parseDouble(
                attendanceField.getText().trim()
            );

            if (total <= 0 || marks < 0 || marks > total
                    || attendance < 0 || attendance > 100) {
                JOptionPane.showMessageDialog(
                    this,
                    "Check marks, total marks, and attendance values.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            double percentage = marks / total * 100;
            String grade;

            if (percentage >= 90) grade = "A+";
            else if (percentage >= 80) grade = "A";
            else if (percentage >= 70) grade = "B";
            else if (percentage >= 60) grade = "C";
            else if (percentage >= 40) grade = "D";
            else grade = "F";

            String attendanceStatus = attendance >= 75
                ? "Eligible"
                : "Attendance Below 75%";

            String report = 
                "====================================\n"
                + "       STUDENT PROGRESS REPORT\n"
                + "====================================\n\n"
                + "Student ID   : " + id + "\n"
                + "Student Name : " + name + "\n\n"
                + String.format("Marks        : %.2f / %.2f%n",
                    marks, total)
                + String.format("Percentage   : %.2f%%%n", percentage)
                + "Grade        : " + grade + "\n"
                + String.format("Attendance   : %.2f%%%n", attendance)
                + "Status       : " + attendanceStatus + "\n\n"
                + "====================================\n"
                + "         END OF REPORT\n"
                + "====================================";

            reportArea.setText(report);
            reportArea.setCaretPosition(0);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Enter valid numbers in marks and attendance fields.",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        studentIdField.setText("");
        studentNameField.setText("");
        marksField.setText("");
        totalMarksField.setText("");
        attendanceField.setText("");
        reportArea.setText("Your generated report will appear here.");
    }
}
