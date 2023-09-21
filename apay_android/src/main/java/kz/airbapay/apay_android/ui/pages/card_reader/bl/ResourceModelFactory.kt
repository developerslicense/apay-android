package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.content.Context
import kz.airbapay.apay_android.R
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

internal class ResourceModelFactory {
    @Throws(IOException::class)
    fun loadFindFourFile(context: Context): MappedByteBuffer {
        return loadModelFromResource(context, R.raw.findfour)
    }

    @Throws(IOException::class)
    fun loadRecognizeDigitsFile(context: Context): MappedByteBuffer {
        return loadModelFromResource(context, R.raw.fourrecognize)
    }

    @Throws(IOException::class)
    private fun loadModelFromResource(context: Context, resource: Int): MappedByteBuffer {
        val fileDescriptor = context.resources.openRawResourceFd(resource)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        val result = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        inputStream.close()
        fileDescriptor.close()
        return result
    }

    companion object {
        var instance: ResourceModelFactory? = null
            get() {
                if (field == null) {
                    field = ResourceModelFactory()
                }
                return field
            }
            private set
    }
}