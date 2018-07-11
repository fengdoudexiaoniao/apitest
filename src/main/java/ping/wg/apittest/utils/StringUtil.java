package ping.wg.apittest.utils;

public class StringUtil {
	public static boolean isNotEmpty(String str) {
		return null != str && !"".equals(str);
	}

	public static boolean isEmpty(String str) {
		return null == str || "".equals(str);
	}

	/**
	 * 
	 * @param sourceStr 待替换字符串
	 * @param matchStr  匹配字符串
	 * @param replaceStr  目标替换字符串
	 * @return
	 */
	public static String replaceFirst(String sourceStr,String matchStr,String replaceStr){
		//匹配字符串首次出现的位置
		int index = sourceStr.indexOf(matchStr);
		//匹配字符串的长度
		int matLength = matchStr.length();
		//目标字符串长度
		int sourLength = sourceStr.length();
		//截取目标字符串从首字母到匹配字符串出现位置之前的字符串
		String beginStr = sourceStr.substring(0,index);
		//截取从匹配字符串字后的字符串
		String endStr = sourceStr.substring(index+matLength,sourLength);
		//替换字后的字符串
		sourceStr = beginStr+replaceStr+endStr;
		return sourceStr;
	}
}
