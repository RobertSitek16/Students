package com.robert.demo.controller;

import com.robert.demo.model.course.Course;
import com.robert.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    @PostMapping
    public void addNewCourse(@RequestBody @Valid Course course){
        courseService.addNewCourse(course);
    }

    @PutMapping(path = "{courseId}")
    public void updateCourse(@PathVariable("courseId") UUID courseId, @RequestBody Course course){
        courseService.updateCourse(courseId, course);
    }

    @DeleteMapping(path = "{courseId}")
    public void deleteCourseById(@PathVariable("courseId") UUID courseId){
        courseService.deleteCourseById(courseId);
    }
}
