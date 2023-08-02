package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import kz.airbapay.apay_android.data.constant.RegexConst
import kz.airbapay.apay_android.data.constant.dateExpired
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.ViewEditText

@Composable
internal fun DateExpiredView(
    dateExpiredText: MutableState<TextFieldValue>,
    dateExpiredError: MutableState<String?>,
    dateExpiredFocusRequester: FocusRequester,
    cvvFocusRequester: FocusRequester,
    modifier: Modifier
) {
    ViewEditText(
        text = dateExpiredText,
        mask = "AA/AA",
        isDateExpiredMask = true,
        regex = Regex(RegexConst.NOT_DIGITS),
        errorTitle = dateExpiredError,
        focusRequester = dateExpiredFocusRequester,
        placeholder = dateExpired(),
        keyboardActions = KeyboardActions(
            onNext = {
                cvvFocusRequester.requestFocus()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifierRoot = modifier,
        actionOnTextChanged = {}
    )
}

/*
*
class DateExpiredEditTextWidget extends StatelessWidget {
  const DateExpiredEditTextWidget({required this.focusNode, required this.focusNodeCvv, super.key});

  final FocusNode focusNode;
  final FocusNode focusNodeCvv;

  @override
  Widget build(BuildContext context) {
    final TextEditingController controller = TextEditingController(text: readState(context).dateExpired);

    return BlocBuilder<HomeBloc, HomeState>(
      builder: (context, state) {
        return _initTextContainer(controller, context);
      },
    );
  }

  Container _initTextContainer(TextEditingController controller, BuildContext context) {
    bool hasError = !isBlank(readState(context).dateExpiredState?.error);

    return Container(
            padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
            decoration: initEditTextBoxDecoration(hasError, focusNode),
            child: TextFormField(
              focusNode: focusNode,
              keyboardType: TextInputType.number,
              controller: controller,
              inputFormatters: [
                DateExpiredMaskFormatter()
              ],
              onFieldSubmitted: (v) {
                FocusScope.of(context).requestFocus(focusNodeCvv);
              },
              onChanged: (value) {

                addState(context, DateExpiredDataEvent(dateExpired: isBlank(value) ? '' : value));

                if (!isBlank(readState(context).cardNumberState?.error)) {
                  addState(context, const DateExpiredEvent(errorDateExpired: ''));
                }
              },
              cursorColor: ColorsSdk.colorBrandMain,
              style: Fonts.regular(),
              textInputAction: TextInputAction.next,
              decoration: inputTextFieldDecoration(
                  text: dateExpired(),
                  isError: hasError,
                  isFocused: focusNode.hasFocus,
                  afterClearText: () => addState(context, const DateExpiredDataEvent(dateExpired: '')),
                  controller: controller
              )
            ));
  }
}

*/