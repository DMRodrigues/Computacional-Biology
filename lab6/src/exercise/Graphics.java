package exercise;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// Class that implements the graphical interface

public class Graphics {
	Kmeans k = new Kmeans();
	final JFrame frame = new JFrame("K-means Simulator");

	public Graphics() {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton input = new JButton("Input file");
		JButton about = new JButton("About");
		JButton exit = new JButton("Exit");

		JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
		buttonPanel.add(input);
		buttonPanel.add(about);
		buttonPanel.add(exit);

		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// sim.setOrgSeq(0);
				k.input();
				execute();

				// JOptionPane.showMessageDialog(frame, "The output was successful!\n"+"(1) output file have been generated!", "Successful", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		 about.addActionListener(new ActionListener()
	        {
	        	@Override
	        	public void actionPerformed(ActionEvent e) {
	        		JOptionPane.showMessageDialog(frame, "IST 1st Semester 2015/2016\n\nComputacional Biology\n\nGroup 9\nVanessa Gaspar\nDiogo Rodrigues\nNuno Pires", "About", JOptionPane.INFORMATION_MESSAGE);
	        	}
	        });
		 
		JPanel east = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		east.add(buttonPanel, gbc);

		JPanel center = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(200, 200);
			}
		};

		JLabel label1 = new JLabel("Welcome to K-means simulator!");
		center.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		center.add(label1);

		frame.add(east, BorderLayout.EAST);
		frame.add(center);

		frame.pack();
		frame.setVisible(true);
	}

	 public void getCluster(){
		 k.setNCluster(Integer.parseInt((String)JOptionPane.showInputDialog(frame,"Insert the number of clusters:\n" ,"Clusters",JOptionPane.PLAIN_MESSAGE,null, null, null))); 
	  }
	 

	public void execute() {
		getCluster();
	 	k.inicialization();
		k.compute(); 
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Graphics();
			}
		});
	}
}