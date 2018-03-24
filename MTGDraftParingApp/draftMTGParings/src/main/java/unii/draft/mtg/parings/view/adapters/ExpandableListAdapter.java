package unii.draft.mtg.parings.view.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Game;
import unii.draft.mtg.parings.logic.pojo.Round;
import unii.draft.mtg.parings.util.config.BaseConfig;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final List<Round> roundList;
    private int childrenSize;
    private final Context context;

    public ExpandableListAdapter(List<Round> roundList, Context context) {
        this.roundList = roundList;
        this.context = context;
        this.childrenSize = 0;
        for (Round round : roundList) {
            this.childrenSize += round.getGameList().size();
        }
    }

    @Override
    public int getGroupCount() {
        return roundList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenSize;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return roundList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return roundList.get(groupPosition).getGameList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_item, null);
        }

        TextView round = convertView
                .findViewById(R.id.group_round_number);

        round.setText(context.getString(R.string.history_group_round, roundList.get(groupPosition).getNumber()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_sub_item, null);
        }

        Game game = roundList.get(groupPosition).getGameList().get(childPosition);
        String winner = game.getWinner();
        String looser = null;
        if (winner.equals(BaseConfig.DRAW)) {
            winner = game.getPlayerNameA();
            looser = game.getPlayerNameB();
        } else {
            looser = game.getWinner().equals(game.getPlayerNameA()) ? game.getPlayerNameB() : game.getPlayerNameA();
        }
        TextView playerNames = convertView
                .findViewById(R.id.group_players_names);

        playerNames.setText(context.getString(R.string.history_group_players_name, winner, looser));

        int pointsWinner = game.getWinner().equals(game.getPlayerNameA()) ? game.getPlayerAPoints() : game.getPlayerBPoints();

        int pointsLooser = game.getWinner().equals(game.getPlayerNameA()) ? game.getPlayerBPoints() : game.getPlayerAPoints();

        TextView playerPoints = convertView.findViewById(R.id.group_players_results);
        playerPoints.setText(context.getString(R.string.history_group_player_results, pointsWinner, game.getDraws(), pointsLooser));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
