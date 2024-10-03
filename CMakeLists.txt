cmake_minimum_required(VERSION 3.10)
project(image-io)

list(APPEND CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/cmake/modules")

# Needed for clang-tidy post process
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)


# Build everything as static for MinGW to avoid any DLL dependency
if(MINGW OR MSYS)
    link_libraries("-static -static-libgcc -static-libstdc++")
    add_compile_options("$<$<COMPILE_LANGUAGE:CXX>:-std=c++17>")
endif()

if("${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
    add_compile_options("$<$<COMPILE_LANGUAGE:CXX>:-march=haswell>")
    add_compile_options("$<$<COMPILE_LANGUAGE:CXX>:-fPIC>" "$<$<COMPILE_LANGUAGE:C>:-fPIC>")
    add_compile_options("$<$<COMPILE_LANGUAGE:CXX>:-std=gnu++17>")
endif()


add_subdirectory(third-party)
