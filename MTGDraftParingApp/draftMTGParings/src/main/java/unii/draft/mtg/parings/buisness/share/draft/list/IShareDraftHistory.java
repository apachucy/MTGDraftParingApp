package unii.draft.mtg.parings.buisness.share.draft.list;


import java.util.List;

import unii.draft.mtg.parings.logic.pojo.Draft;


public interface IShareDraftHistory {
    String getDraftListToString(List<Draft> draftList);
}
