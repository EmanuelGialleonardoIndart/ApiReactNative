package app.supportClases;

import java.util.Arrays;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import app.model.entity.User;
import app.model.repositories.UserRepository;


@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserRepository repository;

	@Override
	public void run(String... args) throws Exception {
		Arrays.asList(new User("maxnu",BCrypt.hashpw("123456", BCrypt.gensalt()),"Emanuel Gialleonardo"),
				      new User("giainda",BCrypt.hashpw("654321", BCrypt.gensalt()),"Maximiliano Gialleonardo"))
		              .forEach(v->this.repository.save(v));
	}
	
	
}
