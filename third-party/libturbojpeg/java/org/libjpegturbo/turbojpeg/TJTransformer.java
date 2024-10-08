/*
 * Copyright (C)2011, 2013-2015, 2023 D. R. Commander.  All Rights Reserved.
 * Copyright (C)2015 Viktor Szathmáry.  All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of the libjpeg-turbo Project nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS",
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.libjpegturbo.turbojpeg;

/**
 * TurboJPEG lossless transformer
 */
public class TJTransformer extends TJDecompressor {

  /**
   * Create a TurboJPEG lossless transformer instance.
   */
  public TJTransformer() throws TJException {
    init();
  }

  /**
   * Create a TurboJPEG lossless transformer instance and associate the JPEG
   * source image stored in <code>jpegImage</code> with the newly created
   * instance.
   *
   * @param jpegImage buffer containing the JPEG source image to transform.
   * (The size of the JPEG image is assumed to be the length of the array.)
   * This buffer is not modified.
   */
  public TJTransformer(byte[] jpegImage) throws TJException {
    init();
    setSourceImage(jpegImage, jpegImage.length);
  }

  /**
   * Create a TurboJPEG lossless transformer instance and associate the JPEG
   * source image of length <code>imageSize</code> bytes stored in
   * <code>jpegImage</code> with the newly created instance.
   *
   * @param jpegImage buffer containing the JPEG source image to transform.
   * This buffer is not modified.
   *
   * @param imageSize size of the JPEG source image (in bytes)
   */
  public TJTransformer(byte[] jpegImage, int imageSize) throws TJException {
    init();
    setSourceImage(jpegImage, imageSize);
  }

  /**
   * Losslessly transform the JPEG source image associated with this
   * transformer instance into one or more JPEG images stored in the given
   * destination buffers.  Lossless transforms work by moving the raw
   * coefficients from one JPEG image structure to another without altering the
   * values of the coefficients.  While this is typically faster than
   * decompressing the image, transforming it, and re-compressing it, lossless
   * transforms are not free.  Each lossless transform requires reading and
   * performing Huffman decoding on all of the coefficients in the source
   * image, regardless of the size of the destination image.  Thus, this method
   * provides a means of generating multiple transformed images from the same
   * source or of applying multiple transformations simultaneously, in order to
   * eliminate the need to read the source coefficients multiple times.
   *
   * @param dstBufs an array of JPEG destination buffers.
   * <code>dstbufs[i]</code> will receive a JPEG image that has been
   * transformed using the parameters in <code>transforms[i]</code>.  Use
   * {@link TJ#bufSize TJ.bufSize()} to determine the maximum size for each
   * buffer based on the transformed or cropped width and height and the level
   * of subsampling used in the destination image (taking into account
   * grayscale conversion and transposition of the width and height.)
   *
   * @param transforms an array of {@link TJTransform} instances, each of
   * which specifies the transform parameters and/or cropping region for the
   * corresponding transformed JPEG image
   */
  public void transform(byte[][] dstBufs, TJTransform[] transforms)
                        throws TJException {
    transformedSizes = transform(getJPEGBuf(), getJPEGSize(), dstBufs,
                                 transforms);
  }

  /**
   * @deprecated Use {@link #set TJDecompressor.set()} and
   * {@link #transform(byte[][], TJTransform[])} instead.
   */
  @SuppressWarnings("checkstyle:JavadocMethod")
  @Deprecated
  public void transform(byte[][] dstBufs, TJTransform[] transforms,
                        int flags) throws TJException {
    processFlags(flags);
    transform(dstBufs, transforms);
  }

  /**
   * Losslessly transform the JPEG source image associated with this
   * transformer instance and return an array of {@link TJDecompressor}
   * instances, each of which has a transformed JPEG image associated with it.
   *
   * @param transforms an array of {@link TJTransform} instances, each of
   * which specifies the transform parameters and/or cropping region for the
   * corresponding transformed JPEG image
   *
   * @return an array of {@link TJDecompressor} instances, each of
   * which has a transformed JPEG image associated with it.
   */
  public TJDecompressor[] transform(TJTransform[] transforms)
                                    throws TJException {
    byte[][] dstBufs = new byte[transforms.length][];
    if (getWidth() < 1 || getHeight() < 1)
      throw new IllegalStateException("JPEG buffer not initialized");
    int srcSubsamp = get(TJ.PARAM_SUBSAMP);
    for (int i = 0; i < transforms.length; i++) {
      int w = getWidth(), h = getHeight();
      int dstSubsamp = srcSubsamp;

      if ((transforms[i].options & TJTransform.OPT_GRAY) != 0)
        dstSubsamp = TJ.SAMP_GRAY;
      if (transforms[i].op == TJTransform.OP_TRANSPOSE ||
          transforms[i].op == TJTransform.OP_TRANSVERSE ||
          transforms[i].op == TJTransform.OP_ROT90 ||
          transforms[i].op == TJTransform.OP_ROT270) {
        w = getHeight();  h = getWidth();
        if (dstSubsamp == TJ.SAMP_422)
          dstSubsamp = TJ.SAMP_440;
        else if (dstSubsamp == TJ.SAMP_440)
          dstSubsamp = TJ.SAMP_422;
        else if (dstSubsamp == TJ.SAMP_411)
          dstSubsamp = TJ.SAMP_441;
        else if (dstSubsamp == TJ.SAMP_441)
          dstSubsamp = TJ.SAMP_411;
      }

      if ((transforms[i].options & TJTransform.OPT_CROP) != 0) {
        if (transforms[i].width != 0) w = transforms[i].width;
        if (transforms[i].height != 0) h = transforms[i].height;
      }
      dstBufs[i] = new byte[TJ.bufSize(w, h, dstSubsamp)];
    }
    TJDecompressor[] tjd = new TJDecompressor[transforms.length];
    transform(dstBufs, transforms);
    for (int i = 0; i < transforms.length; i++)
      tjd[i] = new TJDecompressor(dstBufs[i], transformedSizes[i]);
    return tjd;
  }

  /**
   * @deprecated Use {@link #set TJDecompressor.set()} and
   * {@link #transform(TJTransform[])} instead.
   */
  @SuppressWarnings("checkstyle:JavadocMethod")
  @Deprecated
  public TJDecompressor[] transform(TJTransform[] transforms, int flags)
                                    throws TJException {
    processFlags(flags);
    return transform(transforms);
  }

  /**
   * Returns an array containing the sizes of the transformed JPEG images
   * (in bytes) generated by the most recent transform operation.
   *
   * @return an array containing the sizes of the transformed JPEG images
   * (in bytes) generated by the most recent transform operation.
   */
  public int[] getTransformedSizes() {
    if (transformedSizes == null)
      throw new IllegalStateException("No image has been transformed yet");
    return transformedSizes;
  }

  private native void init() throws TJException;

  private native int[] transform(byte[] srcBuf, int srcSize, byte[][] dstBufs,
    TJTransform[] transforms) throws TJException;

  static {
    TJLoader.load();
  }

  private int[] transformedSizes = null;
}
