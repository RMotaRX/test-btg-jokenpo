package br.com.btg.jokenpo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import br.com.btg.jokenpo.dto.MoveRequest;
import br.com.btg.jokenpo.dto.MoveResponse;
import br.com.btg.jokenpo.dto.PlayerRequest;
import br.com.btg.jokenpo.dto.api.ApiResponse;
import br.com.btg.jokenpo.enums.EnumMovement;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.repository.MoveRepository;
import br.com.btg.jokenpo.repository.PlayerRepository;
import br.com.btg.jokenpo.service.impl.MoveServiceImpl;
import br.com.btg.jokenpo.service.impl.PlayerServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MoveControllerTest {

	private static String HOST = "http://localhost";
	private static String ENDPOINT = "/v1/btg/jokenpo/move";

	@LocalServerPort
	private int randomServerPort;

	private RestTemplate restTemplate;
	private PlayerRepository playerRepository;
	private MoveRepository moveRepository;
	private MoveServiceImpl moveService;
	private PlayerServiceImpl playerService;

	@Before
	public void setup() {
		restTemplate = new RestTemplate();
		playerRepository = new PlayerRepository();
		moveRepository = new MoveRepository();
		moveService = new MoveServiceImpl(moveRepository, playerRepository);
		playerService = new PlayerServiceImpl(playerRepository, moveService);
	}

	@Test
	public void getAllWithoutAnyMovementInsertedAPI() throws URISyntaxException {
		this.playerService.clearAll();
		this.moveService.clearAll();

		ResponseEntity<String> result = restTemplate.getForEntity(getMovementsUri(), String.class);

		Assert.assertEquals(200, result.getStatusCodeValue());
		Assert.assertEquals(true, result.getBody().contains("data"));
		Assert.assertEquals(false, result.getBody().contains("player"));
		Assert.assertEquals(false, result.getBody().contains("movement"));

		ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);
		List<MoveResponse> listResponse = new ModelMapper().map(apiResponse.getData(), List.class);

		Assert.assertEquals(0, listResponse.size());
	}

	@Test
	public void insertMovementAPI() throws URISyntaxException, JokenpoException {

		this.playerService.clearAll();
		this.playerService.insert(new PlayerRequest("P2"));

		MoveRequest moveRequest = new MoveRequest("P2", EnumMovement.PAPER.getName());
		HttpEntity<MoveRequest> requestForInsert = new HttpEntity<>(moveRequest, new HttpHeaders());
		ResponseEntity<String> result = restTemplate.postForEntity(getMovementsUri(), requestForInsert, String.class);

		Assert.assertEquals(200, result.getStatusCodeValue());

		Assert.assertNotEquals(0, this.getAllMovements().size());
	}

	@Test
	public void deleteMovementByNameAPI() throws URISyntaxException, JokenpoException {
		this.playerService.clearAll();
		this.moveService.clearAll();
		this.playerService.insert(new PlayerRequest("P3"));
		this.moveService.insert(new MoveRequest("P3", EnumMovement.SPOCK.getName()));

		restTemplate.delete(getMovementsUri() + "/?playerName=P3");

		Assert.assertEquals(0, this.getAllMovements().size());
	}

	private List<MoveResponse> getAllMovements() throws URISyntaxException {
		ResponseEntity<String> result = restTemplate.getForEntity(getMovementsUri(), String.class);
		ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);

		return new ModelMapper().map(apiResponse.getData(), List.class);
	}

	private URI getMovementsUri() throws URISyntaxException {
		final String baseUrl = HOST + ":" + randomServerPort + ENDPOINT;
		return new URI(baseUrl);
	}
}