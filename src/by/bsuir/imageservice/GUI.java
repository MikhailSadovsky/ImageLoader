package by.bsuir.imageservice;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import by.bsuir.imageservice.api.LoadHandler;
import by.bsuir.imageservice.api.LoadService;
import by.bsuir.imageservice.api.impl.ImageLoadHandlerImpl;
import by.bsuir.imageservice.api.impl.ImageLoadServiceImpl;
import by.bsuir.imageservice.cache.CacheManager;
import by.bsuir.imageservice.cache.Cacheable;
import by.bsuir.imageservice.entity.Image;
import by.bsuir.imageservice.exception.ProjectException;

/**
 * This class is used to display the GUI that will allow the user to download
 * images from a webpage of their choosing.
 * 
 * 
 */
public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(GUI.class);

    private JPanel contentPane;
    private JTextField websiteInput;
    private JTextField locationInput;
    private JTextField threadsInput;
    private JTable imageDetailsTable;
    private Component errorFrame;
    private DefaultTableModel defaultTable;

    private static ExecutorService threadPool;

    private List<LoadService> services;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	GUI frame = new GUI();
	frame.setVisible(true);
    }

    public GUI() {
	extractGUI();
    }

    private void extractGUI() {
	CacheManager.setGUI(this);
	services = new ArrayList<LoadService>();
	services.add(new ImageLoadServiceImpl());
	setBackground(SystemColor.WHITE);
	setTitle("Image Downloader");
	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	setBounds(100, 100, 750, 450);
	setResizable(false);
	contentPane = new JPanel();
	contentPane.setBackground(SystemColor.WHITE);
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	contentPane.setLayout(null);
	addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		exitApp();

	    }
	});

	JLabel websiteLabel = new JLabel("URL:");
	websiteLabel.setForeground(Color.BLACK);
	websiteLabel.setBackground(Color.WHITE);
	websiteLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
	websiteLabel.setBounds(10, 11, 65, 14);
	contentPane.add(websiteLabel);

	websiteInput = new JTextField();
	websiteInput
		.setText("http://i1008.photobucket.com/albums/af201/visuallightbox/Butterfly/12.jpg");
	websiteInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
	websiteInput.setBackground(SystemColor.WHITE);
	websiteInput.setBounds(128, 8, 236, 20);
	contentPane.add(websiteInput);
	websiteInput.setColumns(10);

	JLabel locationLabel = new JLabel("Download Location:");
	locationLabel.setForeground(Color.BLACK);
	locationLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
	locationLabel.setBounds(10, 33, 108, 14);
	contentPane.add(locationLabel);

	locationInput = new JTextField();
	locationInput.setText("D:/images");
	locationInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
	locationInput.setBackground(SystemColor.WHITE);
	locationInput.setBounds(128, 33, 235, 20);
	contentPane.add(locationInput);
	locationInput.setColumns(10);

	JButton locationButton = new JButton("Choose Location");
	locationButton.setBounds(383, 33, 149, 20);
	contentPane.add(locationButton);

	locationButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JFileChooser locationChooser = new JFileChooser();
		locationChooser.setCurrentDirectory(new java.io.File("."));
		locationChooser
			.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		locationChooser.setAcceptAllFileFilterUsed(false);
		int returnVal = locationChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    locationInput.setText(locationChooser.getSelectedFile()
			    .getAbsolutePath());
		}
	    }
	});

	JLabel threadsLabel = new JLabel("Threads:");
	threadsLabel.setForeground(Color.BLACK);
	threadsLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
	threadsLabel.setBounds(383, 11, 55, 14);
	contentPane.add(threadsLabel);

	threadsInput = new JTextField();
	threadsInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
	threadsInput.setBackground(SystemColor.WHITE);
	threadsInput.setBounds(448, 8, 84, 20);
	contentPane.add(threadsInput);
	threadsInput.setColumns(10);

	String[] columns = { "File name", "File URL" };
	defaultTable = new DefaultTableModel(columns, 0);

	imageDetailsTable = new JTable(defaultTable);
	imageDetailsTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
	imageDetailsTable.setBackground(SystemColor.inactiveCaption);
	imageDetailsTable.setBounds(10, 81, 714, 289);
	contentPane.add(imageDetailsTable);

	JButton searchButton = new JButton("Download");
	searchButton.setBounds(10, 381, 142, 20);
	contentPane.add(searchButton);

	searchButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (websiteInput.getText().length() < 10) {
		    showErrorDialog("The website name entered is invalid!",
			    "Invalid website!");
		} else if (threadsInput.getText().length() > 0) {
		    if (Integer.parseInt(threadsInput.getText()) > 8
			    || Integer.parseInt(threadsInput.getText()) < 1) {
			showErrorDialog(
				"The number of threads must be between 1 and 8!",
				"Invalid number of threads!");
		    } else {
			String url = websiteInput.getText();
			String path = locationInput.getText();
			threadPool = Executors.newFixedThreadPool(Integer
				.parseInt(threadsInput.getText()));
			loading(url, path);
			imageDetailsTable.updateUI();

		    }
		} else {
		    showErrorDialog(
			    "The number of threads must be between 1 and 8 and not null!",
			    "Invalid number of threads!");
		}
	    }
	});

	JScrollPane scrollPane = new JScrollPane(imageDetailsTable);
	scrollPane.setBounds(10, 81, 714, 289);
	contentPane.add(scrollPane);
    }

    private void loading(String url, String path) {
	LoadHandler selectedHandler = new ImageLoadHandlerImpl();
	for (LoadService service : services) {
	    LoadHandler searchHandler;
	    searchHandler = service.getHandler(url);
	    selectedHandler = searchHandler;
	}

	if (null != selectedHandler) {
	    try {
		selectedHandler.load(new URL(url), new File(path), threadPool);
	    } catch (ProjectException | MalformedURLException e) {
		showErrorDialog("Can't load this image! " + e.getMessage(),
			"Can't load!");
		log.error(e.getMessage(), e);
	    }
	} else {
	    showErrorDialog("Invalid website url entered! ", "Invalid url!");
	}
    }

    private void exitApp() {
	int response = JOptionPane.showConfirmDialog(contentPane,
		"Do you really want to quit?", "Quit?",
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	if (response == JOptionPane.YES_OPTION) {
	    threadPool.shutdown();
	    System.exit(0);
	}
    }

    private void showErrorDialog(String messsage, String title) {
	JOptionPane.showMessageDialog(errorFrame, messsage, title,
		JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Updates table, when image was added or removed
     * 
     * @param imageMap
     */
    public void updateImageTable(Map<Object, Cacheable> imageMap) {
	defaultTable.setRowCount(0);
	for (Iterator<Object> it = imageMap.keySet().iterator(); it.hasNext();) {
	    Object key = it.next();
	    Image image = (Image) imageMap.get(key);
	    defaultTable.addRow(new String[] { image.getName(),
		    image.getIdentifier() });
	}
    }
}
