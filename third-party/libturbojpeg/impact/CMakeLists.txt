cmake_minimum_required(VERSION 3.10)
project(turbojpeg C ASM)

set(OUR_DIR ${CMAKE_CURRENT_SOURCE_DIR}/..)

set(TARGET turbojpeg)
set(SRC_DIR ${OUR_DIR})
set(HDR_DIR ${OUR_DIR})

include(${CMAKE_CURRENT_LIST_DIR}/ConfigureChecks.cmake)

# headers dependencies
set(HDRS
    ${HDR_DIR}/jpeglib.h
    ${HDR_DIR}/jdsample.h
    ${HDR_DIR}/transupp.h
    ${HDR_DIR}/jdcoefct.h
    ${HDR_DIR}/jerror.h
    ${HDR_DIR}/jpegint.h
    ${HDR_DIR}/jinclude.h
    ${HDR_DIR}/jdct.h
    ${HDR_DIR}/jdmaster.h
    ${HDR_DIR}/jmemsys.h
    ${HDR_DIR}/turbojpeg.h
    ${HDR_DIR}/jmorecfg.h
    ${HDR_DIR}/jdmainct.h
    ${HDR_DIR}/md5/md5.h
    ${HDR_DIR}/jsimd.h
    ${HDR_DIR}/jdhuff.h
    ${HDR_DIR}/jchuff.h
    ${HDR_DIR}/jsimd.h
    ${HDR_DIR}/cdjpeg.h
    ${HDR_DIR}/simd/nasm/jsimdcfg.inc.h
    ${HDR_DIR}/tjutil.h
    ${HDR_DIR}/jsimddct.h
    ${HDR_DIR}/cmyk.h
    ${HDR_DIR}/cderror.h
)

# public headers dependencies
set(PUBLIC_HDRS ${HDR_DIR}/turbojpeg.h ${HDR_DIR}/jpeglib.h)

# sources dependencies
set(SRCS
    ${SRC_DIR}/jcapimin.c
    ${SRC_DIR}/jcapistd.c
    ${SRC_DIR}/jccoefct.c
    ${SRC_DIR}/jccolor.c
    ${SRC_DIR}/jcdctmgr.c
    ${SRC_DIR}/jchuff.c
    ${SRC_DIR}/jcicc.c
    ${SRC_DIR}/jcinit.c
    ${SRC_DIR}/jcmainct.c
    ${SRC_DIR}/jcmarker.c
    ${SRC_DIR}/jcmaster.c
    ${SRC_DIR}/jcomapi.c
    ${SRC_DIR}/jcparam.c
    ${SRC_DIR}/jcphuff.c
    ${SRC_DIR}/jcprepct.c
    ${SRC_DIR}/jcsample.c
    ${SRC_DIR}/jctrans.c
    ${SRC_DIR}/jdapimin.c
    ${SRC_DIR}/jdapistd.c
    ${SRC_DIR}/jdatadst.c
    ${SRC_DIR}/jdatasrc.c
    ${SRC_DIR}/jdcoefct.c
    ${SRC_DIR}/jdcolor.c
    ${SRC_DIR}/jddctmgr.c
    ${SRC_DIR}/jdhuff.c
    ${SRC_DIR}/jdicc.c
    ${SRC_DIR}/jdinput.c
    ${SRC_DIR}/jdmainct.c
    ${SRC_DIR}/jdmarker.c
    ${SRC_DIR}/jdmaster.c
    ${SRC_DIR}/jdmerge.c
    ${SRC_DIR}/jdphuff.c
    ${SRC_DIR}/jdpostct.c
    ${SRC_DIR}/jdsample.c
    ${SRC_DIR}/jdtrans.c
    ${SRC_DIR}/jerror.c
    ${SRC_DIR}/jfdctflt.c
    ${SRC_DIR}/jfdctfst.c
    ${SRC_DIR}/jfdctint.c
    ${SRC_DIR}/jidctflt.c
    ${SRC_DIR}/jidctfst.c
    ${SRC_DIR}/jidctint.c
    ${SRC_DIR}/jidctred.c
    ${SRC_DIR}/jquant1.c
    ${SRC_DIR}/jquant2.c
    ${SRC_DIR}/jutils.c
    ${SRC_DIR}/jmemmgr.c
    ${SRC_DIR}/jmemnobs.c
    ${SRC_DIR}/jaricom.c
    ${SRC_DIR}/jcarith.c
    ${SRC_DIR}/jdarith.c
    ${SRC_DIR}/turbojpeg.c
    ${SRC_DIR}/jdatadst-tj.c
    ${SRC_DIR}/jdatasrc-tj.c
    ${SRC_DIR}/transupp.c
    ${SRC_DIR}/rdbmp.c
    ${SRC_DIR}/rdppm.c
    ${SRC_DIR}/wrbmp.c
    ${SRC_DIR}/wrppm.c
)

# CPU x86_64
if(CPU_TYPE STREQUAL "x86_64")
    enable_language(ASM_NASM)
    set(SIMD_SOURCES
        ${SRC_DIR}/simd/x86_64/jsimd.c
        ${SRC_DIR}/simd/x86_64/jsimdcpu.asm
        ${SRC_DIR}/simd/x86_64/jfdctflt-sse.asm
        ${SRC_DIR}/simd/x86_64/jccolor-sse2.asm
        ${SRC_DIR}/simd/x86_64/jcgray-sse2.asm
        ${SRC_DIR}/simd/x86_64/jchuff-sse2.asm
        ${SRC_DIR}/simd/x86_64/jcphuff-sse2.asm
        ${SRC_DIR}/simd/x86_64/jcsample-sse2.asm
        ${SRC_DIR}/simd/x86_64/jdcolor-sse2.asm
        ${SRC_DIR}/simd/x86_64/jdmerge-sse2.asm
        ${SRC_DIR}/simd/x86_64/jdsample-sse2.asm
        ${SRC_DIR}/simd/x86_64/jfdctfst-sse2.asm
        ${SRC_DIR}/simd/x86_64/jfdctint-sse2.asm
        ${SRC_DIR}/simd/x86_64/jidctflt-sse2.asm
        ${SRC_DIR}/simd/x86_64/jidctfst-sse2.asm
        ${SRC_DIR}/simd/x86_64/jidctint-sse2.asm
        ${SRC_DIR}/simd/x86_64/jidctred-sse2.asm
        ${SRC_DIR}/simd/x86_64/jquantf-sse2.asm
        ${SRC_DIR}/simd/x86_64/jquanti-sse2.asm
        ${SRC_DIR}/simd/x86_64/jccolor-avx2.asm
        ${SRC_DIR}/simd/x86_64/jcgray-avx2.asm
        ${SRC_DIR}/simd/x86_64/jcsample-avx2.asm
        ${SRC_DIR}/simd/x86_64/jdcolor-avx2.asm
        ${SRC_DIR}/simd/x86_64/jdmerge-avx2.asm
        ${SRC_DIR}/simd/x86_64/jdsample-avx2.asm
        ${SRC_DIR}/simd/x86_64/jfdctint-avx2.asm
        ${SRC_DIR}/simd/x86_64/jidctint-avx2.asm
        ${SRC_DIR}/simd/x86_64/jquanti-avx2.asm
    )

    if(CMAKE_ASM_NASM_OBJECT_FORMAT MATCHES "macho*")
        set(CMAKE_ASM_NASM_FLAGS "${CMAKE_ASM_NASM_FLAGS} -DMACHO")
    elseif(CMAKE_ASM_NASM_OBJECT_FORMAT MATCHES "elf*")
        set(CMAKE_ASM_NASM_FLAGS "${CMAKE_ASM_NASM_FLAGS} -DELF")
        set(CMAKE_ASM_NASM_DEBUG_FORMAT "dwarf2")
    endif()

    if(WIN32 OR CYGWIN)
        set(CMAKE_ASM_NASM_FLAGS "${CMAKE_ASM_NASM_FLAGS} -DWIN64")
    endif()

    set(CMAKE_ASM_NASM_FLAGS "${CMAKE_ASM_NASM_FLAGS} -D__x86_64__")
    set(CMAKE_ASM_NASM_FLAGS "${CMAKE_ASM_NASM_FLAGS} -I\"${OUR_DIR}/simd/nasm/\" -I\"${OUR_DIR}/simd/x86_64/\"")
endif()

# CPU ARM
if(CPU_TYPE STREQUAL "arm64" OR CPU_TYPE STREQUAL "arm")
    enable_language(ASM)
    set(SIMD_SOURCES
        ${SIMD_SOURCES}
        ${SRC_DIR}/simd/arm/jcgray-neon.c
        ${SRC_DIR}/simd/arm/jcsample-neon.c
        ${SRC_DIR}/simd/arm/jdsample-neon.c
        ${SRC_DIR}/simd/arm/jdmerge-neon.c
        ${SRC_DIR}/simd/arm/jquanti-neon.c
        ${SRC_DIR}/simd/arm/jfdctfst-neon.c
        ${SRC_DIR}/simd/arm/jidctred-neon.c
        ${SRC_DIR}/simd/arm/jidctfst-neon.c
        ${SRC_DIR}/simd/arm/jcphuff-neon.c
    )
    if(CPU_TYPE STREQUAL "arm64")
        set(SIMD_SOURCES ${SIMD_SOURCES} ${SRC_DIR}/simd/arm/aarch64/jsimd.c ${SRC_DIR}/simd/arm/aarch64/jsimd_neon.S)
    endif()
    if(CPU_TYPE STREQUAL "arm")
        set(SIMD_SOURCES ${SIMD_SOURCES} ${SRC_DIR}/simd/arm/aarch32/jsimd.c ${SRC_DIR}/simd/arm/aarch32/jsimd_neon.S)
    endif()
endif()

# we're building a static library
add_library(${TARGET} STATIC ${HDRS} ${SIMD_SOURCES} ${SRCS})
set_target_properties(${TARGET} PROPERTIES PUBLIC_HEADER "${PUBLIC_HDRS}")

# specify where the public headers of this library are
target_include_directories(
    ${TARGET} PUBLIC $<BUILD_INTERFACE:${HDR_DIR}> $<BUILD_INTERFACE:${CMAKE_CURRENT_BINARY_DIR}/libjpeg>
                     $<INSTALL_INTERFACE:include>
)

target_compile_definitions(${TARGET} PRIVATE BMP_SUPPORTED PPM_SUPPORTED)

set_property(TARGET ${TARGET} PROPERTY LIBJPEGT_SOURCE_DIR ${HDR_DIR})

install(TARGETS ${TARGET} 
        LIBRARY DESTINATION lib
        PUBLIC_HEADER DESTINATION include
        RUNTIME DESTINATION bin)

