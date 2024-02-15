package kz.airbapay.apay_android.ui.pages.card_reader2;

/* Validator.java
 * See the file "LICENSE.md" for the full license governing this code.
 */

import android.text.InputFilter;
import android.text.TextWatcher;

interface Validator extends TextWatcher, InputFilter {
    String getValue();

    boolean isValid();

    boolean hasFullLength();
}
