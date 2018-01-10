package unii.draft.mtg.parings.view;

import android.content.Intent;
import android.os.Bundle;

import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.MainActivity;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start home activity

        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));

        // close splash activity

        finish();
    }

    @Override
    protected void injectDependencies(ActivityComponent activityComponent) {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView() {

    }
}
