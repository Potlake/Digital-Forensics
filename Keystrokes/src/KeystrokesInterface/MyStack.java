/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KeystrokesInterface;

import API.API;
import java.util.Stack;

/**
 *
 * @author User_1
 */
public class MyStack {
    
    API api = new API();
    public Stack<Object[][]> stack = new Stack<Object[][]>();

    public MyStack() {
        int k = 1;
        for (int i = 0; i < 20; i++) {
            Object[][] array = {{api.sb.toString(), }};
            stack.push(array);
        }
    }

    public Stack<Object[][]> getStack() {
        return stack;
    }
}
