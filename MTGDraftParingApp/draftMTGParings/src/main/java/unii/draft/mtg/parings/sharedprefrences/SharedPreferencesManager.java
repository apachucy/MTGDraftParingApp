package unii.draft.mtg.parings.sharedprefrences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager implements ISharedPrefererences{

	@Override
	public SharedPreferences getSharedPreferences(Context context, String name) {		
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

}
