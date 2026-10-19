
package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.studenttracker.database.DatabaseConnection;

public class ProgressReports extends JPanel {

    private final Color navy = new Color(24, 27, 49);
    private final Color purple = new Color(139, 116, 255);
    private final Color background = new Color(246, 245, 252);

    private JTextField studentIdField;
    private JTextArea reportArea;

    public ProgressReports() {
        setLayout(new BorderLayout(0, 20));
        setBackground(background);
        setBorder(new EmptyBorder(30, 35, 30, 35));

        JLabel title = new JLabel("Progress Reports");
        title.setFont(new Font("SansSerif", Font.BOLD, 29));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
            "Generate academic reports from saved student records."
        );
        subtitle.setForeground(new Color(125, 125, 150));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(8));
        heading.add(subtitle);

        add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 22, 20, 22));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        studentIdField = new JTextField();
        studentIdField.setPreferredSize(new Dimension(240, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Student Roll Number"), gbc);

        gbc.gridx = 1;
        form.add(studentIdField, gbc);

        JButton generateButton = new JButton("Generate Report");
        generateButton.setBackground(purple);
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.addActionListener(e -> generateReport());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            studentIdField.setText("");
            reportArea.setText("Your generated report will appear here.");
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttons.setOpaque(false);
        buttons.add(generateButton);
        buttons.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        reportArea = new JTextArea(12, 35);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBackground(new Color(250, 249, 255));
        reportArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        reportArea.setText("Your generated report will appear here.");

        JScrollPane reportScroll = new JScrollPane(reportArea);
        reportScroll.setBorder(
            BorderFactory.createTitledBorder("REPORT PREVIEW")
        );

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);
        center.add(reportScroll, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    private void generateReport() {
        String rollNumber = studentIdField.getText().trim();

        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Enter the student's roll number.",
                "Missing Information", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String studentSql =
            "SELECT student_id, roll_number, name, branch, semester " +
            "FROM students WHERE roll_number = ?";

        String marksSql =
            "SELECT subject_name, marks_obtained, maximum_marks " +
            "FROM subject_marks WHERE student_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement studentStmt =
                 con.prepareStatement(studentSql)) {

            studentStmt.setString(1, rollNumber);

            try (ResultSet student = studentStmt.executeQuery()) {
                if (!student.next()) {
                    JOptionPane.showMessageDialog(
                        this, "Student not found. Check the roll number.",
                        "No Student", JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int studentId = student.getInt("student_id");
                String name = student.getString("name");
                String branch = student.getString("branch");
                int semester = student.getInt("semester");

                StringBuilder report = new StringBuilder();
                report.append("========================================\n");
                report.append("         STUDENT PROGRESS REPORT\n");
                report.append("========================================\n\n");
                report.append("Roll Number : ").append(rollNumber).append("\n");
                report.append("Name        : ").append(name).append("\n");
                report.append("Branch      : ").append(branch).append("\n");
                report.append("Semester    : ").append(semester).append("\n\n");
                report.append("----------------------------------------\n");
                report.append("SUBJECT-WISE MARKS\n");
                report.append("----------------------------------------\n");

                double totalObtained = 0;
                double totalMaximum = 0;
                int subjectCount = 0;

                try (PreparedStatement marksStmt =
                         con.prepareStatement(marksSql)) {

                    marksStmt.setInt(1, studentId);

                    try (ResultSet marks = marksStmt.executeQuery()) {
                        while (marks.next()) {
                            String subject = marks.getString("subject_name");
                            double obtained =
                                marks.getDouble("marks_obtained");
                            double maximum =
                                marks.getDouble("maximum_marks");

                            report.append(String.format(
                                "%-18s %7.2f / %-7.2f%n",
                                subject, obtained, maximum
                            ));

                            totalObtained += obtained;
                            totalMaximum += maximum;
                            subjectCount++;
                        }
                    }
                }

                report.append("\n----------------------------------------\n");

                if (subjectCount == 0) {
                    report.append("No marks have been saved yet.\n");
                } else {
                    double percentage =
                        totalObtained / totalMaximum * 100;

                    String grade;
                    if (percentage >= 90) grade = "A+";
                    else if (percentage >= 80) grade = "A";
                    else if (percentage >= 70) grade = "B";
                    else if (percentage >= 60) grade = "C";
                    else if (percentage >= 40) grade = "D";
                    else grade = "F";

                    report.append(String.format(
                        "Total Marks : %.2f / %.2f%n",
                        totalObtained, totalMaximum
                    ));
                    report.append(String.format(
                        "Percentage  : %.2f%%%n", percentage
                    ));
                    report.append("Overall Grade: ").append(grade).append("\n");
                    report.append("Subjects     : ").append(subjectCount).append("\n");
                }

                report.append("\n========================================\n");
                report.append("             END OF REPORT\n");
                report.append("========================================");

                reportArea.setText(report.toString());
                reportArea.setCaretPosition(0);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not generate report:\n" + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
