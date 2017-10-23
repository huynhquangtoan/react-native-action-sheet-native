package pro.huynhgia.actionsheet;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.List;

import cn.zjy.actionsheet.ActionSheet;

/**
 * Created by toanhuynh on 18/10/2017.
 */

public class ActionSheetModule extends ReactContextBaseJavaModule {

    public ActionSheetModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "AndroidActionSheet";
    }

    @ReactMethod
    public void showActionSheetWithCustomOptions(final ReadableMap params, final Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity == null)
            return;

        ActionSheet.Builder builder = new ActionSheet.Builder();
        builder.setCancelableOnTouchOutside(true);
        if (params.hasKey("title")) {
            ReadableMap title = params.getMap("title");
            if (title.hasKey("title")) {
                String strTitle = title.getString("title");
                int titleColor;
                try {
                    titleColor = Color.parseColor(title.getString("titleColor"));
                } catch (Exception e) {
                    titleColor = Color.BLACK;
                }
                builder.setTitle(strTitle, titleColor);
            }
        }

        int cancelBtnIndex = -1;
        if (params.hasKey("optionBtns")) {
            if (params.hasKey("cancelButtonIndex")) {
                cancelBtnIndex = params.getInt("cancelButtonIndex");
            }
            ReadableArray optionBtns = params.getArray("optionBtns");
            int size = optionBtns.size();
            String[] titles = new String[size];
            int[] colors = new int[size];
            for (int i = 0; i < size; i++) {
                ReadableMap btn = optionBtns.getMap(i);
                titles[i] = btn.getString("btnTitle");
                try {
                    colors[i] = Color.parseColor(btn.getString("btnTitleColor"));
                } catch (Exception e) {
                    colors[i] = Color.BLACK;
                }
            }
            cancelBtnIndex = size;
            builder.setOtherBtn(titles, colors);
        }
        if (params.hasKey("cancelButtonIndex")) {
            cancelBtnIndex = params.getInt("cancelButtonIndex");
        }
        if (params.hasKey("cancelBtn")) {
            ReadableMap cancelBtn = params.getMap("cancelBtn");
            String title = cancelBtn.getString("btnTitle");
            int titleColor;
            try {
                titleColor = Color.parseColor(cancelBtn.getString("btnTitleColor"));
            } catch (Exception e) {
                titleColor = Color.BLACK;
            }
            builder.setCancelBtn(title, titleColor);
        }

        final int tempCancelBtnIndex = cancelBtnIndex;
        builder.setActionSheetListener(new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {
                if (!isByBtn && callback != null) {
                    callback.invoke(tempCancelBtnIndex);
                }
            }

            @Override
            public void onButtonClicked(ActionSheet actionSheet, int index) {
                try {
                    ReadableArray optionBtns = params.getArray("optionBtns");
                    if (index >= optionBtns.size() && callback != null) {
                        callback.invoke(tempCancelBtnIndex);
                        return;
                    }
                    if (callback != null) {
                        ReadableMap btn = optionBtns.getMap(index);
                        callback.invoke(btn.getInt("btnIndex"));
                        return;
                    }
                } catch (Exception e) {
                    Log.e("Error", String.valueOf(e));
                }
            }
        });
        ActionSheet actionSheet = builder.build();
        actionSheet.show(activity.getFragmentManager());
    }

    @ReactMethod
    public void showActionSheetWithOptions(ReadableMap param, final Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity == null)
            return;

        if (!param.hasKey("cancelButtonIndex")) {
            throw new IllegalArgumentException("no cancelButtonIndex");
        }
        if (!param.hasKey("options")) {
            throw new IllegalArgumentException("no options");
        }

        final int cancelBtnIndex = param.getInt("cancelButtonIndex");
        int destructiveButtonIndex = param.hasKey("destructiveButtonIndex") ? param.getInt("destructiveButtonIndex")
                : -1;
        int tintColor;
        try {
            tintColor = Color.parseColor(param.getString("tintColor"));
        } catch (Exception e) {
            tintColor = Color.BLACK;
        }
        ReadableArray options = param.getArray("options");
        List<String> btnTitles = new ArrayList<>();
        int size = options.size();
        for (int i = 0; i < size; i++) {
            btnTitles.add(options.getString(i));
        }

        ActionSheet.Builder builder = new ActionSheet.Builder();
        builder.setCancelableOnTouchOutside(true);
        if (param.hasKey("title")) {
            String title = param.getString("title");
            builder.setTitle(title, tintColor);
        }
        builder.setCancelBtn(btnTitles.get(cancelBtnIndex), tintColor);
        btnTitles.remove(cancelBtnIndex);

        int[] colors = new int[btnTitles.size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = i == destructiveButtonIndex ? Color.RED : tintColor;
        }
        String[] titles = new String[btnTitles.size()];
        btnTitles.toArray(titles);
        builder.setOtherBtn(titles, colors);

        builder.setActionSheetListener(new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isByBtn) {
                if (!isByBtn && callback != null) {
                    callback.invoke(cancelBtnIndex);
                }
            }

            @Override
            public void onButtonClicked(ActionSheet actionSheet, int index) {
                if (callback != null)
                    callback.invoke(index);
            }
        });
        ActionSheet actionSheet = builder.build();
        actionSheet.show(activity.getFragmentManager());
    }
}
