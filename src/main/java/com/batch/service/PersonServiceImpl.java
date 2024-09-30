package com.batch.service;

import com.batch.entities.Person;
import com.batch.persistence.IPersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonServiceImpl implements IPersonService{

    @Autowired
    private IPersonDao personDao;

    @Transactional
    @Override
    public void saveAll(List<Person> personList) {
        personDao.saveAll(personList);
    }



}
