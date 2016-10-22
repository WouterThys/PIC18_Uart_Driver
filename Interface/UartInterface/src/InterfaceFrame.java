import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JButton;
import javax.swing.ImageIcon;


public class InterfaceFrame implements SerialUartInterface {

	private JFrame frame;
	private JTextField textField;
	
	private static JSpinner spnrPortSelect;
	private static SpinnerListModel spnrPorts;
	private String[] portStrings;

	private static JSpinner spnrBaudSelect;
	private static SpinnerListModel spnrBaudRates;
	private String[] baudStrings = {"4800", "9600", "19200", "38400", "57600", "115200"};
	
	private JLabel connectedLed;
	private ImageIcon redLedImg;
	private ImageIcon greenLedImg;
	
	private JButton btnConnect;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfaceFrame window = new InterfaceFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//sudo rm -f LCK*

	/**
	 * Create the application.
	 */
	public InterfaceFrame() {
		initialize();
	}
	
	@Override
	public void onConnectedListener() {
		connectedLed.setIcon(greenLedImg);
	}
	@Override
	public void onDisconnectedListener() {
		connectedLed.setIcon(redLedImg);
	}
	
	private void loadImages() {
		// red led
		InputStream inp = ClassLoader.getSystemResourceAsStream("redled.png");
		BufferedImage led = null;
		try {
			led = ImageIO.read(inp);
			redLedImg = new ImageIcon(resizeImage(led, connectedLed.getWidth(), connectedLed.getHeight()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// green led
		inp = ClassLoader.getSystemResourceAsStream("greenled.png");
		led = null;
		try {
			led = ImageIO.read(inp);
			greenLedImg = new ImageIcon(resizeImage(led, connectedLed.getWidth(), connectedLed.getHeight()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 72, 178, 189);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(12, 55, 70, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblComPort = new JLabel("Com port");
		lblComPort.setBounds(30, 12, 70, 15);
		frame.getContentPane().add(lblComPort);
		
		JLabel lblBaud = new JLabel("Baud");
		lblBaud.setBounds(152, 12, 70, 15);
		frame.getContentPane().add(lblBaud);
		
		
		// Com port spinner
		ArrayList<String> ports = SerialInterface.listPorts();
		portStrings = new String[ports.size()];
		for(int i=0; i<ports.size(); i++) {
			portStrings[i] = ports.get(i);
		}
		
		spnrPorts = new SpinnerListModel(portStrings);
		spnrPortSelect = new JSpinner(spnrPorts);
		spnrPortSelect.setBounds(12, 28, 88, 20);
		frame.getContentPane().add(spnrPortSelect);
		
		// Baud rate spinner
		spnrBaudRates = new SpinnerListModel(baudStrings);
		spnrBaudSelect = new JSpinner(spnrBaudRates);
		spnrBaudSelect.setBounds(112, 28, 78, 20);
		frame.getContentPane().add(spnrBaudSelect);
		
		// Connect button
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!SerialInterface.isConnected()) {
					SerialInterface.startSerialInterface(
							(String)spnrPortSelect.getValue(), 
							Integer.valueOf(spnrBaudSelect.getValue().toString()),
							InterfaceFrame.this
							);
					btnConnect.setText("Disconnect");
				} else {
					SerialInterface.stopSerialInterface();
					btnConnect.setText("Connect");
				}
			}
		});
		btnConnect.setBounds(202, 25, 99, 25);
		frame.getContentPane().add(btnConnect);
		
		
		// Image
		connectedLed = new JLabel("");
		connectedLed.setBounds(419, 0, 29, 27);
		frame.getContentPane().add(connectedLed);
		loadImages();
		connectedLed.setIcon(redLedImg);
	}
	
	private static BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp,0,0,null);
		g2d.dispose();
		
		return dimg;
	}
}
