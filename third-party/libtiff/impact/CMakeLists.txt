cmake_minimum_required(VERSION 3.10)
project(tiff)

set(OUR_DIR ${CMAKE_CURRENT_SOURCE_DIR}/..)

set(TARGET tiff)

set(SRC_DIR ${OUR_DIR}/libtiff)
set(PORT_SRC_DIR ${OUR_DIR}/port)
set(PUBLIC_HDR_DIR ${OUR_DIR})
set(PRIVATE_HDR_DIR ${OUR_DIR}/libtiff)

set(CONF_HDR_DIR ${CMAKE_CURRENT_BINARY_DIR}/libtiff)

# Get version from configure.ac
file(STRINGS "${OUR_DIR}/configure.ac" configure REGEX "^LIBTIFF_.*=")
foreach(line ${configure})
    foreach(
        var
        LIBTIFF_MAJOR_VERSION
        LIBTIFF_MINOR_VERSION
        LIBTIFF_MICRO_VERSION
        LIBTIFF_ALPHA_VERSION
        LIBTIFF_CURRENT
        LIBTIFF_REVISION
        LIBTIFF_AGE
    )
        if(NOT ${var} AND line MATCHES "^${var}=(.*)")
            set(${var} "${CMAKE_MATCH_1}")
            break()
        endif()
    endforeach()
endforeach()

# Package version
set(LIBTIFF_VERSION "${LIBTIFF_MAJOR_VERSION}.${LIBTIFF_MINOR_VERSION}.${LIBTIFF_MICRO_VERSION}")
set(LIBTIFF_VERSION_FULL "${LIBTIFF_VERSION}${LIBTIFF_ALPHA_VERSION}")

# Convert the libtool version variables to proper major and minor versions
math(EXPR SO_MAJOR "${LIBTIFF_CURRENT} - ${LIBTIFF_AGE}")
set(SO_MINOR "${LIBTIFF_AGE}")
set(SO_REVISION "${LIBTIFF_REVISION}")

# Library version (unlike libtool's baroque scheme, WYSIWYG here)
set(SO_COMPATVERSION "${SO_MAJOR}")
set(SO_VERSION "${SO_MAJOR}.${SO_MINOR}.${SO_REVISION}")

# Disable deprecated features to ensure clean build
add_definitions(-DTIFF_DISABLE_DEPRECATED)

# Autotools compatibility
include(${OUR_DIR}/cmake/AutotoolsCompat.cmake)

# Compiler checks (warning flags, etc.)
include(${OUR_DIR}/cmake/CompilerChecks.cmake)

# Linker checks (version script, etc.)
include(${OUR_DIR}/cmake/LinkerChecks.cmake)

# Processor checks (endianness, fill order, floating point, etc.)
include(${OUR_DIR}/cmake/ProcessorChecks.cmake)

# Checks for headers
include(${OUR_DIR}/cmake/IncludeChecks.cmake)

# Checks for functions and other symbols
include(${OUR_DIR}/cmake/SymbolChecks.cmake)

# Checks for type presence and size
include(${OUR_DIR}/cmake/TypeSizeChecks.cmake)

# Check for POSIX Large File Support (LFS)
include(${OUR_DIR}/cmake/LargeFileSupport.cmake)

# Options for internal codec support
include(${OUR_DIR}/cmake/InternalCodecs.cmake)

# Option for C++ libtiffxx library
include(${OUR_DIR}/cmake/CXXLibrary.cmake)

# Check for Deflate codec
set(ZLIB_SUPPORT TRUE)
set(ZIP_SUPPORT ${ZLIB_SUPPORT})

# Windows support
include(${OUR_DIR}/cmake/WindowsSupport.cmake)

# Orthogonal features
include(${OUR_DIR}/cmake/LibraryFeatures.cmake)

# Generate headers
configure_file(${OUR_DIR}/libtiff/tif_config.h.cmake.in ${CMAKE_CURRENT_BINARY_DIR}/libtiff/tif_config.h @ONLY)
configure_file(${OUR_DIR}/libtiff/tiffconf.h.cmake.in ${CMAKE_CURRENT_BINARY_DIR}/libtiff/tiffconf.h @ONLY)
configure_file(${OUR_DIR}/libtiff/tiffvers.h.cmake.in ${CMAKE_CURRENT_BINARY_DIR}/libtiff/tiffvers.h @ONLY)
# public headers dependencies
set(PUBLIC_HDRS ${PUBLIC_HDR_DIR}/libtiff/tiff.h ${PUBLIC_HDR_DIR}/libtiff/tiffio.h ${PUBLIC_HDR_DIR}/libtiff/tiffio.hxx
                ${CMAKE_CURRENT_BINARY_DIR}/libtiff/tiffvers.h ${CMAKE_CURRENT_BINARY_DIR}/libtiff/tiffconf.h
)

# private headers dependencies
set(PRIVATE_HDRS
    ${PRIVATE_HDR_DIR}/t4.h ${PRIVATE_HDR_DIR}/tif_dir.h ${PRIVATE_HDR_DIR}/tif_predict.h ${PRIVATE_HDR_DIR}/tiffiop.h ${PRIVATE_HDR_DIR}/tif_hash_set.h
    ${PRIVATE_HDR_DIR}/uvcode.h ${CMAKE_CURRENT_BINARY_DIR}/libtiff/tif_config.h
)

set(SRCS
    ${SRC_DIR}/tif_aux.c
    ${SRC_DIR}/tif_close.c
    ${SRC_DIR}/tif_codec.c
    ${SRC_DIR}/tif_color.c
    ${SRC_DIR}/tif_compress.c
    ${SRC_DIR}/tif_dir.c
    ${SRC_DIR}/tif_dirinfo.c
    ${SRC_DIR}/tif_dirread.c
    ${SRC_DIR}/tif_dirwrite.c
    ${SRC_DIR}/tif_dumpmode.c
    ${SRC_DIR}/tif_error.c
    ${SRC_DIR}/tif_extension.c
    ${SRC_DIR}/tif_fax3.c
    ${SRC_DIR}/tif_fax3sm.c
    ${SRC_DIR}/tif_flush.c
    ${SRC_DIR}/tif_getimage.c
    ${SRC_DIR}/tif_hash_set.c
    ${SRC_DIR}/tif_jbig.c
    ${SRC_DIR}/tif_jpeg.c
    ${SRC_DIR}/tif_jpeg_12.c
    ${SRC_DIR}/tif_luv.c
    ${SRC_DIR}/tif_lzma.c
    ${SRC_DIR}/tif_lzw.c
    ${SRC_DIR}/tif_next.c
    ${SRC_DIR}/tif_ojpeg.c
    ${SRC_DIR}/tif_open.c
    ${SRC_DIR}/tif_packbits.c
    ${SRC_DIR}/tif_pixarlog.c
    ${SRC_DIR}/tif_predict.c
    ${SRC_DIR}/tif_print.c
    ${SRC_DIR}/tif_read.c
    ${SRC_DIR}/tif_strip.c
    ${SRC_DIR}/tif_swab.c
    ${SRC_DIR}/tif_thunder.c
    ${SRC_DIR}/tif_tile.c
    ${SRC_DIR}/tif_version.c
    ${SRC_DIR}/tif_warning.c
    ${SRC_DIR}/tif_webp.c
    ${SRC_DIR}/tif_write.c
    ${SRC_DIR}/tif_zip.c
    ${SRC_DIR}/tif_zstd.c
)

if(USE_WIN32_FILEIO)
    list(APPEND SRCS ${SRC_DIR}/tif_win32.c)
else()
    list(APPEND SRCS ${SRC_DIR}/tif_unix.c)
endif()

# we're building a library
add_library(tiff STATIC ${PUBLIC_HDRS} ${PRIVATE_HDRS} ${SRCS} ${tiff_port_SOURCES})
set_target_properties(${TARGET} PROPERTIES PUBLIC_HEADER "${PUBLIC_HDRS}")
target_link_libraries(${TARGET} PUBLIC z)

# specify where the public headers of this library are
target_include_directories(
    tiff
    PUBLIC $<BUILD_INTERFACE:${PUBLIC_HDR_DIR}> $<BUILD_INTERFACE:${CONF_HDR_DIR}> $<INSTALL_INTERFACE:include>
    PRIVATE ${PRIVATE_HDR_DIR}
)


if(CXX_SUPPORT)
  set(tiffxx_HEADERS ${SRC_DIR}/tif_stream.cxx)

  # No .def file for this library.
  if (WIN32 AND NOT MINGW)
      add_library(tiffxx STATIC ../placeholder.h)
  else()
      add_library(tiffxx ../placeholder.h)
  endif()

  add_library(TIFF::CXX ALIAS tiffxx)

  target_sources(tiffxx PRIVATE
          ${tiffxx_HEADERS}
          ${SRC_DIR}/tif_stream.cxx)
  target_link_libraries(tiffxx tiff)
  set_target_properties(tiffxx PROPERTIES SOVERSION ${SO_COMPATVERSION})
  if(NOT CYGWIN)
    # This property causes shared libraries on Linux to have the full version
    # encoded into their final filename.  We disable this on Cygwin because
    # it causes cygz-${TIFF_FULL_VERSION}.dll to be created when cygz.dll
    # seems to be the default.
    set_target_properties(tiffxx PROPERTIES VERSION ${SO_VERSION})
  endif()
  if(HAVE_LD_VERSION_SCRIPT)
    set_target_properties(tiffxx PROPERTIES LINK_FLAGS
                          "-Wl,--version-script=${SRC_DIR}/libtiffxx.map")
  endif()


  install(TARGETS tiffxx
          RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}
          LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
          ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR})

endif()

install(TARGETS ${TARGET} 
        LIBRARY DESTINATION lib
        PUBLIC_HEADER DESTINATION include
        RUNTIME DESTINATION bin)

