package fr.smile.retailer.utils;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class CustomNumberUtils {
	
	public static BigDecimal createBigDecimal(String str) {
        if (StringUtils.isBlank(str)) {
            return BigDecimal.valueOf(0);
        }
        // handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }  
        return new BigDecimal(str);
    }
	
    public static Integer createIntegerViaDouble(String str) {
        if (StringUtils.isBlank(str)) {
            return Integer.valueOf(0);
        }
        // decode() handles 0xAABD and 0777 (hex and octal) as well.
        return NumberUtils.createDouble(str).intValue();
    }

}
