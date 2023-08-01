package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.cardNumber
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.ViewEditText

@Composable
internal fun CardNumberView(
    cardNumberText: MutableState<String>,
    cardNumberError: MutableState<String?>,
    paySystemIcon: MutableState<Int?>,
    cardNumberFocusRequester: FocusRequester,
    nameHolderFocusRequester: FocusRequester,
) {

    ViewEditText(
        text = cardNumberText,
        paySystemIcon = paySystemIcon,
        errorTitle = cardNumberError,
        focusRequester = cardNumberFocusRequester,
        placeholder = cardNumber(),
        keyboardActions = KeyboardActions(
            onNext = {
                nameHolderFocusRequester.requestFocus()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        modifierRoot = Modifier.padding(horizontal = 16.dp)
    )

}



/*
class CardNumberEditTextWidget extends StatelessWidget {
   CardNumberEditTextWidget({required this.focusNode, required this.focusNodeNameHolder, super.key});

  final FocusNode focusNode;
  final FocusNode focusNodeNameHolder;
  final TextEditingController controller = TextEditingController();

  @override
  Widget build(BuildContext context) {

    return BlocBuilder<HomeBloc, HomeState>(
      builder: (context, state) {
        return _initCardNumberContainer(context, controller);
      },
    );
  }

  Container _initCardNumberContainer(BuildContext context, TextEditingController controller) {

    bool hasError = !isBlank(readState(context).cardNumberState?.error);
    String? iconPaymentSystem = readState(context).iconPaymentSystem;

    return Container(
            padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
            margin: const EdgeInsets.only(right: 16, left: 16),
            decoration: initEditTextBoxDecoration(hasError, focusNode),
            child: TextFormField(
              inputFormatters: [
                MaskFormatter('AAAA AAAA AAAA AAAA')
              ],
              controller: controller,
              onFieldSubmitted: (v){
                FocusScope.of(context).requestFocus(focusNodeNameHolder);
              },
              focusNode: focusNode,
              keyboardType: TextInputType.number,
              onChanged: (value) {
                addState(context, CardNumberDataEvent(
                    cardNumber: isBlank(value) ? '' : value,
                    iconPaymentSystem: getCardTypeFromNumber(value).icon
                ));

                if (!isBlank(readState(context).cardNumberState?.error)) {
                  addState(context, const CardNumberEvent(errorCardNumber: ''));
                }
              },
              cursorColor: ColorsSdk.colorBrandMain,
              style: Fonts.regular(),
              textInputAction: TextInputAction.next,
              decoration: inputTextFieldDecoration(
                  text: cardNumber(),
                  iconPaymentSystem: iconPaymentSystem,
                  isFocused: focusNode.hasFocus,
                  controller: controller,
                  isError: hasError,
                  afterClearText: () => addState(context, const CardNumberDataEvent(cardNumber: '')),
                  needSuffixExtended: true
              )
            ));
  }
}

*/