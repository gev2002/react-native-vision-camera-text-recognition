import { NativeModules, Platform, ToastAndroid } from 'react-native';
import type { PhotoOptions, Text } from './types';

export async function PhotoRecognizer(options: PhotoOptions): Promise<Text> {
  const { PhotoRecognizerModule } = NativeModules;
  const { uri, orientation } = options;

  if (!uri) {
    throw Error("Can't resolve img uri");
  }

  try {
    let result;
    if (Platform.OS === 'ios') {
      result = await PhotoRecognizerModule.process(
        uri.replace('file://', ''),
        orientation || 'portrait'
      );
    } else {
      result = await PhotoRecognizerModule.process(uri);
    }


    if (Object.keys(result).length === 0) {  
      ToastAndroid.show("No text recognized", ToastAndroid.SHORT);    
      console.log('No text detected');
    } else {
      return result;
    }
  } 
  catch (error) {
    console.error('Error during photo recognition: ', error);
  }
}
