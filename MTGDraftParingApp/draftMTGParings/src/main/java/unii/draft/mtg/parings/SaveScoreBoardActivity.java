package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.config.BundleConst;

/**
 * Created by Unii on 2015-12-03.
 */
public class SaveScoreBoardActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    @Bind(R.id.save_score_boardNameEditText)
    EditText mSavedGameNameExitText;


    @OnClick(R.id.save_score_board_acceptButton)
    void onSavedGameNameClicked(View view) {
        String gameName = mSavedGameNameExitText.getText().toString();
        if (gameName != null && !gameName.trim().equals("")) {
             Intent intent = new Intent();
             intent.putExtra(BundleConst.BUNDLE_KEY_SAVED_GAME_NAME, gameName);
             setResult(RESULT_OK, intent);

            finish();
            //TODO: Add toast message
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score_board);
        ButterKnife.bind(this);


        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);


    }
}
