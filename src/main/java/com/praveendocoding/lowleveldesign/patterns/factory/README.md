# 1

````markdown
# Factory Pattern

The Factory Pattern is a creational design pattern used to **centralize and encapsulate object creation**.

Instead of allowing client code to directly create concrete objects using `new`, the responsibility of deciding which object to create is moved to a separate component.

---

## 1. The Problem

Suppose we have different types of notifications:

- Email
- SMS
- Push
- Slack

All of them implement the same interface:

```java
public interface Notification {
    void send(String message);
}
````

Example implementations:

```java
public class EmailNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}
```

```java
public class SMSNotification implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```

Without a Factory, the client has to know about every concrete implementation:

```java
Notification notification;

if (type.equals("EMAIL")) {
    notification = new EmailNotification();
} else if (type.equals("SMS")) {
    notification = new SMSNotification();
} else if (type.equals("SLACK")) {
    notification = new SlackNotification();
}

notification.send(message);
```

The client is now responsible for:

1. Deciding which notification is required.
2. Creating the concrete object.
3. Using the object.

This creates unnecessary coupling between the client and concrete classes.

---

# 2. What Does Factory Solve?

The Factory Pattern moves the **object creation decision** to a central place.

```text
Client
  |
  | "I need SMS"
  v
NotificationFactory
  |
  | decides which object to create
  v
SMSNotification
```

The client only needs to know about the `Notification` interface.

```java
Notification notification =
        NotificationFactory.create("SMS");

notification.send("Your OTP is 1234");
```

The client does not need to write:

```java
new SMSNotification();
```

---

# 3. Simple Factory

A Simple Factory is the easiest way to understand the concept.

```java
public class NotificationFactory {

    public static Notification create(String type) {

        switch (type) {

            case "EMAIL":
                return new EmailNotification();

            case "SMS":
                return new SMSNotification();

            case "PUSH":
                return new PushNotification();

            case "SLACK":
                return new SlackNotification();

            default:
                throw new IllegalArgumentException(
                        "Unsupported notification type: " + type
                );
        }
    }
}
```

The client becomes:

```java
public class Client {

    public static void main(String[] args) {

        Notification notification =
                NotificationFactory.create("SLACK");

        notification.send("SLA breached!");
    }
}
```

---

# 4. What Did We Gain?

Without Factory:

```text
Client
 ├── EmailNotification
 ├── SMSNotification
 ├── PushNotification
 └── SlackNotification
```

With Factory:

```text
             Client
                |
                v
       NotificationFactory
                |
        ┌───────┼────────┐
        v       v        v
      Email    SMS     Slack
```

The Factory becomes responsible for:

> "Which concrete object should I create?"

The client is responsible only for:

> "I need a Notification and I want to use it."

---

# 5. Why Is This Useful?

Imagine a large application with:

```text
OrderService
PaymentService
LoginService
RefundService
DeliveryService
```

All of them need notifications.

Without a Factory, every service may contain:

```java
if (type.equals("EMAIL")) {
    new EmailNotification();
} else if (type.equals("SMS")) {
    new SMSNotification();
} else if (type.equals("SLACK")) {
    new SlackNotification();
}
```

Now if we add:

```text
WhatsAppNotification
```

many classes may need to be modified.

With a Factory:

```text
OrderService ───────┐
PaymentService ─────┤
LoginService ───────┤
RefundService ──────┤
                    v
          NotificationFactory
                    |
                    v
             Concrete Object
```

The object creation logic is centralized.

---

# 6. Important Point

Factory Pattern is **NOT simply about avoiding `new`**.

The important idea is:

> **Move the responsibility of deciding which concrete object to create away from the client.**

The Factory encapsulates the creation logic.

---

# 7. When Do We Actually Need a Factory?

Do not think:

> "If I have a switch, I must use Factory."

That is not true.

For a small application, this is perfectly fine:

```java
switch (type) {
    case "EMAIL" -> new EmailNotification();
    case "SMS" -> new SMSNotification();
}
```

A Factory becomes useful when:

* Many parts of the application need the same creation logic.
* Object creation is becoming complex.
* The client should not depend on concrete implementations.
* New implementations are frequently added.
* We want one centralized place responsible for object creation.

---

# 8. Simple Factory vs Factory Method

These two are often confused.

## Simple Factory

The Factory itself decides which object to create.

```text
Client
  |
  v
Factory
  |
  ├── Email
  ├── SMS
  └── Slack
```

Example:

```java
NotificationFactory.create("SLACK");
```

The decision is made using something like:

```java
switch (type) {
    case "SLACK":
        return new SlackNotification();
}
```

---

# 9. Factory Method

Factory Method is different.

Factory Method is useful when there is a **common workflow**, but one step of that workflow requires creating an object whose concrete type should be decided by subclasses.

Example:

```java
public abstract class NotificationCreator {

    public void send(String message) {

        // Common workflow

        Notification notification =
                createNotification();

        notification.send(message);
    }

    protected abstract Notification createNotification();
}
```

The parent class defines:

```text
send()
  |
  v
create notification
  |
  v
notification.send()
```

But it does not know which concrete Notification to create.

The subclasses decide.

```java
public class EmailNotificationCreator
        extends NotificationCreator {

    @Override
    protected Notification createNotification() {
        return new EmailNotification();
    }
}
```

```java
public class SlackNotificationCreator
        extends NotificationCreator {

    @Override
    protected Notification createNotification() {
        return new SlackNotification();
    }
}
```

Now:

```java
NotificationCreator creator =
        new SlackNotificationCreator();

creator.send("SLA breached!");
```

The workflow remains in the parent:

```text
NotificationCreator
        |
        | send()
        |
        +--> createNotification()
                    |
                    v
             SlackNotification
                    |
                    v
                  send()
```

---

# 10. The Core Difference

### Simple Factory

The Factory decides:

```text
"Give me SLACK"
       |
       v
Factory decides
       |
       v
SlackNotification
```

### Factory Method

The subclass decides:

```text
SlackNotificationCreator
          |
          v
createNotification()
          |
          v
SlackNotification
```

The key distinction is **who makes the creation decision**.

---

# 11. Why Factory Method?

Consider a common workflow:

```java
public void process() {

    connect();

    authenticate();

    performOperation();

    disconnect();
}
```

Most of the workflow is the same.

But the concrete object used by the workflow may be different.

For example:

```text
PaymentProcessor
       |
       | common workflow
       v
connect()
authenticate()
pay()
disconnect()
       |
       v
Which PaymentGateway?
       |
       +---- Razorpay
       +---- Stripe
       +---- PayPal
```

The parent can define the common workflow:

```java
public abstract class PaymentProcessor {

    public void pay(double amount) {

        PaymentGateway gateway =
                createGateway();

        gateway.connect();
        gateway.authenticate();
        gateway.pay(amount);
        gateway.disconnect();
    }

    protected abstract PaymentGateway createGateway();
}
```

The subclasses decide the concrete gateway:

```java
public class RazorpayProcessor
        extends PaymentProcessor {

    @Override
    protected PaymentGateway createGateway() {
        return new RazorpayGateway();
    }
}
```

```java
public class StripeProcessor
        extends PaymentProcessor {

    @Override
    protected PaymentGateway createGateway() {
        return new StripeGateway();
    }
}
```

---

# 12. Factory Method in One Sentence

> **The parent class defines the common workflow, while subclasses decide which concrete object should be created for that workflow.**

---

# 13. Factory Pattern Mental Model

Remember these three levels:

### Direct Object Creation

```java
new SlackNotification();
```

Use when the client genuinely knows exactly which object it wants.

---

### Simple Factory

```java
NotificationFactory.create("SLACK");
```

Use when the **creation decision should be centralized**.

```text
Client
  |
  v
Factory
  |
  v
Concrete Object
```

---

### Factory Method

```java
creator.send();
```

Use when the **parent has a common workflow**, but subclasses decide which object the workflow should use.

```text
Parent
  |
  | common workflow
  v
Factory Method
  ^
  |
Subclass decides concrete object
```

---

# 14. Final Takeaway

The Factory Pattern is fundamentally about **object creation**.

The important question is:

> **Who should be responsible for deciding which concrete object gets created?**

If the client handles it:

```text
Client → new SlackNotification()
```

If a central Factory handles it:

```text
Client → Factory → SlackNotification
```

If subclasses handle it as part of a common workflow:

```text
Client
  ↓
Subclass
  ↓
Parent common workflow
  ↓
Factory Method
  ↓
Concrete Object
```

### Remember:

**Simple Factory:**

> "Centralize the creation decision."

**Factory Method:**

> "Define a common workflow and let subclasses decide which object that workflow uses."

---

