package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.email
import kz.airbapay.apay_android.data.model.EditTextDVO
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.EditTextViewModel
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.ViewEditTextSimple

@Composable
internal fun EmailView() {
    val editTextDVO = EditTextDVO(
        placeholder = email(),
        keyboardActions = KeyboardActions(
            onNext = {

            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = false,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
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

/*
*
class EmailEditTextWidget extends StatelessWidget {
  const EmailEditTextWidget({required this.focusNode, super.key});

  final FocusNode focusNode;

  @override
  Widget build(BuildContext context) {
    final TextEditingController controller = TextEditingController(text: readState(context).email);

    return BlocBuilder<HomeBloc, HomeState>(
      builder: (context, state) {
        return _initTextContainer(controller, state, context);
      },
    );
  }

  Container _initTextContainer(
      TextEditingController controller,
      HomeState state,
      BuildContext context) {

    bool hasError = !isBlank(readState(context).emailState?.error);

    return Container(
          padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
          margin: const EdgeInsets.only(right: 16, left: 16),
          decoration: initEditTextBoxDecoration(hasError, focusNode),
          child: TextField(
            focusNode: focusNode,
            keyboardType: TextInputType.emailAddress,
            onChanged: (value) {
              addState(context, EmailDataEvent(email: isBlank(value) ? '' : value));

              if (!isBlank(readState(context).cardNumberState?.error)) {
                addState(context, const EmailEvent(errorEmail: '', switched: true));
              }
            },
            controller: controller,
            cursorColor: ColorsSdk.colorBrandMain,
            style: Fonts.regular(),
            textInputAction: TextInputAction.done,
            decoration: inputTextFieldDecoration(
                text: email(),
                isError: hasError,
                isFocused: focusNode.hasFocus,
                afterClearText: () => addState(context, const EmailDataEvent(email: '')),
                controller: controller
            )
          ));
  }
}
*/