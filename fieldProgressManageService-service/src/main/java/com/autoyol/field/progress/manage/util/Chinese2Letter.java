package com.autoyol.field.progress.manage.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/** 
 * @comments
 * @author  zg 
 * @date 2013年12月24日  
 */
public final class Chinese2Letter {   
	private static Logger logger = LoggerFactory.getLogger(Chinese2Letter.class);
  
    /**  
     * 取得给定汉字的首字母（小写）
     * @param chinese 给定的汉字  
     * @return 给定汉字的声母  
     */  
	public static String getFirstLetter(String chinese) {
		if (!StringUtils.hasText(chinese)) {
			return "";
		}
		chinese = getZhFullSpell(chinese);
		chinese = chinese.substring(0, 1);
		return chinese.toLowerCase();
	}
  
	public String getAllFirstLetter(String str) {  
        if (!StringUtils.hasText(str)) {
            return "";  
        }  
  
        String _str = "";  
        for (int i = 0; i < str.length(); i++) {  
            _str = _str + getFirstLetter(str.substring(i, i + 1));  
        }  
  
        return _str;  
    }
	  
    
    /**
     * 返回中文首字母（英文返回空）
     * @param str
     * @return
     */
   public static String getPinYinHeadChar(String str) {
	   String temp = "";
       String demo = "";
       String convert = "";
       for (int j = 0; j < str.length(); j++) {
           char word = str.charAt(j);
           String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
           if (pinyinArray != null) {
               convert += pinyinArray[0].charAt(0);
           } else {
               convert += word;
           }
       }
       for(int i=0;i<convert.length();i++){
           if(convert.charAt(i)>='a' && convert.charAt(i)<='z'){
               //temp=convert.substring(i,i+1).toUpperCase();
        	   temp=convert.substring(i,i+1);
               demo += temp;
           }
       }
       return demo;
    }

    /**
     * 获取每个汉子的开头字母，英文不变
     * @param str
     * @return
     */
   public static String getEachZhFirstChar(String str){
    	StringBuffer pybf = new StringBuffer(); 
        char[] arr = str.toCharArray(); 
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat(); 
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); 
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
        return pybf.toString().replaceAll("\\W", "").trim(); 
    }
    
    /**
     * 获取汉字全拼字母
     * @param str
     * @return
     */
    public static String getZhFullSpell(String str){
		StringBuffer pybf = new StringBuffer();
		char[] arr = str.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if(pinyinArr != null){//有些非中、英文字符会返回null。如：Ⅱ
						pybf.append(pinyinArr[0]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					logger.error("",e);
				}
			} else {
				pybf.append(arr[i]);
			}
		} 
        return pybf.toString(); 
} 

    
    public static void main(String[] args) {   
        Chinese2Letter cte = new Chinese2Letter();
        System.out.println(getFirstLetter("FSDNIOIJOI"));
        System.out.println(getFirstLetter("北京欢迎你"));
        System.out.println(cte.getAllFirstLetter("北京欢迎你"));
    	
    	//System.out.println(getFirstLetter("佳Ⅱ"));
    	System.out.println(getPinYinHeadChar("讴歌MG"));
    }   

}
 