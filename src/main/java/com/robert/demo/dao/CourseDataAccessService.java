package com.robert.demo.dao;

import com.robert.demo.model.course.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CourseDataAccessService implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    public CourseDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Course> selectAllCourses() {
        String sql = "" +
                " SELECT " +
                " course_id, " +
                " name, " +
                " description, " +
                " department, " +
                " teacher_name " +
                " FROM course";
        return jdbcTemplate.query(sql, mapCourseFromDb());
    }

    @Override
    public int insertCourse(UUID courseId, Course course) {
        String sql = "" +
                "INSERT INTO course (" +
                "course_id, " +
                "name, " +
                "description, " +
                "department, " +
                "teacher_name) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                courseId,
                course.getName(),
                course.getDescription(),
                course.getDepartment(),
                course.getTeacherName()
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isCourseNameTaken(String name) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM course " +
                " WHERE name = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {name},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isTheCourseActive(UUID courseId) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM student_course " +
                " WHERE course_id = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {courseId},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean selectExistsCourseName(UUID courseId, String name) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM course " +
                " WHERE name = ? " +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{name},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @Override
    public int updateCourseName(UUID courseId, String name) {
        String sql = "" +
                " UPDATE course " +
                " SET name = ? " +
                " WHERE course_id = ?";
        return jdbcTemplate.update(sql, name, courseId);
    }

    @Override
    public int updateDescription(UUID courseId, String description) {
        String sql = "" +
                " UPDATE course " +
                " SET description = ? " +
                " WHERE course_id = ?";
        return jdbcTemplate.update(sql, description, courseId);
    }

    @Override
    public int updateDepartment(UUID courseId, String department) {
        String sql = "" +
                " UPDATE course " +
                " SET department = ? " +
                " WHERE course_id = ?";
        return jdbcTemplate.update(sql, department, courseId);
    }

    @Override
    public int updateTeacherName(UUID courseId, String teacherName) {
        String sql = "" +
                " UPDATE course " +
                " SET teacher_name = ? " +
                " WHERE course_id = ?";
        return jdbcTemplate.update(sql, teacherName, courseId);
    }

    @Override
    public int deleteCourseById(UUID courseId) {
        String sql = "" +
                " DELETE FROM course " +
                " WHERE course_id = ?";
        return jdbcTemplate.update(sql, courseId);
    }

    private RowMapper<Course> mapCourseFromDb() {
        return (resultSet, i) ->
                new Course(
                        UUID.fromString(resultSet.getString("course_id")),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("department"),
                        Optional.ofNullable(resultSet.getString("teacher_name"))
                                .orElse(null)
                );
    }
}
