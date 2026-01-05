package service;

import dao.StudentDAO;
import model.Student;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();

    /**
     * Add new student
     */
    public boolean addStudent(Student student) {

        if (!isValidStudent(student)) {
            return false;
        }

        // Check if matricule already exists
        if (studentDAO.existsByMatricule(student.getMatricule())) {
            return false;
        }

        return studentDAO.addStudent(student);
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    /**
     * Update student
     */
    public boolean updateStudent(Student student) {

    if (student == null || student.getId() <= 0) {
        return false;
    }

    if (!isValidStudent(student)) {
        return false;
    }

    // Ensure matricule is unique except for this student
    if (studentDAO.existsByMatriculeAndNotId(
            student.getMatricule(), student.getId())) {
        return false;
    }

    return studentDAO.updateStudent(student);
}


    /**
     * Delete student
     */
    public boolean deleteStudent(int studentId) {

        if (studentId <= 0) {
            return false;
        }

        return studentDAO.deleteStudent(studentId);
    }

    /**
     * Validate student fields
     */
    private boolean isValidStudent(Student student) {

        if (student == null) return false;

        if (student.getName() == null || student.getName().trim().isEmpty())
            return false;

        if (student.getMatricule() == null || student.getMatricule().trim().isEmpty())
            return false;

        return true;
    }
}
