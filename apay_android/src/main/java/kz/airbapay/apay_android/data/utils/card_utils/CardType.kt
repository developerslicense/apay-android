package kz.airbapay.apay_android.data.utils.card_utils

import kz.airbapay.apay_android.R

internal enum class CardType(
  val icon: Int?
) {
  MIR(R.drawable.mir),
  MASTER_CARD(R.drawable.master_card),
  MAESTRO(R.drawable.maestro_card),
  VISA(R.drawable.visa),
  AMERICAN_EXPRESS(R.drawable.american_express),
  CHINA_UNION_PAY(R.drawable.union_pay),
  INVALID(null);
}