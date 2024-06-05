# react-native-vision-camera-text-recognition

A plugin to Scanning Text,Translate using ML Kit Text Recognition and ML Kit Translation. With High Performance and many features.
# 🚨 Required Modules
react-native-vision-camera = 4.0.5 <br/>
react-native-worklets-core = 1.3.3

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
    Can translate text. 🌍

## 💡 Usage

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
            language: 'latin',
            mode: 'translate',
            from: 'english',
            to: 'german'
          }}
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
  const options = { language : 'latin', mode: 'recognize' }
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

|   Name   |  Type    |                    Values                    |    Default     |
|:--------:| :---: |:--------------------------------------------:|:--------------:|
| language | string | latin, chinese, devanagari, japanese, korean |     latin      |
|   mode   | string |             recognize, translate             |   recognize    |
| from,to  | string |                  See Below                   | english,german |



```#Supported Languages
```
<h3>Afrikaans: 🇿🇦, 🇨🇫</h3>
<h3>Albanian: 🇦🇱</h3>
<h3>Arabic: 🇦🇪, 🇸🇦</h3>
<h3>Belarusian: 🇧🇾</h3>
<h3>Bulgarian: 🇧🇬</h3>
<h3>Bengali: 🇧🇩, 🇮🇳</h3>
<h3>Catalan: 🇪🇸, 🇦🇩</h3>
<h3>Czech: 🇨🇿</h3>
<h3>Welsh: 🏴</h3>
<h3>Danish: 🇩🇰</h3>
<h3>German: 🇩🇪</h3>
<h3>Greek: 🇬🇷</h3>
<h3>English: 🇬🇧, 🇺🇸</h3>
<h3>Esperanto: 🌍</h3>
<h3>Spanish: 🇪🇸</h3>
<h3>Estonian: 🇪🇪</h3>
<h3>Persian: 🇮🇷, 🇦🇫</h3>
<h3>Finnish: 🇫🇮</h3>
<h3>French: 🇫🇷</h3>
<h3>Irish: 🇮🇪</h3>
<h3>Galician: 🇪🇸</h3>
<h3>Gujarati: 🇮🇳</h3>
<h3>Hebrew: 🇮🇱</h3>
<h3>Hindi: 🇮🇳</h3>
<h3>Croatian: 🇭🇷</h3>
<h3>Haitian: 🇭🇹</h3>
<h3>Hungarian: 🇭🇺</h3>
<h3>Indonesian: 🇮🇩</h3>
<h3>Icelandic: 🇮🇸</h3>
<h3>Italian: 🇮🇹</h3>
<h3>Japanese: 🇯🇵</h3>
<h3>Georgian: 🇬🇪</h3>
<h3>Canada: 🇨🇦</h3>
<h3>Korean: 🇰🇷, 🇰🇵</h3>
<h3>Lithuanian: 🇱🇹</h3>
<h3>Latvian: 🇱🇻</h3>
<h3>Macedonian: 🇲🇰</h3>
<h3>Marathi: 🇮🇳</h3>
<h3>Malay: 🇲🇾, 🇸🇬</h3>
<h3>Maltese: 🇲🇹</h3>
<h3>Dutch: 🇳🇱, 🇧🇪</h3>
<h3>Norwegian: 🇳🇴</h3>
<h3>Polish: 🇵🇱</h3>
<h3>Portuguese: 🇵🇹</h3>
<h3>Romanian: 🇷🇴</h3>
<h3>Russian: 🇷🇺</h3>
<h3>Slovak: 🇸🇰</h3>
<h3>Slovenian: 🇸🇮</h3>
<h3>Swedish: 🇸🇪, 🇫🇮</h3>
<h3>Swahili: 🇰🇪, 🇹🇿</h3>
<h3>Tamil: 🇮🇳, 🇱🇰</h3>
<h3>Telugu: 🇮🇳</h3>
<h3>Thai: 🇹🇭</h3>
<h3>Tagalog: 🇵🇭</h3>
<h3>Turkish: 🇹🇷</h3>
<h3>Ukrainian: 🇺🇦</h3>
<h3>Urdu: 🇵🇰, 🇮🇳</h3>
<h3>Vietnamese: 🇻🇳</h3>
<h3>Chinese: 🇨🇳</h3>
