## Camera Encoder X264

This basic implementation allows you to encode and stream your smartphone's camera in a wireless network.

### Disclaimer

This repository contains sample code intended to demonstrate the capabilities of the MediaCodec API. It is not intended to be used as-is in applications as a library dependency, and will not be maintained as such. Bug fix contributions are welcome, but issues and feature requests will not be addressed.


### Specifications

* Build-in web service, you can see the video via browser in pc and another phone, a modem browser with HTML5 is reauired.
* H.264 video and G.726 audio.
* Streaming via websocket between browser and android phone.
* Decoding H.264 and G.726 in pure Javascript.

### Pre-requisites

Android SDK 25
Android Build Tools v25.0.2
Android Support Repository

## License and third party libraries

The code supplied here is covered under the MIT Open Source License..

* X.264 : git://git.videolan.org/x264.git 
* Java Websocket library : https://github.com/TooTallNate/Java-WebSocket
* H.264 javascript decoder:  https://github.com/mbebenita/Broadway
