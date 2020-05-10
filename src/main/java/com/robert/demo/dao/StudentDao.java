package com.robert.demo.dao;

import com.robert.demo.model.student.Student;
import com.robert.demo.model.student.StudentCourse;

import java.util.List;
import java.util.UUID;

public interface StudentDao {

    List<Student> selectAllStudents();

    Student selectStudentById(UUID studentId);

    List<StudentCourse> selectAllStudentCourses(UUID studentId);

    int insertStudent(UUID studentId, Student student);

    boolean isTheStudentPresent(UUID studentId);

    boolean isEmailTaken(String email);

    boolean selectExistsEmail(UUID studentId, String email);

    boolean doesTheStudentHaveACourse(UUID studentId);

    int updateEmail(UUID studentId, String email);

    int updateFirstName(UUID studentId, String firstName);

    int updateLastName(UUID studentId, String lastName);

    int deleteStudentById(UUID studentId);

}
