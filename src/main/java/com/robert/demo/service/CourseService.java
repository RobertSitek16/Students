package com.robert.demo.service;

import com.robert.demo.exception.ApiRequestException;
import com.robert.demo.model.course.Course;
import com.robert.demo.dao.CourseDataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseDataAccessService courseDataAccessService;

    @Autowired
    public CourseService(CourseDataAccessService courseDataAccessService) {
        this.courseDataAccessService = courseDataAccessService;
    }

    public List<Course> getAllCourses() {
        return courseDataAccessService.selectAllCourses();
    }

    public void addNewCourse(Course course) {
        addNewCourse(null, course);
    }

    void addNewCourse(UUID courseId, Course course){
        UUID newCourseId = Optional.ofNullable(courseId)
                .orElse(UUID.randomUUID());

        if (courseDataAccessService.isCourseNameTaken(course.getName())){
            throw new ApiRequestException(course.getName() + " is taken");
        }

        courseDataAccessService.insertCourse(newCourseId, course);
    }

    public void updateCourse(UUID courseId, Course course) {
        Optional.ofNullable(course.getName())
                .ifPresent(name -> {
                    boolean taken = courseDataAccessService.selectExistsCourseName(courseId, name);
                    if (!taken){
                        courseDataAccessService.updateCourseName(courseId, name);
                    } else {
                        throw new ApiRequestException("Course name is already taken: " + course.getName());
                    }
                });

        Optional.ofNullable(course.getDescription())
                .filter(description -> !StringUtils.isEmpty(description))
                .map(StringUtils::capitalize)
                .ifPresent(description -> courseDataAccessService.updateDescription(courseId, description));

        Optional.ofNullable(course.getDepartment())
                .filter(department -> !StringUtils.isEmpty(department))
                .map(StringUtils::capitalize)
                .ifPresent(department -> courseDataAccessService.updateDepartment(courseId, department));

        Optional.ofNullable(course.getTeacherName())
                .filter(teacherName -> !StringUtils.isEmpty(teacherName))
                .map(StringUtils::capitalize)
                .ifPresent(teacherName -> courseDataAccessService.updateTeacherName(courseId, teacherName));
    }

    public void deleteCourseById(UUID courseId) {
        if (courseDataAccessService.isTheCourseActive(courseId)){
            throw new ApiRequestException("Cannot delete an active course");
        }
        courseDataAccessService.deleteCourseById(courseId);
    }

}
