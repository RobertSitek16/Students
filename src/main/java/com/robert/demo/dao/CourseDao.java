package com.robert.demo.dao;

import com.robert.demo.model.course.Course;

import java.util.List;
import java.util.UUID;

public interface CourseDao {

    List<Course> selectAllCourses();

    int insertCourse(UUID courseId, Course course);

    boolean isCourseNameTaken(String name);

    boolean isTheCourseActive(UUID courseId);

    boolean selectExistsCourseName(UUID courseId, String name);

    int updateCourseName(UUID courseId, String name);

    int updateDescription(UUID courseId, String description);

    int updateDepartment(UUID courseId, String department);

    int updateTeacherName(UUID courseId, String teacherName);

    int deleteCourseById(UUID courseId);
}