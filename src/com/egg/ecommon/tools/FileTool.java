package com.egg.ecommon.tools;

/**
 * 文件工具类
 * <p>Title: FileUtil.java</p>
 * <p>Description: FileUtil</p>
 * <p>Copyright: Copyright(c) 2013，保留所有权利。</p>
 * <p>Company: 班班通（天津）科技发展有限公司</p>
 * @author MXD
 * @version 1.0
 * ======================== change log ==========================
 * 2014-3-13 上午10:54:18 Create File
 * ======================== change log ==========================
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
			FileTool.closeQuickly(out);
			FileTool.closeQuickly(in);
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
