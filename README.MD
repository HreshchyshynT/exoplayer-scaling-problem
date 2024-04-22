***Bug description***

When the app launches and plays a video using AspectRatioFrameLayout.RESIZE_MODE_ZOOM, the video is expected to fill the screen.
However, the video's width appears stretched on my Pixel 7a (Android 14) and the Android Studio's emulator Pixel 7a (Android 14). 
After the app goes to the background and then opens from the recent apps, the video looks like it should from the start.
This bug doesn't appear if I use the Android View system instead of Kotlin Compose (you can checkout to the android-view branch).

*Actual:*

<img src="./screenshots/Screenshot_20240422_121212.png" width="200" />

*Expected:*

<img src="./screenshots/after_restoring_from_background.png" width="200" />

*Video:*

<img src="./screenshots/Screen_recording_20240422_122558.gif" width="200" />

