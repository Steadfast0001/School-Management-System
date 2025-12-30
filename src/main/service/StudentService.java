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

        if (student == null) return false;

        if (student.getName() == null || student.getName().isEmpty())
            return false;

        if (student.getMatricule() == null || student.getMatricule().isEmpty())
            return false;

        studentDAO.addStudent(student);
        return true;
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

        if (student == null || student.getId() <= 0)
            return false;

        studentDAO.updateStudent(student);
        return true;
    }

    /**
     * Delete student
     */
    public boolean deleteStudent(int studentId) {

        if (studentId <= 0) return false;

        studentDAO.deleteStudent(studentId);
        return true;
    }
}
