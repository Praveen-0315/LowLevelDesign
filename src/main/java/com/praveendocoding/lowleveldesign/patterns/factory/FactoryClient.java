package com.praveendocoding.lowleveldesign.patterns.factory;

public class FactoryClient {
    public static void main(String[] args) {
        NotificationCreator notificationCreator;

        notificationCreator = new EmailNotificationCreator();
        notificationCreator.send("Hi, Thank You for signing in");

        notificationCreator = new SMSNotificationCreator();
        notificationCreator.send("Your OTP to sign in is: 881991");

        notificationCreator = new PushNotificationCreator();
        notificationCreator.send("Someone subscribed to your blogs");

        notificationCreator = new SlackNotificationCreator();
        notificationCreator.send("Alert! SLA breached");

    }
}
