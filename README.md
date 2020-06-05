# MultiSelectDialog v3

[![API](https://img.shields.io/badge/API-17%2B-blue.svg)](https://android-arsenal.com/api?level=17)
[![JitPack](https://img.shields.io/jitpack/v/1951FDG/Android-Multi-Select-Dialog.svg)](https://jitpack.io/#1951FDG/Android-Multi-Select-Dialog)

A fast and efficient multi choice select DialogFragment with async search and text highlighting

> **Note**:
> This fork is almost a complete rewrite of the original work by [Abubakker Moallim](https://github.com/abumoallim/Android-Multi-Select-Dialog)

## Todo

-   [ ] handle orientation changes

## Features

-   Uses ListAdapter (computes diffs between Lists on a background thread)
-   Provides multi selection Dialog
-   Search text is highlighted
-   Search through list

_Am I missing some essential feature?_

-   Submit an [issue](https://github.com/1951FDG/Android-Multi-Select-Dialog/issues/new) and let's make this app better together!

## How to use

1.  **Add the JitPack repository to your build file**

    Add it in your root build.gradle at the end of repositories:

```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2.  **Add the dependency**

```gradle
	dependencies {
      		 implementation 'com.github.1951FDG:Android-Multi-Select-Dialog:v3.7'
	}
```

## Screenshots

<img src="https://github.com/1951FDG/openpyn-nordvpn-juiceSSH/blob/master/fastlane/metadata/android/en-US/images/phoneScreenshots/screenshot_02.png" width="50%">

## Demo

For a working implementation of this project, see the [`./app/src/main`](app/src/main) folder.

## Requirements

-   **Minimum Android SDK** `4.2 (API level 17)`
-   **Compile Android SDK** `10 (API level 29)`

## Feedback

Feel free to send us feedback by submitting an [issue](https://github.com/1951FDG/openpyn-nordvpn-juiceSSH/issues/new). Bug reports, feature requests, patches, and well-wishes are always welcome.

> **Note**:
> Pull requests are welcome. For major changes, please submit an issue first to discuss what you would like to change.

## Built with

-   [SVG Cleaner](https://github.com/RazrFalcon/svgcleaner-gui)
-   [Svg2VectorAndroid](https://github.com/1951FDG/Svg2VectorAndroid)

## Attributions

-   [Country Flags Icons](https://www.flaticon.com/packs/countrys-flags)

## Apps using this library

-   [openpyn-nordvpn-juiceSSH](https://github.com/1951FDG/openpyn-nordvpn-juiceSSH)
