package com.qetch.springmvc.test.juc.shangguigu;

/**
 * 类加载机制：
 * 从父类到子类，静态先行，静态加载只有一次，先有模板，后有实例
 * 普通代码块加载优先于构造方法
 *
 * Father father = new Father();     Father.class 类模板  2按照类模板出来具体的类实例
 */
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
    } // 父类构造方法

    {
        System.out.println("222222"); // 父类普通代码块
    }

    static {
        System.out.println("333333"); // 父类静态代码块
    }
}

class Son extends Father {
    public Son() {
        super();
        System.out.println("444444"); // 子类构造方法
    }

    {
        System.out.println("555555"); // 子类普通代码块
    }

    static {
        System.out.println("666666"); // 子类静态代码块
    }
}