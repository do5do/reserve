package com.zerobase.reserve.domain.common.constants;

import java.time.LocalTime;
import java.util.List;

public class StoreConstants {
    public final static String MEMBER_KEY = "b95c5f9e34da4cf9a394955a328775f5";
    public final static String STORE_KEY = "34d95da5f3a6401bb070834306171e94";
    public final static String STORE_NAME = "매장 A";
    public final static String DESCRIPTION = "매장 설명입니다.";
    public final static String PHONE_NUMBER = "010-1234-1234";
    public final static String ADDRESS = "서울시 자치구 도로명";
    public final static String DETAIL_ADDR = "상세 주소";
    public final static String ZIPCODE = "123-4";
    public final static LocalTime OPER_START = LocalTime.of(10, 0);
    public final static LocalTime OPER_END = LocalTime.of(22, 0);
    public final static List<String> CLOSE_DAYS = List.of("일요일");
}
