import { NativeModules, Platform } from 'react-native';
import type { PhotoOptions, Text } from './types';

let imageResizer: any;
try {
  imageResizer = require('@bam.tech/react-native-image-resizer');
} catch (e) {}

export async function PhotoRecognizer(options: PhotoOptions): Promise<Text> {
  const { PhotoRecognizerModule } = NativeModules;
  if (!imageResizer) {
    throw new Error(
      "@bam.tech/react-native-image-resizer is not installed! It's required for PhotoRecognizer."
    );
  }
  const { uri, width, height } = options;
  const img = await imageResizer.createResizedImage(
    uri,
    width,
    height,
    'PNG',
    1,
    0,
    null
  );
  if (!uri) {
    throw Error("Can't resolve img path");
  }
  if (Platform.OS === 'ios') {
    return await PhotoRecognizerModule.process(img.path);
  } else {
    return await PhotoRecognizerModule.process(img.uri);
  }
}
