package org.its.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/greeting")
	public ResponseEntity<String> sayHello(){
		return ResponseEntity.ok("Hello world!");
	}

}
