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

    private var textRecognizer = TextRecognizer()
    private var scanRegion: [String: Int]? = nil
    private static let latinOptions = TextRecognizerOptions()
    private static let chineseOptions = ChineseTextRecognizerOptions()
    private static let devanagariOptions = DevanagariTextRecognizerOptions()
    private static let japaneseOptions = JapaneseTextRecognizerOptions()
    private static let koreanOptions = KoreanTextRecognizerOptions()
    private var data: [String: Any] = [:]


    public override init(proxy: VisionCameraProxyHolder, options: [AnyHashable: Any]! = [:]) {
        super.init(proxy: proxy, options: options)
        let language = options["language"] as? String ?? "latin"
        scanRegion = options["scanRegion"] as? [String: Int]
        switch language {
        case "chinese":
            self.textRecognizer = TextRecognizer.textRecognizer(options: VisionCameraTextRecognition.chineseOptions)
        case "devanagari":
            self.textRecognizer = TextRecognizer.textRecognizer(options: VisionCameraTextRecognition.devanagariOptions)
        case "japanese":
            self.textRecognizer = TextRecognizer.textRecognizer(options: VisionCameraTextRecognition.japaneseOptions)
        case "korean":
            self.textRecognizer = TextRecognizer.textRecognizer(options: VisionCameraTextRecognition.koreanOptions)
        default:
            self.textRecognizer = TextRecognizer.textRecognizer(options: VisionCameraTextRecognition.latinOptions)
        }
    }


    public override func callback(_ frame: Frame, withArguments arguments: [AnyHashable: Any]?) -> Any {
        let buffer = frame.buffer
        var image: VisionImage?;
        do {
            if scanRegion != nil {
                guard let pixelBuffer = CMSampleBufferGetImageBuffer(buffer) else {
                    return [:]
                }
                let ciImage = CIImage(cvPixelBuffer: pixelBuffer).oriented(.right)
                let context = CIContext(options: nil)
                if let cgImage = context.createCGImage(ciImage, from: ciImage.extent) {
                    let imgWidth = Double(cgImage.width)
                    let imgHeight = Double(cgImage.height)
                    let left:Double = Double(scanRegion?["left"] ?? 0) / 100.0 * imgWidth
                    let top:Double = Double(scanRegion?["top"] ?? 0) / 100.0 * imgHeight
                    let width:Double = Double(scanRegion?["width"] ?? 100) / 100.0 * imgWidth
                    let height:Double = Double(scanRegion?["height"] ?? 100) / 100.0 * imgHeight
                    let cropRegion = CGRect(
                        x: left,
                        y: top,
                        width: width,
                        height: height
                    )
                    guard let croppedCGImage = cgImage.cropping(to: cropRegion) else {
                        return [:]
                    }
                    let uiImage = UIImage(cgImage: croppedCGImage)
                    image = VisionImage(image: uiImage)
                    print("using cropped image")
                }
            }else{
                image = VisionImage(buffer: buffer)
                image!.orientation = getOrientation(orientation: frame.orientation)
            }
            let result = try self.textRecognizer.results(in: image!)
            let blocks = VisionCameraTextRecognition.processBlocks(blocks: result.blocks)
            data["resultText"] = result.text
            data["blocks"] = blocks
            if result.text.isEmpty {
                return [:]
            }else{
                return data
            }
        } catch {
            print("Failed to recognize text: \(error.localizedDescription).")
            return [:]
        }
    }

      static func processBlocks(blocks:[TextBlock]) -> Array<Any> {
        var blocksArray : [Any] = []
        for block in blocks {
            var blockData : [String:Any] = [:]
            blockData["blockText"] = block.text
            blockData["blockCornerPoints"] = processCornerPoints(block.cornerPoints)
            blockData["blockFrame"] = processFrame(block.frame)
            blockData["lines"] = processLines(lines: block.lines)
            blocksArray.append(blockData)
        }
        return blocksArray
    }

    private static func processLines(lines:[TextLine]) -> Array<Any> {
        var linesArray : [Any] = []
        for line in lines {
            var lineData : [String:Any] = [:]
            lineData["lineText"] = line.text
            lineData["lineLanguages"] = processRecognizedLanguages(line.recognizedLanguages)
            lineData["lineCornerPoints"] = processCornerPoints(line.cornerPoints)
            lineData["lineFrame"] = processFrame(line.frame)
            lineData["elements"] = processElements(elements: line.elements)
            linesArray.append(lineData)
        }
        return linesArray
    }

    private static func processElements(elements:[TextElement]) -> Array<Any> {
        var elementsArray : [Any] = []

        for element in elements {
            var elementData : [String:Any] = [:]
              elementData["elementText"] = element.text
              elementData["elementCornerPoints"] = processCornerPoints(element.cornerPoints)
              elementData["elementFrame"] = processFrame(element.frame)

            elementsArray.append(elementData)
          }

        return elementsArray
    }

    private static func processRecognizedLanguages(_ languages: [TextRecognizedLanguage]) -> [String] {

            var languageArray: [String] = []

            for language in languages {
                guard let code = language.languageCode else {
                    print("No language code exists")
                    break;
                }
                if code.isEmpty{
                    languageArray.append("und")
                }else {
                    languageArray.append(code)

                }
            }

            return languageArray
        }

    private static func processCornerPoints(_ cornerPoints: [NSValue]) -> [[String: CGFloat]] {
        return cornerPoints.compactMap { $0.cgPointValue }.map { ["x": $0.x, "y": $0.y] }
    }

    private static func processFrame(_ frameRect: CGRect) -> [String: CGFloat] {
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
}
