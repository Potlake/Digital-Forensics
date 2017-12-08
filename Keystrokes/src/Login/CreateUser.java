/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 *
 * @author User_1
 */
public class CreateUser extends JDialog{
    
    private String userName;
    private String password;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
     private JButton btnCreateUser;
    private JButton btnCancel;
    
    public CreateUser(Frame parent)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);
 
        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);
 
        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);
 
        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        btnCreateUser = new JButton("Add User");
        
         btnCreateUser.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
              
               JOptionPane.showMessageDialog(CreateUser.this,
                            "User "+ getUsername() + " " + getPassword(),
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
               
            }
            
            CreateUser user = new CreateUser(getUsername(),getPassword());
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnCreateUser);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    public CreateUser(String userName,String password)
    {
        this.userName = userName;
        this.password = password;
    }
    
    public String getUsername()
    {
         return tfUsername.getText().trim();
    }
    
    public String getPassword()
    {
        return new String(pfPassword.getPassword());
    }
    
    public void SetUsername(String uname)
    {
        userName = uname;
    }
     public void SetPassword(String pass)
     {
         password = pass;
     }
    
}
