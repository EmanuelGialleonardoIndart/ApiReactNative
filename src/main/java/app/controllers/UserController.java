package app.controllers;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.model.entity.User;
import app.model.repositories.UserRepository;
import app.supportClases.Credentials;
import app.supportClases.ErrorMessage;
import app.supportClases.TokenServices;
import app.supportClases.UsernameAndPassword;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {
     
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private TokenServices tokenServices;
	
	private final int EXPIRATION_IN_SEC=86400;
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(){
		return ResponseEntity.ok(this.repository.findAll());
	}
	
	private boolean validLogin(String username,String password) {
		User u=this.repository.findByUsername(username);
	    return u!=null&&(BCrypt.checkpw(password, u.getPassword()));
	}
	
	@PostMapping("/auth")
	public ResponseEntity<?> authentication(@RequestBody UsernameAndPassword uap){
        if(uap.getPassword()==null||uap.getUsername()==null) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("all fields must be filled"));
        }
		if(validLogin(uap.getUsername(),uap.getPassword())) {
			String token=this.tokenServices.generateToken(uap.getUsername(), EXPIRATION_IN_SEC);
		    return ResponseEntity.ok(new Credentials(token,EXPIRATION_IN_SEC,uap.getUsername()));
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage("Incorrect password or username"));
		}
	}
	
}
