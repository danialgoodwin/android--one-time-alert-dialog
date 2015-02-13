/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Danial Goodwin
 * Source: https://github.com/danialgoodwin/android--one-time-alert-dialog
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.simplyadvanced.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/** An AlertDialog that will only appear once for the given key.
 *
 * This can be just like a regular AlertDialog and AlertDialog.Builder, except
 * that `create()` is NOT supported and Builder.show() always returns null.
 * This was done to vastly simplify the code. (If create() is used, then a regular AlertDialog
 * without a prefsKey will be returned, thus show() wouldn't be limited.)
 *
 * Example:
 * <code>
 * private void showRateDialogOnce(Activity activity, String title, String message) {
 *     OneTimeAlertDialog.Builder(activity, "rate")
 *             .setTitle(title)
 *             .setMessage(message)
 *             ...
 *             .show();
 * }
 * </code>
 *
 */
public class OneTimeAlertDialog extends AlertDialog {

    /** The value to be stored and checked in the default SharedPreferences. */
    private String mPrefsKey;

    // Hide superclass constructors.
    @SuppressWarnings("UnusedDeclaration")
    private OneTimeAlertDialog(Context context) { super(context); }
    @SuppressWarnings("UnusedDeclaration")
    private OneTimeAlertDialog(Context context, int theme) { super(context, theme); }
    @SuppressWarnings("UnusedDeclaration")
    private OneTimeAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) { super(context, cancelable, cancelListener); }


    protected OneTimeAlertDialog(Context context, String prefsKey) {
        super(context);
        mPrefsKey = prefsKey;
    }

    protected OneTimeAlertDialog(Context context, int theme, String prefsKey) {
        super(context, theme);
        mPrefsKey = prefsKey;
    }

    protected OneTimeAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener,
            String prefsKey) {
        super(context, cancelable, cancelListener);
        mPrefsKey = prefsKey;
    }

    /** Creates and shows the dialog if the key is not marked as shown. If marked as shown, then
     * nothing happens. */
    @Override
    public void show() {
        if (!isKeyInPrefs(getContext(), mPrefsKey)) {
            super.show();
            markShown();
        }
    }

    /** Manually mark this dialog as already shown. The next time `show()` is called with this key
     * no dialog will be shown. */
    public void markShown() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean(mPrefsKey, true).apply();
    }

    /** Mark this dialog as not already shown. The next time `show()` is called with this key a
     * dialog will be shown. */
    public void markNotShown() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean(mPrefsKey, false).apply();
    }

    private static boolean isKeyInPrefs(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    public static class Builder extends AlertDialog.Builder {

        private String prefsKey;

        // Hide superclass constructors.
        @SuppressWarnings("UnusedDeclaration")
        private Builder(Context context) { super(context); }
        @SuppressWarnings("UnusedDeclaration")
        private Builder(Context context, int theme) { super(context, theme); }

        public Builder(Context context, String prefsKey) {
            super(context);
            this.prefsKey = prefsKey;
        }

        public Builder(Context context, int theme, String prefsKey) {
            super(context, theme);
            this.prefsKey = prefsKey;
        }

        /** Creates and shows the dialog if the key is not marked as shown. If marked as shown, then
         * nothing happens. */
        @SuppressWarnings("NullableProblems")
        @Nullable
        @Override
        public AlertDialog show() {
            if (!isKeyInPrefs(getContext(), prefsKey)) {
                super.show();
                markShown();
            }
            //noinspection ConstantConditions
            return null;
        }

        /** This operation is not supported. Use `show()` instead.
         * @throws java.lang.UnsupportedOperationException */
//        @NonNull
//        @Override
//        public AlertDialog create() {
//            throw new UnsupportedOperationException("Limitation of this subclass. " +
//                    "Use `show()` or `OneTimeFullAlertDialog` (doesn't exist yet) instead.");
//        }

        private void markShown() {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                    .putBoolean(prefsKey, true).apply();
        }

    }

}
