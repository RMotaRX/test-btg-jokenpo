package br.com.btg.jokenpo.service;

import br.com.btg.jokenpo.dto.*;
import br.com.btg.jokenpo.enums.EnumMovement;
import br.com.btg.jokenpo.exception.JokenpoException;
import br.com.btg.jokenpo.service.impl.JokenpoServiceImpl;
import br.com.btg.jokenpo.service.impl.MoveServiceImpl;
import br.com.btg.jokenpo.service.impl.PlayerServiceImpl;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("unused")
public class JokenpoServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private PlayerServiceImpl playerService;

	@Autowired
	private MoveServiceImpl moveService;

	@Autowired
	private JokenpoServiceImpl jokenpoService;

	@Before
	public void setup() {
		this.clearAllData();
	}

	@Test
	public void clearAllDataWithSucess() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2", "P3", "P4", "P5", "P6"));

		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.SPOCK.getName()),
				new MoveRequest("P2", EnumMovement.PAPER.getName()),
				new MoveRequest("P3", EnumMovement.SCISSORS.getName())));

		Assert.assertNotEquals(0, this.playerService.getAll().size());
		Assert.assertNotEquals(0, this.moveService.getAll().size());
		this.jokenpoService.clear();
		Assert.assertEquals(0, this.playerService.getAll().size());
		Assert.assertEquals(0, this.moveService.getAll().size());
	}

	@Test
	public void paperVersusScissorsPlaying() throws JokenpoException {
		this.insertSomePlayers(Arrays.asList("P1", "P2"));

		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.PAPER.getName()),
				new MoveRequest("P2", EnumMovement.SCISSORS.getName())));

		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertNotNull(response.getGameResult());
		String expected = "P2 É O VENCEDOR!".toUpperCase().trim();
		Assert.assertEquals(expected, response.getGameResult());
	}

	@Test
	public void paperVersusScissorsVersusStonePlaying() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2", "P3"));

		
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.PAPER.getName()),
				new MoveRequest("P2", EnumMovement.SCISSORS.getName()),
				new MoveRequest("P3", EnumMovement.STONE.getName())));
		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertNotNull(response.getGameResult());
		String expected = "NINGUEM GANHOU!".toUpperCase().trim();
		Assert.assertEquals(expected, response.getGameResult());
	}

	@Test
	public void lizardVersusScissorsVersusPaperPlaying() throws JokenpoException {
		insertSomePlayers(Arrays.asList("P1", "P2", "P3"));
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.LIZARD.getName()),
				new MoveRequest("P2", EnumMovement.SCISSORS.getName()),
				new MoveRequest("P3", EnumMovement.PAPER.getName())));
		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertNotNull(response.getGameResult());
		
		String expected = "P2 É O VENCEDOR!".toUpperCase().trim();
		String notExpected = "NINGUEM GANHOU!".toUpperCase().trim();
		
		Assert.assertNotEquals(notExpected, response.getGameResult());
		Assert.assertEquals(expected, response.getGameResult());
	}

	@Test
	public void spockVersusPaperPlaying() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2"));
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.SPOCK.getName()),
				new MoveRequest("P2", EnumMovement.PAPER.getName())));
		
		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertNotNull(response.getGameResult());
		
		String expected = "P2 É O VENCEDOR!".toUpperCase().trim();
		String notExpected = "NINGUÉM GANHOU!".toUpperCase().trim();
		
		Assert.assertNotEquals(notExpected, response.getGameResult());
		Assert.assertEquals(expected, response.getGameResult());
	}

	@Test
	public void lizardVersusScissorsPlaying() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2"));

		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
				new MoveRequest("P2", EnumMovement.LIZARD.getName())));

		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertNotNull(response.getGameResult());
		String expected = "P1 É O VENCEDOR!".toUpperCase().trim();
		String notExpected = "NINGUÉM GANHOU!".toUpperCase().trim();
		Assert.assertNotEquals(notExpected, response.getGameResult());
		Assert.assertEquals(expected, response.getGameResult());
	}

	@Test
	public void invalidMovementPlayingWithExpectException() throws JokenpoException {
		insertSomePlayers(Arrays.asList("P1", "P2"));

		thrown.expect(JokenpoException.class);
		thrown.expectMessage("ERR-2001 - Movimento não Encontrado");

		// Movements insert
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
				new MoveRequest("P2", "OTHER_MOVEMENT")));
	}

	@Test
	public void someMovementsPossibilitiesWithSucess() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2", "P3"));

		movementGroupWithDifferentMovements1();
		this.jokenpoService.play();

		movementGroupWithDifferentMovements2();
		this.jokenpoService.play();

		movementGroupWithTwoEqualsMovements();
		this.jokenpoService.play();

		movementGroupWithAllEqualsMovements();
		this.jokenpoService.play();
	}

	@Test
	public void playingRemovingAndIncludingSomeMovements() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2"));

		this.moveService.insert(new MoveRequest("P2", EnumMovement.PAPER.getName()));
		this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));

		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertEquals("P1 É O VENCEDOR!", response.getGameResult());

		this.moveService.insert(new MoveRequest("P2", EnumMovement.PAPER.getName()));
		this.moveService.insert(new MoveRequest("P1", EnumMovement.STONE.getName()));

		this.moveService.deleteByPlayerName("P2");

		Assert.assertEquals(1, this.moveService.getAll().size());

		this.moveService.insert(new MoveRequest("P2", EnumMovement.SPOCK.getName()));

		response = this.jokenpoService.play();

		Assert.assertEquals("P2 É O VENCEDOR!", response.getGameResult());
	}

	@Test
	public void playingRemovingAndIncludingSomePlayers1() throws JokenpoException {
		insertSomePlayers(Arrays.asList("P1", "P2"));

		this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));
		this.moveService.insert(new MoveRequest("P2", EnumMovement.STONE.getName()));

		this.playerService.deleteByName("P1");

		thrown.expect(JokenpoException.class);

		JokenpoResponse response = this.jokenpoService.play();
	}

	@Test
	public void playingRemovingAndIncludingSomePlayers2() throws JokenpoException {
		insertSomePlayers(Arrays.asList("P1", "P2"));

		this.moveService.insert(new MoveRequest("P1", EnumMovement.SCISSORS.getName()));
		this.moveService.insert(new MoveRequest("P2", EnumMovement.STONE.getName()));
		this.playerService.deleteByName("P1");
		this.playerService.insert(new PlayerRequest("P7"));
		this.moveService.insert(new MoveRequest("P7", EnumMovement.PAPER.getName()));

		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertNotEquals(0, response.getHistory());
		Assert.assertEquals("P7 É O VENCEDOR!", response.getGameResult());
	}

	@Test
	public void historyAfterPlayedWithSucess() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2", "P3"));

		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
				new MoveRequest("P2", EnumMovement.LIZARD.getName()),
				new MoveRequest("P3", EnumMovement.STONE.getName())));

		JokenpoResponse response = this.jokenpoService.play();

		Assert.assertNotEquals(0, response.getHistory().size());
		Assert.assertEquals(3, response.getHistory().size());
	}

	@Test
	public void historyBeforePlayWithExpectException() throws JokenpoException {

		insertSomePlayers(Arrays.asList("P1", "P2", "P3", "P4", "P5"));

		thrown.expect(JokenpoException.class);
		JokenpoResponse response = this.jokenpoService.play();
	}

	private List<PlayerResponse> insertSomePlayers(List<String> playerNameList) {
		List<PlayerResponse> list = new ArrayList<>();
		playerNameList.stream().forEach(playerName -> {
			try {
				list.add(this.playerService.insert(new PlayerRequest(playerName)));
			} catch (JokenpoException e) {
				e.printStackTrace();
			}
		});
		return list;
	}

	private List<MoveResponse> insertSomeMovements(List<MoveRequest> movementList) throws JokenpoException {
		List<MoveResponse> list = new ArrayList<>();
		for (MoveRequest movement : movementList)
			list.add(this.moveService.insert(movement));
		return list;
	}

	private void clearAllData() {
		this.playerService.clearAll();
		this.moveService.clearAll();
	}

	private void movementGroupWithDifferentMovements1() throws JokenpoException {
		this.moveService.clearAll();
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.SCISSORS.getName()),
				new MoveRequest("P2", EnumMovement.LIZARD.getName()),
				new MoveRequest("P3", EnumMovement.STONE.getName())));
	}

	private void movementGroupWithDifferentMovements2() throws JokenpoException {
		this.moveService.clearAll();
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.STONE.getName()),
				new MoveRequest("P2", EnumMovement.PAPER.getName()),
				new MoveRequest("P3", EnumMovement.SPOCK.getName())));
	}

	private void movementGroupWithAllEqualsMovements() throws JokenpoException {
		this.moveService.clearAll();
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.LIZARD.getName()),
				new MoveRequest("P2", EnumMovement.LIZARD.getName()),
				new MoveRequest("P3", EnumMovement.LIZARD.getName())));
	}

	private void movementGroupWithTwoEqualsMovements() throws JokenpoException {
		this.moveService.clearAll();
		insertSomeMovements(Arrays.asList(new MoveRequest("P1", EnumMovement.PAPER.getName()),
				new MoveRequest("P2", EnumMovement.LIZARD.getName()),
				new MoveRequest("P3", EnumMovement.PAPER.getName())));
	}
}
