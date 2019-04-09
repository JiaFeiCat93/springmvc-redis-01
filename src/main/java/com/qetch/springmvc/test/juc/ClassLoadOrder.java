package com.qetch.springmvc.test.juc;

public class ClassLoadOrder {

    public static void main(String[] args) {
        new Son();
        System.out.println("====================");
        new Father();
        System.out.println("====================");
        new Son();
    }
}

class Father {
    public Father() {
        System.out.println("111111");
    }

    {
        System.out.println("222222");
    }

    public static void fun1() {
        System.out.println("333333");
    }
}

class Son extends Father {
    public Son() {
        super();
        System.out.println("444444");
    }

    {
        System.out.println("555555");
    }

    public static void fun2() {
        System.out.println("666666");
    }
}