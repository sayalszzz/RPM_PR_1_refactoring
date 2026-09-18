CREATE INDEX idx_grades_student_value ON grades (student_id, grade_value);
CREATE INDEX idx_grades_subject_value ON grades (subject_id, grade_value);

ALTER TABLE grades
    ADD CONSTRAINT chk_grade_value CHECK (grade_value BETWEEN 1 AND 5);
