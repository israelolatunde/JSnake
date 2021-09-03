import javax.swing.JFrame;

public class GameFrame extends JFrame{
	
	//construct the frame
	public GameFrame() {
		
		this.add(new GamePanel());
		this.setTitle("JSnake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		
	}

}
