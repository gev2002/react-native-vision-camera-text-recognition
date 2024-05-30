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
export interface TextRecognitionOptions {
  language: 'latin';
}

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
