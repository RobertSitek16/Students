package com.robert.demo.dao;

import com.robert.demo.model.student.Gender;
import com.robert.demo.model.student.Student;
import com.robert.demo.model.student.StudentCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDataAccessService implements StudentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Student> selectAllStudents() {
        String sql = "" +
                "SELECT " +
                " student_id, " +
                " first_name, " +
                " last_name, " +
                " email, " +
                " gender " +
                "FROM student";
        return jdbcTemplate.query(sql, mapStudentFromDB());
    }

    @Override
    public Student selectStudentById(UUID studentId) {
        String sql = "" +
                "SELECT " +
                " student_id, " +
                " first_name, " +
                " last_name, " +
                " email, " +
                " gender " +
                "FROM student " +
                "WHERE student_id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{studentId},
                mapStudentFromDB()
        );
    }

    @Override
    public List<StudentCourse> selectAllStudentCourses(UUID studentId) {
        String sql = "" +
                "SELECT " +
                " student.student_id, " +
                " course.course_id, " +
                " student_course.start_date, " +
                " student_course.end_date, " +
                " student_course.grade, " +
                " course.name, " +
                " course.description, " +
                " course.department, " +
                " course.teacher_name " +
                " FROM student " +
                " JOIN student_course USING (student_id) " +
                " JOIN course USING (course_id) " +
                " WHERE student.student_id = ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{studentId},
                mapCoursesFromDb()
        );
    }

    private RowMapper<StudentCourse> mapCoursesFromDb() {
        return ((resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id");
            UUID studentId = UUID.fromString(studentIdStr);
            String courseIdStr = resultSet.getString("course_id");
            UUID courseId = UUID.fromString(courseIdStr);

            LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
            LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
            Integer grade = Optional.ofNullable(resultSet.getString("grade"))
                    .map(Integer::parseInt)
                    .orElse(null);

            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String department = resultSet.getString("department");
            String teacherName = resultSet.getString("teacher_name");

            return new StudentCourse(
                    studentId,
                    courseId,
                    name,
                    description,
                    department,
                    teacherName,
                    startDate,
                    endDate,
                    grade
            );
        });
    }

    private RowMapper<Student> mapStudentFromDB() {
        return (resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id");
            UUID studentId = UUID.fromString(studentIdStr);

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");

            String genderStr = resultSet.getString("gender").toUpperCase();
            Gender gender = Gender.valueOf(genderStr);
            return new Student(
                    studentId,
                    firstName,
                    lastName,
                    email,
                    gender
            );
        };
    }

    @Override
    public int insertStudent(UUID studentId, Student student) {
        String sql = "" +
                "INSERT INTO student (" +
                "student_id, " +
                "first_name, " +
                "last_name, " +
                "email, " +
                "gender) " +
                "VALUES (?, ?, ?, ?, ?::gender)";
        return jdbcTemplate.update(
                sql,
                studentId,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().name().toUpperCase()
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isTheStudentPresent(UUID studentId){
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM student " +
                " WHERE student_id = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {studentId},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isEmailTaken(String email) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM student " +
                " WHERE email = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {email},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean selectExistsEmail(UUID studentId, String email) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM student " +
                " WHERE email = ? " +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{email},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean doesTheStudentHaveACourse(UUID studentId) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM student_course " +
                " WHERE student_id = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {studentId},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @Override
    public int updateEmail(UUID studentId, String email) {
        String sql = "" +
                " UPDATE student " +
                " SET email = ? " +
                " WHERE student_id = ?";
        return jdbcTemplate.update(sql, email, studentId);
    }

    @Override
    public int updateFirstName(UUID studentId, String firstName) {
        String sql = "" +
                " UPDATE student " +
                " SET first_name = ?" +
                " WHERE student_id = ?";
        return jdbcTemplate.update(sql, firstName, studentId);
    }

    @Override
    public int updateLastName(UUID studentId, String lastName) {
        String sql = "" +
                " UPDATE student " +
                " SET last_name = ? " +
                " WHERE student_id = ?";
        return jdbcTemplate.update(sql, lastName, studentId);
    }

    @Override
    public int deleteStudentById(UUID studentId) {
        String sql = "" +
                " DELETE FROM student " +
                " WHERE student_id = ?";
        return jdbcTemplate.update(sql, studentId);
    }
}
