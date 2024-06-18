import React, { forwardRef, type ForwardedRef, useMemo } from 'react';
import {
  Camera as VisionCamera,
  useFrameProcessor,
} from 'react-native-vision-camera';
import { createTextRecognitionPlugin } from './scanText';
import { useRunOnJS } from 'react-native-worklets-core';
import type {
  CameraTypes,
  Text,
  Frame,
  ReadonlyFrameProcessor,
  TextRecognitionPlugin,
  TranslatorPlugin,
} from './types';
import { createTranslatorPlugin } from './translateText';

export const Camera = forwardRef(function Camera(
  props: CameraTypes,
  ref: ForwardedRef<any>
) {
  const { device, callback, options = {}, mode, ...p } = props;

  let plugin: any;
  if (mode === 'translate') {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { translate } = useTranslate(options);
    // @ts-ignore
    plugin = translate;
  } else {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const { scanText } = useTextRecognition(options);
    plugin = scanText;
  }
  const useWorklets = useRunOnJS(
    (data): void => {
      callback(data);
    },
    [options]
  );
  const frameProcessor: ReadonlyFrameProcessor = useFrameProcessor(
    (frame: Frame) => {
      'worklet';
      const data: Text | string = plugin(frame);
      // @ts-ignore
      // eslint-disable-next-line react-hooks/rules-of-hooks
      useWorklets(data);
    },
    []
  );
  return (
    <>
      {!!device && (
        <VisionCamera
          pixelFormat="yuv"
          ref={ref}
          frameProcessor={frameProcessor}
          device={device}
          {...p}
        />
      )}
    </>
  );
});

export function useTextRecognition(options?: {}): TextRecognitionPlugin {
  return useMemo(
    () => createTextRecognitionPlugin(options || { language: 'latin' }),
    [options]
  );
}
export function useTranslate(options?: {}): TranslatorPlugin {
  return useMemo(
    () => createTranslatorPlugin(options || { from: 'en', to: 'de' }),
    [options]
  );
}

export { RemoveLanguageModel } from './RemoveLanguageModel';
