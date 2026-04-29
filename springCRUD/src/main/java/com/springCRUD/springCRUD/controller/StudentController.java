package com.springCRUD.springCRUD.controller;

import com.springCRUD.springCRUD.model.Student;
import com.springCRUD.springCRUD.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // POST: Create a new student
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating student: " + e.getMessage());
        }
    }
    
    // GET: Retrieve all students
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.status(HttpStatus.OK).body(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving students: " + e.getMessage());
        }
    }
    
    // GET: Retrieve a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
        try {
            Student student = studentService.getStudentById(id);
            if (student != null) {
                return ResponseEntity.status(HttpStatus.OK).body(student);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving student: " + e.getMessage());
        }
    }
    
    // PUT: Update a student
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        try {
            student.setId(id);
            int result = studentService.updateStudent(student);
            if (result > 0) {
                return ResponseEntity.status(HttpStatus.OK).body("Student updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating student: " + e.getMessage());
        }
    }
    
    // DELETE: Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        try {
            int result = studentService.deleteStudent(id);
            if (result > 0) {
                return ResponseEntity.status(HttpStatus.OK).body("Student deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting student: " + e.getMessage());
        }
    }
}
