package com.example.pku_chem_backend.util;

import com.example.pku_chem_backend.entity.User;
import com.example.pku_chem_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class LogUtil {
    public LogUtil() {}


    public static void createLogFile(String username){
        File file = new File("log/"+username+".log");
        // mkdir
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try{
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLogFileForCurrent(List<String> usernames){
        for(String username : usernames){
            createLogFile(username);
        }
    }

    public void writeLog(String username, String action, String detail, String time, String ip, HttpServletRequest request){
        String logPath = "log/"+username+".log";
        String remoteAddr = getIp(request);
        Log log = new Log();
        log.user = username;
        log.action = Action.valueOf(action);
        log.detail = detail;
        log.time = time;
        log.ip = remoteAddr;
        try{
            Files.write(new File(logPath).toPath(), log.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getIp(HttpServletRequest request){
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    private class Log{
        private String user;
        private Action action;
        private String detail;
        private String time;
        private String ip;

        public String toString(){
            return user + " " + action + " " + detail + " " + time + " " + ip + "\n";
        }
    }

    private enum Action{
        LOGIN("登录"),
        LOGOUT("登出"),
        ADD("添加"),
        DELETE("删除"),
        UPDATE("更新"),
        QUERY("查询");
        private Action(String action){}
    }
}
