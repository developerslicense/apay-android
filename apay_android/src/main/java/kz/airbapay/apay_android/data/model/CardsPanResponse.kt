package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class CardsPanResponse(

	@SerializedName("bank_code")
	val bankCode: String? = null,

	@SerializedName("type")
	val type: String? = null
)

/* на всякий случай, если скажут добавить иконки банков
{
   "id": "6464c11fcc040f6465a6588d",
   "bank_code": "kaspibank",
   "bin": "411111",
   "type": "VISA"
  },
  {
   "id": "64e46fd01214b4b69b60ba67",
   "bank_code": "jysanbank",
   "bin": "539545",
   "type": "MC"
  },
  {
   "id": "64e4707b1214b4b69b60ba68",
   "bank_code": "centercredit",
   "bin": "626405",
   "type": "UPI"
  },
  {
   "id": "64e47088b45d2f8513a29185",
   "bank_code": "centercredit",
   "bin": "401550",
   "type": "VISA"
  },
  {
   "id": "64e4709d1214b4b69b60ba69",
   "bank_code": "halykbank",
   "bin": "623424",
   "type": "UPI"
  },
  {
   "id": "64e470a9b45d2f8513a29186",
   "bank_code": "halykbank",
   "bin": "377514",
   "type": "AE"
  },
  {
   "id": "64e470b11214b4b69b60ba6a",
   "bank_code": "halykbank",
   "bin": "676205",
   "type": "MC"
  },
  {
   "id": "64e470c0b45d2f8513a29187",
   "bank_code": "halykbank",
   "bin": "440257",
   "type": "VISA"
  }*/