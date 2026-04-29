package com.springCRUD.springCRUD.service;

import com.springCRUD.springCRUD.model.Student;
import com.springCRUD.springCRUD.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    // Create a new student
    public Student createStudent(Student student) {
        studentRepository.createStudent(student);
        return student;
    }
    
    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }
    
    // Get student by ID
    public Student getStudentById(Integer id) {
        return studentRepository.getStudentById(id);
    }
    
    // Update a student
    public int updateStudent(Student student) {
        return studentRepository.updateStudent(student);
    }
    
    // Delete a student
    public int deleteStudent(Integer id) {
        return studentRepository.deleteStudent(id);
    }
}
