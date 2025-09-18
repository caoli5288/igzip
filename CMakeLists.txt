cmake_minimum_required(VERSION 3.10)

project(IGZIP)

if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE "Release")
endif()

set(CMAKE_CXX_FLAGS_RELEASE "-O3 -s")
set(CMAKE_C_FLAGS_RELEASE "-O3 -s")

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/igzip-linux-amd64/src/main/resources)
set(CMAKE_INTERPROCEDURAL_OPTIMIZATION ON)

find_package(PkgConfig REQUIRED)

pkg_check_modules(ISAL REQUIRED libisal)

set(JAVA_AWT_LIBRARY "")
set(JAVA_AWT_INCLUDE_PATH "")

find_package(JNI)

include_directories(${JNI_INCLUDE_DIRS})

add_library(igzip_jni SHARED
    igzip-java/src/main/c/igzip_jni.c
)

target_link_libraries(igzip_jni 
    ${ISAL_STATIC_LIBRARIES}
)

target_include_directories(igzip_jni PRIVATE
    ${ISAL_INCLUDE_DIRS}
)