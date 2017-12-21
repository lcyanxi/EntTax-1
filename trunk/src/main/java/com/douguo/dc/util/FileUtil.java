package com.douguo.dc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil {

	private final static Log logger = LogFactory.getLog(FileUtil.class);
	private FileWriter writer;
	private PrintWriter pw;

	public static void main(String[] args) {
		String fileName = "";
		if (args.length > 1) {
			fileName = args[0];
		} else {
			fileName = "/tmp/push/1.txt";
		}

		long startTime = System.currentTimeMillis();
		FileUtil fUtil = new FileUtil(fileName);
		fUtil.testWriteFile();
		fUtil.finish();
		long endTime = System.currentTimeMillis();
		System.out.println("写入文件耗时" + (endTime - startTime) + "毫秒.");
	}
	
	public FileUtil(String fileName,boolean append) {
		try {
			writer = new FileWriter(fileName,append);
			pw = new PrintWriter(new BufferedWriter(writer));
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public FileUtil(String fileName) {
		try {
			writer = new FileWriter(fileName);
			pw = new PrintWriter(new BufferedWriter(writer));
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void writeFile(String s) {
		pw.println(s);
	}

	public void finish() {// 关闭输入流，将文字从缓存写入文件
		try {
			pw.flush();
			writer.close();
		} catch (IOException iox) {
			System.err.println(iox);
			logger.info(iox.getMessage());
		}
	}

	public static Properties getConfig(String fullFilePath) {
		Properties p = new Properties();
		File pFile = new File(fullFilePath); // properties文件
		FileInputStream pInStream = null;
		try {
			pInStream = new FileInputStream(pFile);
			p.load(pInStream); // Properties 对象已生成，包括文件中的数据
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("plase check the file " + fullFilePath
					+ " is exist!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return p;
	}
	
	public static void checkFile(String filePath) {
		File f = new File(filePath);
		if (!f.exists()) {
			if (filePath.endsWith("/")) {
				f.mkdirs();
			} else {
				try {
					f.createNewFile();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public void testWriteFile() {
		long totalStart = System.currentTimeMillis();
		try {
			int num = 0; // 记录写文件写了多少行
			int rows = 10000000;
			long startTime = System.currentTimeMillis();
			while (rows > 0) {
				rows--;
				writeFile("1234567890123456789012345678901234567890123456789012345678904321");
				num++;
				if (num % 100000 == 0) {
					long endTime = System.currentTimeMillis();
					System.out.println("写入文件第" + num + "行，耗时"
							+ (endTime - startTime) + "毫秒.");
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			finish(); // 关闭输入流
		}
		long totalEnd = System.currentTimeMillis();
		System.out.println("----总耗时：" + (totalEnd - totalStart) + "毫秒");
	}
}