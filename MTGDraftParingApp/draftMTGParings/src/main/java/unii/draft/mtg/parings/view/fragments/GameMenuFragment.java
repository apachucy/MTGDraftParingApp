package unii.draft.mtg.parings.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.Lazy;
import unii.draft.mtg.parings.MainActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.RoundActivity;
import unii.draft.mtg.parings.ScoreBoardActivity;
import unii.draft.mtg.parings.buisness.algorithm.base.BaseAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.IParingAlgorithm;
import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;
import unii.draft.mtg.parings.buisness.algorithm.roundrobin.RoundRobinRounds;
import unii.draft.mtg.parings.buisness.algorithm.tournament.TournamentRounds;
import unii.draft.mtg.parings.buisness.sittings.SittingsMode;
import unii.draft.mtg.parings.sharedprefrences.ISharedPreferences;
import unii.draft.mtg.parings.util.AlgorithmChooser;
import unii.draft.mtg.parings.util.helper.IDatabaseHelper;
import unii.draft.mtg.parings.util.validation.ValidationHelper;
import unii.draft.mtg.parings.view.activities.options.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.view.activities.options.SittingsActivity;
import unii.draft.mtg.parings.view.adapters.AddPlayerAdapter;
import unii.draft.mtg.parings.view.adapters.DividerItemDecorator;
import unii.draft.mtg.parings.view.custom.IActivityHandler;
import unii.draft.mtg.parings.view.custom.IPlayerList;


public class GameMenuFragment extends BaseFragment {
    private Paint paint;

    private IPlayerList mPlayerNameList;
    private IActivityHandler mActivityHandler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Unbinder mUnbinder;

    private Activity mActivity;
    private boolean isPreviousDraftNotEnded;
    @Nullable
    @BindView(R.id.init_playerList)
    RecyclerView mPlayerList;

    @Nullable
    @BindView(R.id.init_playerNameTextInput)
    TextInputLayout mPlayerNameTextInput;

    @Nullable
    @BindView(R.id.init_roundsTextInput)
    TextInputLayout mRoundsTextInput;


    @Nullable
    @BindView(R.id.init_addPlayerFromHistoryButton)
    Button mAddPlayerFromHistoryButton;

    @Inject
    Lazy<ISharedPreferences> mSharedPreferenceManager;
    @Inject
    Lazy<AlgorithmChooser> mAlgorithmChooser;
    @Inject
    Lazy<IDatabaseHelper> mDatabaseHelper;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mPlayerNameList = (IPlayerList) mActivity;
        if (!(mActivity instanceof IActivityHandler)) {
            throw new RuntimeException("Activity should implement IActivityHandler");
        }
        mActivityHandler = (IActivityHandler) mActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_menu, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        injectDependencies();
        initFragmentData();
        initFragmentView(inflater);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        paint = null;
    }

    @OnClick(R.id.init_addPlayerButton)
    void onAddPlayerClick() {
        // add new player
        if (!ValidationHelper.isTextInputFieldEmpty(mPlayerNameTextInput,
                getString(R.string.warning_empty_field)) && !isNameAddedBefore(mPlayerNameTextInput.getEditText().getText()
                .toString())) {
            mPlayerNameList.getPlayerList().add(mPlayerNameTextInput.getEditText().getText()
                    .toString());
            mPlayerNameTextInput.getEditText().setText("");
            mAdapter.notifyDataSetChanged();

        } else if (isNameAddedBefore(mPlayerNameTextInput.getEditText().getText()
                .toString())) {
            mPlayerNameTextInput.setError(getResources().getString(R.string.warning_player_name_added));
        }
    }

    @OnClick(R.id.init_roundsButton)
    void onStartGameClick() {
        if (isValidRoundEditText()) {
            if (mPlayerNameList.getPlayerList().isEmpty() || mPlayerNameList.getPlayerList().size() < 2) {
                FancyToast.makeText(mActivity,
                        getString(R.string.warning_need_players),
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                // if number of player where bigger than
                // round ask user to change it
            } else if (mRoundsTextInput.getVisibility() == View.VISIBLE && Integer.parseInt(mRoundsTextInput.getEditText().getText()
                    .toString()) >= mPlayerNameList.getPlayerList().size()) {
                mActivityHandler.showInfoDialog(getString(R.string.dialog_warning_title),
                        getString(R.string.dialog_warning_message),
                        getString(R.string.dialog_start_button));
            } else {
                int rounds = 0;
                if (mRoundsTextInput.getVisibility() == View.VISIBLE && mRoundsTextInput != null && mRoundsTextInput.getEditText() != null) {
                    rounds = Integer.parseInt(mRoundsTextInput.getEditText().getText().toString());
                } else if (mRoundsTextInput.getVisibility() != View.VISIBLE) {
                    int players = mPlayerNameList.getPlayerList().size();
                    if (mSharedPreferenceManager.get().getPairingType() == PairingMode.PAIRING_TOURNAMENT) {
                        rounds = new TournamentRounds().getMaxRound(players);
                    } else {
                        rounds = new RoundRobinRounds().getMaxRound(players);
                    }
                }

                String textBody = getString(R.string.dialog_start_message, mPlayerNameList.getPlayerList().size(), rounds);
                if (mSharedPreferenceManager.get().getPairingType() == PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS) {
                    textBody = getString(R.string.dialog_start_message_warning_replays, mPlayerNameList.getPlayerList().size(), rounds);
                }
                mActivityHandler.showInfoDialog(getString(R.string.dialog_start_title), textBody, getString(R.string.start_game), mStartGameDialogOnClickListener);

            }
        }
    }

    @OnClick(R.id.init_addPlayerFromHistoryButton)
    public void onAddPlayersFromHistoryClicked() {
        if (playersNotExistInHistory()) {
            FancyToast.makeText(getContext(), getString(R.string.no_players_from_history), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            return;
        }
        final List<String> playersNameFromHistory = mDatabaseHelper.get().getAllPlayersNames();

        MaterialDialog.ListCallbackMultiChoice listCallbackMultiChoice = new MaterialDialog.ListCallbackMultiChoice() {
            @Override
            public boolean onSelection(MaterialDialog dialog, @NonNull Integer[] which, CharSequence[] text) {

                for (Integer aWhich : which) {
                    String playerName = playersNameFromHistory.get(aWhich);
                    if (!isNameAddedBefore(playerName)) {
                        mPlayerNameList.getPlayerList().add(playerName);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        };

        showMultipleChoiceListDialog(getContext(), getString(R.string.add_players_from_history), playersNameFromHistory,
                listCallbackMultiChoice, getString(R.string.add_item));
    }


    protected void initFragmentView(@NonNull LayoutInflater inflater) {
        if (isPreviousDraftNotEnded) {
            //display dialog that can kill this application and move to scoreboard :)
            showDialogWithTwoOptions(getActivity(), getString(R.string.dialog_load_not_ended_draft_title), getString(R.string.dialog_load_not_ended_draft_body),
                    getString(R.string.positive), getString(R.string.negative), mDialogLoadDraftClickListener);
        }
        mAdapter = new AddPlayerAdapter(mPlayerNameList.getPlayerList());
        mPlayerList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mPlayerList.setLayoutManager(mLayoutManager);
        mPlayerList.addItemDecoration(new DividerItemDecorator(getActivity(), DividerItemDecorator.VERTICAL_LIST));

        mPlayerNameTextInput.setHint(getString(R.string.hint_player_name));
        mRoundsTextInput.setHint(getString(R.string.hint_rounds));
        mRoundsTextInput.setErrorEnabled(true);
        mPlayerNameTextInput.setErrorEnabled(true);
        if (isGameInTournamentMode()) {
            mRoundsTextInput.setVisibility(View.GONE);
        }

        mAddPlayerFromHistoryButton.setEnabled(!playersNotExistInHistory());

        mPlayerList.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(removeItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mPlayerList);
    }

    @Override
    protected void initFragmentView() {
        //Not-implemented
    }

    @Override
    protected void initFragmentData() {
        BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.get().getCurrentAlgorithm();
        isPreviousDraftNotEnded = !baseAlgorithm.isCacheEmpty()
                && baseAlgorithm.playedRound() < baseAlgorithm.getMaxRound();
        paint = new Paint();
    }

    private boolean isGameInTournamentMode() {
        return mSharedPreferenceManager.get().getPairingType() == PairingMode.PAIRING_TOURNAMENT || mSharedPreferenceManager.get().getPairingType() == PairingMode.PAIRING_ROUND_ROBIN;
    }

    private boolean isValidRoundEditText() {
        return (mRoundsTextInput.getVisibility() != View.VISIBLE) || (mRoundsTextInput.getVisibility() == View.VISIBLE && !ValidationHelper.isTextInputFieldEmpty(mRoundsTextInput,
                getString(R.string.warning_empty_field)));
    }


    private boolean playersNotExistInHistory() {
        final List<String> playersNameFromHistory = mDatabaseHelper.get().getAllPlayersNames();
        return playersNameFromHistory.isEmpty();
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mStartGameDialogOnClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            int rounds = 0;
            if (mRoundsTextInput.getVisibility() == View.VISIBLE && mRoundsTextInput != null && mRoundsTextInput.getEditText() != null) {
                rounds = Integer.parseInt(mRoundsTextInput.getEditText().getText().toString());
            } else if (mRoundsTextInput.getVisibility() != View.VISIBLE) {
                rounds = new TournamentRounds().getMaxRound(mPlayerNameList.getPlayerList().size());
            }

            if (rounds != 0) {
                dialog.dismiss();
                Intent intent;
                //set parings Factory
                mAlgorithmChooser.get().getCurrentAlgorithm().startAlgorithm(mPlayerNameList.getPlayerList(), rounds);
                if (mAlgorithmChooser.get().getCurrentAlgorithm() instanceof BaseAlgorithm) {
                    BaseAlgorithm baseAlgorithm = (BaseAlgorithm) mAlgorithmChooser.get().getCurrentAlgorithm();
                    if (!baseAlgorithm.cacheDraft()) {
                        displayErrorDialog();
                        return;
                    }
                }
                //set started activity
                if (mSharedPreferenceManager.get().getGeneratedSittingMode() == SittingsMode.SITTINGS_RANDOM) {
                    intent = new Intent(mActivity, SittingsActivity.class);
                } else if (mSharedPreferenceManager.get().getPairingType() == PairingMode.PAIRING_MANUAL) {
                    intent = new Intent(mActivity,
                            ManualPlayerPairingActivity.class);
                } else {
                    intent = new Intent(mActivity,
                            RoundActivity.class);

                }
                startActivity(intent);
                mActivity.finish();
            }
        }
    };


    private boolean isNameAddedBefore(@NonNull String playerName) {
        boolean isAddedBefore = false;

        for (String name : mPlayerNameList.getPlayerList()) {
            if (playerName.equals(name)) {
                isAddedBefore = true;
                break;
            }

        }
        return isAddedBefore;
    }

    private void injectDependencies() {
        getActivityComponent().inject(this);
    }


    private void displayErrorDialog() {
        ((MainActivity) getActivity()).showInfoDialog(getString(R.string.dialog_error_algorithm_title),
                getString(R.string.dialog_error_algorithm__message),
                getString(R.string.dialog_start_button), mDialogErrorButtonClickListener);
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback mDialogErrorButtonClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
        }
    };

    @NonNull
    private MaterialDialog.SingleButtonCallback mDialogLoadDraftClickListener = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(MaterialDialog dialog, DialogAction which) {
            dialog.dismiss();
            Intent intent;
            IParingAlgorithm currentAlgorithm = mAlgorithmChooser.get().getCurrentAlgorithm();
            if (currentAlgorithm.getCurrentRound() == currentAlgorithm.playedRound()) {
                intent = new Intent(getActivity(), ScoreBoardActivity.class);
            } else {
                intent = new Intent(getActivity(), RoundActivity.class);
            }

            startActivity(intent);
            getActivity().finish();
        }
    };


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

            if (direction == ItemTouchHelper.LEFT) {
                mPlayerNameList.getPlayerList().remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mPlayerNameList.getPlayerList().size());
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
                    paint.setColor(ContextCompat.getColor(getContext(), R.color.red));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, paint);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_36dp);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, paint);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
