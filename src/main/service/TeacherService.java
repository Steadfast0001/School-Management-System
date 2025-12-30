package service;

import dao.TeacherDAO;
import model.Teacher;
import java.util.List;

public class TeacherService {

    private final TeacherDAO teacherDAO = new TeacherDAO();

    /**
     * Add teacher
     */
    public boolean addTeacher(Teacher teacher) {

        if (teacher == null) return false;

        if (teacher.getName() == null || teacher.getName().isEmpty())
            return false;

        if (teacher.getEmail() == null || teacher.getEmail().isEmpty())
            return false;

        teacherDAO.addTeacher(teacher);
        return true;
    }

    /**
     * Get all teachers
     */
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAllTeachers();
    }

    /**
     * Update teacher
     */
    public boolean updateTeacher(Teacher teacher) {

        if (teacher == null || teacher.getId() <= 0)
            return false;

        teacherDAO.updateTeacher(teacher);
        return true;
    }

    /**
     * Delete teacher
     */
    public boolean deleteTeacher(int teacherId) {

        if (teacherId <= 0) return false;

        teacherDAO.deleteTeacher(teacherId);
        return true;
    }
}
