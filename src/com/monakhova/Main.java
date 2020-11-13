package com.monakhova;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    public static int currentThread;
    public static Thread[] Threads;
    public static boolean[] EnabledThreads;
    public static Timer myTimer;

    public static class ElapsedEvent implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            boolean changed = false;
            for (int i = currentThread + 1; i < 3; i++) {
                if (!EnabledThreads[i]) {
                    System.out.println("Thread in queue " + currentThread);
                    currentThread = i;
                    System.out.println("Thread Initialized " + currentThread);
                    changed = true;
                    Threads[i] = new Thread(new RunThread());
                    Threads[i].start();
                    EnabledThreads[i] = true;
                    break;
                } else if (Threads[i]==null||Threads[i].isAlive()) {
                    System.out.println("Thread in queue " + currentThread);
                    currentThread = i;
                    System.out.println("Thread Switched " + currentThread);
                    changed = true;
                    break;
                }
            }
            if (!changed) {
                for (int i = 0; i < currentThread; i++) {
                    if (Threads[i]==null||Threads[i].isAlive()) {
                        System.out.println("Thread in queue " + currentThread);
                        currentThread = i;
                        System.out.println("Thread Switched " + currentThread);
                        break;
                    }
                }
            }
        }
    }

    public static class RunThread implements Runnable
    {
        @Override
        public void run()
        {
            boolean stop = false;
            int ThreadNum = currentThread;
            int num = 0;
            while (!stop) {
                if (currentThread == ThreadNum)
                {
                    System.out.println("The number is " + num);
                    num++;
                    if (num > 100)
                    {
                        stop = true;
                    } else {
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Threadd stopped");

                boolean changed = false;
                for (int i = currentThread + 1; i < 3; i++) {
                    if (Threads[i]==null||Threads[i].isAlive()) {
                        currentThread = i;
                        System.out.println("Thread Switched " + currentThread);
                        changed = true;
                        break;
                    }
                }

                if (!changed) {
                    for (int i = 0; i < currentThread; i++)
                    {
                        if (Threads[i]==null||Threads[i].isAlive()) {
                            currentThread = i;
                            System.out.println("Thread Switched " + currentThread);
                            break;
                        }
                    }
                }
            myTimer.stop();
            myTimer.start();
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        okno okno= new okno();

        currentThread = 0;
        Threads = new Thread[3];

        EnabledThreads = new boolean[3];

        EnabledThreads[0] = true;
        Threads[0] = new Thread(new RunThread());
        Threads[0].start();
        System.out.println("Thread initialized 0");

        myTimer = new Timer(20*50,new ElapsedEvent());
        myTimer.start();

        boolean alive = true;
        while (alive)
        {
            alive = false;
            for (int i = 0; i < 3; i++) {
                if (Threads[i]==null||Threads[i].isAlive()) {
                    alive = true;
                    break;
                }
            }
            Thread.sleep(100);
        }
        System.out.println("All Threads Stopped");

    }
}


class okno extends JFrame
{
    // Обработчик событий нажатий на клавиши
    private class myKey implements KeyListener
    {
        // Метод, который срабатывает при нажатии
        public void keyPressed(KeyEvent e)
        {
            // Получение кода нажатой клавиши
            int key_ = e.getKeyCode();

            // Выход из программы, если нажат - Esc
            if (key_==27)
            {
                System.exit(1);
            }

        }
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
    }

    // Конструктор класса
    public okno()
    {
        // Подключение обработчика события для клавиатуры к окну
        addKeyListener(new myKey());
        // Установка активности окна
        setFocusable(true);

        // Задание размеров и положения окна
        setBounds(400,400,200,300);
        // Задание заголовка окна
        setTitle("");
        // Запрет изменения размеров окна
        setResizable(false);
        // Операция завершения работы приложения при
        // закрытии окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Сделать окно видимым
        setVisible(true);
    }
}
