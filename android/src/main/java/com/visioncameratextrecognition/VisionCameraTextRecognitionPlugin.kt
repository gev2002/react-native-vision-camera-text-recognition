package com.visioncameratextrecognition

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.media.Image
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mrousavy.camera.frameprocessors.Frame
import com.mrousavy.camera.frameprocessors.FrameProcessorPlugin
import com.mrousavy.camera.frameprocessors.VisionCameraProxy

class VisionCameraTextRecognitionPlugin(proxy: VisionCameraProxy, options: Map<String, Any>?) :
    FrameProcessorPlugin() {

    private var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var scanRegion: Map<*, *>? = null
    private val latinOptions = TextRecognizerOptions.DEFAULT_OPTIONS
    private val chineseOptions = ChineseTextRecognizerOptions.Builder().build()
    private val devanagariOptions = DevanagariTextRecognizerOptions.Builder().build()
    private val japaneseOptions = JapaneseTextRecognizerOptions.Builder().build()
    private val koreanOptions = KoreanTextRecognizerOptions.Builder().build()

    init {
        val language = options?.get("language").toString()
        scanRegion =  options?.get("scanRegion") as Map<*, *>?
        recognizer = when (language) {
            "latin" -> TextRecognition.getClient(latinOptions)
            "chinese" -> TextRecognition.getClient(chineseOptions)
            "devanagari" -> TextRecognition.getClient(devanagariOptions)
            "japanese" -> TextRecognition.getClient(japaneseOptions)
            "korean" -> TextRecognition.getClient(koreanOptions)
            else -> TextRecognition.getClient(latinOptions)
        }
    }

    override fun callback(frame: Frame, arguments: Map<String, Any>?): HashMap<String, Any>? {
        val data = WritableNativeMap()
        var image: InputImage? = null
        if (scanRegion != null) {
            var bm: Bitmap? = BitmapUtils.getBitmap(frame)
            if (bm === null) return null
            val left = (scanRegion!!["left"] as Double) / 100.0 * bm.width
            val top = (scanRegion!!["top"] as Double) / 100.0 * bm.height
            val width = (scanRegion!!["width"] as Double) / 100.0 * bm.width
            val height = (scanRegion!!["height"] as Double) / 100.0 * bm.height
            bm = Bitmap.createBitmap(
                bm,
                left.toInt(),
                top.toInt(),
                width.toInt(),
                height.toInt(),
                null,
                false
            )
            image = InputImage.fromBitmap(bm,frame.imageProxy.imageInfo.rotationDegrees);
        } else {
            val mediaImage: Image = frame.image
            image = InputImage.fromMediaImage(mediaImage, frame.imageProxy.imageInfo.rotationDegrees)
        }
        val task: Task<Text> = recognizer.process(image)
        try {
            val text: Text = Tasks.await(task)
            if (text.text.isEmpty()) {
                return WritableNativeMap().toHashMap()
            }
            data.putString("resultText", text.text)
            data.putArray("blocks", getBlocks(text.textBlocks))
            return data.toHashMap()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        fun getBlocks(blocks: MutableList<Text.TextBlock>): WritableNativeArray {
            val blockArray = WritableNativeArray()
            blocks.forEach { block ->
                val blockMap = WritableNativeMap().apply {
                    putString("blockText", block.text)
                    putArray("blockCornerPoints", block.cornerPoints?.let { getCornerPoints(it) })
                    putMap("blockFrame", getFrame(block.boundingBox))
                    putArray("lines", getLines(block.lines))
                }
                blockArray.pushMap(blockMap)
            }
            return blockArray
        }

        private fun getLines(lines: MutableList<Text.Line>): WritableNativeArray {
            val lineArray = WritableNativeArray()
            lines.forEach { line ->
                val lineMap = WritableNativeMap().apply {
                    putString("lineText", line.text)
                    putArray("lineCornerPoints", line.cornerPoints?.let { getCornerPoints(it) })
                    putMap("lineFrame", getFrame(line.boundingBox))
                    putArray(
                        "lineLanguages",
                        WritableNativeArray().apply { pushString(line.recognizedLanguage) })
                    putArray("elements", getElements(line.elements))
                }
                lineArray.pushMap(lineMap)
            }
            return lineArray
        }

        private fun getElements(elements: MutableList<Text.Element>): WritableNativeArray {
            val elementArray = WritableNativeArray()
            elements.forEach { element ->
                val elementMap = WritableNativeMap().apply {
                    putString("elementText", element.text)
                    putArray(
                        "elementCornerPoints",
                        element.cornerPoints?.let { getCornerPoints(it) })
                    putMap("elementFrame", getFrame(element.boundingBox))
                }
                elementArray.pushMap(elementMap)
            }
            return elementArray
        }

        private fun getCornerPoints(points: Array<Point>): WritableNativeArray {
            val cornerPoints = WritableNativeArray()
            points.forEach { point ->
                cornerPoints.pushMap(WritableNativeMap().apply {
                    putInt("x", point.x)
                    putInt("y", point.y)
                })
            }
            return cornerPoints
        }

        private fun getFrame(boundingBox: Rect?): WritableNativeMap {
            return WritableNativeMap().apply {
                boundingBox?.let {
                    putDouble("x", it.exactCenterX().toDouble())
                    putDouble("y", it.exactCenterY().toDouble())
                    putInt("width", it.width())
                    putInt("height", it.height())
                    putInt("boundingCenterX", it.centerX())
                    putInt("boundingCenterY", it.centerY())
                }
            }
        }
    }
}


