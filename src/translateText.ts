import type {
  Frame,
  FrameProcessorPlugin,
  TranslatorPlugin,
} from './types';
import { VisionCameraProxy } from 'react-native-vision-camera';

import { LINKING_ERROR } from './scanText';

export function createTranslatorPlugin(
  options: {},
): TranslatorPlugin {
  const plugin: FrameProcessorPlugin | undefined =
    VisionCameraProxy.initFrameProcessorPlugin('translate', {
      ...options,
    });
  if (!plugin) {
    throw new Error(LINKING_ERROR);
  }
  return {
    translate: (frame: Frame): string => {
      'worklet';
      return plugin.call(frame) as string;
    },
  };
}
