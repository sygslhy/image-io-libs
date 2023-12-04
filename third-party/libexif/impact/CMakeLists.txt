cmake_minimum_required(VERSION 3.10)
project(exif)

set(OUR_DIR ${CMAKE_CURRENT_SOURCE_DIR}/..)

set(TARGET exif)
set(SRC_DIR ${OUR_DIR}/libexif)
set(PUBLIC_HDR_DIR ${OUR_DIR})
set(PRIVATE_HDR_DIR ${OUR_DIR}/libexif)

set(LIBJPEG_SRC_DIR ${OUR_DIR}/libjpeg)
set(LIBJPEG_PRIVATE_HDR_DIR ${OUR_DIR}/libjpeg)

# public headers dependencies
set(PUBLIC_HDRS ${PUBLIC_HDR_DIR}/libexif/_stdint.h
                ${PUBLIC_HDR_DIR}/libexif/exif-data.h
                ${PUBLIC_HDR_DIR}/libexif/exif-byte-order.h
                ${PUBLIC_HDR_DIR}/libexif/exif-data-type.h
                ${PUBLIC_HDR_DIR}/libexif/exif-ifd.h
                ${PUBLIC_HDR_DIR}/libexif/exif-log.h
                ${PUBLIC_HDR_DIR}/libexif/exif-tag.h
                ${PUBLIC_HDR_DIR}/libexif/exif-content.h
                ${PUBLIC_HDR_DIR}/libexif/exif-mnote-data.h
                ${PUBLIC_HDR_DIR}/libexif/exif-mem.h
                ${PUBLIC_HDR_DIR}/libexif/exif-entry.h
                ${PUBLIC_HDR_DIR}/libexif/exif-format.h
                ${PUBLIC_HDR_DIR}/libexif/exif-loader.h
                ${PUBLIC_HDR_DIR}/libexif/exif-utils.h
                ${PUBLIC_HDR_DIR}/libjpeg/jpeg-data.h)

# private headers dependencies
set(PRIVATE_HDRS
    ${PRIVATE_HDR_DIR}/canon/exif-mnote-data-canon.h
    ${PRIVATE_HDR_DIR}/canon/mnote-canon-entry.h
    ${PRIVATE_HDR_DIR}/canon/mnote-canon-tag.h
    ${PRIVATE_HDR_DIR}/fuji/exif-mnote-data-fuji.h
    ${PRIVATE_HDR_DIR}/fuji/mnote-fuji-entry.h
    ${PRIVATE_HDR_DIR}/fuji/mnote-fuji-tag.h
    ${PRIVATE_HDR_DIR}/olympus/exif-mnote-data-olympus.h
    ${PRIVATE_HDR_DIR}/olympus/mnote-olympus-entry.h
    ${PRIVATE_HDR_DIR}/olympus/mnote-olympus-tag.h
    ${PRIVATE_HDR_DIR}/pentax/exif-mnote-data-pentax.h
    ${PRIVATE_HDR_DIR}/pentax/mnote-pentax-entry.h
    ${PRIVATE_HDR_DIR}/pentax/mnote-pentax-tag.h
    ${PRIVATE_HDR_DIR}/exif.h
    ${PRIVATE_HDR_DIR}/exif-byte-order.h
    ${PRIVATE_HDR_DIR}/exif-content.h
    ${PRIVATE_HDR_DIR}/exif-data-type.h
    ${PRIVATE_HDR_DIR}/exif-entry.h
    ${PRIVATE_HDR_DIR}/exif-format.h
    ${PRIVATE_HDR_DIR}/exif-ifd.h
    ${PRIVATE_HDR_DIR}/exif-loader.h
    ${PRIVATE_HDR_DIR}/exif-log.h
    ${PRIVATE_HDR_DIR}/exif-mem.h
    ${PRIVATE_HDR_DIR}/exif-mnote-data.h
    ${PRIVATE_HDR_DIR}/exif-mnote-data-priv.h
    ${PRIVATE_HDR_DIR}/exif-system.h
    ${PRIVATE_HDR_DIR}/exif-tag.h
    ${PRIVATE_HDR_DIR}/exif-utils.h
    ${PRIVATE_HDR_DIR}/i18n.h
    ${LIBJPEG_PRIVATE_HDR_DIR}/jpeg-marker.h
)

# sources dependencies
set(SRCS
    ${SRC_DIR}/canon/exif-mnote-data-canon.c
    ${SRC_DIR}/canon/mnote-canon-entry.c
    ${SRC_DIR}/canon/mnote-canon-tag.c
    ${SRC_DIR}/fuji/exif-mnote-data-fuji.c
    ${SRC_DIR}/fuji/mnote-fuji-entry.c
    ${SRC_DIR}/fuji/mnote-fuji-tag.c
    ${SRC_DIR}/olympus/exif-mnote-data-olympus.c
    ${SRC_DIR}/olympus/mnote-olympus-entry.c
    ${SRC_DIR}/olympus/mnote-olympus-tag.c
    ${SRC_DIR}/pentax/exif-mnote-data-pentax.c
    ${SRC_DIR}/pentax/mnote-pentax-entry.c
    ${SRC_DIR}/pentax/mnote-pentax-tag.c
    ${SRC_DIR}/exif-byte-order.c
    ${SRC_DIR}/exif-content.c
    ${SRC_DIR}/exif-data.c
    ${SRC_DIR}/exif-entry.c
    ${SRC_DIR}/exif-format.c
    ${SRC_DIR}/exif-ifd.c
    ${SRC_DIR}/exif-loader.c
    ${SRC_DIR}/exif-log.c
    ${SRC_DIR}/exif-mem.c
    ${SRC_DIR}/exif-mnote-data.c
    ${SRC_DIR}/exif-tag.c
    ${SRC_DIR}/exif-utils.c
    ${LIBJPEG_SRC_DIR}/jpeg-data.c
    ${LIBJPEG_SRC_DIR}/jpeg-marker.c
)

# we're building a library
add_library(${TARGET} STATIC ${PRIVATE_HDRS} ${PUBLIC_HDRS} ${SRCS})
set_target_properties(${TARGET} PROPERTIES PUBLIC_HEADER "${PUBLIC_HDRS}")

# specify where the public headers of this library are
target_include_directories(
    ${TARGET}
    PUBLIC $<BUILD_INTERFACE:${PUBLIC_HDR_DIR}> $<INSTALL_INTERFACE:include>
    PRIVATE ${PRIVATE_HDR_DIR}
)

target_compile_definitions(${TARGET} PRIVATE GETTEXT_PACKAGE="libexif-12")

install(TARGETS ${TARGET}
    LIBRARY DESTINATION lib
    PUBLIC_HEADER DESTINATION include/libexif
    RUNTIME DESTINATION bin)
