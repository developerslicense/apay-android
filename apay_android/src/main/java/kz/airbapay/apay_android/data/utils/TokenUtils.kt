package kz.airbapay.apay_android.data.utils

import android.util.Log
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

fun tokenGenerate(): String {
    val jwt = Jwts.builder()
        .claim("emailId", "test123@gmail.com")
        .claim("emailIdOTP", "123456")
        .claim("phoneNo", "1111111111")
        .signWith(SignatureAlgorithm.HS256, "secret".toByteArray())
        .compact()
    Log.v("JWT : - ", jwt)

    return jwt
}