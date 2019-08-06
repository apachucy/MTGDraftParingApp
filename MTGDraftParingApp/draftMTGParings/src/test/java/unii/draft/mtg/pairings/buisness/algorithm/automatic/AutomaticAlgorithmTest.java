package unii.draft.mtg.pairings.buisness.algorithm.automatic;

import android.content.Context;
import androidx.annotation.NonNull;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import unii.draft.mtg.parings.buisness.algorithm.automatic.AutomaticParingAlgorithm;
import unii.draft.mtg.parings.logic.pojo.DraftDataProvider;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.GamePreferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticAlgorithmTest {
    @InjectMocks
    private AutomaticParingAlgorithm objectUnderTest;

    @Mock
    private GamePreferences mGamePreferences;
    @Mock
    private DraftDataProvider mDraftDataProvider;
    @Mock
    private Context mContext;

 /*  @Before
    public void setupTest() {
        MockitoAnnotations.initMocks(this);

    }*/

    @Test
    public void isAlgorithmSetupCorrectly() {
        //given
        int rounds = 2;
        List<String> playerNames = new ArrayList<>();
        playerNames.add("Tomek");
        playerNames.add("Mirek");
        playerNames.add("Arek");
        playerNames.add("Sirek");

        //when
        objectUnderTest.startAlgorithm(playerNames, rounds);
        //then
        assertEquals(objectUnderTest.getMaxRound(), 2);
        assertEquals(objectUnderTest.getCurrentRound(), 0);
        assertEquals(objectUnderTest.getDefaultRandomSeed(), playerNames.size());
        assertNull(objectUnderTest.getPlayerWithBye());

    }

    @Test
    public void verifyThatFirstRoundOddPairingWorking() {
        //given
        List<String> playerNames = new ArrayList<>(Arrays.asList("Arek", "Tomek", "Mirek", "Sirek", "Zbyszek"));
        //hacks for 'when' without mock's
        objectUnderTest.setDraftStartedPlayersList(populatePlayersList(playerNames));
        objectUnderTest.setCurrentRound(0);
        //when
        List<Game> gameList = objectUnderTest.getParings(0);
        //then
        assertNotNull(gameList);
        assertEquals(gameList.size(), 2);
        String playerName = gameList.get(0).getPlayerNameA();
        assertEquals(playerName, gameList.get(0).getPlayerNameA());
        assertNotEquals(playerName, gameList.get(0).getPlayerNameB());

        assertNotEquals(playerName, gameList.get(1).getPlayerNameA());
        assertNotEquals(playerName, gameList.get(1).getPlayerNameB());
        assertNotNull(objectUnderTest.getPlayerWithBye());
        assertNotEquals(playerName, objectUnderTest.getPlayerWithBye().getPlayerName());

    }

    @Test
    public void verifyThatFirstRoundPairwisePairingWorking() {
        //given
        List<String> playerNames = new ArrayList<>(Arrays.asList("Arek", "Tomek", "Mirek", "Sirek"));
        //hacks for 'when' without mock's
        objectUnderTest.setDraftStartedPlayersList(populatePlayersList(playerNames));
        objectUnderTest.setCurrentRound(0);
        //when
        List<Game> gameList = objectUnderTest.getParings(0);
        //then
        assertNotNull(gameList);
        assertEquals(gameList.size(), 2);
        String playerName = gameList.get(0).getPlayerNameA();
        assertEquals(playerName, gameList.get(0).getPlayerNameA());
        assertNotEquals(playerName, gameList.get(0).getPlayerNameB());

        assertNotEquals(playerName, gameList.get(1).getPlayerNameA());
        assertNotEquals(playerName, gameList.get(1).getPlayerNameB());
        assertNull(objectUnderTest.getPlayerWithBye());

    }

    @Test
    public void verifyThatPlayerShouldBeReturnedWhenTrueNameWereProvided() {
        //given
        List<String> playerNames = new ArrayList<>(Arrays.asList("Arek", "Tomek", "Mirek", "Sirek"));
        //hacks for 'when' without mock's
        objectUnderTest.setDraftStartedPlayersList(populatePlayersList(playerNames));
        objectUnderTest.setCurrentRound(0);
        //when
        Player selectedPlayer = objectUnderTest.getPlayer("Arek");
        //then
        assertNotNull(selectedPlayer);
        assertEquals(selectedPlayer.getPlayerName(), "Arek");
    }

    @Test
    public void verifyThatPlayerShouldNotBeReturnedWhenNullWereProvided() {
        //given
        List<String> playerNames = new ArrayList<>(Arrays.asList("Arek", "Tomek", "Mirek", "Sirek"));
        //hacks for 'when' without mock's
        objectUnderTest.setDraftStartedPlayersList(populatePlayersList(playerNames));
        objectUnderTest.setCurrentRound(0);
        //when
        Player selectedPlayer = objectUnderTest.getPlayer(null);
        //then
        assertNull(selectedPlayer);
    }

    @Test
    public void verifyThatPlayerShouldNotBeReturnedWhenNameNotOnListWereProvided() {
        //given
        List<String> playerNames = new ArrayList<>(Arrays.asList("Arek", "Tomek", "Mirek", "Sirek"));
        //hacks for 'when' without mock's
        objectUnderTest.setDraftStartedPlayersList(populatePlayersList(playerNames));
        objectUnderTest.setCurrentRound(0);
        //when
        Player selectedPlayer = objectUnderTest.getPlayer("Zbyszek");
        //then
        assertNull(selectedPlayer);
    }

    @Test
    public void verifyThatPlayerListShouldBeReturnedWhenPlayerNamesWereProvided() {
        //given
        List<String> playerNames = new ArrayList<>(Arrays.asList("Arek", "Tomek", "Mirek", "Sirek"));
        //hacks for 'when' without mock's
        objectUnderTest.setDraftStartedPlayersList(populatePlayersList(playerNames));
        objectUnderTest.setCurrentRound(0);
        //when
        List<Player> selectedPlayer = objectUnderTest.getSortedPlayerList();
        //then
        assertNotNull(selectedPlayer);
        assertEquals(selectedPlayer.size(), 4);
        assertThat(selectedPlayer, checkPlayerName("Arek"));
        assertThat(selectedPlayer, checkPlayerName("Tomek"));
        assertThat(selectedPlayer, checkPlayerName("Mirek"));
        assertThat(selectedPlayer, checkPlayerName("Sirek"));


    }

    @NonNull
    private List<Player> populatePlayersList(@NonNull List<String> players) {
        List<Player> draftStartedPlayers = new ArrayList<>();
        for (String playerName : players) {
            Player player = new Player(playerName);
            draftStartedPlayers.add(player);
        }
        return draftStartedPlayers;
    }


    @NonNull
    private Matcher<List<Player>> checkPlayerName(final String playerName) {
        return new BaseMatcher<List<Player>>() {
            @Override
            public boolean matches(final Object item) {
                final List<Player> playerList = (List<Player>) item;

                for (Player player : playerList) {
                    if (player.getPlayerName().equals(playerName)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(@NonNull final Description description) {
                description.appendText("name should be equals ").appendValue(playerName);
            }

            @Override
            public void describeMismatch(final Object item, final
            Description description) {
                description.appendText(" name was not found");
            }
        };
    }
}
