#!/bin/bash
NDK=$NDKROOT
HOST_TAG=linux-x86_64
#HOST_TAG 的值根据系统修改
#macOS  darwin-x86_64
#Linux   linux-x86_64
#32-bit Windows  windows
#64-bit Windows  windows-x86_64
TOOLCHAIN=$NDK/toolchains/llvm/prebuilt/$HOST_TAG
API=21

function build_android
{
echo "Compiling FFmpeg for $CPU and prefix is $PREFIX"
./configure \
    --prefix=$PREFIX 
    --disable-x86asm \
    --disable-neon \
    --disable-hwaccels \
    --disable-gpl \
    --disable-postproc \
    --enable-shared \
    --enable-jni \
    --disable-mediacodec \
    --disable-decoder=h264_mediacodec \
    --disable-static \
    --disable-doc \
    --disable-ffmpeg \
    --disable-ffplay \
    --disable-ffprobe \
    --disable-avdevice \
    --disable-doc \
    --disable-symver \
    --cross-prefix=$CROSS_PREFIX \
    --target-os=android \
    --arch=$ARCH \
    --cpu=$CPU \
    --cc=$CC \
    --cxx=$CXX \
    --enable-cross-compile \
    --sysroot=$SYSROOT \
    --extra-cflags="-Os -fpic $OPTIMIZE_CFLAGS" \ 
    --extra-ldflags="$ADDI_LDFLAGS" \
    $ADDITIONAL_CONFIGURE_FLAG
make clean
make -j2
make install
echo "The Compilation of FFmpeg for $CPU is completed"
}


# arm64-v8a   aarch64-linux-android

# ARCH=arm64
# CPU=armv8-a
# TRIPLE=aarch64-linux-android
# STRIP=aarch64-linux-android
# OPTIMIZE_CFLAGS="-march=$CPU"


# armeabi-v7a armv7a-linux-androideabi

ARCH=arm
CPU=armv7-a
TRIPLE=armv7a-linux-androideabi
STRIP=arm-linux-androideabi
OPTIMIZE_CFLAGS="-mfloat-abi=softfp -mfpu=vfp -marm -march=$CPU "

# # x86 i686-linux-android

# ARCH=x86
# CPU=x86
# TRIPLE=i686-linux-android
# STRIP=i686-linux-android
# OPTIMIZE_CFLAGS="-march=i686 -mtune=intel -mssse3 -mfpmath=sse -m32"

# x86-64  x86_64-linux-android

# ARCH=x86_64
# CPU=x86-64
# TRIPLE=x86_64-linux-android
# STRIP=x86_64-linux-android
# OPTIMIZE_CFLAGS="-march=$CPU -msse4.2 -mpopcnt -m64 -mtune=intel"

CC=$TOOLCHAIN/bin/$TRIPLE$API-clang
CXX=$TOOLCHAIN/bin/$TRIPLE$API-clang++
SYSROOT=$NDK/toolchains/llvm/prebuilt/$HOST_TAG/sysroot
CROSS_PREFIX=$TOOLCHAIN/bin/$STRIP-
PREFIX=$(pwd)/android/$CPU
build_android
