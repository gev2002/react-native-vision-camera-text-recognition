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

type Languages =
  | 'afrikaans'
  | 'albanian'
  | 'arabic'
  | 'belarusian'
  | 'bengali'
  | 'bulgarian'
  | 'catalan'
  | 'chinese'
  | 'czech'
  | 'danish'
  | 'dutch'
  | 'english'
  | 'esperanto'
  | 'estonian'
  | 'finnish'
  | 'french'
  | 'galician'
  | 'georgian'
  | 'german'
  | 'greek'
  | 'gujarati'
  | 'haitianCreole'
  | 'hebrew'
  | 'hindi'
  | 'hungarian'
  | 'icelandic'
  | 'indonesian'
  | 'irish'
  | 'italian'
  | 'japanese'
  | 'kannada'
  | 'korean'
  | 'latvian'
  | 'lithuanian'
  | 'macedonian'
  | 'malay'
  | 'maltese'
  | 'marathi'
  | 'norwegian'
  | 'persian'
  | 'polish'
  | 'portuguese'
  | 'romanian'
  | 'russian'
  | 'slovak'
  | 'slovenian'
  | 'spanish'
  | 'swahili'
  | 'tagalog'
  | 'tamil'
  | 'telugu'
  | 'thai'
  | 'turkish'
  | 'ukrainian'
  | 'urdu'
  | 'vietnamese'
  | 'welsh';

interface RecognizeOptions {
  language: 'latin' | 'chinese' | 'devanagari' | 'japanese' | 'korean';
  mode: 'recognize';
}

interface TranslateOptions {
  language: 'latin' | 'chinese' | 'devanagari' | 'japanese' | 'korean';
  mode: 'translate';
  from: Languages;
  to: Languages;
}

export type TextRecognitionOptions = RecognizeOptions | TranslateOptions;

export type TextData = {
  result: {
    blocks: [] | BlocksData;
    text: string;
  };
};

type BlocksData = [
  cornerPoints: CornerPoints,
  frame: FrameType,
  lines: LinesType,
  recognizedLanguages: string[] | [],
];

type CornerPoints = [{ x: number; y: number }];

type FrameType = {
  boundingCenterX: number;
  boundingCenterY: number;
  height: number;
  width: number;
  x: number;
  y: number;
};

type LinesType = [
  cornerPoints: CornerPoints,
  elements: ElementsType,
  frame: FrameType,
  recognizedLanguages: string[],
  text: string,
];

type ElementsType = [
  cornerPoints: CornerPoints,
  frame: FrameType,
  text: string,
];
export interface Text {
  [key: number | string]: TextData;
}

export type CameraTypes = {
  callback: (data: Text) => void;
  options: TextRecognitionOptions;
} & CameraProps;

export type TextRecognitionPlugin = {
  scanText: (frame: Frame) => Text;
};
