package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.RegexConst
import kz.airbapay.apay_android.data.constant.holderName
import kz.airbapay.apay_android.data.model.EditTextDVO
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.EditTextViewModel
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.ViewEditTextSimple

@Composable
internal fun NameHolder(

) {
    val editTextDVO = EditTextDVO(
        placeholder = holderName(),
        regexForClear = Regex(RegexConst.TEXTS),
        keyboardActions = KeyboardActions(
            onNext = {

            }
        ),
    )

    val viewModelNameHolder = EditTextViewModel(
        _content = editTextDVO
    )

    ViewEditTextSimple(
        viewModel = viewModelNameHolder,
        modifierRoot = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    )
}

/*class NameHolderEditTextWidget extends StatelessWidget {
  const NameHolderEditTextWidget({required this.focusNode, required this.focusNodeDateExpired, super.key});

  final FocusNode focusNode;
  final FocusNode focusNodeDateExpired;

  @override
  Widget build(BuildContext context) {
    final TextEditingController controller = TextEditingController(text: readState(context).nameHolder);

    return BlocBuilder<HomeBloc, HomeState>(
      builder: (context, state) {
        return _initTextContainer(controller, context);
      },
    );
  }

  Container _initTextContainer(
      TextEditingController controller,
      BuildContext context) {

    bool hasError = !isBlank(readState(context).nameHolderState?.error);

    return Container(
          padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
          margin: const EdgeInsets.only(right: 16, left: 16),
          decoration: initEditTextBoxDecoration(hasError, focusNode),
          child: TextFormField(
            focusNode: focusNode,
            controller: controller,
            textCapitalization: TextCapitalization.characters,
            keyboardType: TextInputType.text,
            inputFormatters: <TextInputFormatter>[
              FilteringTextInputFormatter.allow(RegExp("[a-zA-Z -]")),
            ],
            onFieldSubmitted: (v) {
              FocusScope.of(context).requestFocus(focusNodeDateExpired);
            },
            onChanged: (value) {

              addState(context, NameHolderDataEvent(nameHolder: isBlank(value) ? '' : value));

              if (!isBlank(readState(context).cardNumberState?.error)) {
                addState(context, const NameHolderEvent(errorNameHolder: ''));
              }
            },
            cursorColor: ColorsSdk.mainBrand,
            style: Fonts.regular(),
            textInputAction: TextInputAction.next,
            decoration: inputTextFieldDecoration(
                isError: hasError,
                text: holderName(),
                isFocused: focusNode.hasFocus,
                afterClearText: () => addState(context, const NameHolderDataEvent(nameHolder: '')),
                controller: controller),
          )
      );
  }
}
*/