package com.praveendocoding.lowleveldesign.patterns.singleton;


public class Singleton {

    static class SingletonLazyBrute {
        private static SingletonLazyBrute instance;

        private SingletonLazyBrute() {};

        public static SingletonLazyBrute getInstance() {
            if (instance == null) {
                instance = new SingletonLazyBrute();
            }
            return instance;
        }
    }

    static class ThreadSafeSingletonBrute {
        private static ThreadSafeSingletonBrute instance;

        private ThreadSafeSingletonBrute() {};

        public static synchronized ThreadSafeSingletonBrute getInstance() {
            if (instance == null) {
                instance = new ThreadSafeSingletonBrute();
            }
            return instance;
        }
    }

    static class DoubleCheckedSingleton{
        // volatile prevents the other thread to read instance's value without complete initialisation
        /*
        * instance =  new DoubleCheckedSingleton();
        * 1. Allocate memory
        * 2. Initialize Singleton object
        * 3. Assign the memory address to instance
        *
        * JVM can do these steps in any order to optimise; to prevent this we use volatile
        * */
        private volatile static DoubleCheckedSingleton instance;

        private DoubleCheckedSingleton(){};

        public static DoubleCheckedSingleton getInstance(){
            if(instance == null){
                synchronized (DoubleCheckedSingleton.class){
                    instance  = new DoubleCheckedSingleton();
                }
            }
            return instance;
        }

    }

    /* Thread Safe, Easy to implement but waste Resources if Singleton is never used*/
    static class EagerSingleton{
        private static final EagerSingleton instance = new EagerSingleton();

        private EagerSingleton(){};

        public static EagerSingleton getInstance(){
            return instance;
        }
    }

    public static class BillPughSingleton{
        private BillPughSingleton(){};

        private class Holder{
            private static final BillPughSingleton INSTANCE = new BillPughSingleton();
        }

        public static BillPughSingleton getInstance(){
            return Holder.INSTANCE;
        }
    }

    /*
     ----------  Recommended ----------
    * JVM guarantees:
    * 1. Thread-safe initialization
    * 2. Serialization safety
    * 3. Reflection safety
    * 4. Single instance guarantee
    *
    * Cons: Does not extends any class, so if your Singleton class extends a base class you can't use Enum
    * */
    enum EnumSingleton{
        INSTANCE;

        public void doSomething(){
            System.out.println("does Something");
        }
    }
}
