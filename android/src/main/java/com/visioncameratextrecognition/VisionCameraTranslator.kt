package com.visioncameratextrecognition

import android.media.Image
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mrousavy.camera.core.types.Orientation
import com.mrousavy.camera.frameprocessors.Frame
import com.mrousavy.camera.frameprocessors.FrameProcessorPlugin
import com.mrousavy.camera.frameprocessors.VisionCameraProxy

class VisionCameraTranslator(proxy: VisionCameraProxy, options: Map<String, Any>?) :
    FrameProcessorPlugin() {

    private val conditions = DownloadConditions.Builder().requireWifi().build()
    private var translatorOptions = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.GERMAN)
        .build()
    private val translator = Translation.getClient(translatorOptions)
    private var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val ORIENTATIONS = SparseIntArray()
    private var from = ""
    private var to = ""
    private var models = emptyArray<String>()
    private var isDownloaded: Boolean = false
    private val modelManager = RemoteModelManager.getInstance()
    private var translatedText = ""
    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
        this.from = options?.get("from") as? String ?: ""
        this.to = options?.get("to") as? String ?: ""
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
        downloadModel()
    }

    override fun callback(frame: Frame, params: MutableMap<String, Any>?) : Any? {
        val mediaImage: Image = frame.image
        val rotation = getOrientation(frame.orientation)
        val image = InputImage.fromMediaImage(mediaImage, rotation)
        val task: Task<Text> = recognizer.process(image)
        try {
            val resultText: String = Tasks.await<Text>(task).text
            Log.d("TEXT", "TEXT $resultText")
            if (this.isDownloaded && resultText.isNotEmpty()) {
                this.translateText(resultText)
            }
            if (resultText.isEmpty()){
                this.translatedText = ""
            }
            return this.translatedText
        }catch (e:Exception){
            Log.e("ERROR", "$e")
            return null
        }
    }

    private fun translateText(text: String) {
      //  this.translatedText = ""
        translator.translate(text)
            .addOnSuccessListener {
                this.translatedText = it
            }
            .addOnFailureListener {
                Log.e("ERROR", "$it")
                this.translatedText = ""
            }
    }


    private fun getOrientation(
        orientation: Orientation
    ): Int {
        return when (orientation) {
            Orientation.PORTRAIT -> 0
            Orientation.LANDSCAPE_LEFT -> 90
            Orientation.PORTRAIT_UPSIDE_DOWN -> 180
            Orientation.LANDSCAPE_RIGHT -> 270
        }
    }
    private fun downloadModel(){
        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener {
                it.forEach() { language ->
                    models.plus(language.language)
                }
                if (models.contains(from) && models.contains(to)) {
                    isDownloaded = true
                }
            }
            .addOnFailureListener {
                this.isDownloaded = false
            }

        if (!isDownloaded){
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    this.isDownloaded = true
                }
                .addOnFailureListener {
                    this.isDownloaded = false
                }
        }
    }
}
