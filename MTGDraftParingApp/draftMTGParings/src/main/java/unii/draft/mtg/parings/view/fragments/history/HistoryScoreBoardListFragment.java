package unii.draft.mtg.parings.view.fragments.history;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.buisness.share.draft.list.IShareDraftHistory;
import unii.draft.mtg.parings.buisness.share.draft.list.ShareDraftDataHistory;
import unii.draft.mtg.parings.logic.pojo.Draft;
import unii.draft.mtg.parings.util.converter.Converter;
import unii.draft.mtg.parings.util.converter.DraftConverter;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.adapters.HistoryScoreBoardAdapter;
import unii.draft.mtg.parings.view.fragments.BaseFragment;

import static unii.draft.mtg.parings.view.adapters.HistoryScoreBoardAdapter.VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;

public class HistoryScoreBoardListFragment extends BaseFragment {
    private Paint mPaint;
    private HistoryScoreBoardAdapter mAdapter;
    private Activity mContext;
    private IDisplayDetailFragment mDisplayHistoryScoreBoardDetail;
    private IShareDraftHistory mShareDraftHistory;
    private List<Draft> mDraftListForShare;
    private Unbinder mUnbinder;

    @Nullable
    @BindView(R.id.settings_menuRecyclerView)
    RecyclerView mRecyclerView;

    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof IDisplayDetailFragment) {
            mDisplayHistoryScoreBoardDetail = (IDisplayDetailFragment) activity;
        } else {
            //noinspection ThrowableInstanceNeverThrown
            new Throwable("Activity should implement IDisplayDetailFragment");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
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
        mPaint = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.settings_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if (mDraftListForShare == null) {
                    mDraftListForShare = createDraftListForSharing();
                }
                shareAction(mShareDraftHistory.getDraftListToString(mDraftListForShare));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initFragmentView() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorator(mContext, DividerItemDecorator.VERTICAL_LIST));
        mAdapter = new HistoryScoreBoardAdapter(mContext, mDatabaseHelper.get(), mDisplayHistoryScoreBoardDetail);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter.getItemViewType(0) == VIEW_TYPE_EMPTY_LIST_PLACEHOLDER) {
            return;
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(removeItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initFragmentData() {
        mShareDraftHistory = new ShareDraftDataHistory(getContext());
        mPaint = new Paint();
    }

    @NonNull
    private List<Draft> createDraftListForSharing() {
        List<Draft> draftList = new ArrayList<>();
        List<unii.draft.mtg.parings.database.model.Draft> databaseDraftList = mDatabaseHelper.get().getAllDraftList();
        Converter<Draft, unii.draft.mtg.parings.database.model.Draft> draftConverter = new DraftConverter();
        for (unii.draft.mtg.parings.database.model.Draft draft : databaseDraftList) {
            draftList.add(draftConverter.convert(draft, mDatabaseHelper.get().getAllPlayersInDraft(draft.getId()).get(0).getPlayerName()));
        }
        return draftList;
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }

    /**
     * Code base on: https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
     */
    private ItemTouchHelper.SimpleCallback removeItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT && viewHolder instanceof HistoryScoreBoardAdapter.ViewHolder) {
                mAdapter.removeItem(position);
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            Bitmap icon;
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;

                if (dX < 0) {
                    mPaint.setColor(ContextCompat.getColor(getContext(), R.color.red));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, mPaint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_36dp);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, mPaint);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}
