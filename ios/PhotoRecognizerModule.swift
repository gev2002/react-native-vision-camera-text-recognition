import Foundation
import UIKit
import React
import MLKitVision
import MLKitTextRecognition

@objc(PhotoRecognizerModule)
class PhotoRecognizerModule: NSObject {

    private static let options = TextRecognizerOptions()
    private let textRecognizer = TextRecognizer.textRecognizer(options:options)
    private var data: [String: Any] = [:]

    @objc(process:withResolver:withRejecter:)
    private func process(uri: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
            let image =  UIImage(contentsOfFile: uri)
            if image != nil {
                do {
                    let visionImage = VisionImage(image: image!)
                    visionImage.orientation = .up
                    let result = try textRecognizer.results(in: visionImage)
                    let blocks = VisionCameraTextRecognition.processBlocks(blocks: result.blocks)
                    data["resultText"] = result.text
                    data["blocks"] = blocks
                    if result.text.isEmpty {
                        resolve([:])
                    }else{
                        resolve(data)
                    }
                }catch{
                    reject("Error","Processing Image",nil)
                }
            }else{
                reject("Error","Can't Find Photo",nil)
            }
    }
}
