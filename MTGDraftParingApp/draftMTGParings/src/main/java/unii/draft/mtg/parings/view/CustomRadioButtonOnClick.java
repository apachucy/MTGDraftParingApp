package unii.draft.mtg.parings.view;

import unii.draft.mtg.parings.pojo.Game;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class CustomRadioButtonOnClick implements OnClickListener {
	private Game mGame;

	public CustomRadioButtonOnClick(Game game) {
		mGame = game;
	}

	@Override
	public void onClick(View v) {
		CheckBox r = ((CheckBox) v);
		mGame.setDraw(r.isChecked());
	}

}
