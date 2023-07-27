package kz.airbapay.apay_android.ui.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun LoadImageSrc(
    imageSrc: Int?,
    circularImage: Boolean = false,
    modifier: Modifier = Modifier,
    contentScaleStandard: ContentScale = ContentScale.Crop,
    imageBackgroundColor: Color? = null
) {
    imageSrc?.let {
        LoadImageUrl(
            null,
            circularImage,
            imageSrc,
            imageSrc,
            modifier,
            contentScaleStandard,
            imageBackgroundColor
        )
    }
}

@Composable
fun LoadImageUrl(
    imageUrl: String?,
    circularImage: Boolean = false,
    progressImageSrc: Int? /*= R.drawable.placeholder_product*/,
    errorImageSrc: Int? /*= R.drawable.placeholder_product*/,
    modifier: Modifier = Modifier,
    contentScaleStandard: ContentScale = ContentScale.Crop,
    imageBackgroundColor: Color? = null
) {
    if (circularImage) {
        Card(
            shape = RoundedCornerShape(90.dp),
            elevation = 0.1.dp,
            modifier = modifier
        ) {
            CoilImage(
                imageModel = imageUrl,
                contentScale = ContentScale.Fit,
                colorFilter = imageBackgroundColor?.let { ColorFilter.tint(it) },
                placeHolder = progressImageSrc?.let { painterResource(progressImageSrc) },
                error = errorImageSrc?.let { painterResource(errorImageSrc) },
                modifier = Modifier.fillMaxSize()
            )
        }

    } else {
        CoilImage(
            imageModel = imageUrl,
            contentScale = contentScaleStandard,
            colorFilter = imageBackgroundColor?.let { ColorFilter.tint(it) },
            placeHolder = progressImageSrc?.let { painterResource(progressImageSrc) },
            error = errorImageSrc?.let { painterResource(errorImageSrc) },
            modifier = modifier
        )
    }
}



