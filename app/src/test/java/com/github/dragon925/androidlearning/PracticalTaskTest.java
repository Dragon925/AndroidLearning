package com.github.dragon925.androidlearning;

import org.junit.Test;

public class PracticalTaskTest {

    @Test
    public void practicalTask_Task3() {
        PracticalTask.myClosure.run();
        System.out.println("-----");
        PracticalTask.repeatTask(10, PracticalTask.myClosure);
    }

    @Test
    public void practicalTask_Task4() {
        PracticalTask.move();
    }
}
