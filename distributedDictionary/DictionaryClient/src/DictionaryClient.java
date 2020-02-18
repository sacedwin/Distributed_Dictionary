/* Name: Sachin Antony Edwin Earayil
 * Student Id: 947044
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class DictionaryClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private static String serverAddress;
	private static int serverPort;
	private static Socket socket;
	private JTextField textField;
	private JTextArea textArea;
	private JLabel lblNewLabel;
	private JLabel lblMeaning;
	private JLabel lblNewLabel_1;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length != 2)		//if the command lacks any of the required parameters
		{
			System.err.println("Error! Port number or server address missing!");
			System.exit(1);
		}
		
		serverAddress = args[0];
		serverPort = Integer.parseInt(args[1]);
		try
		{
			socket = new Socket(serverAddress, serverPort);
		}
		catch(SocketException e)
		{
			System.err.println("Socket error! " + e);
			JOptionPane.showMessageDialog(null, "Socket error!");
			System.exit(1);
		}
		catch(IOException e)
		{
			System.err.println("Connection Failiure!: " + e);
			JOptionPane.showMessageDialog(null, "Connection Failed!");
			System.exit(1);
		}
		catch(Exception e)
		{
			System.err.println(e);
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryClient frame = new DictionaryClient();
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
	public DictionaryClient() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "The app is exiting! Thanks for using our services!");
				System.out.println("Client closed!");
				System.exit(0);
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add();		//when the ADD button is pressed the function add() is called
			}
		});
		
		lblNewLabel_1 = new JLabel("Dictionary");
		lblNewLabel_1.setBackground(Color.BLACK);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridheight = 2;
		gbc_lblNewLabel_1.gridwidth = 3;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		lblNewLabel = new JLabel("Word");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 3;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 3;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		lblMeaning = new JLabel("Meaning");
		GridBagConstraints gbc_lblMeaning = new GridBagConstraints();
		gbc_lblMeaning.insets = new Insets(0, 0, 5, 5);
		gbc_lblMeaning.gridx = 0;
		gbc_lblMeaning.gridy = 4;
		contentPane.add(lblMeaning, gbc_lblMeaning);
		
		textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 2;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 4;
		contentPane.add(textArea, gbc_textArea);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 5;
		contentPane.add(btnAdd, gbc_btnAdd);
		
		JButton btnQuery = new JButton("Query");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				query();		//when the QUERY button is pressed the function query() is called
			}
		});
		GridBagConstraints gbc_btnQuery = new GridBagConstraints();
		gbc_btnQuery.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuery.gridx = 1;
		gbc_btnQuery.gridy = 5;
		contentPane.add(btnQuery, gbc_btnQuery);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();		//when the REMOVE button is pressed the function remove() is called
			}
		});
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove.gridx = 2;
		gbc_btnRemove.gridy = 5;
		contentPane.add(btnRemove, gbc_btnRemove);
	}
	
	private void query()		//function to send a word to a server and get it's meaning
	{
		if(!textField.getText().isEmpty())
		{
			String clientMsg = "query " + textField.getText();
			DataInputStream reader = null;
			DataOutputStream writer = null;
			try 
			{			
				writer = new DataOutputStream(socket.getOutputStream());
				writer.writeUTF(clientMsg);
				writer.flush();			//a message is created and sent to the server
				
				reader = new DataInputStream(socket.getInputStream());
				String temp = null;				
				temp= reader.readUTF();		//message received from the server
				
				if(temp.equals("Dictionary doesnt contain this word!\n"))
					JOptionPane.showMessageDialog(null,temp);
				else
					textArea.setText(temp);
			} 
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null,"Error! Disruptions in connection!");
				System.out.println(e);
			}
			catch (Exception e) 
			{
				System.out.println(e);
				e.printStackTrace();
			}
		}
		else
			JOptionPane.showMessageDialog(null,"The word field is empty!");
	}
	
	private void add()		//function to add a new word to the dictionary in the server
	{
		if(textField.getText().isEmpty()||textArea.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "The fields cannot be empty");
		}
		else
		{
			String clientMsg = "add " + textField.getText() +" "+ textArea.getText();
			DataInputStream reader = null;
			DataOutputStream writer = null;
			try 
			{
				writer = new DataOutputStream(socket.getOutputStream());
				writer.writeUTF(clientMsg);
				writer.flush();		//a message is created and sent to the server
				
				reader = new DataInputStream(socket.getInputStream());
				JOptionPane.showMessageDialog(null,reader.readUTF());		//message received from the server
			} 
			catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,"Error! Disruptions in connection!");
				System.out.println(e);			
			}
			catch(Exception e)
			{
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
	
	private void remove()		//function to remove a word from the dictionary
	{
		if(textField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "The fields cannot be empty");
		}
		else
		{
			String clientMsg = "remove " + textField.getText();
			DataInputStream reader = null;
			DataOutputStream writer = null;
			try 
			{
				writer = new DataOutputStream(socket.getOutputStream());
				writer.writeUTF(clientMsg);
				writer.flush();			//a message is created and sent to the server
				
				reader = new DataInputStream(socket.getInputStream());
				JOptionPane.showMessageDialog(null,reader.readUTF());		//message received from the server
			} 
			catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null,"Error! Disruptions in connection!");
				System.out.println(e);			
			}
			catch(Exception e)
			{
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
}
