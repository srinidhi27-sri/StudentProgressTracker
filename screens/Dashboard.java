
package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Dashboard extends JFrame {

    private final Color navy = new Color(24, 27, 49);
    private final Color purple = new Color(139, 116, 255);
    private final Color background = new Color(246, 245, 252);
    private final Color textColor = new Color(45, 48, 72);

    private JPanel content;
    private CardLayout cardLayout;

    public Dashboard() {
        setTitle("Student Hub | Student Progress Tracker");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setBackground(navy);
        sidebar.setPreferredSize(new Dimension(225, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(28, 16, 20, 16));

        JLabel logo = new JLabel("✦ STUDENT HUB");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));

        addMenu(sidebar, "⌂   Dashboard", "dashboard");
        addMenu(sidebar, "♙   Students", "students");
        addMenu(sidebar, "▤   Marks", "marks");
        addMenu(sidebar, "◷   Attendance", "attendance");
        addMenu(sidebar, "▧   Progress Reports", "reports");

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("Academic Portal • 2026");
        footer.setForeground(new Color(185, 185, 215));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);

        // CONTENT AREA: all sections appear here
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);

        content.add(createHome(), "dashboard");
        content.add(new StudentRegistration(), "students");
        content.add(new MarksManagement(), "marks");
        content.add(new AttendanceManagement(), "attendance");
       content.add(new ProgressReports(), "reports");

        add(content, BorderLayout.CENTER);
        cardLayout.show(content, "dashboard");
    }

    private void addMenu(JPanel sidebar, String label, String page) {
        JButton button = new JButton(label);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(193, 44));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(page.equals("dashboard") ? purple : navy);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(0, 12, 0, 5));
        button.setOpaque(true);

        button.addActionListener(e -> cardLayout.show(content, page));

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(9));
    }

    private JPanel createHome() {
        JPanel main = new JPanel(new BorderLayout(0, 22));
        main.setBackground(background);
        main.setBorder(new EmptyBorder(28, 30, 28, 30));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(textColor);

        JLabel subtitle = new JLabel(
            "Welcome back! Here's your academic overview."
        );
        subtitle.setForeground(new Color(125, 125, 150));

        heading.add(title);
        heading.add(Box.createVerticalStrut(7));
        heading.add(subtitle);
        main.add(heading, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(sectionTitle("YOUR OVERVIEW"));
        body.add(Box.createVerticalStrut(14));

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 125));

        stats.add(createStat("Total Students", "120",
            "Registered learners"));
        stats.add(createStat("Average Marks", "85%",
            "Overall performance"));
        stats.add(createStat("Attendance", "92%",
            "Average attendance"));

        body.add(stats);
        body.add(Box.createVerticalStrut(28));
        body.add(sectionTitle("QUICK ACTIONS"));
        body.add(Box.createVerticalStrut(14));

        JPanel actions = new JPanel(new GridLayout(2, 2, 14, 14));
        actions.setOpaque(false);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        addQuickAction(actions, "＋  Register Student", "students");
        addQuickAction(actions, "✎  Manage Marks", "marks");
        addQuickAction(actions, "◷  Record Attendance", "attendance");
        addQuickAction(actions, "▧  View Reports", "reports");

        body.add(actions);
        body.add(Box.createVerticalStrut(26));
        body.add(sectionTitle("WELCOME"));
        body.add(Box.createVerticalStrut(12));

        JLabel welcome = new JLabel(
            "  ✦   Select a section from the sidebar to get started."
        );
        welcome.setOpaque(true);
        welcome.setBackground(Color.WHITE);
        welcome.setForeground(textColor);
        welcome.setBorder(new EmptyBorder(20, 15, 20, 15));
        welcome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        body.add(welcome);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scroll, BorderLayout.CENTER);

        return main;
    }

    private JPanel createStat(String name, String value, String detail) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 12, 10));

        JLabel label = new JLabel(name);
        label.setForeground(new Color(110, 105, 145));
        label.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel number = new JLabel(value);
        number.setForeground(textColor);
        number.setFont(new Font("SansSerif", Font.BOLD, 27));

        JLabel description = new JLabel(detail);
        description.setForeground(new Color(140, 140, 160));
        description.setFont(new Font("SansSerif", Font.PLAIN, 11));

        card.add(label);
        card.add(Box.createVerticalStrut(9));
        card.add(number);
        card.add(Box.createVerticalStrut(4));
        card.add(description);

        return card;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(125, 125, 150));
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void addQuickAction(JPanel panel, String name, String page) {
        JButton button = new JButton(
            "<html><b>" + name + "</b><br>"
            + "<font color='#85859C'>Open this section →</font></html>"
        );
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(Color.WHITE);
        button.setForeground(textColor);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setBorder(new EmptyBorder(14, 16, 14, 10));
        button.setFocusPainted(false);
        button.addActionListener(e -> cardLayout.show(content, page));
        panel.add(button);
    }

    private JPanel createPlaceholder(String titleText, String description) {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(description);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(125, 125, 150));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(8));
        heading.add(subtitle);

        panel.add(heading, BorderLayout.NORTH);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new Dashboard().setVisible(true)
        );
    }
}
