package com.pq.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/14.
 */
public class SocketServer {
    BufferedWriter bufferedWriter=null;
    BufferedReader bufferedReader=null;

    public static void main(String[] args) {
        SocketServer socketServer=new SocketServer();
        socketServer.startServer();
    }

    public void startServer(){
        ServerSocket serverSocket=null;
        Socket socket=null;
        try {
            serverSocket=new ServerSocket(9898);
            System.out.println("服务器已经启动--");
            //支持多个客户端接入
            while (true) { //阻塞住等待客户端接入
                socket = serverSocket.accept();
                managerConnection(socket);
            }
           /* System.out.println("客户端已经连接成功--");
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //测试模拟心跳包 间隔3秒给客户端发送一个数据
           *//* new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        System.out.println("发送心跳给客户端");
                        bufferedWriter.write("heart beat once...\n");//这个\n不可少哦 因为我们的流读的是一行一行否则认为没有结束
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },3000,3000);*//*

            String receiveMsg;
            while((receiveMsg=bufferedReader.readLine())!=null) {
                System.out.println(receiveMsg);
                //当客户端收到服务端的数据后我们就给客户端返回一条消息
                //此时客户端是被动的，服务端不能主动发送数据给客户端
                bufferedWriter.write("我是服务端给你的响应"+"\n");
                bufferedWriter.flush();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //每次连接后把socket交给这个方法来处理 也可以写一个类 采用相应的模式来实现
    public void managerConnection(final Socket socket){
        try {
            System.out.println("客户端已经连接成功--");
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //测试模拟心跳包 间隔3秒给客户端发送一个数据
           /* new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        System.out.println("发送心跳给客户端");
                        bufferedWriter.write("heart beat once...\n");//这个\n不可少哦 因为我们的流读的是一行一行否则认为没有结束
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },3000,3000);*/

            String receiveMsg;
            while((receiveMsg=bufferedReader.readLine())!=null) {
                System.out.println(receiveMsg);
                //当客户端收到服务端的数据后我们就给客户端返回一条消息
                //此时客户端是被动的，服务端不能主动发送数据给客户端
                bufferedWriter.write("我是服务端给你的响应"+"\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedWriter.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
