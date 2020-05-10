package com.robert.demo.model.course;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Course {

    private final UUID courseId;

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotBlank
    private final String department;

    private final String teacherName;

    public Course(
            @JsonProperty("courseId") UUID courseId,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("department") String department,
            @JsonProperty("teacherName") String teacherName) {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.department = department;
        this.teacherName = teacherName;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDepartment() {
        return department;
    }

    public String getTeacherName() {
        return teacherName;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}
