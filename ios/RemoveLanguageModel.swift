import Foundation
import React
import MLKitTranslate

@objc(RemoveLanguageModel)
class RemoveLanguageModel: NSObject {

  @objc(remove:withResolver:withRejecter:)
  private func remove(name: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
      guard let modelName = TranslateLanguage(from: name) else {
          resolve(false)
          return
      }
      let model = TranslateRemoteModel.translateRemoteModel(language: modelName)
      ModelManager.modelManager().deleteDownloadedModel(model) { error in
          guard error == nil else {
              return
          }
          resolve(true)
      }
  }
}





