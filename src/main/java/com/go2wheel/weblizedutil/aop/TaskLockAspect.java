package com.go2wheel.weblizedutil.aop;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.go2wheel.weblizedutil.exception.JobOnGoingException;
import com.go2wheel.weblizedutil.exception.UnexpectlyCallMethodException;
import com.go2wheel.weblizedutil.model.JobLog;
import com.go2wheel.weblizedutil.service.JobLogDbService;
import com.go2wheel.weblizedutil.util.ExceptionUtil;
import com.go2wheel.weblizedutil.util.TaskLocks;

@Aspect
@Component
public class TaskLockAspect {
	
	//https://blog.espenberntsen.net/2010/03/20/aspectj-cheat-sheet/
	
//	@Pointcut("execution(public * *(..))")
	
//	execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)
//            throws-pattern?)
	
	@Autowired
	private JobLogDbService jobLogDbService;
	
	
//	@Around("execution(@com.go2wheel.weblizedutil.aop.Exclusive * *(..)) && @annotation(exclusive)")
//	public Object myAdvice(ProceedingJoinPoint proceedingJoinPoint, Exclusive exclusive) throws Throwable{
//		Server server = (Server) proceedingJoinPoint.getArgs()[1];
//		Lock lock = TaskLocks.getBoxLock(server.getHost(), exclusive.value());
//		if (lock.tryLock()) {
//			try {
//				return proceedingJoinPoint.proceed();
//			} finally {
//				lock.unlock();
//			}
//		} else {
//			throw new JobOnGoingException();
//		}
//	}
	
	@Around("execution(@com.go2wheel.weblizedutil.aop.MeasureTimeCost * *(..))")
	public Object measureAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		long startTime = System.currentTimeMillis();
		Object o = proceedingJoinPoint.proceed();
		if (o instanceof TimeCost) {
			((TimeCost)o).setStartTime(startTime);
			((TimeCost)o).setEndTime(System.currentTimeMillis());
		}
		return o;
	}
	
	
	@Around("execution(@com.go2wheel.weblizedutil.aop.TrapException * *(..)) && @annotation(trapException)")
	public void jobExceptionAdvice(ProceedingJoinPoint proceedingJoinPoint, TrapException trapException) throws Throwable{
		JobExecutionContext context = (JobExecutionContext) proceedingJoinPoint.getArgs()[0]; 
		try {
			proceedingJoinPoint.proceed();
		} catch (Throwable e) {
			saveException(e, trapException, context);
			throw e;
		}
	}

	private void saveException(Throwable e, TrapException trapException, JobExecutionContext context) throws UnexpectlyCallMethodException {
		JobLog jl = new JobLog();
		JobDataMap data = context.getMergedJobDataMap();
		Properties p = new Properties();
		p.putAll(data);
		
		jl.setCtx(p.toString());
		jl.setJobClass(trapException.value().getName());
		jl.setCreatedAt(new Date());
		jl.setExp(ExceptionUtil.stackTraceToString(e));
		jobLogDbService.save(jl);
	}
	
//	@Pointcut("execution(* @com.go2wheel.weblizedutil.aop。Exclusive *.*(..))")
//	@Pointcut("execution(* com.go2wheel.weblizedutil.borg.BorgTaskFacade.*(..))")
//	@Pointcut("execution(public * *(..))")
//	@Pointcut("@annotation(com.go2wheel.weblizedutil.aop.Exclusive")
//	public void exclusiveMethod() {}

}
