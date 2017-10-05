package unii.draft.mtg.parings.sharedprefrences;


import android.support.annotation.Nullable;

import unii.draft.mtg.parings.logic.pojo.DraftDataProvider;

public interface IGamePreferences {
    boolean saveDraftDataProvider(DraftDataProvider draftDataProvider);

    @Nullable
    DraftDataProvider getDraftDataProvider();

    boolean isEmpty();

    void clean();
}
