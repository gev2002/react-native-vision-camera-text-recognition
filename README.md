# react-native-vision-camera-text-recognition

A plugin to Scanning Text,Translate using ML Kit Text Recognition and ML Kit Translation. With High Performance and many features.
# 🚨 Required Modules
react-native-vision-camera = 4.3.2 <br/>
react-native-worklets-core = 1.3.3 <br/>

Required if you are using Photo Recognizer. <br/>
@bam.tech/react-native-image-resizer = 3.0.10

## 💻 Installation

```sh
npm install react-native-vision-camera-text-recognition
yarn add react-native-vision-camera-text-recognition
```
## 👷Features
    Easy To Use.
    Works Just Writing few lines of Code.
    Works With React Native Vision Camera.
    Works for Both Cameras.
    Works Fast.
    Works With Android 🤖 and IOS.📱
    Writen With Kotlin and Swift.
    Can Recognize Text From Photo. 📸
    Can translate text. 🌍

## 💡 Usage
### 📚 For Live Recognition Text
```js
import React, { useState } from 'react'
import { useCameraDevice } from 'react-native-vision-camera'
import { Camera } from 'react-native-vision-camera-text-recognition';

function App (){
  const [data,setData] = useState(null)
  const device = useCameraDevice('back');
  console.log(data)
  return(
    <>
      {!!device && (
        <Camera
          style={StyleSheet.absoluteFill}
          device={device}
          isActive
          options={{
            language: 'latin'
          }}
          mode={'recognize'}
          callback={(d) => setData(d)}
        />
      )}
    </>
  )
}

export default App;



```

### 🌍 For Translate Text
```js
import React, { useState } from 'react'
import { useCameraDevice } from 'react-native-vision-camera'
import { Camera } from 'react-native-vision-camera-text-recognition';

function App (){
  const [data,setData] = useState(null)
  const device = useCameraDevice('back');
  console.log(data)
  return(
    <>
      {!!device && (
        <Camera
          style={StyleSheet.absoluteFill}
          device={device}
          isActive
          options={{
            from: 'en',
            to: 'de'
          }}
          mode={'translate'}
          callback={(d) => setData(d)}
        />
      )}
    </>
  )
}

export default App;

```

### Also You Can Use Like This

```js
import React from 'react';
import { StyleSheet } from "react-native";
import {
  Camera,
  useCameraDevice,
  useFrameProcessor,
} from "react-native-vision-camera";
import { useTextRecognition } from "react-native-vision-camera-text-recognition";

function App() {
  const device = useCameraDevice('back');
  const options = { language : 'latin' }
  const {scanText} = useTextRecognition(options)
  const frameProcessor = useFrameProcessor((frame) => {
    'worklet'
    const data = scanText(frame)
    console.log(data, 'data')
  }, [])
  return (
    <>
      {!!device && (
        <Camera
          style={StyleSheet.absoluteFill}
          device={device}
          isActive
          mode={'recognize'}
          frameProcessor={frameProcessor}
        />
      )}
    </>
  );
}
export default App;


```
---
## ⚙️ Options

|   Name   |  Type    |                    Values                    |  Default  |
|:--------:| :---: |:--------------------------------------------:|:---------:|
| language | string | latin, chinese, devanagari, japanese, korean |   latin   |
|   mode   | string |             recognize, translate             | recognize |
| from,to  | string |                  See Below                   |   en,de   |


##  Recognize By Photo 📸

```js
import { PhotoRecognizer } from "react-native-vision-camera-text-recognition";

const result = await PhotoRecognizer({
    uri:assets.uri,
    width:assets.width,
    height:assets.height
})
console.log(result);

```
|  Name   |  Type  | Required |
|:-------:|:------:|:--------:|
|   uri   | string |   yes    |
|  width  | number |   yes    |
| height | number |   yes    |


### You can also remove unnecessary translation model



```js
import { RemoveLanguageModel } from "react-native-vision-camera-text-recognition";

const bool = await RemoveLanguageModel("en")
```
<h2>Supported Languages.</h2>

```
<h3>Afrikaans: 🇿🇦, 🇨🇫 <---> code : "af"</h3>
<h3>Albanian: 🇦🇱 <---> code : "sq"</h3>
<h3>Arabic: 🇦🇪, 🇸🇦 <---> code : "ar"</h3>
<h3>Belarusian: 🇧🇾 <---> code : "be"</h3>
<h3>Bulgarian: 🇧🇬 <---> code : "bn"</h3>
<h3>Bengali: 🇧🇩 <---> code : "bg"</h3>
<h3>Catalan: 🏴 <---> code : "ca"</h3>
<h3>Czech: 🇨🇿 <---> code : "cs"</h3>
<h3>Welsh: 🏴󠁧󠁢󠁷󠁬󠁳󠁿 <---> code : "cy"</h3>
<h3>Danish: 🇩🇰 <---> code : "da"</h3>
<h3>German: 🇩🇪 <---> code : "de"</h3>
<h3>Greek: 🇬🇷 <---> code : "el"</h3>
<h3>English: 🇬🇧, 🇺🇸 <---> code : "en"</h3>
<h3>Esperanto: 🌍 <---> code : "eo"</h3>
<h3>Spanish: 🇪🇸 <---> code : "es"</h3>
<h3>Estonian: 🇪🇪 <---> code : "et"</h3>
<h3>Persian: 🇮🇷 <---> code : "fa"</h3>
<h3>Finnish: 🇫🇮 <---> code : "fi"</h3>
<h3>French: 🇫🇷 <---> code : "fr"</h3>
<h3>Irish: 🇮🇪 <---> code : "ga"</h3>
<h3>Galician: 🏴 <---> code : "gl"</h3>
<h3>Gujarati: 🏴 <---> code : "gu"</h3>
<h3>Hebrew: 🇮🇱 <---> code : "he"</h3>
<h3>Hindi: 🇮🇳 <---> code : "hi"</h3>
<h3>Croatian: 🇭🇷 <---> code : "hr"</h3>
<h3>Haitian: 🇭🇹 <---> code : "ht"</h3>
<h3>Hungarian: 🇭🇺 <---> code : "hu"</h3>
<h3>Indonesian: 🇮🇩 <---> code : "id"</h3>
<h3>Icelandic: 🇮🇸 <---> code : "is"</h3>
<h3>Italian: 🇮🇹 <---> code : "it"</h3>
<h3>Japanese: 🇯🇵 <---> code : "ja"</h3>
<h3>Georgian: 🇬🇪 <---> code : "ka"</h3>
<h3>Kannada: 🇨🇦 <---> code : "kn"</h3>
<h3>Korean: 🇰🇷, 🇰🇵 <---> code : "ko"</h3>
<h3>Lithuanian: 🇱🇹 <---> code : "lt"</h3>
<h3>Latvian: 🇱🇻 <---> code : "lv"</h3>
<h3>Macedonian: 🇲🇰 <---> code : "mk"</h3>
<h3>Marathi: 🇮🇳 <---> code : "mr"</h3>
<h3>Malay: 🇲🇾 <---> code : "ms"</h3>
<h3>Maltese: 🇲🇹 <---> code : "mt"</h3>
<h3>Dutch: 🇳🇱 <---> code : "nl"</h3>
<h3>Norwegian: 🇳🇴 <---> code : "no"</h3>
<h3>Polish: 🇵🇱 <---> code : "pl"</h3>
<h3>Portuguese: 🇵🇹 <---> code : "pt"</h3>
<h3>Romanian: 🇷🇴 <---> code : "ro"</h3>
<h3>Russian: 🇷🇺 <---> code : "ru"</h3>
<h3>Slovak: 🇸🇰 <---> code : "sk"</h3>
<h3>Slovenian: 🇸🇮 <---> code : "sl"</h3>
<h3>Swedish: 🇸🇪 <---> code : "sv"</h3>
<h3>Swahili: 🇰🇪 <---> code : "sw"</h3>
<h3>Tamil: 🇱🇰 <---> code : "ta"</h3>
<h3>Telugu: 🇮🇳 <---> code : "te"</h3>
<h3>Thai: 🇹🇭 <---> code : "th"</h3>
<h3>Tagalog: 🇵🇭 <---> code : "tl"</h3>
<h3>Turkish: 🇹🇷 <---> code : "tr"</h3>
<h3>Ukrainian: 🇺🇦 <---> code : "uk"</h3>
<h3>Urdu: 🇵🇰 <---> code : "ur"</h3>
<h3>Vietnamese: 🇻🇳 <---> code : "vi"</h3>
<h3>Chinese: 🇨🇳 <---> code : "zh"</h3>
