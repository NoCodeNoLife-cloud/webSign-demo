package code.controller;

import code.config.sign.Signature;
import code.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class SignTestController {
	/**
	 * This method handles the web request and returns a response entity with a null body and an HTTP status of OK.
	 *
	 * @return a ResponseEntity with body and an HTTP status
	 */
	@Signature
	@ResponseBody
	@PostMapping("test/{id}")
	public ResponseEntity<?> myController(@PathVariable("id") String id, @RequestParam String client, @RequestBody User user) {
		return new ResponseEntity<>(String.join(",", id, client, user.toString()), HttpStatus.OK);
	}
}
