import type { Frame, TranslatorPlugin, TranslatorOptions } from './types';
import { VisionCameraProxy } from 'react-native-vision-camera';

const LINKING_ERROR = `Can't load plugin translate.Try cleaning cache or reinstall plugin.`;

export function createTranslatorPlugin(
  options?: TranslatorOptions
): TranslatorPlugin {
  const plugin = VisionCameraProxy.initFrameProcessorPlugin('translate', {
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
