-- Таблица студентов
CREATE TABLE IF NOT EXISTS students (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    group_name VARCHAR(50)
    );

-- Таблица предметов
CREATE TABLE IF NOT EXISTS subjects (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        title VARCHAR(100) NOT NULL UNIQUE
    );

-- Таблица оценок
CREATE TABLE IF NOT EXISTS grades (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      student_id INT,
                                      subject_id INT,
                                      grade_value DOUBLE NOT NULL,
                                      FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
    );