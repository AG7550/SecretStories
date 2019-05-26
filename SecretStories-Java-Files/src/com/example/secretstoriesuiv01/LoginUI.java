package com.example.secretstoriesuiv01;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {

	private JPanel contentPane;
	private JTextField tbxUsername;
	private JTextField tbxPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI frame = new LoginUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//H���R
			}
		});
		btnLogin.setBounds(158, 160, 89, 23);
		contentPane.add(btnLogin);
		
		tbxUsername = new JTextField();
		tbxUsername.setBounds(145, 48, 86, 20);
		contentPane.add(tbxUsername);
		tbxUsername.setColumns(10);
		
		tbxPassword = new JTextField();
		tbxPassword.setBounds(145, 95, 86, 20);
		contentPane.add(tbxPassword);
		tbxPassword.setColumns(10);
	}
}

