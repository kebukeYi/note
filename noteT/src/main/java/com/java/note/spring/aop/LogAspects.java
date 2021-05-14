package com.java.note.spring.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;


@Aspect
public class LogAspects {


    @Pointcut("execution(public int com.java.note.spring.aop.MathCalculator.*(..))")
    public void pointCut() {
    }


    //Advice

    //初始化工作
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println("" + joinPoint.getSignature().getName() + "" + Arrays.asList(args) + "}");
    }

    //只有方法没有异常，调用这个
    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {
        System.out.println("" + joinPoint.getSignature().getName() + " logReturn {" + result + "}");
    }

    //只有方法发生了异常，调用这个
    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        System.out.println("" + joinPoint.getSignature().getName() + "" + exception + "}");
    }

    //相当于 finally 块 都会执行
    @After(value = "pointCut()")
    public void logEnd(JoinPoint joinPoint) {
        System.out.println("" + joinPoint.getSignature().getName() + "logEnd @After");
    }

    //既然Around Advice可以在Joinpoint之前和之后都能执行相应的逻辑，那么，它自然可以完成BeforeAdvice和After Advice的功能。
    // 不过，通常情况下，还是应该根据场景选用更为具体的Advice类型。
    // Around Advice应用场景非常广泛,我想大家对于J2EE中的Servlet规范提供的Filter功能应该很熟悉吧。
    // 实际上，它就是Around Advice的一种体现。使用它，我们就可以完成“资源初始化”、“安全检查”之类横切系统的关注点了。
    @Around(value = "pointCut()")
    public void logAround(JoinPoint joinPoint) {
        System.out.println("" + joinPoint.getSignature().getName() + "logEnd @After");
    }


}
