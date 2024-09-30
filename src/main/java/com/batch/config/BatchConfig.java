package com.batch.config;

import com.batch.steps.ItemDecompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWriterStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import  org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfiguration {


    @Bean
    @JobScope
    public ItemDecompressStep itemDecompressStep(){
        return new ItemDecompressStep();
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep(){
        return new ItemReaderStep();
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep(){
        return new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemWriterStep itemWriterStep(){
        return new ItemWriterStep();
    }

    @Bean
    public Step decompressFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("decompressFileStep", jobRepository)
                .tasklet(itemDecompressStep(), transactionManager)
                .build();

    }

    @Bean
    public Step readFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("readFileStep", jobRepository)
                .tasklet(itemReaderStep(), transactionManager)
                .build();

    }

    @Bean
    public Step processDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("processDataStep", jobRepository)
                .tasklet(itemProcessorStep(), transactionManager)
                .build();

    }

    @Bean
    public Step writerDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("writerDataStep", jobRepository)
                .tasklet(itemWriterStep(), transactionManager)
                .build();

    }

    @Bean
    public Job readCSVJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("readCSVJob", jobRepository)
                .start(decompressFileStep(jobRepository,transactionManager))
                .next(readFileStep(jobRepository,transactionManager))
                .next(processDataStep(jobRepository,transactionManager))
                .next(writerDataStep(jobRepository,transactionManager))
                .build();
    }
}
