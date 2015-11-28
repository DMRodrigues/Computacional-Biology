package exercise;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Graphics {
	Simulator5 sim = new Simulator5();
	final JFrame frame = new JFrame("Sequence evaluation simulator");
	
    public Graphics() {
    	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton input = new JButton("Input FASTA file");
        JButton generate = new JButton("Generate sequence");
        JButton exit = new JButton("Exit");
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(input);
        buttonPanel.add(generate);
        buttonPanel.add(exit);

        input.addActionListener(new ActionListener()
        {

		@Override
		public void actionPerformed(ActionEvent e) {
			//sim.execute();
			sim.input();
			getvalues();
			execute();
			
			sim.output();
			JOptionPane.showMessageDialog(frame, "The output was successful!\n"+"(1) output file have been generated!", "Successful", JOptionPane.INFORMATION_MESSAGE);
		}
        });
        
        generate.addActionListener(new ActionListener()
        {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		getparameters();
        		sim.generate();
        		getvalues();
        		execute();
        		sim.output();
        		JOptionPane.showMessageDialog(frame, "The output was successful!\n"+"(1) output file have been generated!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        	}
        });
        
        exit.addActionListener(new ActionListener()
        {
        	
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        
        JPanel east = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;
        east.add(buttonPanel, gbc);

        JPanel center = new JPanel(){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        
        JLabel label1 = new JLabel("Welcome to simulator!");
        center.setBorder(BorderFactory.createLineBorder(Color.BLACK));
   
        center.add(label1);
        
        frame.add(east, BorderLayout.EAST);
        frame.add(center);

        frame.pack();
        frame.setVisible(true);
    }

    public void getvalues(){
    	sim.setGEN(Integer.parseInt((String)JOptionPane.showInputDialog(frame,"Insert the number of generations:\n","Generations",JOptionPane.PLAIN_MESSAGE,null, null, null)));
		sim.setMR(Double.parseDouble((String)JOptionPane.showInputDialog(frame,"Insert the mutation rate:\n","Mutation Rate",JOptionPane.PLAIN_MESSAGE,null, null, "Insert values between 0 and 1")));
		sim.setRR(Double.parseDouble((String)JOptionPane.showInputDialog(frame,"Insert the recombination rate:\n","Recombination Rate",JOptionPane.PLAIN_MESSAGE,null, null, "Insert values between 0 and 1")));
		sim.setRFL(Integer.parseInt((String)JOptionPane.showInputDialog(frame,"Insert the recombination fragment length:\n","Recombination Fragment Length",JOptionPane.PLAIN_MESSAGE,null, null, null)));	
    }
    
    public void getparameters(){
    	sim.setPS(Integer.parseInt((String)JOptionPane.showInputDialog(frame, "Insert the population size:\n", "Population Size", JOptionPane.PLAIN_MESSAGE, null, null, null)));
    	sim.setSS(Integer.parseInt((String)JOptionPane.showInputDialog(frame, "Insert the sequence size:\n", "Sequence Size", JOptionPane.PLAIN_MESSAGE, null, null, null)));
    }
    
    public void execute(){
    	for(int i=0; i<sim.getGEN();i++){
			sim.mutation();
			sim.recombine();
		}
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