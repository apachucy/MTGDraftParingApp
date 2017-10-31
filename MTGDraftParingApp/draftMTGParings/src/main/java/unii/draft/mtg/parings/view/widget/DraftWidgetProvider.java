package unii.draft.mtg.parings.view.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import unii.draft.mtg.parings.R;

//TODO: prepare better text

/**
 * Implementation of App Widget functionality.
 */
public class DraftWidgetProvider extends AppWidgetProvider {
    public static final String BUNDLE_EXTRA = "unii.draft.mtg.parings.view.widget.BUNDLE_EXTRA";
    private static WidgetViewModel widgetViewModel = null;


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (!intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (!bundle.containsKey(BUNDLE_EXTRA)) {
            return;
        }
        updateAppWidget(context, bundle);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static void updateAppWidget(Context context, Bundle bundle) {
        widgetViewModel = (WidgetViewModel) bundle.getSerializable(BUNDLE_EXTRA);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_draft);
        views.setImageViewResource(R.id.widget_icon, R.drawable.ic_launcher);
        String widgetData;
        if (widgetViewModel != null) {
            widgetData = context.getString(R.string.widget_content, widgetViewModel.getCurrentRound(), widgetViewModel.getWinningPlayer());
        } else {
            widgetData = context.getString(R.string.widget_empty_content);
        }
        views.setTextViewText(R.id.widget_text, widgetData);
        // This time we dont have widgetId. Reaching our widget with that way.
        ComponentName appWidget = new ComponentName(context, DraftWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidget, views);
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_draft);

        views.setImageViewResource(R.id.widget_icon, R.drawable.ic_launcher);
        String widgetData;
        if (widgetViewModel != null) {
            widgetData = context.getString(R.string.widget_content, widgetViewModel.getCurrentRound(), widgetViewModel.getWinningPlayer());
        } else {
            widgetData = context.getString(R.string.widget_empty_content);
        }
        views.setTextViewText(R.id.widget_text, widgetData);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

