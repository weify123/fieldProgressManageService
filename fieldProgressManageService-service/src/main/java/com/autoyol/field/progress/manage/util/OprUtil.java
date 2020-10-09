package com.autoyol.field.progress.manage.util;

import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.response.SysDictRespVo;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static java.util.stream.IntStream.range;

public class OprUtil {

    public static final String STRING_NULL = "null";

    public static <P, P1> boolean objEquals(final P p1, final P1 p2,
                                            final BiFunction<P, P1, Boolean> opr) {
        return Optional.ofNullable(p1)
                .filter(x -> Objects.nonNull(p2))
                .map(str -> opr.apply(str, p2))
                .orElse(false);
    }

    public static <P, P1> boolean isChange(final P p1, final P1 p2,
                                           final BiFunction<P, P1, Boolean> opr) {
        if (Objects.isNull(p1)) {
            return Objects.nonNull(p2);
        }
        return Optional.of(p1)
                .filter(x -> Objects.nonNull(p2))
                .map(str -> opr.apply(str, p2))
                .orElse(true);
    }

    public static <T, P> boolean hitSearch(final P searchName, final T str,
                                           final BiFunction<T, P, Boolean> opr) {
        return Optional.ofNullable(searchName)
                .filter(p -> Objects.nonNull(str))
                .map(inputName -> opr.apply(str, inputName))
                .orElse(true);
    }

    public static <T extends List, P> boolean hitListSearch(final T list, final P searchName,
                                                            final BiFunction<T, P, Boolean> opr) {
        return Optional.ofNullable(searchName)
                .filter(ls -> !CollectionUtils.isEmpty(list))
                .map(inputName -> opr.apply(list, inputName))
                .orElse(true);
    }

    public static <T extends List, P> boolean matchListSearch(final T list, final P param,
                                                              final BiFunction<T, P, Boolean> opr) {
        if (StringUtils.isEmpty(param)) {
            return true;
        }

        return Optional.of(param)
                .filter(ls -> !CollectionUtils.isEmpty(list))
                .map(inputName -> opr.apply(list, inputName))
                .orElse(false);
    }

    public static <T extends List, P> boolean matchCityListSearch(final T list, final P o,
                                                                  final BiFunction<T, P, Boolean> opr) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        return Optional.of(list)
                .filter(ls -> !StringUtils.isEmpty(o))
                .map(ls -> opr.apply(ls, o))
                .orElse(false);
    }

    public static <C> String getTypeValue(final C code, final String key, final Map<String, List<SysDictRespVo>> map) {
        return getObjOrDefault(map.get(key), ls -> ls.stream().filter(dict -> dict.getCode().equals(code)).findFirst()
                .orElse(new SysDictRespVo())
                .getLabel(), STRING_EMPTY);
    }

    public static String getLabelByCode(List<SysDictEntity> dictList, String status) {
        if (StringUtils.isEmpty(status) || STRING_NULL.equals(status) || CollectionUtils.isEmpty(dictList)) {
            return STRING_EMPTY;
        }
        return dictList.stream()
                .filter(dict -> dict.getCode().equals(Integer.parseInt(status))).findFirst()
                .map(SysDictEntity::getLabel).orElse(STRING_EMPTY);
    }

    public static String getLabelByLabel1(List<SysDictEntity> dictList, String status) {
        if (StringUtils.isEmpty(status) || STRING_NULL.equals(status) || CollectionUtils.isEmpty(dictList)) {
            return STRING_EMPTY;
        }
        return dictList.stream()
                .filter(dict -> status.equals(dict.getLabel1())).findFirst()
                .map(SysDictEntity::getLabel).orElse(STRING_EMPTY);
    }

    public static String getLabelByCodeAndLabel1(List<SysDictEntity> dictList, List<Integer> codeList, String label1) {
        if (CollectionUtils.isEmpty(dictList)) {
            return STRING_EMPTY;
        }
        return dictList.stream()
                .filter(dict -> dict.getLabel1().equals(label1))
                .filter(dict -> codeList.contains(dict.getCode()))
                .map(SysDictEntity::getLabel).reduce((x, y) -> x + SPLIT_COMMA + y).orElse(STRING_EMPTY);
    }

    public static int getCodeByLabel(List<SysDictEntity> dictList, String label) {
        if (StringUtils.isEmpty(label) || STRING_NULL.equals(label) || CollectionUtils.isEmpty(dictList)) {
            return NEG_ONE;
        }
        return dictList.stream()
                .filter(dict -> dict.getLabel().equals(label)).findFirst()
                .map(SysDictEntity::getCode).orElse(NEG_ONE);
    }

    public static int getCodeByLabel1(List<SysDictEntity> dictList, String label) {
        if (StringUtils.isEmpty(label) || STRING_NULL.equals(label) || CollectionUtils.isEmpty(dictList)) {
            return NEG_ONE;
        }
        return dictList.stream()
                .filter(dict -> label.equals(dict.getLabel1())).findFirst()
                .map(SysDictEntity::getCode).orElse(NEG_ONE);
    }

    public static String getLabel1ByCode(List<SysDictEntity> dictList, String status) {
        if (StringUtils.isEmpty(status) || STRING_NULL.equals(status) || CollectionUtils.isEmpty(dictList)) {
            return null;
        }
        return dictList.stream()
                .filter(dict -> dict.getCode().equals(Integer.parseInt(status))).findFirst()
                .map(SysDictEntity::getLabel1).orElse(STRING_EMPTY);
    }

    public static <P, T> T getObjOrDefault(P param, Function<P, T> function, T s) {
        boolean isTrue = param instanceof String && (org.apache.commons.lang3.StringUtils.isEmpty((String) param) ||
                STRING_NULL.equalsIgnoreCase((String) param));
        if (isTrue) {
            return null;
        }
        return Optional.ofNullable(param).map(function).orElse(s);
    }

    public static <T> List<T> getList(String str, String split, Function<String, T> function) {
        return Optional.ofNullable(str)
                .map(s -> Stream.of(org.apache.commons.lang3.StringUtils.split(s, split))
                        .map(function).collect(Collectors.toList()))
                .orElse(null);
    }

    public static <O, T> List<T> getList(List<O> list, Function<O, T> function) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(function).collect(Collectors.toList());
    }

    public static int getRet(String[] v1Str, String[] v2Str) {
        int ret = 0;
        for (int i = 0; i < Integer.min(v1Str.length, v2Str.length); i++) {
            if (Integer.parseInt(v1Str[i]) > Integer.parseInt(v2Str[i])) {
                ret = 1;
                break;
            }
            if (Integer.parseInt(v1Str[i]) < Integer.parseInt(v2Str[i])) {
                ret = -1;
                break;
            }
        }
        return ret;
    }

    public static String getTypeStrBuilder(String... strings) {
        StringBuffer typeBuilder = new StringBuffer();
        Stream.of(strings).forEach(str -> {
            typeBuilder.append(str);
            typeBuilder.append(SPLIT_COMMA);
        });
        return typeBuilder.substring(0, typeBuilder.length() - 1);
    }

    public static boolean isAllLessThanZero(AtomicInteger... atomics) {
        return Stream.of(atomics).map(AtomicInteger::get).reduce(Integer::sum).orElse(NEG_ZERO) <= NEG_ZERO;
    }

    public static AtomicInteger getMultiCount(Stream<String> stringStream) {
        Map<String, Long> mobileMap = stringStream
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        AtomicInteger count = new AtomicInteger();
        mobileMap.forEach((k, v) -> {
            if (v > INT_ONE) {
                count.getAndIncrement();
            }
        });
        return count;
    }

    public static Integer countBatch(Integer size) {
        return (size + INT_THOUSAND - 1) / INT_THOUSAND;
    }

    public static int getMark(List<SysDictEntity> dictEntities, Integer reqStatus) {
        return Optional.ofNullable(dictEntities).filter(x -> Objects.nonNull(reqStatus)).map(dictList ->
                dictList.stream().map(dict -> {
                    if (Objects.nonNull(dict.getLabel1()) && Stream.of(dict.getLabel1().split(SPLIT_COMMA))
                            .mapToInt(Integer::parseInt)
                            .anyMatch(reqStatus::equals)) {
                        return dict.getCode();
                    }
                    return NEG_ONE;
                }).filter(code -> code > NEG_ONE).findFirst().orElse(NEG_ZERO)
        ).orElse(NEG_ZERO);
    }

    public static int getPageNo(int pageNo, int pages) {
        if (pageNo < INT_ONE) {
            return INT_ONE;
        }
        return Optional.of(pageNo).filter(no -> no <= pages).orElse(pages);
    }

    public static String division(int a, int b) {
        String result = "";
        float num = (float) a / b;
        DecimalFormat df = new DecimalFormat("0.0");
        result = df.format(num);
        return result;
    }

    public static <T, S> T convertBean(T target, S source) {
        try {
            ConvertBeanUtil.copyProperties(target, source);
            return target;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> void setTackBackLog(String field, T val1, T val2, StringBuffer buffer) {
        buffer.append(field);
        buffer.append("从");
        buffer.append(val1);
        buffer.append("变更为");
        buffer.append(val2);
        buffer.append(SPLIT_SEMICOLON);
    }

    public static <T> List<String> buildTypeList(String... fieldArray) {
        return Stream.of(fieldArray).collect(Collectors.toList());
    }

    public static <T> void setTackBackLog(String field, T val1, StringBuffer buffer) {
        buffer.append("\"").append(field).append("\"");
        buffer.append(":");
        buffer.append("\"").append(val1).append("\"");
        buffer.append(SPLIT_SEMICOLON);
    }

    public static <T, R> R getLastVal(T obj, Predicate<T> predicate, Function<T, R> function, R defaultStr) {
        return Optional.ofNullable(obj).filter(predicate).map(function).orElse(defaultStr);
    }

    public static <T, R> R getObj(List<T> list, Predicate<T> predicate, Function<T, R> function, R defaultObj) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().filter(predicate).map(function).findFirst().orElse(defaultObj);
    }

    public static String getStationStr(Map<String, List<SysDictRespVo>> map, String station) {
        if (org.apache.commons.lang.StringUtils.contains(station, SPLIT_AND)) {
            return Optional.of(station)
                    .map(stationTmp -> Arrays.asList(station.split(SPLIT_AND)))
                    .map(list -> {
                        StringBuilder allStationBuilder = new StringBuilder();
                        list.forEach(s -> {
                            allStationBuilder.append(getTypeValue(Integer.parseInt(s), STATION_TYPE, map));
                            allStationBuilder.append(SPLIT_AND);
                        });
                        return allStationBuilder.toString().substring(0, allStationBuilder.toString().length() - 1);
                    }).orElse(STRING_EMPTY);

        } else {
            return org.apache.commons.lang.StringUtils.isBlank(station) ? STRING_EMPTY : getTypeValue(Integer.parseInt(station), STATION_TYPE, map);
        }
    }

    public static String getStationCodeStr(String stationChinese, List<SysDictEntity> dictList) {
        if (StringUtils.isEmpty(stationChinese) || CollectionUtils.isEmpty(dictList)){
            return null;
        }
        List<String> stationList = getList(stationChinese, SPLIT_AND, Function.identity());
        return stationList.stream().map(station -> dictList.stream()
                .filter(dict -> objEquals(dict.getLabel(), station, String::equals))
                .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .map(dict -> String.valueOf(dict.getCode()))
                .reduce((x, y) -> x + SPLIT_AND + y).orElse(null);
    }

    public static String translateTemplate(String content, Map<String, Object> map) {
        String[] strings = org.apache.commons.lang3.StringUtils.split(content, SPLIT_DOLLAR);
        StringBuffer builder = new StringBuffer();
        range(0, strings.length).forEach(i -> {
            builder.append(Optional.ofNullable(map.get(strings[i])).orElse(strings[i]));
        });
        return builder.toString();
    }

    public static int getServiceType(String serviceType) {
        int type;
        switch (serviceType.toLowerCase()) {
            case TACK:
                type = INT_THREE;
                break;
            case BACK:
                type = INT_FOUR;
                break;
            default:
                type = NEG_ZERO;
        }
        return type;
    }

    public static String getServiceType(Integer serviceType) {
        if (Objects.isNull(serviceType)) {
            return null;
        }
        String type;
        switch (serviceType) {
            case INT_THREE:
                type = TACK;
                break;
            case INT_FOUR:
                type = BACK;
                break;
            default:
                type = STRING_EMPTY;
        }
        return type;
    }
}
