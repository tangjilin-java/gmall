package ab.tjl.gmall.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//在方法上生效
@Retention(RetentionPolicy.RUNTIME)//在虚拟机运行时开始
public @interface LoginRequired {

    boolean loginSuccess() default true;

}
