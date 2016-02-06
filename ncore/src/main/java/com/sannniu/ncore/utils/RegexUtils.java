package com.sannniu.ncore.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式-工具类
 */
public class RegexUtils {

    /**
     * 检查收货人输入格式
     * 
     * @param cons_name_data
     *            收货人
     * @return
     */
    public static boolean isConsNameYes(String cons_name_data) {
        String strPattern = "^(?!·)(?!.*?·$)(?!•)(?!.*?•$)[A-Za-z•·\u4e00-\u9fa5]+$";
        return isBase(cons_name_data, strPattern);
    }

    /**
     * 检查电话号码(固话+手机号)
     * 
     * @param mobile_numer
     *            电话号码
     * @return
     */
    public static boolean isPhone(String mobile_numer) {
        String strPattern = "^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[0-9]\\d{9})$";
        return isBase(mobile_numer, strPattern);
    }

    /**
     * 校验手机号码
     * 
     * @param strMobile
     * @return
     */
    public static boolean isMobile(String strMobile) {
        String strPattern = "^(0?1[0-9]\\d{9})$";
        return isBase(strMobile, strPattern);
    }

    /**
     * 检查邮箱
     * 
     * @param strEmail
     *            邮箱地址
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        return isBase(strEmail, strPattern);
    }

    /**
     * 校验汉字姓名【2-6个中文】
     * 
     * @param string
     * @return
     */
    public static boolean isChineseName(String string) {
        String strPattern = "^[\u4e00-\u9fa5]{2,6}$";
        return isBase(string, strPattern);
    }
    
    /**
     * 
     * @Description: 判断是否是中文
     * @author zhaofangyi 
     * @param word
     * @return
     * @date 2014-12-17 下午4:45:45
     */
    public static boolean isChineseWord(String word) {
        String strPattern = "^[\u4e00-\u9fa5]+";
        return isBase(word, strPattern);
    }
    
    /**
     * 
     * @Description: 过滤中文
     * @author zhaofangyi 
     * @param word
     * @return
     * @date 2014-12-17 下午4:47:41
     */
    public static String filterChinese(String word){
        word = word.replaceAll("[^(\\u4e00-\\u9fa5)]", "");
        return word;
    }
    
    /**
     * 
     * @Description: 是否含有非法字符
     * @author zhaofangyi 
     * @param word
     * @return
     * @date 2014-12-17 下午5:37:00
     */
    public static boolean isLegalWord(String word){
        String strPattern = "^[A-Za-z/\u4e00-\u9fa5]+";
        return isBase(word, strPattern);
    }
    
    public static boolean isEmolyeeNum(String string){
        String strPattern = "^[A-Za-z0-9]+$";
        return isBase(string, strPattern);
    }

    
    public static boolean isMobileCheckCode(String string){
        String strPattern = "^[A-Za-z0-9]{6}$";
        return isBase(string, strPattern);
    }
    
    public static String hideMobileinfo(String mobileNum){
        
        return  mobileNum.subSequence(0, 3) + "****"
                + mobileNum.substring(mobileNum.length() - 4, mobileNum.length());
    }
    /**
     * 基础正则表达式
     * 
     * @param message
     * @param pattern
     * @return
     */
    private static boolean isBase(String message, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(message);
        return m.matches();
    }
    /**
     * @Description: 验证出生日期
     * @author mahaifeng 
     * @param birthday
     * @return
     * @date 2014-12-19 上午10:31:02
     */
    public static boolean birthdayCheck(String birthday){
    	String year = birthday.substring(0, 4);
		String month = birthday.substring(4, 6);
		String day = birthday.substring(6, 8);
		String dateStr = year + "-" + month + "-" + day;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		boolean flag=true;
		try {
			Date date = df.parse(dateStr);
			if(date==null) flag=false;
		} catch (Exception e) {
			flag=false;
		}
		if(flag){
			int nowYear=Calendar.getInstance().get(Calendar.YEAR);
			if(!(1900<=Integer.valueOf(year)&&Integer.valueOf(year)<=nowYear&&
					1<=Integer.valueOf(month)&&Integer.valueOf(month)<=12&&1<=Integer.valueOf(day)&&Integer.valueOf(day)<=31)){
				return false;
			}
			return true;
		}else{
			return false;
		}
    }
}
