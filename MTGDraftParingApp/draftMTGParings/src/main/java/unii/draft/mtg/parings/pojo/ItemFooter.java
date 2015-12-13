package unii.draft.mtg.parings.pojo;

import unii.draft.mtg.parings.view.adapters.IAdapterItem;

/**
 * Created by Unii on 2015-12-12.
 */
public class ItemFooter implements IAdapterItem
{

    @Override
    public int getItemType() {
        return ItemType.FOOTER;
    }
}
