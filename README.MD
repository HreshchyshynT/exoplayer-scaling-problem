***Bug description***

When the app launches and plays a video using AspectRatioFrameLayout.RESIZE_MODE_ZOOM, the video is expected to fill the screen. 
However, on the specified devices running Android 14, the video's width appears stretched. I could reproduce it only on my Pixel 7a (Android 14) and
on the Android Studio's emulator Pixel 7a (Android 14). After app goes background and then opened from the receny apps - video looks like it should from the start. 
