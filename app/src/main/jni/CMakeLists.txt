cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -Wall -DANDROID_NDK -DDISABLE_IMPORTGL -DNO_CRYPTO -DGL_GLEXT_PROTOTYPES=1")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -UNDEBUG -DNO_CRYPTO -DGL_GLEXT_PROTOTYPES")

#add x264 header
set(X264_HEADER ${CMAKE_CURRENT_LIST_DIR}/x264/${ANDROID_ABI}/output/include)
include_directories(${X264_HEADER})

#add g72x header
include_directories(${PROJECT_SOURCE_DIR}/g72x)

file(GLOB MEDIAENCODER_SOURCE
main_jni.cpp
h264encoder.cpp
g72x/g726_32.c
g72x/g711.c
g72x/g72x.c
)

add_library(MediaEncoder SHARED ${MEDIAENCODER_SOURCE})

#add_library(main_jni SHARED
#            main_jni.cpp)

#link libx264 library
set(LIBX264_LIB_DIR ${CMAKE_CURRENT_LIST_DIR}/x264)
add_library(X264 STATIC IMPORTED)
set_target_properties(X264 PROPERTIES IMPORTED_LOCATION ${LIBX264_LIB_DIR}/${ANDROID_ABI}/output/lib/libx264.a)

# Include libraries needed for native-audio-jni lib
target_link_libraries(
MediaEncoder
X264
android
log
OpenSLES
)