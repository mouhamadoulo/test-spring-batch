package com.molo.spbatch.config;

import com.molo.spbatch.student.Student;
import com.molo.spbatch.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final StudentRepository studentRepository;

    @Bean
    public FlatFileItemReader<Student> itemReader() {
        return new FlatFileItemReaderBuilder<Student>()
                .name("csvReader")
                .resource(new FileSystemResource("src/main/resources/students.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("id", "firstname", "lastname", "age")
                .targetType(Student.class)
                .build();
    }

    @Bean
    public StudentProcessor processor() {
        return new StudentProcessor();
    }

    @Bean
    public RepositoryItemWriter<Student> writer() {
        return new RepositoryItemWriterBuilder<Student>()
                .repository(studentRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<Student,Student>chunk(10)
                .reader(itemReader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importStudents", jobRepository)
                .start(importStep())
                .build();
    }
}
