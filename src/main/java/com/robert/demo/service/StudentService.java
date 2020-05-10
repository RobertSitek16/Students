package com.robert.demo.service;

import com.robert.demo.EmailValidator;
import com.robert.demo.exception.ApiRequestException;
import com.robert.demo.model.student.Student;
import com.robert.demo.model.student.StudentCourse;
import com.robert.demo.dao.StudentDataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentDataAccessService studentDataAccessService;

    private final EmailValidator emailValidator;

    @Autowired
    public StudentService(StudentDataAccessService studentDataAccessService, EmailValidator emailValidator) {
        this.studentDataAccessService = studentDataAccessService;
        this.emailValidator = emailValidator;
    }

    public List<Student> getAllStudents() {
        return studentDataAccessService.selectAllStudents();
    }

    public Student getStudentById(UUID studentId) {
        if (!studentDataAccessService.isTheStudentPresent(studentId)){
            throw new ApiRequestException("Student with studentId = " + studentId + " doesn't exists.");
        }
        return studentDataAccessService.selectStudentById(studentId);
    }

    public void addNewStudent(Student student) {
        addNewStudent(null, student);
    }

    void addNewStudent(UUID studentId, Student student) {
        UUID newStudentId = Optional.ofNullable(studentId)
                .orElse(UUID.randomUUID());

        if (!emailValidator.test(student.getEmail())){
            throw new ApiRequestException("Email '"+ student.getEmail() + "' is not valid");
        }

        if (studentDataAccessService.isEmailTaken(student.getEmail())){
            throw new ApiRequestException(student.getEmail() + " is taken");
        }

        studentDataAccessService.insertStudent(newStudentId, student);
    }

    public List<StudentCourse> getAllCoursesForStudent(UUID studentId) {
        return studentDataAccessService.selectAllStudentCourses(studentId);
    }

    public void updateStudent(UUID studentId, Student student) {
        Optional.ofNullable(student.getEmail())
                .ifPresent(email -> {
                    boolean taken = studentDataAccessService.selectExistsEmail(studentId, email);
                    if (!taken){
                        if (!emailValidator.test(student.getEmail())){
                            throw new ApiRequestException("Email '"+ student.getEmail() + "' is not valid");
                        }
                        studentDataAccessService.updateEmail(studentId, email);
                    } else {
                        throw new ApiRequestException("Email is already taken: " + student.getEmail());
                    }
                });

        Optional.ofNullable(student.getFirstName())
                .filter(firstName -> !StringUtils.isEmpty(firstName))
                .map(StringUtils::capitalize)
                .ifPresent(firstName -> studentDataAccessService.updateFirstName(studentId, firstName));

        Optional.ofNullable(student.getLastName())
                .filter(lastName -> !StringUtils.isEmpty(lastName))
                .map(StringUtils::capitalize)
                .ifPresent(lastName -> studentDataAccessService.updateLastName(studentId, lastName));
    }

    public void deleteStudentById(UUID studentId) {
        if (studentDataAccessService.doesTheStudentHaveACourse(studentId)){
            throw new ApiRequestException("Cannot delete a student with an active course");
        }
        studentDataAccessService.deleteStudentById(studentId);
    }

}
