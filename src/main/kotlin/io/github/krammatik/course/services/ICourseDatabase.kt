package io.github.krammatik.course.services

import io.github.krammatik.course.dto.CourseCreationRequest
import io.github.krammatik.models.Course

interface ICourseDatabase {

    fun getCourses(): List<Course>

    fun getCourseById(id: String): Course?

    fun updateCourse(course: Course): Course

    fun createCourse(request: CourseCreationRequest): Course

}