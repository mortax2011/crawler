/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author mohamedmortada
 */
class Atomicvariables {
    private AtomicInteger c = new AtomicInteger(0);
    Set<String> pagesVisited = new HashSet<String>();
    public void increment() {
        c.incrementAndGet();
    }

    public int countervalue() {
        return c.get();
    }
   
    public synchronized Set<String> getvisited(){
        return pagesVisited;
    }
}
