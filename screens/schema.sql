CREATE TABLE IF NOT EXISTS students (
    student_id INTEGER PRIMARY KEY AUTOINCREMENT,
    roll_number TEXT NOT NULL UNIQUE CHECK (length(trim(roll_number)) > 0),
    name TEXT NOT NULL CHECK (length(trim(name)) > 0),
    branch TEXT NOT NULL CHECK (length(trim(branch)) > 0),
    semester INTEGER NOT NULL CHECK (semester > 0)
);

CREATE TABLE IF NOT EXISTS subject_marks (
    mark_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    subject_name TEXT NOT NULL CHECK (length(trim(subject_name)) > 0),
    marks_obtained REAL NOT NULL CHECK (marks_obtained >= 0),
    maximum_marks REAL NOT NULL CHECK (maximum_marks > 0),
    CHECK (marks_obtained <= maximum_marks),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);
