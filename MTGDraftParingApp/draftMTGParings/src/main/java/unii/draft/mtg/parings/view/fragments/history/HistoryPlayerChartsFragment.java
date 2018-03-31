package unii.draft.mtg.parings.view.fragments.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.util.config.BaseConfig;
import unii.draft.mtg.parings.util.config.BundleConst;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

import static com.github.mikephil.charting.utils.ColorTemplate.JOYFUL_COLORS;

public class HistoryPlayerChartsFragment extends BaseFragment {
    private Unbinder mUnbinder;
    private Long mPlayerId;
    private unii.draft.mtg.parings.database.model.Player mPlayer;
    private PieData mPieData;
    @Nullable
    @BindView(R.id.player_statistic_pie_chart)
    PieChart mPieChart;

    @Nullable
    @BindView(R.id.history_player_detail_playerNameTextView)
    TextView playerNameTextView;

    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_statistics, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void initFragmentView() {
        mPieChart.setData(mPieData);
        playerNameTextView.setText(getString(R.string.history_pie_chart_title, mPlayer.getPlayerName()));


    }

    @Override
    protected void initFragmentData() {
        Bundle bundle = getArguments();
        if (!bundle.containsKey(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL)) {
            getActivity().finish();
        }
        mPlayerId = bundle.getLong(BundleConst.BUNDLE_KEY_PLAYER_DRAFT_DETAIL);
        mPlayer = mDatabaseHelper.get().getPlayer(mPlayerId);

        mPieData = prepareDataSet(prepareDataFromDatabase());
        mPieData.setValueFormatter(new PercentFormatter());
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }


    private PieData prepareDataSet(List<PieEntry> entryList) {
        String dataSetName = getString(R.string.history_player_statistics);
        PieDataSet dataSet = new PieDataSet(entryList, dataSetName);
        dataSet.setColors(JOYFUL_COLORS);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.navigationBarColor));
        dataSet.setValueTextSize(13f);
        return new PieData(dataSet);
    }

    private List<PieEntry> prepareDataFromDatabase() {
        List<Game> games = mDatabaseHelper.get().getAllGamesForPlayer(mPlayerId);
        float win = 0f;
        float lose = 0f;
        float draw = 0f;

        for (Game game : games) {
            if (mPlayer.getPlayerName().equals(game.getWinner())) {
                win += 1;
            } else if (BaseConfig.DRAW.equals(game.getWinner())) {
                draw += 1;
            } else {
                lose += 1;
            }
        }
        float total = win + lose + draw;
        win = win * 100f / total;
        lose = lose * 100f / total;
        draw = draw * 100f / total;
        List<PieEntry> pieEntryList = new ArrayList<>();
        if (win > 0f) {
            PieEntry pieWin = new PieEntry(win, getResources().getString(R.string.win));
            pieEntryList.add(pieWin);
        }
        if (lose > 0f) {
            PieEntry pieLose = new PieEntry(lose, getResources().getString(R.string.lose));
            pieEntryList.add(pieLose);
        }
        if (draw > 0f) {
            PieEntry pieDraw = new PieEntry(draw, getResources().getString(R.string.draw));
            pieEntryList.add(pieDraw);
        }
        return pieEntryList;
    }
}
