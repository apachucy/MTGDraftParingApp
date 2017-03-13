package unii.draft.mtg.pairings.buisness.share.scoreboard;


import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.buisness.share.scoreboard.ShareDataContent;
import unii.draft.mtg.parings.logic.pojo.Player;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ShareDataContentTest {

    @Mock
    private Context context;

    private ShareDataContent objectUnderTest;

    @Test
    public void verifyThatWhenPuttingNullWillReturnNullFromGetPlayerWithPoints() {
        //given
        objectUnderTest = new ShareDataContent(context);
        //when
        String formattedText = objectUnderTest.getPlayerWithPoints(null);
        //then
        assertNull(formattedText);

    }


    @Test
    public void verifyThatWhenPuttingEmptyListWillReturnNullFromGetPlayerWithPoints() {
        //given
        objectUnderTest = new ShareDataContent(context);
        List<Player> emptyPlayerList = new ArrayList<>();
        //when
        String formattedText = objectUnderTest.getPlayerWithPoints(emptyPlayerList);
        //then
        assertNull(formattedText);
    }


    @Test
    public void verifyThatListWillBeProperlyFormattedFromGetPlayerWithPoints() {
        //given
        objectUnderTest = new ShareDataContent(context);
        List<Player> playerList = new ArrayList<>();
        Player player1 = new Player("Tomek");
        player1.setGamePoints(3);
        Player player2 = new Player("Michal");
        player2.setGamePoints(0);

        playerList.add(player1);
        playerList.add(player2);
        //when
        String formattedText = objectUnderTest.getPlayerWithPoints(playerList);
        //then
        assertNotNull(formattedText);
        verify(context).getString(anyInt(), anyString());
        verify(context, times(2)).getString(anyInt(), anyString(), anyInt());
        //assertEquals("Draft winner is: Tomek\nTomek scored: 3 points\nMichal scored: 0 points\n", formattedText);
    }
}
