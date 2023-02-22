package sigma.app.api.controller.user;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import sigma.app.api.model.user.User;
import sigma.app.api.object.user.UserDTO;
import sigma.app.api.repository.user.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;

	@PostMapping
	@Transactional
	public  ResponseEntity<UserDTO> CreateUser(@RequestBody @Valid UserDTO userObject, UriComponentsBuilder uriBuilder) {
		User user = new User(userObject);
		userRepository.save(user);
		UserDTO userDTO = new UserDTO(user);
		URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
	@PutMapping
	@Transactional
	public ResponseEntity<UserDTO> UpdateUser(@RequestBody @Valid UserDTO userObject) {
		User user = userRepository.getReferenceById(userObject.id());
		user.setName(userObject.name());
		user.setPassword(userObject.password());
		user.setEmail(userObject.email());
		
		UserDTO userDTO = new UserDTO(user);
		
		return ResponseEntity.ok(userDTO);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping
	public ResponseEntity<Page<UserDTO>> ListUser(Pageable pageable) {
		Page page =  userRepository.findAll(pageable).map(UserDTO::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	public  ResponseEntity<UserDTO> GetUser(@PathVariable String id) {
		UserDTO userDTO = new UserDTO(userRepository.getReferenceById(Long.valueOf(id)));
		return ResponseEntity.ok(userDTO);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity DeleteUser(@PathVariable String id) {
		userRepository.deleteById(Long.valueOf(id));
		return ResponseEntity.noContent().build();
	}
}
