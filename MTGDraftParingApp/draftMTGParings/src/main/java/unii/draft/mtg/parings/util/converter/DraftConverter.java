package unii.draft.mtg.parings.util.converter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import unii.draft.mtg.parings.logic.pojo.Draft;

public class DraftConverter implements Converter<Draft, unii.draft.mtg.parings.database.model.Draft> {
    @Nullable
    @Override
    public Draft convert(unii.draft.mtg.parings.database.model.Draft draft) {
        return null;
    }

    @NonNull
    @Override
    public Draft convert(unii.draft.mtg.parings.database.model.Draft draft, String data) {
        return new Draft(data, draft);
    }
}
