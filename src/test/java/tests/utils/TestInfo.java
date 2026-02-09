package tests.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestInfo {
    // Ini atribut yang bisa kamu isi nanti di test case
    String expected() default "-";
    String note() default "-";
    String priority() default "Normal";
    String group() default "";
    String testType() default "Positive Cases";
}