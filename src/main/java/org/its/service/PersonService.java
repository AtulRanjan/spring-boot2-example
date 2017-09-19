package org.its.service;

import java.util.List;

import org.its.model.Person;

public interface PersonService {

	public List<Person> getAll();
	
	public Person getById(Integer personId);
	
	public void delete(Integer personId);
	
	public Person create(Person person);
}
