package unii.draft.mtg.parings.view.custom;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Game;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author Arkadiusz Pachucy <br>
 *         All changes in radiogroup will be saved <br>
 *         to prevent malicious live-cycle of android list<br>
 */
public class CustomOnCheckedChangeListener implements OnCheckedChangeListener {
    private Game mGame;

    public CustomOnCheckedChangeListener(Game game) {
        mGame = game;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.row_leftPlayerRadio0:
                mGame.setPlayerAPoints(0);
                break;
            case R.id.row_leftPlayerRadio1:
                mGame.setPlayerAPoints(1);
                break;
            case R.id.row_leftPlayerRadio2:
                mGame.setPlayerAPoints(2);
                break;

            case R.id.row_rightPlayerRadio0:
                mGame.setPlayerBPoints(0);
                break;
            case R.id.row_rightPlayerRadio1:
                mGame.setPlayerBPoints(1);
                break;
            case R.id.row_rightPlayerRadio2:
                mGame.setPlayerBPoints(2);
                break;
            case R.id.row_drawRadio0:
                mGame.setDraws(0);
                break;
            case R.id.row_drawRadio1:
                mGame.setDraws(1);
                break;
            case R.id.row_drawRadio2:
                mGame.setDraws(2);
                break;
        }
    }

}
