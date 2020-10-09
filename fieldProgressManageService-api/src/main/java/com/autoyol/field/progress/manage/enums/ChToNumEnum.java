package com.autoyol.field.progress.manage.enums;

import com.autoyol.commons.utils.StringUtils;

import java.util.stream.Stream;

public enum ChToNumEnum {
    ONE("一", 1),
    TWO("二", 2),
    THREE("三", 3),
    FOUR("四", 4),
    FIVE("五", 5),
    SIX("六", 6),
    SEVEN("七", 7),
    EIGHT("八", 8),
    NINE("九", 9),
    ;
    String ch;
    Integer num;

    ChToNumEnum(String ch, Integer num) {
        this.ch = ch;
        this.num = num;
    }

    public String getCh() {
        return ch;
    }

    public Integer getNum() {
        return num;
    }

    public static ChToNumEnum getByCh(String ch) {
        if (StringUtils.isEmpty(ch)) {
            return null;
        }
        return Stream.of(ChToNumEnum.values()).filter(chToNumEnum -> chToNumEnum.getCh().equals(ch)).findFirst().orElse(null);
    }
}
