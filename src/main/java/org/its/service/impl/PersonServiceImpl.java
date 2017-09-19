package org.its.service.impl;

import java.util.List;

import org.its.model.Person;
import org.its.repository.PersonRepository;
import org.its.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonRepository personRepository;
	
	@Override
	public List<Person> getAll() {
		return personRepository.findAll();
	}

	@Override
	public Person getById(Integer personId) {
		return personRepository.findById(personId).orElseThrow(IllegalStateException::new);
	}

	@Override
	public void delete(Integer personId) {
		personRepository.deleteById(personId);
	}

	@Override
	public Person create(Person person) {
		return personRepository.save(person);
		
	}

}
