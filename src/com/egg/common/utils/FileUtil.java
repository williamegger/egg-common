package com.egg.common.utils;

/**
 * 文件工具类
 */
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.egg.common.log.LogKit;

public class FileUtil {

	private static final int BUFFER_SIZE = 8192;

	/**
	 * 拷贝
	 */
	public static long copy(InputStream in, OutputStream out, boolean closeOut) throws IOException {
		long total = 0;
		try {
			byte[] b = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(b)) > 0) {
				out.write(b, 0, len);
				total += len;
			}
			out.flush();
		} finally {
			if (closeOut) {
				closeQuickly(out);
			}
			closeQuickly(in);
		}
		return total;
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
	 * 保存文件
	 */
	public static boolean save(InputStream in, String target) {
		return save(in, new File(target));
	}

	/**
	 * 保存文件
	 */
	public static boolean save(InputStream in, File file) {
		OutputStream out = null;
		try {
			boolean b = mkdirs(file);
			if (!b) {
				LogKit.error("创建文件夹失败 [" + file.getParentFile().getPath() + "]");
				return false;
			}

			out = new FileOutputStream(file);
			copy(in, out, true);
			return true;
		} catch (Exception e) {
			LogKit.error("保存文件失败 [" + file.getPath() + "]", e);
			return false;
		} finally {
			closeQuickly(out);
			closeQuickly(in);
		}
	}

	/**
	 * 删除文件或文件夹
	 */
	public static void del(String filepath) {
		File file = new File(filepath);
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (File f : list) {
				del(f.getPath());
			}
		}
		file.delete();
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

	/**
	 * 得到扩展名：.xx
	 */
	public static String ext(String filename) {
		if (filename == null || filename.isEmpty()) {
			return "";
		}
		int ind = filename.lastIndexOf(".");
		if (ind == -1) {
			return "";
		}
		return filename.substring(ind).toLowerCase();
	}

}
