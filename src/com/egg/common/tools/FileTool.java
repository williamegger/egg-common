package com.egg.common.tools;

/**
 * 文件工具类
 */
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileTool {

	private static final Log LOG = LogFactory.getLog(FileTool.class);

	/**
	 * 保存文件
	 */
	public static boolean save(InputStream in, String target) {
		if (in == null) {
			return false;
		}
		if (StringUtils.isBlank(target)) {
			return false;
		}
		OutputStream out = null;
		try {
			File file = new File(target);
			boolean b = FileTool.mkdirs(file);
			if (!b) {
				LOG.error(FileTool.class + ".save():创建文件夹失败 [" + file.getParentFile().getPath() + "]");
				return false;
			}
			out = new FileOutputStream(file);
			Streams.copy(in, out, false);
			return true;
		} catch (Exception e) {
			LOG.error(FileTool.class + ".save():保存文件方法异常:", e);
			return false;
		} finally {
			FileTool.quicklyClose(out);
			FileTool.quicklyClose(in);
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
	 * 快速关闭
	 */
	public static void quicklyClose(Closeable obj) {
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

	/**
	 * 得到扩展名：.xxx
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
