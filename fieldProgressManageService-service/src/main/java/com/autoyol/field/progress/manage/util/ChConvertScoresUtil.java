package com.autoyol.field.progress.manage.util;

import com.autoyol.field.progress.manage.enums.ChToNumEnum;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ChConvertScoresUtil {
    private static final String[] SEPARATOR_ARRAY = {"分之", "/", "╱"};
    private static final String[] KILO = {"km", "公里"};
    private static final String EQUAL = "=";
    private static final Pattern pattern = Pattern.compile("[0-9]*");
    private static final Pattern pattern_num = Pattern.compile("\\d+(\\.\\d)?");
    public static final String ONE_NINE_ZERO = "1900";

    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        if (str.indexOf(".") > 0) {
            if (str.indexOf(".") == str.lastIndexOf(".") && str.split("\\.").length == 2) {
                return pattern.matcher(str.replace(".", "")).matches();
            } else {
                return false;
            }
        } else {
            return pattern.matcher(str).matches();
        }
    }


    private static String[] getStrArr(String str) {
        if (StringUtils.contains(str, SEPARATOR_ARRAY[0])) {
            if (StringUtils.contains(str, ".")) {
                str = StringUtils.substring(str, 0, 6);
            } else {
                str = StringUtils.substring(str, 0, 4);
            }
        } else {
            str = StringUtils.substring(str, 0, 3);
        }
        String finalStr = str;
        return Stream.of(SEPARATOR_ARRAY)
                .filter(x -> StringUtils.contains(finalStr, x))
                .map(x -> StringUtils.split(finalStr, x))
                .findFirst().orElse(null);
    }

    private static Double divide(Double a, Double b) {
        return Optional.of(a).filter(x -> x > b).map(x -> b / x).orElse(a / b);
    }

    private static Double getScore(String[] strArr) {
        return Stream.of(strArr)
                .filter(ChConvertScoresUtil::isNumeric)
                .map(Double::new).reduce(ChConvertScoresUtil::divide)
                .orElseGet(() -> {
                    ChToNumEnum chToNumEnum = ChToNumEnum.getByCh(strArr[0]);
                    if (Objects.isNull(chToNumEnum)) {
                        return null;
                    }
                    int fenMu = chToNumEnum.getNum();

                    ChToNumEnum chEnum = ChToNumEnum.getByCh(strArr[1]);
                    if (Objects.isNull(chEnum)) {
                        return null;
                    }
                    int fenZi = chEnum.getNum();
                    return divide((double) fenMu, (double) fenZi);
                });
    }

    public static Double convertOil(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (StringUtils.contains(str, "一半") || StringUtils.contains(str, "半箱")) {
            return 0.5;
        }
        if (StringUtils.contains(str, "满")) {
            return 1.0;
        }
        if (StringUtils.contains(str, "~") || StringUtils.contains(str, "-") ||
                StringUtils.contains(str, "，") || StringUtils.contains(str, "？")) {
            return null;
        }
        String[] s1 = getStrArr(str);
        if (Objects.isNull(s1)) {
            return null;
        }
        return getScore(s1);
    }

    public static String convertKilo(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (StringUtils.containsIgnoreCase(str, "km") || StringUtils.contains(str, "公里")) {
            String[] tmp = Stream.of(KILO)
                    .filter(x -> StringUtils.contains(str, x))
                    .map(x -> StringUtils.split(str, x))
                    .findFirst().orElse(null);
            if (Objects.isNull(tmp) || !isNumeric(tmp[0])){
                return null;
            }
            return tmp[0];
        }
        return null;
    }

    public static Double convertFeeToDouble(String str){
        try {
            if (StringUtils.isEmpty(str)){
                return null;
            }
            if (StringUtils.contains(str, EQUAL)){
                return Double.parseDouble(StringUtils.split(str, EQUAL)[1]);
            }
            Matcher matcher = pattern_num.matcher(str);
            List<Double> doubleList = Lists.newArrayList();
            while (matcher.find()) {
                doubleList.add(Double.parseDouble(matcher.group()));
            }
            if (CollectionUtils.isEmpty(doubleList)){
                return null;
            }
            return doubleList.stream().reduce(Double::sum).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal convertGuidePrice(String str){
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            BigDecimal decimal = new BigDecimal(str);
            if (decimal.compareTo(BigDecimal.valueOf(73582)) > 0){
                return null;
            }
            return decimal;
        } catch (Exception e) {
            return null;
        }
    }
    public static void main(String[] args) {
        System.out.println(convertOil("4分之1.2"));
        System.out.println(convertKilo("ss公里"));
        System.out.println(convertFeeToDouble("28+摩的10"));
        System.out.println(convertGuidePrice("20190914163000"));
    }
}
