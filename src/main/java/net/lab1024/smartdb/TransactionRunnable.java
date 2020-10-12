package net.lab1024.smartdb;

/**
 * 事务代码块
 *
 * @author zhuoluodada@qq.com
 */
public interface TransactionRunnable {

    boolean run(SmartDbNode smartDbNode);

}
