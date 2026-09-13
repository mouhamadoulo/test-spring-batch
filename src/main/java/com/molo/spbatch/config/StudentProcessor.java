package com.molo.spbatch.config;

import com.molo.spbatch.student.Student;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student student) throws Exception {

        return student;
    }
}
