package com.batch.steps;

import com.batch.entities.Person;
import com.batch.service.IPersonService;
import com.batch.service.PersonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ItemWriterStep implements Tasklet {

    @Autowired
    private IPersonService personService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("--------> Inicio del paso de escritura en la BD <--------");

        List<Person> personList = (List<Person>) chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("personNewList");

        if(personList == null) {
            log.info("No hay personas para guardar");
            return RepeatStatus.FINISHED;
        }

        personList.forEach(person -> {
            if(person != null){
                log.info(person.toString());
            }
        });


        personService.saveAll(personList);


        log.info("--------> Fin del paso de escritura en la BD <--------");

        return RepeatStatus.FINISHED;
    }
}
