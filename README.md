# react-native-vision-camera-text-recognition

A plugin to Scanning Text using ML Kit Text Recognition. With High Performance.

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
          // optional
          options={{
            language:'latin'
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

|   Name   |  Type    |                 Values                 | Default |
|:--------:| :---: |:--------------------------------------:|:-------:|
| language | string | latin |  latin  |

















