package br.com.btg.jokenpo.controller;

import br.com.btg.jokenpo.dto.PlayerRequest;
import br.com.btg.jokenpo.dto.PlayerResponse;
import br.com.btg.jokenpo.dto.api.ApiResponse;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.repository.MoveRepository;
import br.com.btg.jokenpo.repository.PlayerRepository;
import br.com.btg.jokenpo.service.impl.MoveServiceImpl;
import br.com.btg.jokenpo.service.impl.PlayerServiceImpl;
import com.google.gson.Gson;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PlayerControllerTest {

    private static String HOST = "http://localhost";
    private static String ENDPOINT = "/v1/btg/jokenpo/player";

    @LocalServerPort
    private int randomServerPort;

    private RestTemplate restTemplate;
    private PlayerRepository playerRepository;
    private MoveRepository moveRepository;
    private MoveServiceImpl moveService;
    private PlayerServiceImpl playerService;

    @Before
    public void setup(){
        restTemplate = new RestTemplate();
        playerRepository = new PlayerRepository();
        moveRepository = new MoveRepository();
        moveService = new MoveServiceImpl(moveRepository, playerRepository);
        playerService = new PlayerServiceImpl(playerRepository, moveService);
    }

	@Test
    public void getAllWithoutAnyPlayersInsertedAPI() throws URISyntaxException {
        this.playerService.clearAll();

        ResponseEntity<String> result = restTemplate.getForEntity(getPlayerUri(), String.class);

        Assert.assertEquals(200, result.getStatusCodeValue());

        ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);

        Assert.assertNotNull(apiResponse.getMeta().getTimestamp());

        List<PlayerResponse> listResponse = new ModelMapper().map(apiResponse.getData(), List.class);

        Assert.assertEquals(0, listResponse.size());
    }

    @Test
    public void insertPlayerAPI() throws URISyntaxException {
        this.playerService.clearAll();

        HttpEntity<PlayerRequest> requestForInsert = new HttpEntity<>(
                new PlayerRequest("P1"), new HttpHeaders());

        ResponseEntity<String> result = restTemplate.postForEntity(getPlayerUri(), requestForInsert, String.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(1, this.getAllPlayers().size());
    }

    @Test
    public void deletePlayerByNameAPI() throws URISyntaxException, JokenpoException {
    	this.playerService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));

        restTemplate.delete(getPlayerUri() + "/?playerName=P1");

        Assert.assertEquals(0, this.getAllPlayers().size());
    }

	private List<PlayerResponse> getAllPlayers() throws URISyntaxException {
        ResponseEntity<String> result = restTemplate.getForEntity(getPlayerUri(), String.class);
        
		ApiResponse apiResponse = new Gson().fromJson(result.getBody(), ApiResponse.class);

        return new ModelMapper().map(apiResponse.getData(), List.class);
    }

    private URI getPlayerUri() throws URISyntaxException {
        final String baseUrl = HOST + ":" + randomServerPort + ENDPOINT;
        return new URI(baseUrl);
    }
}