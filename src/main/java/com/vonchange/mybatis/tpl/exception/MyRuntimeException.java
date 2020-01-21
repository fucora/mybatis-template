package com.vonchange.mybatis.tpl.exception;



/**
 *自定义RuntimeException
 * @author von_change@163.com
 * @date 2015-6-14 下午12:49:15
 */
public class MyRuntimeException extends NestedRuntimeException {

	public MyRuntimeException(String msg) {
		super(msg);
	}
	public static MyRuntimeException instance(Object... msg) {
		StringBuilder sb = new StringBuilder();
		for (Object object : msg) {
			sb.append(String.valueOf(object));
		}
	    String msgStr=sb.toString();
		return new MyRuntimeException(msgStr);	
	}

	private static final long serialVersionUID = 1L;

}
