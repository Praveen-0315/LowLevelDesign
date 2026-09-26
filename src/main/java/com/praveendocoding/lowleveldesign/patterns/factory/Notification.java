package com.praveendocoding.lowleveldesign.patterns.factory;

public interface Notification {
    public void send(String message);
}



class EmailNotification implements Notification{
    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}

class SMSNotification implements Notification{
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class PushNotification implements Notification{
    @Override
    public void send(String message) {
        System.out.println("Sending Push: " + message);
    }
}

class SlackNotification implements Notification{
    @Override
    public void send(String message){
        System.out.println("Sending Slack: " + message);
    }
}
