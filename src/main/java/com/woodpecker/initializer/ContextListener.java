package com.woodpecker.initializer;
import com.woodpecker.controller.UserController;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    //初始化函数
    @Override
    public void contextInitialized(ServletContextEvent event){
        System.out.println("Server started.");

    }

    //退出函数
    @Override
    public void contextDestroyed(ServletContextEvent event){
        System.out.println("Server stopped");
    }
}
