package com.estudiantes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.estudiantes.dto.StudentDto;
import com.estudiantes.dto.StudentRequest;
import com.estudiantes.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void listsAllStudentsUsingCaseStudyFieldNames() throws Exception {
        when(studentService.findAll()).thenReturn(List.of(
                new StudentDto(1, "Ana López", "ana.lopez@example.com", LocalDate.of(2026, 9, 25)),
                new StudentDto(2, "Luis Pérez", "luis.perez@example.com", LocalDate.of(2026, 9, 26))));

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].full_name").value("Ana López"))
                .andExpect(jsonPath("$[0].email").value("ana.lopez@example.com"))
                .andExpect(jsonPath("$[0].enrollment_date").value("2026-09-25"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].fullName").doesNotExist())
                .andExpect(jsonPath("$[0].enrollmentDate").doesNotExist());

        verify(studentService).findAll();
    }

    @Test
    void registersStudentAndReturnsGeneratedId() throws Exception {
        when(studentService.save(any(StudentRequest.class))).thenReturn(
                new StudentDto(7, "Ana López", "ana.lopez@example.com", LocalDate.of(2026, 9, 25)));

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "full_name": "Ana López",
                                  "email": "ana.lopez@example.com",
                                  "enrollment_date": "2026-09-25"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.full_name").value("Ana López"))
                .andExpect(jsonPath("$.email").value("ana.lopez@example.com"))
                .andExpect(jsonPath("$.enrollment_date").value("2026-09-25"));

        verify(studentService).save(new StudentRequest(
                "Ana López", "ana.lopez@example.com", LocalDate.of(2026, 9, 25)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{}",
            "{\"full_name\":\"Ana López\",\"enrollment_date\":\"2026-09-25\"}",
            "{\"full_name\":\"Ana López\",\"email\":\"correo-invalido\",\"enrollment_date\":\"2026-09-25\"}",
            "{\"full_name\":\"Ana López\",\"email\":\"ana@example.com\",\"enrollment_date\":\"fecha-invalida\"}"
    })
    void rejectsInvalidInputBeforeCallingService(String body) throws Exception {
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }
}
