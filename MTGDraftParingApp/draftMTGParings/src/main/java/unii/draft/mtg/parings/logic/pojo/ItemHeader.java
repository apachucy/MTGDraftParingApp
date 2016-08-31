package unii.draft.mtg.parings.logic.pojo;

import unii.draft.mtg.parings.view.adapters.IAdapterItem;

/**
 * Created by Unii on 2015-12-12.
 */
public class ItemHeader implements IAdapterItem {
    @Override
    public int getItemType() {
        return ItemType.HEADER;
    }
}
