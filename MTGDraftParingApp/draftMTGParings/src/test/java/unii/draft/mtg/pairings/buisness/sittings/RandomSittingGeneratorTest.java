package unii.draft.mtg.pairings.buisness.sittings;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.buisness.sittings.RandomSittingGenerator;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RandomSittingGeneratorTest {

    private RandomSittingGenerator objectUnderTest;

    @Test
    public void verifyThatWhenPuttingNullWillReturnEmptyListFromGenerator() {
        //given
        objectUnderTest = new RandomSittingGenerator();
        //when
        List<String> emptyList = objectUnderTest.generateSittings(null);
        //then
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }

    @Test
    public void verifyThatWhenPuttingEmptyListWillReturnEmptyListFromGenerator() {
        //given
        objectUnderTest = new RandomSittingGenerator();
        List<String> playerNameList = new ArrayList<>();
        //when
        List<String> emptyList = objectUnderTest.generateSittings(playerNameList);
        //then
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

    }


    @Test
    public void verifyThatWhenPuttinListWillReturnListFromGenerator() {
        //given
        objectUnderTest = new RandomSittingGenerator();
        List<String> playerNameList = new ArrayList<>();
        String player1 = "Tomek";
        String player2 = "Michal";
        String player3 = "Marek";
        playerNameList.add(player1);
        playerNameList.add(player2);
        playerNameList.add(player3);
        //when
        List<String> playerRandomList = objectUnderTest.generateSittings(playerNameList);
        //then
        assertNotNull(playerRandomList);
        assertTrue(playerRandomList.contains(player1));
        assertTrue(playerRandomList.contains(player2));
        assertTrue(playerRandomList.contains(player3));
    }
}
