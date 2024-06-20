import { NativeModules, Platform } from 'react-native';
import ImageResizer from '@bam.tech/react-native-image-resizer';
import type { PhotoOptions, Text } from './types';

export async function PhotoRecognizer(options: PhotoOptions): Promise<Text> {
  const { PhotoRecognizerModule: PhotoRecognizer } = NativeModules;
  const { uri, width, height } = options;
  const img = await ImageResizer.createResizedImage(
    uri,
    width,
    height,
    'PNG',
    1,
    0,
    null
  );
  console.log(img);
  if (!img.path) {
    throw Error("Can't resolve img path");
  }
  if (Platform.OS === 'ios') {
    return await PhotoRecognizer.process(img.path);
  } else {
    return await PhotoRecognizer.process(img.uri);
  }
}
