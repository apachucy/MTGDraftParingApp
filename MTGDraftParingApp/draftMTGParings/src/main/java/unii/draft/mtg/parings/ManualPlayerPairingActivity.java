package unii.draft.mtg.parings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.view.adapters.MatchPlayerCustomListAdapter;
import unii.draft.mtg.parings.view.adapters.MatchPlayerCustomSpinnerAdapter;


public class ManualPlayerPairingActivity extends BaseActivity {

    @Bind(R.id.matchPlayer_firstPlayerSpinner)
    Spinner mPlayer1Spinner;
    @Bind(R.id.matchPlayer_secondPlayerSpinner)
    Spinner mPlayer2Spinner;
    @Bind(R.id.matchPlayer_PlayersList)
    ListView mMatchPlayerList;


    @OnClick(R.id.matchPlayer_CleanButton)
    void onCleanButtonClicked(View view) {
        mGameList.clear();
        mPlayerListAdapter.notifyDataSetChanged();
        mPlayerList.clear();
        mPlayerList.addAll(((IAlgorithmConfigure) getApplication()).getInstance().getSortedPlayerList());
        //hack
        mPlayer1Spinner.setSelection(1);
        mPlayer2Spinner.setSelection(1);
        //end hack
        mPlayerListAdapter.notifyDataSetChanged();
    }



    @OnClick(R.id.matchPlayer_ParingButton)
    void onParingButtonClicked(View view) {
        if (((Player) mPlayer1Spinner.getSelectedItem()).getPlayerName()
                != ((Player) mPlayer2Spinner.getSelectedItem()).getPlayerName() && (mPlayerList != null && !mPlayerList.isEmpty())) {
            mGameList.add(new Game(((Player) mPlayer1Spinner.getSelectedItem()).getPlayerName(), ((Player) mPlayer2Spinner.getSelectedItem()).getPlayerName()));
            Player player1 = (Player) mPlayer1Spinner.getSelectedItem();
            Player player2 = (Player) mPlayer2Spinner.getSelectedItem();
            mPlayerList.remove(player1);
            mPlayerList.remove(player2);

            if (mPlayerList.isEmpty()) {
                mPlayerList.add(new Player(getString(R.string.spinner_empty_player_list)));
            }
            mPlayerSpinnerAdapter.notifyDataSetChanged();
            mPlayerListAdapter.notifyDataSetChanged();

        } else if (mPlayerList == null || mPlayerList.isEmpty() || mPlayerList.get(0).getPlayerName().equals(getString(R.string.spinner_empty_player_list))) {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_empty_list), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_equal_names), Toast.LENGTH_LONG).show();

        }
    }



    @OnClick(R.id.matchPlayer_PlayButton)
    void onStartGameButtonClicked(View view) {
        if (!mPlayerList.isEmpty() && mPlayerList.size() > 1) {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_paring_not_finished), Toast.LENGTH_LONG).show();
        } else if (mPlayerList.size() > 1 && mPlayerList.get(0).hasBye()) {
            Toast.makeText(ManualPlayerPairingActivity.this, getString(R.string.activity_paring_warning_bye), Toast.LENGTH_LONG).show();
        } else {
            AlgorithmFactory.getInstance().setPlayerGameList(mGameList);
            //someone has a bye
            if (mPlayerList.size() == 1 && !mPlayerList.get(0).getPlayerName().equals(getString(R.string.spinner_empty_player_list))) {
                AlgorithmFactory.getInstance().setPlayerWithBye(mPlayerList.get(0));
            }
            Intent intent = new Intent(ManualPlayerPairingActivity.this, ParingDashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Bind(R.id.toolbar)
    Toolbar mToolBar;

    private List<Player> mPlayerList;
    private List<Game> mGameList;
    private MatchPlayerCustomSpinnerAdapter mPlayerSpinnerAdapter;
    private MatchPlayerCustomListAdapter mPlayerListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_player_pairing);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        mToolBar.setLogo(R.drawable.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_name);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setTitle(R.string.app_name);
        init();
    }

    private void init() {
        mPlayerList = new ArrayList<>();
        mPlayerList.addAll(((IAlgorithmConfigure) getApplication()).getInstance().getSortedPlayerList());
        mPlayerSpinnerAdapter = new MatchPlayerCustomSpinnerAdapter(this, mPlayerList);
        mPlayer1Spinner.setAdapter(mPlayerSpinnerAdapter);
        mPlayer2Spinner.setAdapter(mPlayerSpinnerAdapter);
        mGameList = new ArrayList<Game>();
        mPlayerListAdapter = new MatchPlayerCustomListAdapter(this, mGameList);
        mMatchPlayerList.setAdapter(mPlayerListAdapter);

    }


}
