package com.visioncameratextrecognition

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel

class RemoveLanguageModel(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    @ReactMethod
    fun remove(name: String, promise: Promise) {
        val modelName = translateLanguage(name)?.let { TranslateRemoteModel.Builder(it).build() }
        if (modelName != null) {
            modelManager.deleteDownloadedModel(modelName)
                .addOnSuccessListener {
                    promise.resolve(true)
                }
                .addOnFailureListener {
                    promise.resolve(false)
                }
        } else {
            promise.resolve(false)
        }
    }

    private val modelManager = RemoteModelManager.getInstance()
    override fun getName(): String {
        return NAME
    }

    companion object {
        const val NAME = "RemoveLanguageModel"
    }

}