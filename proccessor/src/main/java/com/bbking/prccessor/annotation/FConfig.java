package com.bbking.prccessor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 *
 * @author gangwen.xu
 * Date  : 2018/5/8
 * Time  : 下午6:18
 * 类描述 :
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface FConfig {
    String file();
}
