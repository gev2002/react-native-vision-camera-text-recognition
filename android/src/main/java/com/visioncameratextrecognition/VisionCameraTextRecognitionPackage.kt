package com.visioncameratextrecognition

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.bridge.NativeModule
import com.mrousavy.camera.frameprocessors.FrameProcessorPluginRegistry

class VisionCameraTextRecognitionPackage : ReactPackage {
    companion object {
        init {
            FrameProcessorPluginRegistry.addFrameProcessorPlugin("scanText") { proxy, options ->
                VisionCameraTextRecognitionPlugin(proxy, options)
            }
            FrameProcessorPluginRegistry.addFrameProcessorPlugin("translate") { proxy, options ->
                VisionCameraTranslatorPlugin(proxy, options)
            }
        }
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
            return listOf(
                 RemoveLanguageModel(reactContext),
                 PhotoRecognizerModule(reactContext)
             )
    }
}

