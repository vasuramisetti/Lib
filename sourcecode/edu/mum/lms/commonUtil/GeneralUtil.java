package edu.mum.lms.commonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class GeneralUtil {

    private static Logger log = EnvironmentUtil.loggerForThisClass();

    private static final double NA = -999.99;
    
    private static final long WORD_MASK = 0x00000000000fffffL;
    private static final int WORD_SIZE = 20;
    


    public static void main(String[] argv) throws Exception {  
        
        
        
        //Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        //System.out.println(CalendarUtil.getCalendarString(cal));
       

    }

    public static String getUserInput(String prompt){
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);
       String line = scanner.nextLine();
       scanner.close();
       return line;

    }

    public static boolean validateEmail(String addr) {
        if (addr == null || addr.length() == 0) {
            return false;
        }
        //return Pattern.matches(".*@.*\\..*", addr);
        return Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", addr);
    }

    public static boolean isNumber(String arg){
        return arg.matches("^[0-9]+([.][0-9]+)?$");
    }
    
    public static double roundTwoPlaces(double n) {
        return roundDecimalPlaces(n, 2);
    }

    public static double roundThreePlaces(double n) {
        return roundDecimalPlaces(n, 3);
    }

    private static double roundDecimalPlaces(double value, int decimalPoint) {
        if(Double.isInfinite(value) || Double.isNaN(value)){
            return 0;
        }
        BigDecimal bigDecimal   =   new BigDecimal(value);
        bigDecimal  =   bigDecimal.setScale(decimalPoint, RoundingMode.HALF_EVEN);
        return bigDecimal.doubleValue();
    }
    
}

