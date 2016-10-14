package com.egg.common.utils;

/**
 * 文件工具类
 */
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {

	private static final int BUFFER_SIZE = 8192;
	private static final String UTF8 = "UTF-8";
	private static final String LINE = "\r\n";

	// ----------------------
	// Save Methods
	// ----------------------
	public static long save(String str, String target) throws IOException {
		return save(str, new File(target));
	}

	public static long save(String str, File file) throws IOException {
		mkdirs(file);
		return save(str, new FileOutputStream(file), true);
	}

	public static long save(String str, OutputStream out, boolean closeOut) throws IOException {
		return save(str.getBytes(UTF8), out, closeOut);
	}

	public static long save(byte[] b, String target) throws IOException {
		return save(b, new File(target));
	}

	public static long save(byte[] b, File file) throws IOException {
		mkdirs(file);
		return save(b, new FileOutputStream(file), true);
	}

	public static long save(byte[] b, OutputStream out, boolean closeOut) throws IOException {
		try {
			out.write(b);
			out.flush();
			return b.length;
		} finally {
			if (closeOut) {
				closeQuickly(out);
			}
		}
	}

	public static long save(InputStream in, String target) throws IOException {
		return save(in, new File(target));
	}

	public static long save(InputStream in, File file) throws IOException {
		return save(in, new FileOutputStream(file), true);
	}

	public static long save(InputStream in, OutputStream out, boolean closeOut) throws IOException {
		try {
			long total = 0;
			byte[] b = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(b)) > 0) {
				total += len;
				out.write(b, 0, len);
				out.flush();
			}
			return total;
		} finally {
			closeQuickly(in);
			if (closeOut) {
				closeQuickly(out);
			}
		}
	}

	// ----------------------
	// Delete File Methods
	// ----------------------
	public static void del(String filepath) {
		del(new File(filepath));
	}

	public static void del(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (File f : list) {
				del(f);
			}
		}
		file.delete();
	}

	// --------------------------------
	// File Extension Name Methods
	// --------------------------------
	/**
	 * 得到扩展名，包含“.”
	 */
	public static String getExt(File file) {
		if (file == null) {
			return "";
		}
		return getExt(file.getName());
	}

	/**
	 * 得到扩展名，包含“.”
	 */
	public static String getExt(String name) {
		if (name == null || name.isEmpty()) {
			return "";
		}
		int ind = name.lastIndexOf(".");
		if (ind == -1) {
			return "";
		}
		return name.substring(ind);
	}

	// ----------------------
	// Read Text Methods
	// ----------------------
	public static String readText(String filepath) throws IOException {
		return readText(new File(filepath));
	}

	public static String readText(File file) throws IOException {
		return readText(new FileInputStream(file));
	}

	public static String readText(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append(LINE);
			}
			return sb.toString();
		} finally {
			closeQuickly(reader);
		}
	}

	/**
	 * 快速关闭
	 */
	public static void closeQuickly(Closeable obj) {
		if (obj != null) {
			try {
				obj.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 创建文件所在文件夹
	 */
	public static boolean mkdirs(File file) {
		if (file == null) {
			return false;
		}
		File dir = file.getParentFile();
		if (dir.exists()) {
			return true;
		}
		return dir.mkdirs();
	}

}
