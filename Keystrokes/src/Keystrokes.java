/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User_1
 */
public class Keystrokes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        ConnectDatabase dao = new ConnectDatabase();
        dao.connectToDataBase();
        
        KeystrokesForm form = new KeystrokesForm();
        form.setVisible(true);
    }
    
}
