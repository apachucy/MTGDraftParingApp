package unii.draft.mtg.parings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.algorithm.AlgorithmFactory;
import unii.draft.mtg.parings.algorithm.IAlgorithmConfigure;
import unii.draft.mtg.parings.pojo.Game;
import unii.draft.mtg.parings.pojo.Player;
import unii.draft.mtg.parings.view.MatchPlayerCustomListAdapter;
import unii.draft.mtg.parings.view.MatchPlayerCustomSpinnerAdapter;


public class MatchPlayerActivity extends Activity {

    private Spinner mPlayer1Spinner;
    private Spinner mPlayer2Spinner;
    private ListView mMatchPlayerList;
    private Button mClearButton;
    private Button mAddParingButton;
    private Button mStartGameButton;

    private List<Player> mPlayerList;
    private List<Game> mGameList;
    private MatchPlayerCustomSpinnerAdapter mPlayerSpinnerAdapter;
    private MatchPlayerCustomListAdapter mPlayerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_player);

        mPlayer1Spinner = (Spinner) findViewById(R.id.matchPlayer_firstPlayerSpinner);
        mPlayer2Spinner = (Spinner) findViewById(R.id.matchPlayer_secondPlayerSpinner);
        mMatchPlayerList = (ListView) findViewById(R.id.matchPlayer_PlayersList);
        mClearButton = (Button) findViewById(R.id.matchPlayer_CleanButton);
        mAddParingButton = (Button) findViewById(R.id.matchPlayer_ParingButton);
        mStartGameButton = (Button) findViewById(R.id.matchPlayer_PlayButton);

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

        mAddParingButton.setOnClickListener(mOnButtonClickListener);
        mClearButton.setOnClickListener(mOnButtonClickListener);
        mStartGameButton.setOnClickListener(mOnButtonClickListener);
    }


    private View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.matchPlayer_CleanButton:

                    mGameList.clear();
                    mPlayerListAdapter.notifyDataSetChanged();


                    mPlayerList.clear();

                    mPlayerList.addAll(((IAlgorithmConfigure) getApplication()).getInstance().getSortedPlayerList());
                    //hack
                    mPlayer1Spinner.setSelection(1);
                    mPlayer2Spinner.setSelection(1);
                   //end hack
                    mPlayerListAdapter.notifyDataSetChanged();


                    break;
                case R.id.matchPlayer_ParingButton:
                    if (((Player) mPlayer1Spinner.getSelectedItem()).getPlayerName()
                            != ((Player) mPlayer2Spinner.getSelectedItem()).getPlayerName() && (mPlayerList != null && !mPlayerList.isEmpty())) {
                        mGameList.add(new Game(((Player) mPlayer1Spinner.getSelectedItem()).getPlayerName(), ((Player) mPlayer2Spinner.getSelectedItem()).getPlayerName()));
                        Player player1 = (Player) mPlayer1Spinner.getSelectedItem();
                        Player player2 = (Player) mPlayer2Spinner.getSelectedItem();
                        mPlayerList.remove(player1);
                        mPlayerList.remove(player2);

                        if(mPlayerList.isEmpty()){
                            mPlayerList.add(new Player(getString(R.string.spinner_empty_player_list)));
                        }
                        mPlayerSpinnerAdapter.notifyDataSetChanged();
                        mPlayerListAdapter.notifyDataSetChanged();

                    } else if (mPlayerList == null || mPlayerList.isEmpty() || mPlayerList.get(0).getPlayerName().equals(getString(R.string.spinner_empty_player_list))) {
                        Toast.makeText(MatchPlayerActivity.this, getString(R.string.activity_paring_warning_empty_list), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MatchPlayerActivity.this, getString(R.string.activity_paring_warning_equal_names), Toast.LENGTH_LONG).show();

                    }

                    break;
                case R.id.matchPlayer_PlayButton:
                    //mPlayerList can contain 1 player it is a player with a bye
                    if(!mPlayerList.isEmpty() && mPlayerList.size() > 1 ) {
                        Toast.makeText(MatchPlayerActivity.this, getString(R.string.activity_paring_warning_paring_not_finished), Toast.LENGTH_LONG).show();
                    }else if(mPlayerList.size() > 1 && mPlayerList.get(0).hasBye()){
                        Toast.makeText(MatchPlayerActivity.this, getString(R.string.activity_paring_warning_bye), Toast.LENGTH_LONG).show();

                    }else{
                        AlgorithmFactory.getInstance().setPlayerGameList(mGameList);
                        //someone has a bye
                        if(mPlayerList.size() == 1 && !mPlayerList.get(0).getPlayerName().equals(getString(R.string.spinner_empty_player_list))){
                            AlgorithmFactory.getInstance().setPlayerWithBye(mPlayerList.get(0));
                        }
                        Intent intent = new Intent(MatchPlayerActivity.this, ParingsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;

            }


        }
    };
}
