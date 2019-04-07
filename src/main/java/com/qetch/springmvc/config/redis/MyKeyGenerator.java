package com.qetch.springmvc.config.redis;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class MyKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... params) {
		//规定  本类名+方法名+参数名 为key
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append("-");
        sb.append(method.getName());
        sb.append("-");
        for (Object param : params) {
            sb.append(param.toString());
        }
        return sb.toString();
	}
}