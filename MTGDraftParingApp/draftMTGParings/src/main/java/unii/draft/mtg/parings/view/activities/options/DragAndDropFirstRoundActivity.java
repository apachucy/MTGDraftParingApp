package unii.draft.mtg.parings.view.activities.options;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thesurix.gesturerecycler.GestureAdapter;
import com.thesurix.gesturerecycler.GestureManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.RoundActivity;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.logic.pojo.Player;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.view.adapters.DragAndDropAdapter;

public class DragAndDropFirstRoundActivity extends BaseActivity {
    @Inject
    AlgorithmChooser mAlgorithmChooser;
    private List<String> mPlayerNameList;
    private GestureAdapter mAdapter;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.drag_and_drop_recyclerView)
    RecyclerView recyclerView;


    @OnClick(R.id.floating_action_button_next)
    void onEndRoundClicked() {

        List<String> list = mAdapter.getData();
        mAlgorithmChooser.getCurrentAlgorithm().reoderPlayerList(list);
        Intent intent = new Intent(this,
                RoundActivity.class);
        startActivity(intent);
        list.size();
        finish();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop_first_round);
        ButterKnife.bind(this);
        initToolBar();
        initData();
        initView();
    }

    private void initData() {
        try {
            if (mAlgorithmChooser.getCurrentAlgorithm() instanceof BaseAlgorithm) {
                BaseAlgorithm base = (BaseAlgorithm) mAlgorithmChooser.getCurrentAlgorithm();
                base.isLoadCachedDraftWasNeeded();
            }
            mPlayerNameList = getPlayerNameList(mAlgorithmChooser.getCurrentAlgorithm().getSortedPlayerList());
        } catch (NullPointerException exception) {
            finish();
        } finally {
            //Nothing here
        }
    }

    @Override
    protected void injectDependencies(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        mToolBar.setLogoDescription(R.string.app_header_path_configuration);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_header_path_configuration);
    }

    @Override
    protected void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DragAndDropAdapter(mPlayerNameList);
        recyclerView.setAdapter(mAdapter);

        GestureManager gestureManager = new GestureManager
                .Builder(recyclerView)
                .setSwipeEnabled(false)
                .setLongPressDragEnabled(true)
                .setManualDragEnabled(false).build();
    }

    @NonNull
    private List<String> getPlayerNameList(@NonNull List<Player> playerList) {
        List<String> playerNameList = new ArrayList<>();
        for (Player player : playerList) {
            playerNameList.add(player.getPlayerName());
        }
        return playerNameList;
    }
}
