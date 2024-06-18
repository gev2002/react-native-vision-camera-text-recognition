package com.visioncameratextrecognition

import android.graphics.Point
import android.graphics.Rect
import android.media.Image
import android.util.SparseIntArray
import android.view.Surface
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
import com.mrousavy.camera.core.types.Orientation
import com.mrousavy.camera.frameprocessors.Frame
import com.mrousavy.camera.frameprocessors.FrameProcessorPlugin
import com.mrousavy.camera.frameprocessors.VisionCameraProxy
import java.util.HashMap

class VisionCameraTextRecognitionModule(proxy: VisionCameraProxy, options: Map<String, Any>?) : FrameProcessorPlugin() {

    private var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val latinOptions = TextRecognizerOptions.DEFAULT_OPTIONS
    private val chineseOptions = ChineseTextRecognizerOptions.Builder().build()
    private val devanagariOptions = DevanagariTextRecognizerOptions.Builder().build()
    private val japaneseOptions = JapaneseTextRecognizerOptions.Builder().build()
    private val koreanOptions = KoreanTextRecognizerOptions.Builder().build()
    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)

        val language = options?.get("language")
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
        val mediaImage: Image? = frame.image
        if (mediaImage != null) {
            val rotation = getFrameRotation(frame.orientation)
            val image = InputImage.fromMediaImage(mediaImage, rotation)
            val task: Task<Text> = recognizer.process(image)
            try {
                val text: Text = Tasks.await(task)
                val blockArray = WritableNativeArray()
                for (block in text.textBlocks) {
                    val blockMap = WritableNativeMap()
                    data.putString("resultText", text.text)
                    val blockText = block.text
                    blockMap.putString("blockText", blockText)
                    val blockCornerPoints = block.cornerPoints?.let { getCornerPoints(it) }
                    blockMap.putArray("blockCornerPoints", blockCornerPoints)
                    val blockFrame = getFrame(block.boundingBox)
                    blockMap.putMap("blockFrame", blockFrame)
                    val blockLanguages = WritableNativeArray()
                    blockLanguages.pushString(block.recognizedLanguage)
                    blockMap.putArray("blockLanguages", blockLanguages)
                    for (line in block.lines) {
                        val lineArr = WritableNativeArray()
                        val lineMap = WritableNativeMap()
                        val lineText = line.text
                        lineMap.putString("lineText", lineText)
                        val lineCornerPoints = line.cornerPoints?.let { getCornerPoints(it) }
                        lineMap.putArray("lineCornerPoints", lineCornerPoints)
                        val lineFrame = getFrame(line.boundingBox)
                        lineMap.putMap("lineFrame", lineFrame)
                        val lineLanguages = WritableNativeArray()
                        lineLanguages.pushString(line.recognizedLanguage)
                        lineMap.putArray("lineLanguages", lineLanguages)
                        for (element in line.elements) {
                            val elementArr = WritableNativeArray()
                            val elementMap = WritableNativeMap()
                            val elementText = element.text
                            elementMap.putString("elementText", elementText)
                            val elementCornerPoints = element.cornerPoints?.let { getCornerPoints(it) }
                            elementMap.putArray("elementCornerPoints", elementCornerPoints)
                            val elementFrame = getFrame(element.boundingBox)
                            elementMap.putMap("elementFrame", elementFrame)
                            elementArr.pushMap(elementMap)
                            lineMap.putArray("elements", elementArr)
                        }
                        lineArr.pushMap(lineMap)
                        blockMap.putArray("lines", lineArr)
                    }
                    blockArray.pushMap(blockMap)
                }
                data.putArray("blocks", blockArray)
                mediaImage.close()
                return if (text.text.isEmpty()){
                 WritableNativeMap().toHashMap()
                }else {
                data.toHashMap()
                }
            } catch (e: Exception) {
                mediaImage.close()
                e.printStackTrace()
                return null
            }
        } else {
            return null
        }
    }

    private fun getCornerPoints(points: Array<Point>): WritableNativeArray {
        val cornerPoints = WritableNativeArray()
        for (point in points) {
            val pointMap = WritableNativeMap()
            pointMap.putInt("x", point.x)
            pointMap.putInt("y", point.y)
            cornerPoints.pushMap(pointMap)
        }
        return cornerPoints
    }

    private fun getFrame(boundingBox: Rect?): WritableNativeMap {
        val frame = WritableNativeMap()
        if (boundingBox != null) {
            frame.putDouble("x", boundingBox.exactCenterX().toDouble())
            frame.putDouble("y", boundingBox.exactCenterY().toDouble())
            frame.putInt("width", boundingBox.width())
            frame.putInt("height", boundingBox.height())
            frame.putInt("boundingCenterX", boundingBox.centerX())
            frame.putInt("boundingCenterY", boundingBox.centerY())
        }
        return frame
    }

    private fun getFrameRotation(orientation: Orientation): Int {
        return when (orientation) {
            Orientation.PORTRAIT -> 0
            Orientation.LANDSCAPE_LEFT -> 90
            Orientation.PORTRAIT_UPSIDE_DOWN -> 180
            Orientation.LANDSCAPE_RIGHT -> 270
        }
    }
}
