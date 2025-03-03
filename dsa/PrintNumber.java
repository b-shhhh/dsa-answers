/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dsa;

/**
 *
 * @author Acer
 */

/*
 * Multithreaded Number Printing
 * ---------------------------------
 * This Java program uses three threads to print numbers in a sequence "0102030405...".
 * Each thread has a distinct role:
 *    - A `NumberPrinter` class defines methods to print zeros, even, and odd numbers.
 *    - A `ThreadController` synchronizes threads using a shared lock.
 *    - `printZero()`: Prints 0 before every number.
 *    - `printEven()`: Prints even numbers in order.
 *    - `printOdd()`: Prints odd numbers in order.
 * The program ensures proper synchronization using `wait()` and `notifyAll()` to avoid race conditions.
 */

class NumberPrinter {
    public void printZero() {
        System.out.print(0);
    }

    public void printEven(int num) {
        System.out.print(num);
    }

    public void printOdd(int num) {
        System.out.print(num);
    }
}

class ThreadController {
    private final int n;
    private final NumberPrinter printer;
    private int counter = 1;
    private final Object lock = new Object();
    private boolean printZero = true; // Flag to alternate between 0 and numbers

    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    public void printZero() {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (!printZero) {
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printZero();
                printZero = false;
                lock.notifyAll();
            }
        }
    }

    public void printEven() {
        for (int i = 2; i <= n; i += 2) {
            synchronized (lock) {
                while (printZero || counter % 2 == 1) {
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printEven(i);
                counter++;
                printZero = true;
                lock.notifyAll();
            }
        }
    }

    public void printOdd() {
        for (int i = 1; i <= n; i += 2) {
            synchronized (lock) {
                while (printZero || counter % 2 == 0) {
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printOdd(i);
                counter++;
                printZero = true;
                lock.notifyAll();
            }
        }
    }
}

public class PrintNumber {
    public static void main(String[] args) {
        int n = 7; // Change output length here
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);

        Thread zeroThread = new Thread(controller::printZero);
        Thread evenThread = new Thread(controller::printEven);
        Thread oddThread = new Thread(controller::printOdd);

        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}

