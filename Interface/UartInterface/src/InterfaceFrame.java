import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import classes.Input;
import javax.swing.JScrollBar;


public class InterfaceFrame implements SerialUartInterface {

	private JFrame frame;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
	
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
	private JTable tblInput;
	
	private DefaultTableModel tblModel;
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
	
	@Override
	public void onDataReadyListener(String input) {
		// Add row to table
		Input newMessage = new Input(input);
		DefaultTableModel dtm = (DefaultTableModel) tblInput.getModel();
		
		dtm.addRow(new Object[]{
				dateFormat.format(newMessage.getTime().getTime()),
				newMessage.getSender(),
				newMessage.getCommand(),
				newMessage.getMessage()});
		resizeTable(tblInput);
		tblInput.setRowSelectionInterval(tbl., index1);
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
		
		JLabel lblNewLabel = new JLabel("Input");
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
		spnrBaudSelect.setValue("9600");
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
		
		// Table
		String[] header = {"Time", "Sender", "Command", "Message"};
		String[][] data = {{"","","",""}};
		tblModel = new DefaultTableModel(data, header);
		tblInput = new JTable(tblModel);
		tblInput.setShowHorizontalLines(false);
		tblInput.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tblInput.setBounds(12, 69, 424, 87);
		frame.getContentPane().add(tblInput);
		JScrollPane jp=new JScrollPane(tblInput);
	    jp.setBounds(12, 69, 424, 87);
	    jp.setVisible(true);
	    frame.add(jp);
	    frame.getContentPane().add(jp);
	}
	
	private void resizeTable(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int col = 0; col < table.getColumnCount(); col++) {
			int width = 15; //Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, col);
				Component comp = table.prepareRenderer(renderer, row, col);
				width = Math.max(comp.getPreferredSize().width+1, width);
			}
			if (width > 300) {
				width = 300;
			}
			columnModel.getColumn(col).setPreferredWidth(width);
		}
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
