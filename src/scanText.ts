import { VisionCameraProxy } from 'react-native-vision-camera';
import type {
  Frame,
  TextRecognitionPlugin,
  TextRecognitionOptions,
  Text,
  FrameProcessorPlugin,
} from './types';
import { Platform } from 'react-native';

const LINKING_ERROR: string =
  `The package 'react-native-vision-camera-text-recognition' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

export function createTextRecognitionPlugin(
  options: TextRecognitionOptions
): TextRecognitionPlugin {
  const plugin: FrameProcessorPlugin | undefined =
    VisionCameraProxy.initFrameProcessorPlugin('scanText', {
      ...options,
    });
  if (!plugin) {
    throw new Error(LINKING_ERROR);
  }
  return {
    scanText: (frame: Frame): Text => {
      'worklet';
      return plugin.call(frame) as unknown as Text;
    },
  };
}
