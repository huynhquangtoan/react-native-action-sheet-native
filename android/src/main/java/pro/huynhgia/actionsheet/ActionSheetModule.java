package pro.huynhgia.actionsheet;

import android.view.MenuItem;
import android.graphics.Color;
import android.content.DialogInterface;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;


import pro.huynhgia.bottomsheetbuilder.BottomSheetBuilder;
import pro.huynhgia.bottomsheetbuilder.BottomSheetMenuDialog;
import pro.huynhgia.bottomsheetbuilder.adapter.BottomSheetItemClickListener;

public class ActionSheetModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext mContext;

    public ActionSheetModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "ActionSheet";
    }

    @ReactMethod
    public void SheetView(final ReadableMap props, final Callback onSelecctionCallback, final Callback onCancelCallback) {

        ReadableArray items = props.getArray("items");
        if (items.size() == 0) {
            return;
        }
        String title = props.getString("title");
        String titleTextColor = props.getString("titleTextColor");
        String itemTextColor = props.getString("itemTextColor");
        String itemTintColor = props.getString("itemTintColor");
        String backgroundColor = props.getString("backgroundColor");

        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getCurrentActivity());

        bottomSheetBuilder = bottomSheetBuilder.setMode(BottomSheetBuilder.MODE_LIST);
        if (title != null && title.trim().length() > 0) {
            bottomSheetBuilder = bottomSheetBuilder.addTitleItem(title);

            if (titleTextColor != null && titleTextColor.length() > 0) {
                bottomSheetBuilder = bottomSheetBuilder.setTitleTextColor(Color.parseColor(titleTextColor));
            }
        }
        if (itemTextColor != null && itemTextColor.length() > 0) {
            bottomSheetBuilder = bottomSheetBuilder.setItemTextColor(Color.parseColor(itemTextColor));
        }

        if (itemTintColor != null && itemTintColor.length() > 0) {
            bottomSheetBuilder = bottomSheetBuilder.setIconTintColor(Color.parseColor(itemTintColor));
        }
        if (backgroundColor != null && backgroundColor.length() > 0) {
            bottomSheetBuilder = bottomSheetBuilder.setBackgroundColor(Color.parseColor(backgroundColor));
        }

        for (int index = 0; index < items.size(); index++) {
            ReadableMap item = items.getMap(index);
            bottomSheetBuilder = bottomSheetBuilder.addItem(index, item.getString("title"), null);
        }

        bottomSheetBuilder = bottomSheetBuilder.setItemClickListener(new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                onSelecctionCallback.invoke(item.getItemId());
            }
        });

        BottomSheetMenuDialog dialog = bottomSheetBuilder.createDialog();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onCancelCallback.invoke();
            }
        });

        dialog.show();
    }

}