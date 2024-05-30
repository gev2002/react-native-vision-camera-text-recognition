import Foundation
import VisionCamera
import MLKitVision
import MLKitTextRecognition
import MLKitTextRecognitionChinese
import MLKitTextRecognitionDevanagari
import MLKitTextRecognitionJapanese
import MLKitTextRecognitionKorean
import MLKitCommon

@objc(VisionCameraTextRecognition)
public class VisionCameraTextRecognition: FrameProcessorPlugin {

    private var textRecognizer  = TextRecognizer()
    private static let latinOptions = TextRecognizerOptions()
    private static let chineseOptions = ChineseTextRecognizerOptions()
    private static let devanagariOptions = DevanagariTextRecognizerOptions()
    private static let japaneseOptions = JapaneseTextRecognizerOptions()
    private static let koreanOptions = KoreanTextRecognizerOptions()


    public override init(proxy: VisionCameraProxyHolder, options: [AnyHashable: Any]! = [:]) {
        super.init(proxy: proxy, options: options)
        let language = options["language"]! as! String
        if language == "latin" {
            self.textRecognizer =  TextRecognizer.textRecognizer(options:VisionCameraTextRecognition.latinOptions)
        }else if language == "chinese"{
            self.textRecognizer =  TextRecognizer.textRecognizer(options:VisionCameraTextRecognition.chineseOptions)
        }else if language == "devanagari"{
            self.textRecognizer = TextRecognizer.textRecognizer(options:VisionCameraTextRecognition.devanagariOptions)
        }else if language == "japanese"{
            self.textRecognizer = TextRecognizer.textRecognizer(options:VisionCameraTextRecognition.japaneseOptions)
        }else if language == "korean"{
            self.textRecognizer = TextRecognizer.textRecognizer(options:VisionCameraTextRecognition.koreanOptions)
        }
        else {
            self.textRecognizer =  TextRecognizer.textRecognizer(options:VisionCameraTextRecognition.latinOptions)
        }
    }

    public override func callback(_ frame: Frame, withArguments arguments: [AnyHashable: Any]?) -> Any {
                let buffer = frame.buffer
        let image = VisionImage(buffer: buffer)
        image.orientation = getOrientation(orientation: frame.orientation)

        var result: Text
        do {
            result = try self.textRecognizer
                .results(in: image)
        } catch let error {
            print("Failed to recognize text with error: \(error.localizedDescription).")
            return []
        }


        return [
            "result": [
            "text": result.text,
            "blocks": getBlockArray(result.blocks),
    ]
    ]
    }


    private func getBlockArray(_ blocks: [TextBlock]) -> [[String: Any]] {

        var blockArray: [[String: Any]] = []

        for block in blocks {
            blockArray.append([
                "text": block.text,
                "recognizedLanguages": getRecognizedLanguages(block.recognizedLanguages),
                "cornerPoints": getCornerPoints(block.cornerPoints),
                "frame": getFrame(block.frame),
                "lines": getLineArray(block.lines),
        ])
        }

        return blockArray
    }

    private func getLineArray(_ lines: [TextLine]) -> [[String: Any]] {

        var lineArray: [[String: Any]] = []

        for line in lines {
            lineArray.append([
                "text": line.text,
                "recognizedLanguages": getRecognizedLanguages(line.recognizedLanguages),
                "cornerPoints": getCornerPoints(line.cornerPoints),
                "frame": getFrame(line.frame),
                "elements": getElementArray(line.elements),
        ])
        }

        return lineArray
    }


    private func getElementArray(_ elements: [TextElement]) -> [[String: Any]] {

        var elementArray: [[String: Any]] = []

        for element in elements {
            elementArray.append([
                "text": element.text,
                "cornerPoints": getCornerPoints(element.cornerPoints),
                "frame": getFrame(element.frame),
        ])
        }

        return elementArray
    }

    private func getRecognizedLanguages(_ languages: [TextRecognizedLanguage]) -> [String] {

        var languageArray: [String] = []

        for language in languages {
            guard let code = language.languageCode else {
                print("No language code exists")
                break;
            }
            languageArray.append(code)
        }

        return languageArray
    }


    private func getCornerPoints(_ cornerPoints: [NSValue]) -> [[String: CGFloat]] {

        var cornerPointArray: [[String: CGFloat]] = []

        for cornerPoint in cornerPoints {
            guard let point = cornerPoint as? CGPoint else {
                print("Failed to convert corner point to CGPoint")
                break;
            }
            cornerPointArray.append([ "x": point.x, "y": point.y])
        }

        return cornerPointArray
    }

    private func getFrame(_ frameRect: CGRect) -> [String: CGFloat] {

        let offsetX = (frameRect.midX - ceil(frameRect.width)) / 2.0
        let offsetY = (frameRect.midY - ceil(frameRect.height)) / 2.0

        let x = frameRect.maxX + offsetX
        let y = frameRect.minY + offsetY

        return [
            "x": frameRect.midX + (frameRect.midX - x),
            "y": frameRect.midY + (y - frameRect.midY),
            "width": frameRect.width,
            "height": frameRect.height,
            "boundingCenterX": frameRect.midX,
            "boundingCenterY": frameRect.midY
    ]
    }

    private func getOrientation(orientation: UIImage.Orientation) -> UIImage.Orientation {
        switch orientation {
            case .right, .leftMirrored:
                return .up
            case .left, .rightMirrored:
                return .down
            case .up, .downMirrored:
                return .left
            case .down, .upMirrored:
                return .right
            default:
                return .up
        }
    }
}
