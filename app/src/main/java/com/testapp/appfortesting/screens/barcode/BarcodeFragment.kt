package com.testapp.appfortesting.screens.barcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.Gravity
import com.google.zxing.WriterException
import com.google.zxing.BarcodeFormat
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.fragment_barcode.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.MultiFormatWriter
import com.google.zxing.EncodeHintType
import com.testapp.appfortesting.R
import net.glxn.qrgen.android.QRCode
import java.util.*


class BarcodeFragment : Fragment() {

    companion object {
        fun newInstance() = BarcodeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // barcode data
        val barcode_data = "some weird shit is happening in my life right now"

        // barcode image
        val bitmap = QRCode.from(barcode_data).withSize(1000, 1000).bitmap()
        ivBarcode.setImageBitmap(bitmap)

        //barcode text
        tvBarcode.gravity = Gravity.CENTER_HORIZONTAL
        tvBarcode.text = barcode_data

    }

    private val WHITE = -0x1
    private val BLACK = -0x1000000

    @Throws(WriterException::class)
    fun encodeAsBitmap(contents: String?, format: BarcodeFormat, img_width: Int, img_height: Int): Bitmap? {
        if (contents == null) {
            return null
        }
        var hints: MutableMap<EncodeHintType, Any>? = null
        val encoding = guessAppropriateEncoding(contents)
        if (encoding != null) {
            hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = encoding
        }
        val writer = MultiFormatWriter()
        val result: BitMatrix
        try {
            result = writer.encode(contents, format, img_width, img_height, hints)
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }

        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result.get(x, y)) BLACK else WHITE
            }
        }

        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun guessAppropriateEncoding(contents: CharSequence): String? {
        // Very crude at the moment
        for (i in 0 until contents.length) {
            if (contents[i].toInt() > 0xFF) {
                return "UTF-8"
            }
        }
        return null
    }
}