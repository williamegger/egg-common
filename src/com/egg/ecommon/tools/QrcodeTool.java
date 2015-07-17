package com.egg.ecommon.tools;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrcodeTool {
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 生成二维码
	 */
	public static void buildQRCode(String content, int width, int height, OutputStream out) {
		try {
			BitMatrix bitMatrix = getQRBitMatrix(content, width, height, ErrorCorrectionLevel.M);
			writeToStream(bitMatrix, "jpg", out);
		} catch (Exception e) {
			LOG.error(QrcodeTool.class + ".buildQRCode():", e);
		}
	}

	/**
	 * 生成二维码，并添加LOGO
	 */
	public static void buildQRCode4LOGO(String content, int width, int height, OutputStream out, String logopath) {
		BufferedImage logo = null;
		if (StringUtils.isNotBlank(logopath)) {
			try {
				logo = ImageIO.read(new File(logopath));
			} catch (Exception e) {
				LOG.error(QrcodeTool.class + ".buildQRCode4LOGO():", e);
			}
		}

		if (logo == null) {
			buildQRCode(content, width, height, out);
		} else {
			try {
				BitMatrix bitMatrix = getQRBitMatrix(content, width, height, ErrorCorrectionLevel.H);
				BufferedImage qrImg = toBufferedImage(bitMatrix, BLACK, 0xFFFFFFFE);

				int x = (qrImg.getWidth() - logo.getWidth()) / 2;
				int y = (qrImg.getHeight() - logo.getHeight()) / 2;
				Graphics2D graphics = (Graphics2D) qrImg.getGraphics();
				graphics.drawImage(logo, x, y, null);
				graphics.dispose();

				ImageIO.write(qrImg, "jpg", out);
			} catch (Exception e) {
				LOG.error(QrcodeTool.class + ".buildQRCode4LOGO():", e);
			}
		}
	}

	// ------------
	// private
	// ------------
	private static BitMatrix getQRBitMatrix(String content, int width, int height, ErrorCorrectionLevel level) throws Exception {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, level);
		hints.put(EncodeHintType.MARGIN, 1);
		// hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		return new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "ISO-8859-1"),
				BarcodeFormat.QR_CODE, width, height, hints);
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		return toBufferedImage(matrix, BLACK, WHITE);
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix, int on, int off) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? on : off);
			}
		}
		return image;
	}

	private static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		writeToStream(matrix, format, stream, BLACK, WHITE);
	}

	private static void writeToStream(BitMatrix matrix, String format, OutputStream stream, int on, int off)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix, on, off);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}

	private static final Log LOG = LogFactory.getLog(QrcodeTool.class);
}
