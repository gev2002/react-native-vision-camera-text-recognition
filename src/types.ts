export type {
  Frame,
  ReadonlyFrameProcessor,
  FrameProcessorPlugin,
  FrameInternal,
  CameraProps,
  CameraDevice,
} from 'react-native-vision-camera';
export type { ForwardedRef } from 'react';
import type { CameraProps, Frame } from 'react-native-vision-camera';

export type Languages =
  | 'af'
  | 'sq'
  | 'ar'
  | 'be'
  | 'bn'
  | 'bg'
  | 'ca'
  | 'zh'
  | 'cs'
  | 'da'
  | 'nl'
  | 'en'
  | 'eo'
  | 'et'
  | 'fi'
  | 'fr'
  | 'gl'
  | 'ka'
  | 'de'
  | 'el'
  | 'gu'
  | 'ht'
  | 'he'
  | 'hi'
  | 'hu'
  | 'is'
  | 'id'
  | 'ga'
  | 'it'
  | 'ja'
  | 'kn'
  | 'ko'
  | 'lv'
  | 'lt'
  | 'mk'
  | 'ms'
  | 'mt'
  | 'mr'
  | 'no'
  | 'fa'
  | 'pl'
  | 'pt'
  | 'ro'
  | 'ru'
  | 'sk'
  | 'sl'
  | 'es'
  | 'sw'
  | 'tl'
  | 'ta'
  | 'te'
  | 'th'
  | 'tr'
  | 'uk'
  | 'ur'
  | 'vi'
  | 'cy';

export type ScanRegion = {
  left: number,
  top: number,
  width: number,
  height: number,
}

export type TextRecognitionOptions = {
  language: 'latin' | 'chinese' | 'devanagari' | 'japanese' | 'korean';
  scanRegion?: ScanRegion
};

export type TranslatorOptions = {
  from: Languages;
  to: Languages;
};

export type CameraTypes = {
  callback: (data: string | Text[]) => void;
  mode: 'translate' | 'recognize';
} & CameraProps & (
  | { mode: 'recognize'; options: TextRecognitionOptions }
  | { mode: 'translate'; options: TranslatorOptions }
  );;

export type TextRecognitionPlugin = {
  scanText: (frame: Frame) => Text;
};

export type TranslatorPlugin = {
  translate: (frame: Frame) => string;
};

export type Text = {
  blocks: BlocksData[];
  resultText: string;
};

type BlocksData = {
  blockFrame: FrameType,
  blockCornerPoints: CornerPointsType,
  lines: LinesData,
  blockLanguages: string[] | [],
  blockText: string,
};

type CornerPointsType = [{ x: number; y: number }];

type FrameType = {
  boundingCenterX: number;
  boundingCenterY: number;
  height: number;
  width: number;
  x: number;
  y: number;
};

type LinesData = [
  lineCornerPoints: CornerPointsType,
  elements: ElementsData,
  lineFrame: FrameType,
  lineLanguages: string[] | [],
  lineText: string,
];

type ElementsData = [
  elementCornerPoints: CornerPointsType,
  elementFrame: FrameType,
  elementText: string,
];

export type PhotoOptions = {
  uri: string;
  orientation?:
    | 'landscapeRight'
    | 'portrait'
    | 'portraitUpsideDown'
    | 'landscapeLeft';
};
