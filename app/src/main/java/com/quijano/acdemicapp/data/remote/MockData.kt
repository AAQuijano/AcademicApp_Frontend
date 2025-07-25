package com.quijano.acdemicapp.data.remote

import com.quijano.acdemicapp.model.User
import com.quijano.acdemicapp.network.Grade
import com.quijano.acdemicapp.network.Score

object MockData {
    val testStudents = listOf(
        User(username = "ana_student", password = "password123", role = "student"),
        User(username = "carlos_prof", password = "password123", role = "professor")
    )

    val testGrades = listOf(
        Grade(id = "1", subject = "Matemáticas", type = "Examen", value = 8.5, comment = "Buen trabajo", studentId = "stu001", professorId = "prof001"),
        Grade(id = "2", subject = "Historia", type = "Proyecto", value = 9.0, comment = null, studentId = "stu002", professorId = "prof001")
    )

    val testScores = listOf(
        Score(id = "1", subject = "Matemáticas", description = "Examen parcial", professorId = "prof001"),
        Score(id = "2", subject = "Historia", description = "Proyecto final", professorId = "prof001")
    )
}