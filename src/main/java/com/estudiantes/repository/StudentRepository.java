package com.estudiantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estudiantes.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
