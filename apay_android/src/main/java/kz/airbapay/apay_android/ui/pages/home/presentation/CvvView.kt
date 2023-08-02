package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import kz.airbapay.apay_android.data.constant.RegexConst
import kz.airbapay.apay_android.data.constant.cvv
import kz.airbapay.apay_android.data.utils.messageLog
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.ViewEditText

@Composable
internal fun CvvView(
    cvvText: MutableState<TextFieldValue>,
    cvvError: MutableState<String?>,
    cvvFocusRequester: FocusRequester,
    emailFocusRequester: FocusRequester?,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current

    ViewEditText(
        text = cvvText,
        regex = Regex(RegexConst.NOT_DIGITS),
        mask = "AAA",
        errorTitle = cvvError,
        focusRequester = cvvFocusRequester,
        placeholder = cvv(),
        keyboardActions = KeyboardActions(
            onNext = {
                emailFocusRequester?.requestFocus()
            },
            onDone = {
                focusManager.clearFocus(true)
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.NumberPassword,
            imeAction = if (emailFocusRequester == null) ImeAction.Done else ImeAction.Next
        ),
        modifierRoot = modifier,
        actionOnTextChanged = {
            messageLog("aaaaaaaaaa |$it|")
        },
        visualTransformation = PasswordVisualTransformation()
    )

}

/*
class CVVEditTextWidget extends StatelessWidget {
  const CVVEditTextWidget({required this.focusNode, required this.focusNodeEmail, super.key});

  final FocusNode focusNode;
  final FocusNode focusNodeEmail;

  @override
  Widget build(BuildContext context) {
    final TextEditingController controller = TextEditingController(text: readState(context).cvv);

    return BlocBuilder<HomeBloc, HomeState>(
      builder: (context, state) {
        return _initTextContainer(controller, context);
      },
    );
  }

  Container _initTextContainer(
      TextEditingController controller,
      BuildContext context) {
    bool hasError = !isBlank(readState(context).cvvState?.error);

    return Container(
              padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
              margin: const EdgeInsets.only(right: 16, left: 16),
              decoration: initEditTextBoxDecoration(hasError, focusNode),
              child: TextFormField(
                focusNode: focusNode,
                keyboardType: TextInputType.number,
                controller: controller,
                obscureText: true,
                enableSuggestions: false,
                autocorrect: false,
                maxLength: 3,
                onFieldSubmitted: (v){
                  FocusScope.of(context).requestFocus(focusNodeEmail);
                },
                onChanged: (value) {

                  addState(context, CvvDataEvent(cvv: isBlank(value) ? '' : value));

                  if (!isBlank(readState(context).cardNumberState?.error)) {
                    addState(context, const CvvEvent(errorCvv: ''));
                  }
                },
                cursorColor: ColorsSdk.colorBrandMain,
                style: Fonts.regular(),
                textInputAction: TextInputAction.next,
                decoration: inputTextFieldDecoration(
                    text: cvv(),
                    isFocused: focusNode.hasFocus,
                    controller: controller,
                    isError: hasError,
                    afterClearText: () => addState(context, const CvvDataEvent(cvv: '')),
                    prefix: InkWell(
                        onTap: () {
                          _onClickShowCvvInfo(context);
                        },
                        child: Padding(
                            padding: const EdgeInsets.only(top: 13, bottom: 13, right: 10),
                            child: SvgPicture.asset('assets/images/Hint.svg')
                        )),
                )));
  }

  void _onClickShowCvvInfo(BuildContext context) {
    showModalBottomSheet(
        context: context,
        builder: (context) {
          return Wrap(
            children: [
              Container(
                decoration: const BoxDecoration(
                    color: ColorsSdk.bgBlock,
                    borderRadius: BorderRadius.vertical(top: Radius.circular(16.0)),
                ),
                child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(top: 11, bottom: 16),
                        child: SvgPicture.asset('assets/images/line_gray.svg')
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Padding(
                              padding: EdgeInsets.only(top: 6, bottom: 6, right: 40),
                          ),
                          Text(
                              cvv(),
                              style: Fonts.subtitleBold(),
                          ),
                          InkWell(
                            onTap: () {
                              Navigator.of(context).pop();
                            },
                            child: Padding(
                              padding: const EdgeInsets.only(top: 6, bottom: 6, right: 16),
                              child: SvgPicture.asset('assets/images/cancel.svg')
                          ))
                        ],
                      ),
                      const Divider(
                        height: 1,
                        color: ColorsSdk.gray10,
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top: 22, bottom: 32, right: 16, left: 16),
                        child: Text(
                            cvvInfo(),
                            style: Fonts.regular(),
                          )
                      )
                    ]
                ),
              )
            ]
          );
        });
  }
*/