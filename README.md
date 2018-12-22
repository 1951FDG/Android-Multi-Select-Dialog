# MultiSelectDialog v3

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

## Apps using this library

-   [openpyn-nordvpn-juiceSSH](https://github.com/1951FDG/openpyn-nordvpn-juiceSSH)

## Screenshots

<img src="https://github.com/1951FDG/openpyn-nordvpn-juiceSSH/blob/master/fastlane/metadata/android/en-US/images/phoneScreenshots/screenshot_02_1541723220359.png" width="50%">

## Requirements

-   **Minimum Android SDK** `4.1 (API level 16)`
-   **Compile Android SDK** `9 (API level 28)`

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
      		 implementation 'com.github.1951FDG:Android-Multi-Select-Dialog:v3.0'
	}
```

## Demo

For a working implementation of this project, see the `app/src/` folder.

## Feedback

Feel free to send us feedback by submitting an [issue](https://github.com/1951FDG/Android-Multi-Select-Dialog/issues/new). Bug reports, feature requests, patches, and well-wishes are always welcome.

## Contributing

Pull requests are welcome. For major changes, please submit an issue first to discuss what you would like to change.

## Attributions

-   [Country Flags Icons](https://www.flaticon.com/packs/countrys-flags)
