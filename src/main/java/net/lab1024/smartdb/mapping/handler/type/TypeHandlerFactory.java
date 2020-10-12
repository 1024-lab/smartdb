package net.lab1024.smartdb.mapping.handler.type;

import net.lab1024.smartdb.SmartDbEnum;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerFactory {

    private static final ConcurrentHashMap<Class, TypeHandler<?>> enumHandlerMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class, TypeHandler<? extends SmartDbEnum>> smartDbEnumHandlerMap = new ConcurrentHashMap<>();
    private static final Map<Class<?>, TypeHandler<?>> typeHandlerMap = new HashMap<Class<?>, TypeHandler<?>>();

    static {
        //boolean
        register(Boolean.class, new BooleanTypeHandler());
        register(boolean.class, new BooleanTypeHandler());
        //byte
        register(Byte.class, new ByteTypeHandler());
        register(byte.class, new ByteTypeHandler());
        //short
        register(Short.class, new ShortTypeHandler());
        register(short.class, new ShortTypeHandler());
        //int
        register(Integer.class, new IntegerTypeHandler());
        register(int.class, new IntegerTypeHandler());
        //long
        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());
        //float
        register(Float.class, new FloatTypeHandler());
        register(float.class, new FloatTypeHandler());
        //double
        register(Double.class, new DoubleTypeHandler());
        register(double.class, new DoubleTypeHandler());
        //char
        register(Character.class, new CharacterTypeHandler());
        register(char.class, new CharacterTypeHandler());
        //string
        register(Reader.class, new ClobReaderTypeHandler());
        register(String.class, new StringTypeHandler());
        //object
        register(Object.class, new ObjectTypeHandler());
        //BigDecimal
        register(BigDecimal.class, new BigDecimalTypeHandler());
        //byte array
        register(InputStream.class, new BlobInputStreamTypeHandler());
        register(Byte[].class, new ByteObjectArrayTypeHandler());
        register(Byte[].class, new BlobByteObjectArrayTypeHandler());
        register(Byte[].class, new BlobByteObjectArrayTypeHandler());
        register(byte[].class, new ByteArrayTypeHandler());
        register(byte[].class, new BlobTypeHandler());
        register(byte[].class, new BlobTypeHandler());
        //Date
        register(Date.class, new DateTypeHandler());
        register(java.sql.Date.class, new SqlDateTypeHandler());
        register(java.sql.Time.class, new SqlTimeTypeHandler());
        register(java.sql.Timestamp.class, new SqlTimestampTypeHandler());

        register(Instant.class, new InstantTypeHandler());
        register(LocalDateTime.class, new LocalDateTimeTypeHandler());
        register(LocalDate.class, new LocalDateTypeHandler());
        register(LocalTime.class, new LocalTimeTypeHandler());
        register(OffsetDateTime.class, new OffsetDateTimeTypeHandler());
        register(OffsetTime.class, new OffsetTimeTypeHandler());
        register(ZonedDateTime.class, new ZonedDateTimeTypeHandler());
        register(Month.class, new MonthTypeHandler());
        register(Year.class, new YearTypeHandler());
        register(YearMonth.class, new YearMonthTypeHandler());

        // 生命最重要，关心每一个人，口罩问题，吃的问题
        // 给员工打电话，关系，特殊地区
        // 给朋友，同行打电话，即使不谈业务，也要慰问慰问，交流，比如武汉的同行，
        // 国家要灭病毒，机会，干掉一批，扶持一批，线上求速度，不求完美。
        // 领导冲在第一线，先行。
        // 怎么解决问题，所有细节问题都放到了每个人的面前，之前所有问题都能解决，现在有困难，就要抢业务
        // 所有人要统一思想，现在是最好的沟通情感的地方，和每个人。之前很多人很闲，现在就是时间。之前很多人忙，现在不忙，吧之前的流程制度，做的不好的都可以统统做好。
        // 意识先行，要有敏锐度。
        // 关键时期要更加主动沟通，加强沟通
        // ------于2020年2月2日
    }

    public static <T extends Enum> TypeHandler<T> getEnumTypeHandler(Class<T> cls) {
        TypeHandler<T> typeHandler = (TypeHandler<T>) enumHandlerMap.get(cls);
        if (typeHandler == null) {
            typeHandler = new EnumTypeHandler<>(cls);
            enumHandlerMap.put(cls, typeHandler);
        }
        return typeHandler;
    }

    public static <T extends SmartDbEnum> TypeHandler<?> getSmartDbEnumTypeHandler(Class<T> cls) {
        TypeHandler<T> typeHandler = (TypeHandler<T>) smartDbEnumHandlerMap.get(cls);
        if (typeHandler == null) {
            typeHandler = new SmartDbEnumTypeHandler<>(cls);
            smartDbEnumHandlerMap.put(cls, typeHandler);
        }
        return typeHandler;
    }

    private static void register(Class javaType, TypeHandler<?> handler) {
        typeHandlerMap.put(javaType, handler);
    }

    public static TypeHandler<?> getHandler(Class<?> cls) {
        return typeHandlerMap.get(cls);
    }

}
