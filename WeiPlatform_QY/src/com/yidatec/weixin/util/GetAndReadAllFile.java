package com.yidatec.weixin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author jishu_vip
 * 
 */
public class GetAndReadAllFile {


	/**
	 * 获取文件的扩展名
	 * 
	 * @param filename
	 * @param defExt
	 * @return
	 */
	public static String getExtension(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');


			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1);
			}
		}
		return defExt;
	}


	public static String getExtension(String filename) {
		return getExtension(filename, "");
	}


	/**
	 * 获取一个文件夹下的所有文件 要求：后缀名为txt (可自己修改)
	 * 
	 * @param file
	 * @return
	 */
	public static List<String> getFileList(File file,final String fileType) {
		List<String> result = new ArrayList<String>();
		if (!file.isDirectory()) {
			System.out.println(file.getAbsolutePath());
			result.add(file.getAbsolutePath());
		} else {
			// 内部匿名类，用来过滤文件类型
			File[] directoryList = file.listFiles(new FileFilter() {
				public boolean accept(File file) {
					//int len = file.getName().indexOf(".");
					//String csvtype = file.getName().substring(len + 1);
					String csvtype = getExtension(file.getName());
					if (file.isFile() && file.getName().indexOf(fileType) > -1 && fileType.equals(csvtype)) {
						return true;
					} else {
						return false;
					}
				}
			});
			for (int i = 0; i < directoryList.length; i++) {
				result.add(directoryList[i].getAbsolutePath());
			}
		}
		return result;
	}


	/**
	 * 以UTF-8编码方式读取文件内容
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String getContentByLocalFile(File path) throws IOException {
		InputStream input = new FileInputStream(path);
		InputStreamReader reader = new InputStreamReader(input, "utf-8");
		BufferedReader br = new BufferedReader(reader);
		StringBuilder builder = new StringBuilder();
		// System.out.print(trimExtension(path.getName())+" ");


		String temp1 = br.readLine();
		String temp2 = br.readLine();
		while (temp2 != null) {
			// builder.append(temp);
			temp1 = temp2;
			temp2 = br.readLine();
		}
		return temp1;
	}


	/**
	 * 去掉文件的扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String trimExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length()))) {
				return filename.substring(0, i);
			}
		}
		return filename;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// Test get and read all file
		List<String> fileList = getFileList(new File("D:/FTP Site"),"csv");
		String fileContent = null;
		String[] content = null;

        System.out.println(fileList.get(0).toString());
		
		System.out.println("**" + fileList.size());
		for (String s : fileList) {
			// 打印文件名
			int cnt = 0;
//			boolean flag = false;
//			for (int i = 0; i < s.length(); i++) {
//				if (flag == true&&s.charAt(i) != '.') {
//					System.out.print(s.charAt(i));
//				}
//					if (s.charAt(i) == '.')
//						break;
//					if (s.charAt(i) == '\\') {
//						cnt++;
//						if (cnt == 2)
//							flag = true;
//				}
//			}


			// System.out.println(trimExtension(s));
			// 文件内容
			fileContent = getContentByLocalFile(new File(s));
			// 打印文件内容
			// System.out.println(fileContent);
			// 以逗号为单位进行拆分字段
			content = fileContent.split(",");
			System.out.println(content[1]);
		}


		// System.out.println();


	}
}
