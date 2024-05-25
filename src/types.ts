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
  blockFrameBottom: number;
  blockFrameLeft: number;
  blockFrameRight: number;
  blockFrameTop: number;
  blockText: string;
  elementFrameBottom: number;
  elementFrameLeft: number;
  elementFrameRight: number;
  elementFrameTop: number;
  elementText: string;
  lineFrameBottom: number;
  lineFrameLeft: number;
  lineFrameRight: number;
  lineFrameTop: number;
  lineText: string;
  resultText: string;
  size: number;
};

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
