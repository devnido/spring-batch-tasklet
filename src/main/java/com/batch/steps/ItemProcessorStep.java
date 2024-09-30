package com.batch.steps;

import com.batch.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ItemProcessorStep implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("--------> Inicio del paso de procesamiento de los datos <--------");

        List<Person> personList = (List<Person>) chunkContext
                                                    .getStepContext()
                                                    .getStepExecution()
                                                    .getJobExecution()
                                                    .getExecutionContext()
                                                    .get("personList");

        assert personList != null;
        List<Person> personFinalList = personList.stream().peek(person -> {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            person.setInsertionDate(format.format(LocalDateTime.now()));
        }).toList();

        chunkContext.getStepContext()
                    .getStepExecution()
                    .getExecutionContext()
                    .put("personNewList", personFinalList);


        log.info("--------> Fin del paso de procesamiento de los datos <--------");
        return RepeatStatus.FINISHED;
    }
}
