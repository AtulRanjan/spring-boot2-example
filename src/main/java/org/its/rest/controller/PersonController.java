package org.its.rest.controller;

import java.util.List;

import org.its.model.Person;
import org.its.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping
	public ResponseEntity<List<Person>> getAllPersons(){
		return ResponseEntity.ok(personService.getAll()); 
	}
	@GetMapping("/{personId}")
	public ResponseEntity<Person> getByPersonId(@PathVariable Integer personId){
		return ResponseEntity.ok(personService.getById(personId));
	}
	/*@PutMapping("/{personId")
	public ResponseEntity<List<Person>> updatePerson(@PathVariable Integer personId,
			@RequestBody Person person){
		return ResponseEntity.ok(personService.getAll()); 
	}*/
	@DeleteMapping("/{personId}")
	public ResponseEntity<Integer> deletePerson(@PathVariable Integer personId){
		personService.deletePerson(personId);
		return ResponseEntity.ok(personId); 
	}
	@PostMapping
	public ResponseEntity<Person> createPerson(@RequestBody Person person){
		return ResponseEntity.ok(personService.createPorson(person)); 
	}
}
