import { NativeModules } from 'react-native';

export async function RemoveLanguageModel(modelName: string): Promise<boolean> {
  const { RemoveLanguageModel } = NativeModules;
  return await RemoveLanguageModel.remove(modelName);
}
