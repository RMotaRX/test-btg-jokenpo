package br.com.btg.jokenpo.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import br.com.btg.jokenpo.dto.MoveRequest;
import br.com.btg.jokenpo.dto.PlayerRequest;
import br.com.btg.jokenpo.enums.EnumMovement;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.repository.MoveRepository;
import br.com.btg.jokenpo.repository.PlayerRepository;
import br.com.btg.jokenpo.service.impl.MoveServiceImpl;
import br.com.btg.jokenpo.service.impl.PlayerServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class JokenpoControllerTest {

    private static String HOST = "http://localhost";
    private static String ENDPOINT = "/v1/btg/jokenpo/play";

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
    public void playWithNobodyWonAPI() throws URISyntaxException, JokenpoException {

    	this.playerService.clearAll();
        this.moveService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));
        this.playerService.insert(new PlayerRequest("P2"));
        this.playerService.insert(new PlayerRequest("P3"));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SPOCK.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.SCISSORS.getName()));
        this.moveService.insert(new MoveRequest("P3", EnumMovement.PAPER.getName()));
        
        ResponseEntity<String> result = restTemplate.getForEntity(getJokenpoUri(), String.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        Assert.assertEquals(true, result.getBody().contains("history"));
        Assert.assertEquals(true, result.getBody().contains("result"));
        Assert.assertEquals(true, result.getBody().contains("NINGUEM GANHOU"));
    }

    @Test
    public void playWithWinnerAPI() throws URISyntaxException, JokenpoException {

    	this.playerService.clearAll();
        this.moveService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));
        this.playerService.insert(new PlayerRequest("P2"));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));
        this.moveService.insert(new MoveRequest("P2", EnumMovement.PAPER.getName()));

        ResponseEntity<String> result = restTemplate.getForEntity(getJokenpoUri(), String.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(true, result.getBody().contains("data"));
        Assert.assertEquals(true, result.getBody().contains("history"));
        Assert.assertEquals(true, result.getBody().contains("result"));
        Assert.assertEquals(true, result.getBody().contains("P1 GANHOU"));
    }

    @Test
    public void clearAllAPI() throws URISyntaxException, JokenpoException {

    	this.playerService.clearAll();
        this.moveService.clearAll();
        this.playerService.insert(new PlayerRequest("P1"));
        this.moveService.insert(new MoveRequest("P1", EnumMovement.PAPER.getName()));

        Assert.assertEquals(1, this.playerService.getAll().size());
        Assert.assertEquals(1, this.moveService.getAll().size());

        restTemplate.delete(getJokenpoUri() + "/?playerName=P1");

        Assert.assertEquals(0, this.playerService.getAll().size());
        Assert.assertEquals(0, this.moveService.getAll().size());
    }

    private URI getJokenpoUri() throws URISyntaxException {
        final String baseUrl = HOST + ":" + randomServerPort + ENDPOINT;
        return new URI(baseUrl);
    }
}