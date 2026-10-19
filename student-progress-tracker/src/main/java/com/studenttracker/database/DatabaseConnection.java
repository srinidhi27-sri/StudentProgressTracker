package com.studenttracker.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {
    private static final Path DATABASE_PATH = Path.of("database", "student_progress.db")
            .toAbsolutePath()
            .normalize();
    private static final String[] SUBJECTS = {
            "Java", "DBMS", "Data Structures", "Mathematics", "Computer Networks"
    };
    private static final String[][] SAMPLE_STUDENTS = {
            {"PBL26-001", "Aarav Mehta", "Computer Science", "3"},
            {"PBL26-002", "Diya Nair", "Information Technology", "4"},
            {"PBL26-003", "Kabir Shah", "Electronics and Communication", "2"},
            {"PBL26-004", "Mira Iyer", "Electrical Engineering", "5"},
            {"PBL26-005", "Rohan Das", "Mechanical Engineering", "3"},
            {"PBL26-006", "Anaya Rao", "Civil Engineering", "6"},
            {"PBL26-007", "Ishaan Kapoor", "Data Science", "1"},
            {"PBL26-008", "Tara Menon", "Cyber Security", "4"}
    };
    private static final int[][] SAMPLE_MARKS = {
            {86, 91, 78, 88, 82},
            {92, 84, 89, 81, 90},
            {76, 83, 88, 79, 85},
            {89, 94, 86, 91, 87},
            {81, 77, 84, 86, 80},
            {88, 82, 90, 85, 93},
            {95, 89, 92, 87, 90},
            {84, 88, 81, 90, 86}
    };

    private DatabaseConnection() {
    }

    public static void main(String[] args) {
        try {
            initializeDatabase();
            insertSampleDataIfEmpty();
            System.out.println("Database ready: " + DATABASE_PATH);
            System.out.println("Students: " + countRows("students"));
            System.out.println("Subject marks: " + countRows("subject_marks"));
        } catch (SQLException exception) {
            System.err.println("Database initialization failed: " + exception.getMessage());
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static Path getDatabasePath() {
        return DATABASE_PATH;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Files.createDirectories(DATABASE_PATH.getParent());
        } catch (IOException exception) {
            throw new SQLException("Could not create the database directory: "
                    + DATABASE_PATH.getParent(), exception);
        }

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            return connection;
        } catch (SQLException exception) {
            try {
                connection.close();
            } catch (SQLException closeException) {
                exception.addSuppressed(closeException);
            }
            throw exception;
        }
    }

    public static void initializeDatabase() throws SQLException {
        String schema = readSchema();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : schema.split(";")) {
                String sqlStatement = sql.trim();
                if (!sqlStatement.isEmpty()) {
                    statement.execute(sqlStatement);
                }
            }
        }
    }

    private static void insertSampleDataIfEmpty() throws SQLException {
        try (Connection connection = getConnection()) {
            if (countRows(connection, "students") != 0
                    || countRows(connection, "subject_marks") != 0) {
                System.out.println("Sample data was not inserted; existing records were preserved.");
                return;
            }

            connection.setAutoCommit(false);
            try (PreparedStatement studentStatement = connection.prepareStatement(
                    "INSERT INTO students (roll_number, name, branch, semester) VALUES (?, ?, ?, ?)");
                 PreparedStatement markStatement = connection.prepareStatement(
                         "INSERT INTO subject_marks "
                                 + "(student_id, subject_name, marks_obtained, maximum_marks) "
                                 + "VALUES (?, ?, ?, ?)")) {
                for (int i = 0; i < SAMPLE_STUDENTS.length; i++) {
                    String[] student = SAMPLE_STUDENTS[i];
                    studentStatement.setString(1, student[0]);
                    studentStatement.setString(2, student[1]);
                    studentStatement.setString(3, student[2]);
                    studentStatement.setInt(4, Integer.parseInt(student[3]));
                    studentStatement.executeUpdate();

                    int studentId;
                    try (Statement idStatement = connection.createStatement();
                         ResultSet resultSet = idStatement.executeQuery("SELECT last_insert_rowid()")) {
                        resultSet.next();
                        studentId = resultSet.getInt(1);
                    }

                    for (int j = 0; j < SUBJECTS.length; j++) {
                        markStatement.setInt(1, studentId);
                        markStatement.setString(2, SUBJECTS[j]);
                        markStatement.setInt(3, SAMPLE_MARKS[i][j]);
                        markStatement.setInt(4, 100);
                        markStatement.executeUpdate();
                    }
                }
                connection.commit();
                System.out.println("Inserted 8 sample students and 40 subject marks.");
            } catch (SQLException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw exception;
            }
        }
    }

    private static int countRows(String tableName) throws SQLException {
        try (Connection connection = getConnection()) {
            return countRows(connection, tableName);
        }
    }

    private static int countRows(Connection connection, String tableName) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private static String readSchema() throws SQLException {
        InputStream schemaStream = DatabaseConnection.class.getResourceAsStream("/schema.sql");
        if (schemaStream == null) {
            throw new SQLException("Could not find schema.sql on the application classpath.");
        }

        try (InputStream input = schemaStream) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new SQLException("Could not read schema.sql.", exception);
        }
    }
}