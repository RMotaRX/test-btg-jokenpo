package br.com.btg.jokenpo.controller;

import br.com.btg.jokenpo.dto.api.ApiResponse;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.service.impl.JokenpoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/play")
@CrossOrigin(origins = "*")
public class JokenpoController {

	private JokenpoServiceImpl jokenpoService;

	@Autowired
	public JokenpoController(JokenpoServiceImpl jokenpoService) {
		this.jokenpoService = jokenpoService;
	}

	@DeleteMapping(value = "")
	public ResponseEntity<Object> reset() throws JokenpoException {
		return ResponseEntity.ok(new ApiResponse<>(this.jokenpoService.clear()));
	}

	@GetMapping(value = "")
	public ResponseEntity<Object> play() throws JokenpoException {
		return ResponseEntity.ok(new ApiResponse<>(this.jokenpoService.play()));
	}
}
