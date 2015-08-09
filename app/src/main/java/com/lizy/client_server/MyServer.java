package com.lizy.client_server;

import android.os.Handler;
import android.os.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lizy on 1/08/15.
 */
public class MyServer {
    Thread m_objThread;
    ServerSocket m_server;
    String m_strMessage;
    DataDisplay m_dataDisplay;
    Object m_connected;

    public MyServer(){

    }

    public void setEventListener(DataDisplay dataDisplay){
        m_dataDisplay = dataDisplay;

    }

    public void startListening() {
        m_objThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    m_server = new ServerSocket(2001);
                    Message serverStatus = Message.obtain();
                    serverStatus.obj="server is listening at port 2001";
                    mHandler.sendMessage(serverStatus);
                    Socket connectedSocket = m_server.accept();
                    Message clientMessage = Message.obtain();
                    ObjectInputStream ois = new ObjectInputStream(connectedSocket.getInputStream());
                    String strMessage = (String) ois.readObject();
                    clientMessage.obj = strMessage;
                    mHandler.sendMessage(clientMessage);
                    ObjectOutputStream oos = new ObjectOutputStream(connectedSocket.getOutputStream());
                    oos.writeObject("Hai...");
                    ois.close();
                    oos.close();
                    m_server.close();
                }
                catch (Exception e){
                    Message msg3 = Message.obtain();
                    msg3.obj = e.getMessage();
                    mHandler.sendMessage(msg3);
                }
            }
        });
        m_objThread.start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message status){
            m_dataDisplay.Display(status.obj.toString());
        }
    };
}
