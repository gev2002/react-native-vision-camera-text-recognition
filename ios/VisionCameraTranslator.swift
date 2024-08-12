import Foundation
import VisionCamera
import MLKitVision
import MLKitTextRecognition
import MLKitTranslate

public class VisionCameraTranslator: FrameProcessorPlugin {
    private var models: [String] = []
    private var isDownloaded: Bool = false
    private let localModels = ModelManager.modelManager().downloadedTranslateModels
    private var translator: Translator
    private var translatorOptions: TranslatorOptions
    private var text = ""
    private let conditions = ModelDownloadConditions(
        allowsCellularAccess: false,
        allowsBackgroundDownloading: true
    )
    private var from = ""
    private var to = ""
    private var textRecognizer = TextRecognizer.textRecognizer(options: TextRecognizerOptions())

    public override init(proxy: VisionCameraProxyHolder, options: [AnyHashable: Any]! = [:]) {
        let from = options["from"]  as? String ?? "en"
        let to = options["to"]  as? String ?? "de"

        self.from  = from
        self.to  = to

        let sourceLanguage = TranslateLanguage(from: self.from) ?? .english
        let targetLanguage = TranslateLanguage(from: self.to) ?? .german
        self.translatorOptions = TranslatorOptions(sourceLanguage: sourceLanguage, targetLanguage: targetLanguage)
        self.translator = Translator.translator(options: translatorOptions)
        super.init(proxy: proxy, options: options)
        downloadModel()
    }

    public override func callback(_ frame: Frame, withArguments arguments: [AnyHashable: Any]?) -> Any {
        let buffer = frame.buffer
        let image = VisionImage(buffer: buffer)

        let orientation = getOrientation(
               orientation: frame.orientation
             )
        image.orientation = orientation
        let resultText = recognizeText(image: image)!.text
        if isDownloaded && !resultText.isEmpty {
            self.translateText(text:resultText){translatedText in
                self.text = translatedText!
            }
        }
        if resultText.isEmpty {
            self.text = ""
        }
        return self.text
    }
    private func downloadModel(){
        localModels.forEach { language in
            models.append(language.language.rawValue)
            if models.contains(from) && models.contains(to) {
                self.isDownloaded = true
            }
        }
        if !isDownloaded {
            translator.downloadModelIfNeeded(with: conditions) { error in
                guard error == nil else {
                    self.isDownloaded = false
                        return
                }
                self.isDownloaded = true
            }
        }
    }

    private func recognizeText(image:VisionImage)-> Text?{
        do {
            let result = try self.textRecognizer.results(in: image)
            return result
        } catch {
            print("Failed to recognize text: \(error.localizedDescription).")
            return nil
        }

    }
    private func getOrientation(orientation: UIImage.Orientation) -> UIImage.Orientation {
        switch orientation {
        case .up:
          return .up
        case .left:
          return .right
        case .down:
          return .down
        case .right:
          return .left
        default:
          return .up
        }
    }

    private func translateText(text: String, completion: @escaping (String?) -> Void) {
        if !isDownloaded {
            print("Translation model is not downloaded.")
            completion(nil)
            return
        }
        translator.translate(text) { translatedText, error in
            guard error == nil, let translatedText = translatedText else {
                print("Translation failed: \(error?.localizedDescription ?? "Unknown error").")
                completion(nil)
                return
            }
            completion(translatedText)
        }
    }
}


