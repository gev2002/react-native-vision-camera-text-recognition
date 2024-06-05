package com.visioncameratextrecognition

import android.annotation.SuppressLint
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
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.*
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mrousavy.camera.frameprocessors.Frame
import com.mrousavy.camera.frameprocessors.FrameProcessorPlugin
import com.mrousavy.camera.frameprocessors.VisionCameraProxy
import java.util.HashMap


enum class Mode {
    translate,
    recognize
}


class VisionCameraTextRecognitionModule(proxy: VisionCameraProxy, options: Map<String, Any>?) :
    FrameProcessorPlugin() {

    private val conditions = DownloadConditions.Builder().requireWifi().build()
    private var translatorOptions = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.GERMAN)
        .build()
    private val translator = Translation.getClient(translatorOptions)
    private var mode: Mode = Mode.recognize
    private var translatedText: String = ""
    private var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val latinOptions = TextRecognizerOptions.DEFAULT_OPTIONS
    private val chineseOptions = ChineseTextRecognizerOptions.Builder().build()
    private val devanagariOptions = DevanagariTextRecognizerOptions.Builder().build()
    private val japaneseOptions = JapaneseTextRecognizerOptions.Builder().build()
    private val koreanOptions = KoreanTextRecognizerOptions.Builder().build()


    init {
        val language = options?.get("language")
        recognizer = when (language) {
            "latin" -> TextRecognition.getClient(latinOptions)
            "chinese" -> TextRecognition.getClient(chineseOptions)
            "devanagari" -> TextRecognition.getClient(devanagariOptions)
            "japanese" -> TextRecognition.getClient(japaneseOptions)
            "korean" -> TextRecognition.getClient(koreanOptions)
            else -> TextRecognition.getClient(latinOptions)
        }
        val modeFromTS = options?.get("mode") as? String ?: ""
        if (modeFromTS == "translate") {
            this.mode = Mode.translate
            val from = options?.get("from") as? String ?: ""
            val to = options?.get("to") as? String ?: ""

            val sourceLanguage = translateLanguage(from)
            val targetLanguage = translateLanguage(to)

            if (sourceLanguage != null && targetLanguage != null) {
                translatorOptions = TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguage)
                    .setTargetLanguage(targetLanguage)
                    .build()
            } else {
                println("Invalid language strings provided for translation.")
            }
        } else if (modeFromTS == "recognize") {
            this.mode = Mode.recognize
        }

    }

    private fun downloadModel(completion: (Boolean) -> Unit) {
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                completion(true)
            }
            .addOnFailureListener {
                completion(false)
            }
    }

    private fun translatorFunction(text: String, completion: (String) -> Unit) {
        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                completion(translatedText)
            }
            .addOnFailureListener {
                completion("")
            }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun callback(frame: Frame, arguments: Map<String, Any>?): HashMap<String, Any>? {

        val result = WritableNativeMap()
        val mediaImage: Image = frame.image
        val image =
            InputImage.fromMediaImage(mediaImage, frame.imageProxy.imageInfo.rotationDegrees)
        val task: Task<Text> = recognizer.process(image)
        try {
            val text: Text = Tasks.await<Text>(task)
            if (mode == Mode.translate) {
                downloadModel { isDownloaded ->
                    if (isDownloaded) {
                        translatorFunction(text = text.text) { translatedText ->
                            this.translatedText = translatedText
                        }
                    } else {
                        println("Model not downloaded.")
                    }
                }
            }

            if (this.mode == Mode.translate) {
                result.putString("text", this.translatedText)
            } else {
                result.putString("text", text.text)
            }
            result.putArray("blocks", getBlockArray(text.textBlocks))
        } catch (e: Exception) {
            return null
        }
        val data = WritableNativeMap()
        data.putMap("result", result)
        return data.toHashMap()
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

    private fun getRecognizedLanguages(recognizedLanguage: String): WritableNativeArray {
        val recognizedLanguages = WritableNativeArray()
        recognizedLanguages.pushString(recognizedLanguage)
        return recognizedLanguages
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

    private fun getLineArray(lines: MutableList<Text.Line>): WritableNativeArray {
        val lineArray = WritableNativeArray()

        for (line in lines) {
            val lineMap = WritableNativeMap()

            lineMap.putString("text", line.text)
            lineMap.putArray("recognizedLanguages", getRecognizedLanguages(line.recognizedLanguage))
            lineMap.putArray("cornerPoints", line.cornerPoints?.let { getCornerPoints(it) })
            lineMap.putMap("frame", getFrame(line.boundingBox))
            lineMap.putArray("elements", getElementArray(line.elements))

            lineArray.pushMap(lineMap)
        }
        return lineArray
    }

    private fun getElementArray(elements: MutableList<Text.Element>): WritableNativeArray {
        val elementArray = WritableNativeArray()

        for (element in elements) {
            val elementMap = WritableNativeMap()

            elementMap.putString("text", element.text)
            elementMap.putArray("cornerPoints", element.cornerPoints?.let { getCornerPoints(it) })
            elementMap.putMap("frame", getFrame(element.boundingBox))
        }
        return elementArray
    }

    private fun getBlockArray(blocks: MutableList<Text.TextBlock>): WritableNativeArray {
        val blockArray = WritableNativeArray()

        for (block in blocks) {
            val blockMap = WritableNativeMap()

            blockMap.putString("text", block.text)
            blockMap.putArray(
                "recognizedLanguages",
                getRecognizedLanguages(block.recognizedLanguage)
            )
            blockMap.putArray("cornerPoints", block.cornerPoints?.let { getCornerPoints(it) })
            blockMap.putMap("frame", getFrame(block.boundingBox))
            blockMap.putArray("lines", getLineArray(block.lines))

            blockArray.pushMap(blockMap)
        }
        return blockArray
    }


}




