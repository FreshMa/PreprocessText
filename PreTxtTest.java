package preprocessTxt;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class PreTxtTest {
	public static void main(String[]args){
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				PreTxtUI mainUI = new PreTxtUI();
				mainUI.setTitle("kid pre-processing");
				mainUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainUI.setVisible(true);
			}
		});
	}
}
