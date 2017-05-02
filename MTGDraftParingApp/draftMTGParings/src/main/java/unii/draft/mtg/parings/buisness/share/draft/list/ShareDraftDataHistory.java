package unii.draft.mtg.parings.buisness.share.draft.list;


import android.content.Context;

import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.Draft;


public class ShareDraftDataHistory implements IShareDraftHistory {
    private Context mContext;

    public ShareDraftDataHistory(Context context) {
        mContext = context;
    }

    @Override
    public String getDraftListToString(List<Draft> draftList) {
        if (draftList == null || draftList.isEmpty()) {
            return null;
        }
        String drafts = "";
        for (Draft draft : draftList) {
            drafts += getDraftToString(draft);
        }
        return drafts;
    }

    private String getDraftToString(Draft draft) {
        return mContext.getString(R.string.shared_data_formatted_draft, draft.getDraftName(), draft.getWinnerName(), draft.getNumberOfPlayers(), draft.getDraftRounds());
    }
}
