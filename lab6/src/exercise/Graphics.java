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
        JButton exit = new JButton("Exit");
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(input);
        buttonPanel.add(new JButton("Generate sequence"));
        buttonPanel.add(exit);

        input.addActionListener(new ActionListener()
        {

		@Override
		public void actionPerformed(ActionEvent e) {
			//sim.execute();
			sim.input();
			getvalues();
			//System.out.println(sim.getMR());
			//System.out.println(sim.getRR());
			//System.out.println(sim.getGEN());
			execute();
			
			sim.output();
			JOptionPane.showMessageDialog(frame, "The output was successful!\n"+"(1) output file have been generated!", "Successful", JOptionPane.INFORMATION_MESSAGE);
			//System.out.println(sim.getMR());
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
    
    public void execute(){
    	//sim.setMR(0.3);
    	//sim.setRR(0.3);
    	//sim.setRFL(5);
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