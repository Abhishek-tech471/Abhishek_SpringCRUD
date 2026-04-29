package com.springCRUD.springCRUD.repository;

import com.springCRUD.springCRUD.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // RowMapper to map database rows to Student objects
    private RowMapper<Student> studentRowMapper = new RowMapper<Student>() {
        @Override
        public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setEmail(rs.getString("email"));
            student.setCourse(rs.getString("course"));
            return student;
        }
    };
    
    // CREATE: Insert a new student
    public int createStudent(Student student) {
        String sql = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, student.getName(), student.getEmail(), student.getCourse());
    }
    
    // READ: Get all students
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM students";
        return jdbcTemplate.query(sql, studentRowMapper);
    }
    
    // READ: Get student by ID
    public Student getStudentById(Integer id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        List<Student> students = jdbcTemplate.query(sql, new Object[]{id}, studentRowMapper);
        return students.isEmpty() ? null : students.get(0);
    }
    
    // UPDATE: Update an existing student
    public int updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, course = ? WHERE id = ?";
        return jdbcTemplate.update(sql, student.getName(), student.getEmail(), student.getCourse(), student.getId());
    }
    
    // DELETE: Delete a student by ID
    public int deleteStudent(Integer id) {
        String sql = "DELETE FROM students WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
