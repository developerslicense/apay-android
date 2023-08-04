package kz.airbapay.apay_android

import junit.framework.TestCase.assertEquals

internal fun isAssert(
    actual: String?,
    expected: String
) {
    assertEquals(expected, actual)
}

internal fun isAssert(
    actual: Long?,
    expected: Long
) {
    assertEquals(expected, actual)
}

internal fun isAssert(
    actual: Long?,
    expected: String
) {
    assertEquals(expected, actual)
}

internal fun isAssert(
    actual: Double?,
    expected: String
) {
    assertEquals(expected, actual)
}

internal fun isAssert(
    actual: Int?,
    expected: String
) {
    assertEquals(expected, actual)
}

internal fun isAssert(
    actual: Int?,
    expected: Int
) {
    assertEquals(expected, actual)
}

internal fun isAssert(
    actual: Boolean,
    expected: Boolean
) {
    assertEquals(expected, actual)
}