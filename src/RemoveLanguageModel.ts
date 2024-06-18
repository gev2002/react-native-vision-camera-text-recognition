import { NativeModules } from 'react-native';
import type { Languages } from './types';

export async function RemoveLanguageModel(code: Languages): Promise<boolean> {
  const { RemoveLanguageModel: Remove } = NativeModules;
  return await Remove.remove(code);
}
