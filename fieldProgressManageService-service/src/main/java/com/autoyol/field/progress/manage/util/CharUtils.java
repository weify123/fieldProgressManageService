package com.autoyol.field.progress.manage.util;

import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class CharUtils {

	private static final String regEx_all = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_html = "<(?!(A|a|/A|/a))[^>]*>"; // 定义HTML标签的正则表达式
	// private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
	private static final String regEx_space = "\t|\r|\n";// 定义空格回车换行符
	private static final String reg_start_A = "<A";// 定义空格回车换行符
	private static final String reg_end_A = "</A>";// 定义空格回车换行符

	private static final int max_text = 140;// 内容多少个字

	public static String get(String content) {
		// 1: 粗加工数据保留a标签
		String str = delHTMLTag(content);// 粗加工的breaf
		Pattern p_html = Pattern.compile(regEx_all, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(str);

		String aText = m_html.replaceAll("@@");
		System.out.println(aText);

		String[] arrs = aText.split("@@");
		int len = arrs.length;
		int sum = 0;
		String text = null;
		for (int i = 0; i < len; i++) {
			String arr = arrs[i];
			if (!StringUtils.hasText(arrs[i])) {
				continue;
			}

			int preSum = sum;
			sum += arr.length();
			if (i == 0 && sum > max_text) {
				text = arr.substring(0, max_text);
				break;
			} else if (i % 2 == 1 && sum > max_text) {//a标签中的内容
				text = str.substring(0, str.indexOf(arr));
				text = text.substring(0, text.lastIndexOf("</a>") + 4);
				break;
			} else if (i % 2 == 0 && sum > max_text) {//a标签外的内容
				int tampLen = max_text - preSum;
				String tampText = arr.substring(0, tampLen);
				text = str.substring(0, str.indexOf(tampText) + tampLen);
				break;
			} else {
				text = str;
			}
			
			System.out.println("i:" + i + ", arr:" + arr + ", sum: " + sum);
		}

		return text;
	}

	public static void main(String[] args) {
		// String s = "<p><a href='javascript:;' target='topic:16'>#奥迪A6的话题#</a><a
		// href='javascript:;' target='car:110'>Smart - Fortwo(进口)</a></p><div
		// class='media-wrap image-wrap'><img
		// src='https://at-images-test.oss-cn-hangzhou.aliyuncs.com/community/1523869226451.jpg'
		// width='auto' height='auto' style='width:auto;height:auto'/></div><p
		// style='text-align:center;'></p><ul><li>aaaaaa</li><li>bbbbbb</li></ul><ol><li>cccccc</li><li>dddddd</li></ol><p></p><p><span
		// style='font-size:24px'>全新奥迪</span>A4L的长、宽、高分别达到5,015毫米、1,874毫米、1,455</p><p>毫米，<strong><span
		// style='color:#c0392b'>轴距达3,012毫</span></strong>米，相比上一代车型，宽度增加19毫米、轴距增加67毫米，而长度和高度<sup><em>则分别减</em>少了2<strong>0毫米和30</strong>毫米。简单地说，全</sup></p><p>新奥迪A6L，带给人们更加轻盈与优雅的<sub>感觉，拥有</sub>更加大气的身量。</p>";
		String s = "<img src='https://at-images-test.oss-cn-hangzhou.aliyuncs.com/community/1523865317992.jpg'><a href='javascript:;'>#什么话题什么话题#</a><p>这是测试帖子</p><p>测试帖子</p><p><br></p><div role='split' class='split'></div><p><br></p>";
		String c = get(s);
		System.out.println(c);
	}

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delHTMLTag(String htmlStr) {
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签

		Pattern psA = Pattern.compile(reg_start_A, Pattern.CASE_INSENSITIVE);
		Matcher msA = psA.matcher(htmlStr);
		htmlStr = msA.replaceAll("<a"); //

		Pattern peA = Pattern.compile(reg_end_A, Pattern.CASE_INSENSITIVE);
		Matcher meA = peA.matcher(htmlStr);
		htmlStr = meA.replaceAll("</a>"); // 过滤空格回车标签
		htmlStr = htmlStr.trim(); // 返回文本字符串

		return htmlStr;
	}

	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		return htmlStr;
	}

	public static String decodeUrlCode(String decodeStr) throws Exception {
		if (!StringUtils.hasText(decodeStr)) {
			return null;
		}

		decodeStr = URLDecoder.decode(decodeStr, "UTF-8");
		return decodeStr;
	}

	public static String createAsterisk(String name){
		if(StringUtils.isEmpty(name)){
			return name;
		}

		if (name.length() <= 1) {
			return name;
		}else {
			return name.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + dealAsterisk(name.length() - 1));
		}
	}

	public static String dealAsterisk(int length) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			stringBuffer.append("*");
		}
		return stringBuffer.toString();
	}



}
