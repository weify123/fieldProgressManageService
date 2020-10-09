package com.autoyol.field.progress.manage.aop;


import com.autoyol.commons.utils.GsonUtils;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.entity.ConsumerInfo;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.util.CharUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class Aop {
    private static ThreadLocal<String> key = new ThreadLocal<String>();

    @After("methodAop()")
    public void doAfter() {
        key.remove();
    }

    @Pointcut("@annotation(com.autoyol.field.progress.manage.aop.UserInfoAutoSet)")
    private void methodAop() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Aop.class);
    private static final String ERROR_PREFIX = "/error";// 错误路径

    @Before("methodAop()") // 指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
    public void Interceptor(JoinPoint pjp) {
        ServletRequestAttributes arr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = arr.getRequest();
        String uri = request.getRequestURI();
        key.set(uri);
        Object[] args = pjp.getArgs();
        if (args == null || args.length < 1) {
            return;
        }

        // 1: 一些Spring内部异常直接抛出
        if (ERROR_PREFIX.equals(uri)) {
            return;
        }

        // 2: 打印参数 接口中有HttpServletRequest，也会转成JSON输出，转换的过程会报错
        for (Object obj : args) {
            if (!obj.getClass().getName().equals("org.apache.catalina.connector.RequestFacade")) {
                String name = obj.getClass().getName();
                if (obj != null) {
                    try {
                        LOGGER.debug("【方法参数】：", GsonUtils.toJson(obj));
                    } catch (Exception e) {
                        LOGGER.error("日志输出异常：", e);
                    }
                }

            }
        }

        // 3: 执行权限拦截
        if (uri.startsWith(CONSOLE_PREFIX)) { // Console
            this.consoleHandle(request, args);
        } else { // H5端口权限控制
            // 如果是一些特定的地址不需要拦截memNo注入
            if (uri.startsWith("/public/person/h5") && !uri.startsWith("/public/person/h5/getBlackStatus")) {
                return;
            }
            // 任务中心给前端nodejs直接调用,不走gateway
            if (uri.startsWith("taskCenter")) {
                return;
            }
            this.authHandle(request, args);
        }
    }

    private static final String CONSOLE_FIELD_HANDLE_USER = "handleUser";// 操作人
    private static final String CONSOLE_FIELD_HANDLE_USER_NO = "handleUserNo";// 操作人编号

    private static final String CONSOLE_PREFIX = "/console";// 后台管理URI的开始前缀
    private static final Class<?> CONSOLE_CLAZZ = BaseRequest.class; // 后台权限用户
    private static final String CONSOLE_AUTH_NAME = "Console-AUTH-Name";// 后台用户昵称 Header
    private static final String CONSOLE_AUTH_ID = "Console-AUTH-ID";// 后台用户ID Header

    private void consoleHandle(HttpServletRequest request, Object[] args) {
        Object targetObj = null;
        Class<?> paramClazz = null;
        for (Object obj : args) {
            Class<?> pc = obj.getClass();
            if (CONSOLE_CLAZZ.isAssignableFrom(pc)) {
                paramClazz = pc;
                targetObj = obj;
                break;
            }
        }
        Object userName = request.getHeader(CONSOLE_AUTH_NAME);
        if (targetObj == null) {
            return;
        }

        Object userNo = request.getHeader(CONSOLE_AUTH_ID);
        Object newUserId = request.getHeader(CacheConstraint.NEW_USER_ID);
        userNo = Objects.isNull(userNo) ? newUserId : userNo;

        Object newUserName = request.getHeader(CacheConstraint.NEW_USER_NAME);
        userName = Objects.isNull(userName) ? newUserName : userName;

        String userNameStr = null;
        if (userName != null) {
            try {
                userNameStr = CharUtils.decodeUrlCode(userName.toString());
            } catch (Exception e) {
                LOGGER.error("编码异常：", e);
            }
        }
        LOGGER.debug("【Console】管理员：{}，编号：{}", userNameStr, userNo);

        try {
            // 1: 操作人编号
            if (userName != null) {
                PropertyDescriptor pd1 = new PropertyDescriptor(CONSOLE_FIELD_HANDLE_USER, paramClazz);
                Method m1 = pd1.getWriteMethod();
                m1.invoke(targetObj, userNameStr);
            }

            // 2: 操作编号
            if (userNo != null) {
                PropertyDescriptor pd1 = new PropertyDescriptor(CONSOLE_FIELD_HANDLE_USER_NO, paramClazz);
                Method m2 = pd1.getWriteMethod();
                m2.invoke(targetObj, Integer.parseInt(userNo.toString()));
            }
        } catch (Exception e) {
            LOGGER.error("【Console】户权限抓取执行异常", e);
        }
    }

    private static final Class<?> MEM_USER_CLAZZ = ConsumerInfo.class;
    private static final String MEM_NO = "X-AUTH-ID";
    private static final String MEM_NICK_NAME = "X-AUTH-Nickname";
    private static final String MEM_REAL_NAME = "X-AUTH-RealName";
    private static final String MEM_USER_HEAD = "X-AUTH-ImagePath";
    // 字段
    private static final String MEM_FIELD_MEM_NO = "memNo";
    private static final String MEM_FIELD_REAL_NAME = "realName";
    private static final String MEM_FIELD_NICK_NAME = "nickName";
    private static final String MEM_FIELD_USER_HEAD = "userHead";
    // private static final String MEM_USER_HEAD = "X-AUTH-RealName";

    private void authHandle(HttpServletRequest request, Object[] args) {
        Object memNo = request.getHeader(MEM_NO);
        Object nickName = request.getHeader(MEM_NICK_NAME);
        Object realName = request.getHeader(MEM_REAL_NAME);
        Object userHead = request.getHeader(MEM_USER_HEAD);
        String nickNameStr = null;
        Object realNameStr = null;
        Object userHeadStr = null;
        try {
            if (nickName != null) {
                nickNameStr = CharUtils.decodeUrlCode(nickName.toString());
            }
            if (realName != null) {
                realNameStr = CharUtils.decodeUrlCode(realName.toString());
            }
            if (userHead != null) {
                userHeadStr = CharUtils.decodeUrlCode(userHead.toString());
            }
        } catch (Exception e) {
            LOGGER.error("编码异常：", e);
        }

        LOGGER.debug("【C端】--> 会员号：{}，昵称：{}，真实姓名：{}, 头像：{}", memNo, nickNameStr, realNameStr, userHeadStr);

        Object targetObj = null;
        Class<?> paramClazz = null;
        for (Object obj : args) {
            Class<?> pc = obj.getClass();
            if (MEM_USER_CLAZZ.isAssignableFrom(pc)) {
                paramClazz = pc;
                targetObj = obj;
                break;
            }
        }

        if (targetObj == null) {
            return;
        }

        try {
            // 1: 会员号
            PropertyDescriptor pd = new PropertyDescriptor(MEM_FIELD_MEM_NO, paramClazz);
            Method m1 = pd.getWriteMethod();
            if (memNo != null) {
                m1.invoke(targetObj, memNo.toString());
            }

            // 2: 会员昵称
            if (nickName != null) {
                pd = new PropertyDescriptor(MEM_FIELD_NICK_NAME, paramClazz);
                Method m2 = pd.getWriteMethod();
                m2.invoke(targetObj, nickNameStr);
            }

            // 3: 会员真名
            if (realName != null) {
                pd = new PropertyDescriptor(MEM_FIELD_REAL_NAME, paramClazz);
                Method m3 = pd.getWriteMethod();
                m3.invoke(targetObj, realNameStr);
            }

            // 4: 用户头像
            if (userHead != null) {
                pd = new PropertyDescriptor(MEM_FIELD_USER_HEAD, paramClazz);
                Method m4 = pd.getWriteMethod();
                m4.invoke(targetObj, userHeadStr);
            }
        } catch (Exception e) {
            LOGGER.error("C端】权限抓取执行异常", e);
        }
    }
}
