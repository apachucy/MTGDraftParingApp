package unii.draft.mtg.parings;

import java.util.List;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.algorithm.IParingAlgorithm;
import unii.draft.mtg.parings.algorithm.ManualParingAlgorithm;
import unii.draft.mtg.parings.algorithm.ParingAlgorithm;
import unii.draft.mtg.parings.config.BaseConfig;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.sharedprefrences.SettingsPreferencesFactory;
import unii.draft.mtg.parings.view.CustomDialogFragment;
import unii.draft.mtg.parings.view.PlayerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayerPositionActivity extends Activity {
    private TextView mRoundTextView;
    private TextView mWinnerTextView;
    private Button mNextGameButton;
    private ListView mPlayerListView;
    private IParingAlgorithm mAlgorithm;
    private PlayerAdapter mAdapter;
    private CustomDialogFragment mCustomDialogFragment;
    private static final String TAG_DIALOG = PlayerPositionActivity.class
            .getName() + "TAG_DIALOG";

    // help library
    private ShowcaseView mShowcaseView;
    private int mShowCaseID = 169;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_position);

        mWinnerTextView = (TextView) findViewById(R.id.player_position_winnerTextView);
        mRoundTextView = (TextView) findViewById(R.id.player_position_roundTextView);
        mNextGameButton = (Button) findViewById(R.id.player_position_nextGameButton);
        mPlayerListView = (ListView) findViewById(R.id.player_position_playerListView);

        mAlgorithm = AlgorithmFactory.getInstance();
        List<Player> playerList = mAlgorithm.getSortedPlayerList();
        mAdapter = new PlayerAdapter(this, playerList);
        View header = getLayoutInflater().inflate(R.layout.header_player_list,
                null);

        mPlayerListView.addHeaderView(header);
        mPlayerListView.setAdapter(mAdapter);

        mRoundTextView.setText(getString(R.string.text_after_game) + " "
                + mAlgorithm.getCurrentRound() + " "
                + getString(R.string.text_from) + " "
                + +mAlgorithm.getMaxRound());
        mNextGameButton.setOnClickListener(mOnButtonClick);

        // when there was last game change button name
        // show winner
        if (mAlgorithm.getCurrentRound() == mAlgorithm.getMaxRound()) {
            mNextGameButton.setText(getString(R.string.text_end_round));
            mWinnerTextView.setVisibility(View.VISIBLE);
            mWinnerTextView.setText(getString(R.string.text_winner) + " "
                    + playerList.get(0).getPlayerName());
        } else {
            mWinnerTextView.setVisibility(View.INVISIBLE);

        }
        mCustomDialogFragment = CustomDialogFragment.newInstance(
                getString(R.string.dialog_end_title),
                getString(R.string.dialog_end_message),
                getString(R.string.dialog_start_button), mOnDialogButtonClick);

        // hints for users
        ViewTarget target = new ViewTarget(R.id.header_playerPointTextView,
                this);
        mShowcaseView = new ShowcaseView.Builder(this, true).setTarget(target)
                .setContentTitle(getString(R.string.help_title))
                .setStyle(R.style.showcaseThem)
                .setOnClickListener(mOnHelpClick)
                .setContentText(getString(R.string.help_points))
                .singleShot(mShowCaseID).build();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams buttonBottomLeft = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            buttonBottomLeft.setMargins(BaseConfig.MARGIN_NOT_SET, BaseConfig.MARGIN_NOT_SET, BaseConfig.MARGIN_NOT_SET, BaseConfig.MARGIN_BOTTOM);
            buttonBottomLeft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            buttonBottomLeft.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mShowcaseView.setButtonPosition(buttonBottomLeft);
        }
    }

    private OnClickListener mOnButtonClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.player_position_nextGameButton:
                    if (mAlgorithm.getCurrentRound() >= mAlgorithm.getMaxRound()) {
                        mCustomDialogFragment
                                .show(getFragmentManager(), TAG_DIALOG);
                    } else {

                        Intent intent = null;
                        if (SettingsPreferencesFactory.getInstance().areManualParings()) {
                            intent = new Intent(PlayerPositionActivity.this,
                                    MatchPlayerActivity.class);
                        } else {

                            intent = new Intent(PlayerPositionActivity.this,
                                    ParingsActivity.class);

                        }
                        startActivity(intent);
                        finish();
                  }

                    break;
                default:
                    break;
            }
        }
    };

    private OnClickListener mOnDialogButtonClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mCustomDialogFragment != null) {
                mCustomDialogFragment.dismiss();
            }
            finish();
        }
    };

    private OnClickListener mOnHelpClick = new OnClickListener() {
        private int mCounter = 0;

        @Override
        public void onClick(View v) {
            switch (mCounter) {
                // PMW
                case 0:

                    mShowcaseView.setShowcase(new ViewTarget(
                            findViewById(R.id.header_playerPMWTextView)), true);

                    mShowcaseView.setContentText(getString(R.string.help_pmw));
                    break;
                // OMW
                case 1:
                    // mShowcaseView.setTarget(Target.NONE);
                    mShowcaseView.setShowcase(new ViewTarget(
                            findViewById(R.id.header_playerOMWTextView)), true);

                    mShowcaseView.setContentText(getString(R.string.help_omw));
                    break;
                // PGW
                case 2:
                    mShowcaseView.setShowcase(new ViewTarget(
                            findViewById(R.id.header_playerPGWTextView)), true);

                    mShowcaseView.setContentText(getString(R.string.help_pgw));

                    break;
                // OGW
                case 3:
                    mShowcaseView.setShowcase(new ViewTarget(
                            findViewById(R.id.header_playerOGWTextView)), true);

                    mShowcaseView.setContentText(getString(R.string.help_ogw));
                    break;
                default:
                    mShowcaseView.hide();
                    break;
            }
            mCounter++;

        }
    };

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
