package trainer;

import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

import stream.StreamWorkerAbs;
import stream.plaintext.PlainTextFormatAbs;
import stream.plaintext.PlainTextFormatSimple;
import stream.plaintext.PlainTextFormatTwitter;
import stream.plaintext.StreamPlainTextWorker;
import stream.twitter.StreamTwitterWorker;
import trainer.util.CBEntryBox;
import trainer.util.TFPlaceHolder;
import trainer.util.TTokensModel;
import trainer.util.TextFieldNer;

import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;

import trainer.util.LBCounter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import dictionary.dictionaryEntry.DictionaryEntry;
import dictionary.io.DictionaryIO;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Toolkit;

public class TrainerUI {

	private JFrame frmNerTrainer;
	private TFPlaceHolder tfTagsStreaming;
	private CBEntryBox cbEntryMethod;
	private JButton btnSelectFile;
	
	private StreamWorkerAbs streamWorker = null;
	private JTextField tfTokenToAdd;
	private JButton btnNextTweet;
	private LBCounter lblCounter;
	private JButton btnStartTrainer;
	private TextFieldNer tfToRecognice;
	private JTextField tfTokenResult;
	private JTable tokensTable;
	private JList<String> listCategoriesResult;
	private DefaultListModel<String> listCategoriesResultModel;
	private JList<String> listCategoriesToSelect;
	private DefaultListModel<String> listCategoriesToSelectModel;
	
	private String[] selectedFilePath = new String[]{ null, null};
	private JComboBox<String> cbFormatFile;
	private JButton btnStopTrainer;
	private JComboBox<String> cbTwitterFormat;
	private JMenuItem mntmSaveTweets;
	private boolean saveAsCancel = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrainerUI window = new TrainerUI();
					window.frmNerTrainer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrainerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNerTrainer = new JFrame();
		frmNerTrainer.setIconImage(Toolkit.getDefaultToolkit().getImage(TrainerUI.class.getResource("/images/trainer-logo.png")));
		frmNerTrainer.setTitle("Trainer");
		frmNerTrainer.setBounds(100, 100, 800, 600);
		frmNerTrainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		
		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		GroupLayout groupLayout = new GroupLayout(frmNerTrainer.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
				.addComponent(statusBar, GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
					.addGap(8)
					.addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
		);
		
		lblCounter = new LBCounter("Total tweets: ");
		GroupLayout gl_statusBar = new GroupLayout(statusBar);
		gl_statusBar.setHorizontalGroup(
			gl_statusBar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statusBar.createSequentialGroup()
					.addComponent(lblCounter, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(618, Short.MAX_VALUE))
		);
		gl_statusBar.setVerticalGroup(
			gl_statusBar.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statusBar.createSequentialGroup()
					.addComponent(lblCounter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		statusBar.setLayout(gl_statusBar);
		
		JPanel inputPanel = new JPanel();
		
		JPanel streamPanel = new JPanel();
		
		JPanel tokensPanel = new JPanel();
		
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(inputPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
						.addComponent(streamPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(tokensPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(streamPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(inputPanel, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tokensPanel, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
		);
		
		JScrollPane tablePanel = new JScrollPane();
		GroupLayout gl_tokensPanel = new GroupLayout(tokensPanel);
		gl_tokensPanel.setHorizontalGroup(
			gl_tokensPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(tablePanel, GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
		);
		gl_tokensPanel.setVerticalGroup(
			gl_tokensPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(tablePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
		);
		
		tokensTable = new JTable(new TTokensModel());
		tablePanel.setViewportView(tokensTable);
		tokensPanel.setLayout(gl_tokensPanel);
		
		cbEntryMethod = new CBEntryBox();
		
		tfTagsStreaming = new TFPlaceHolder("tag1, tag2, tag3 ...");
		tfTagsStreaming.setColumns(10);
		tfTagsStreaming.setVisible(false);
		
		cbTwitterFormat = new JComboBox<String>();
		cbTwitterFormat.setModel(new DefaultComboBoxModel<String>(new String[] {"By tags", "By users"}));
		cbTwitterFormat.setVisible(false);
		cbTwitterFormat.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch(cbTwitterFormat.getSelectedIndex()){
			    	case 0:
			    		tfTagsStreaming.setPlaceholder("tag1, tag2, tag3 ...");
			    		break;
			    	case 1:
			    		tfTagsStreaming.setPlaceholder("user1, user2, user3 ...");
			    		break;
		    	}
		    }
		});
		
		btnSelectFile = new JButton("Select file");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectFile();
			}
		});
		btnSelectFile.setVisible(true);
		cbFormatFile = new JComboBox<String>();
		cbFormatFile.setModel(new DefaultComboBoxModel<String>(new String[] {"Plain Text", "Twitter Format"}));
		cbFormatFile.setVisible(true);
		
		cbEntryMethod.addEntry("Load from file", new JComponent[] {btnSelectFile, cbFormatFile});
		cbEntryMethod.addEntry("Streaming Twitter", new JComponent[] {tfTagsStreaming, cbTwitterFormat});
		
		
		btnStartTrainer = new JButton("Start Trainer");
		btnStartTrainer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startTrainer();
			}
		});
		
		tfToRecognice = new TextFieldNer();
		tfToRecognice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tfTokenToAdd.setText(tfToRecognice.getSelectedText());
			}
		});
		tfToRecognice.setContentType("text/html;charset=UTF-8");
		tfToRecognice.setEditable(false);
		
		btnNextTweet = new JButton("Next tweet");
		btnNextTweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getNextTweet();
			}
		});
		btnNextTweet.setEnabled(false);
		
		btnStopTrainer = new JButton("Stop Trainer");
		btnStopTrainer.setVisible(false);
		btnStopTrainer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopTrainer();
			}
		});
		
		GroupLayout gl_streamPanel = new GroupLayout(streamPanel);
		gl_streamPanel.setHorizontalGroup(
			gl_streamPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_streamPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_streamPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_streamPanel.createSequentialGroup()
							.addComponent(tfToRecognice, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNextTweet))
						.addGroup(gl_streamPanel.createSequentialGroup()
							.addComponent(cbEntryMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbTwitterFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfTagsStreaming, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbFormatFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSelectFile)
							.addGap(18, 18, Short.MAX_VALUE)
							.addComponent(btnStopTrainer)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnStartTrainer)))
					.addContainerGap())
		);
		gl_streamPanel.setVerticalGroup(
			gl_streamPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_streamPanel.createSequentialGroup()
					.addGroup(gl_streamPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbEntryMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfTagsStreaming, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSelectFile)
						.addComponent(btnStartTrainer)
						.addComponent(cbFormatFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStopTrainer)
						.addComponent(cbTwitterFormat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_streamPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_streamPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNextTweet))
						.addGroup(gl_streamPanel.createSequentialGroup()
							.addGap(10)
							.addComponent(tfToRecognice, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)))
					.addContainerGap())
		);
		streamPanel.setLayout(gl_streamPanel);
		
		JPanel tokenInsertPanel = new JPanel();
		
		JPanel finalToken = new JPanel();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JLabel lblTokenToAdd = new JLabel("Token to add");
		
		tfTokenResult = new JTextField();
		tfTokenResult.setEnabled(false);
		tfTokenResult.setColumns(10);
		
		JLabel lblCategories = new JLabel("Categories");
		
		JButton btnAddToken = new JButton("Add token");
		btnAddToken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToken();
			}
		});
		
		JButton btnReplaceToken = new JButton("Replace token");
		btnReplaceToken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replaceToken();
			}
		});
		GroupLayout gl_finalToken = new GroupLayout(finalToken);
		gl_finalToken.setHorizontalGroup(
			gl_finalToken.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_finalToken.createSequentialGroup()
					.addGroup(gl_finalToken.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTokenToAdd)
						.addComponent(lblCategories))
					.addGap(18)
					.addGroup(gl_finalToken.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_finalToken.createSequentialGroup()
							.addComponent(btnAddToken)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnReplaceToken))
						.addComponent(tfTokenResult, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_finalToken.setVerticalGroup(
			gl_finalToken.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_finalToken.createSequentialGroup()
					.addGroup(gl_finalToken.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTokenToAdd)
						.addComponent(tfTokenResult, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_finalToken.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategories)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
					.addGroup(gl_finalToken.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddToken)
						.addComponent(btnReplaceToken))
					.addContainerGap())
		);
		
		listCategoriesResult = new JList<String>();
		listCategoriesResultModel = new DefaultListModel<String>();
		listCategoriesResult.setModel(listCategoriesResultModel);
		listCategoriesResult.setEnabled(false);
		scrollPane_1.setViewportView(listCategoriesResult);
		finalToken.setLayout(gl_finalToken);
		GroupLayout gl_inputPanel = new GroupLayout(inputPanel);
		gl_inputPanel.setHorizontalGroup(
			gl_inputPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_inputPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(tokenInsertPanel, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(finalToken, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_inputPanel.setVerticalGroup(
			gl_inputPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_inputPanel.createSequentialGroup()
					.addGroup(gl_inputPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(tokenInsertPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
						.addComponent(finalToken, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JLabel lblToken = new JLabel("Token");
		
		tfTokenToAdd = new JTextField();
		tfTokenToAdd.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent arg0) {
					updateTokenResult();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					updateTokenResult();
				}
				@Override
				public void removeUpdate(DocumentEvent arg0) { 
					updateTokenResult();
				}
				
				private void updateTokenResult(){
					tfTokenResult.setText(tfTokenToAdd.getText());
				}
			});
		tfTokenToAdd.setColumns(10);
		
		JLabel lblCategory = new JLabel("Category");
		
		JScrollPane scrollPane = new JScrollPane();
		
		listCategoriesToSelect = new JList<String>();
		listCategoriesToSelect.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				List<String> tmpList = listCategoriesToSelect.getSelectedValuesList();
				listCategoriesResultModel.clear();
				for(String s: tmpList){
					listCategoriesResultModel.addElement(s);
				}
			}
		});
		listCategoriesToSelectModel = new DefaultListModel<String>();
		listCategoriesToSelect.setModel(listCategoriesToSelectModel);
		scrollPane.setViewportView(listCategoriesToSelect);
		
		JButton btnAddCategory = new JButton("Add category");
		btnAddCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String category = JOptionPane.showInputDialog("New category");
				if(!category.trim().isEmpty())
					addCategory(category.trim());
			}
		});
		GroupLayout gl_tokenInsertPanel = new GroupLayout(tokenInsertPanel);
		gl_tokenInsertPanel.setHorizontalGroup(
			gl_tokenInsertPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tokenInsertPanel.createSequentialGroup()
					.addComponent(lblToken)
					.addGap(30)
					.addComponent(tfTokenToAdd, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
					.addGap(5))
				.addGroup(gl_tokenInsertPanel.createSequentialGroup()
					.addComponent(lblCategory)
					.addGap(18)
					.addGroup(gl_tokenInsertPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddCategory)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_tokenInsertPanel.setVerticalGroup(
			gl_tokenInsertPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tokenInsertPanel.createSequentialGroup()
					.addGroup(gl_tokenInsertPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblToken)
						.addComponent(tfTokenToAdd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tokenInsertPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategory)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
					.addGap(6)
					.addComponent(btnAddCategory)
					.addContainerGap())
		);
		tokenInsertPanel.setLayout(gl_tokenInsertPanel);
		inputPanel.setLayout(gl_inputPanel);
		mainPanel.setLayout(gl_mainPanel);
		frmNerTrainer.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmNerTrainer.setJMenuBar(menuBar);
		
		JMenu mnfile = new JMenu("File");
		mnfile.setMnemonic('F');
		menuBar.add(mnfile);
		
		JMenuItem mntmnew = new JMenuItem("New");
		mntmnew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmnew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearTrainer();
			}
		});
		mnfile.add(mntmnew);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openDictionary();
			}
		});
		mnfile.add(mntmOpen);
		
		JSeparator separator = new JSeparator();
		mnfile.add(separator);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveDictionary();
			}
		});
		mnfile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save as");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveAsDictionary();
			}
		});
		mnfile.add(mntmSaveAs);
		
		JSeparator separator_1 = new JSeparator();
		mnfile.add(separator_1);
		
		mntmSaveTweets = new JMenuItem("Save tweets");
		mntmSaveTweets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveListTweets();
			}
		});
		mntmSaveTweets.setEnabled(false);
		
		lblCounter.forActivate(new JComponent[]{btnNextTweet, mntmSaveTweets});
		
		mnfile.add(mntmSaveTweets);
		
		JSeparator separator_2 = new JSeparator();
		mnfile.add(separator_2);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnfile.add(mntmClose);
		
	}
	private void saveDictionary(){
		saveDictionary(false);
	}
	private void saveAsDictionary(){
		saveDictionary(true);
	}
	
	private void saveDictionary(boolean saveAs) {
		if(saveAs == true || selectedFilePath[0] == null)
			this.selectSavePath();
		
		if(saveAsCancel == true){
			saveAsCancel = false;
			return ;
		}
		
		TTokensModel model = (TTokensModel) this.tokensTable.getModel();
		HashMap<String, Vector<String>> dictionary = new HashMap<String, Vector<String>>();
		for(int index=0; index < this.listCategoriesToSelectModel.size(); index++){
			String category = this.listCategoriesToSelectModel.getElementAt(index);
			if( ! dictionary.containsKey(category) ){
				dictionary.put(category, new Vector<String>());
			}
		}
		Iterator<DictionaryEntry> it = model.iterator();
		while(it.hasNext()){
			DictionaryEntry tmp = it.next();
			for(String category: tmp.getCategory()){
				(dictionary.get(category)).add(tmp.getText());
			}
		}
		
		if( DictionaryIO.savePlainTextWithCategories(selectedFilePath[0], dictionary) )
			JOptionPane.showMessageDialog(null, "The dictionary has been saved", "Save success", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Ups.. An error ocurred", "Save fail", JOptionPane.ERROR_MESSAGE);
	}

	private void replaceToken() {
		String tokenTmp = this.tfTokenResult.getText().trim();
		Vector<String> categories = new Vector<String>();
		for(int i=0; i < this.listCategoriesResultModel.size(); i++)
			categories.add(this.listCategoriesResultModel.get(i));
		( (TTokensModel) this.tokensTable.getModel() ).replaceToken(tokenTmp, categories);
	}

	private void addToken() {
		String tokenTmp = this.tfTokenResult.getText().trim();
		Vector<String> categories = new Vector<String>();
		for(int i=0; i < this.listCategoriesResultModel.size(); i++)
			categories.add(this.listCategoriesResultModel.get(i));
		( (TTokensModel) this.tokensTable.getModel() ).addToken(tokenTmp, categories);
		this.tfToRecognice.addEntry(tokenTmp, categories);
	}
	
	private void selectSavePath(){
		JFileChooser chooser = new JFileChooser("./");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain Text (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(frmNerTrainer);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFilePath[0] = chooser.getSelectedFile().getPath();
			selectedFilePath[1] = chooser.getSelectedFile().getName();
			if(!selectedFilePath[0].endsWith(".txt")){
				selectedFilePath[0] += ".txt";
				selectedFilePath[1] += ".txt";
			}
		}else{
			saveAsCancel = true;
		}
	}

	private void selectFile() {
		JFileChooser chooser = new JFileChooser("./");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain Text (*.txt)", "txt");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(frmNerTrainer);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFilePath[0] = chooser.getSelectedFile().getPath();
			selectedFilePath[1] = chooser.getSelectedFile().getName();
		}
	}

	private void getNextTweet() {
		String tweet = this.streamWorker.getNextTweet();
		if(tweet != null) this.tfToRecognice.setText(tweet);
	}

	private void startTrainer() {
		switch(cbEntryMethod.getSelectedIndex()){
			case 0:
				if(this.selectedFilePath[0] != null && this.selectedFilePath[1] != null){
					PlainTextFormatAbs format = null;
					switch(this.cbFormatFile.getSelectedIndex()){
						case 0:
							format = new PlainTextFormatSimple();
							break;
						case 1:
							format = new PlainTextFormatTwitter();
							break;
					}
					this.streamWorker = new StreamPlainTextWorker(this.selectedFilePath[0], format);
					this.streamWorker.addObserver(this.lblCounter);
					this.streamWorker.start();
				}
				break;
			case 1:
				this.streamWorker = new StreamTwitterWorker(this.tfTagsStreaming.getText(), this.cbTwitterFormat.getSelectedIndex());
				this.streamWorker.addObserver(this.lblCounter);
				this.streamWorker.start();
				break;
		}
		this.btnStartTrainer.setVisible(false);
		this.btnStopTrainer.setVisible(true);
	}
	
	private void stopTrainer() {
		this.streamWorker.interrupt();
		this.streamWorker = null;
		this.lblCounter.updateCounter(0);
		this.btnStartTrainer.setVisible(true);
		this.btnStopTrainer.setVisible(false);
	}
	
	private void openDictionary(){
		selectFile();
		if(this.selectedFilePath[0] != null){
			Vector<DictionaryEntry> entries = (Vector<DictionaryEntry>) DictionaryIO.loadPlainTextWithCategories(this.selectedFilePath[0]);
			for(DictionaryEntry entry: entries){
				Vector<String> categories = new Vector<String>();
				for(String category: entry.getCategory()){
					categories.add(category.trim());
					addCategory(category.trim());
				}
				( (TTokensModel) this.tokensTable.getModel() ).addToken(entry.getText(), categories);
			}
			this.tfToRecognice.loadDictionary(this.selectedFilePath[0]);
		}
	}
	
	private void addCategory(String category){
		if ( ! ( (DefaultListModel<String>) listCategoriesToSelect.getModel() ).contains(category) )
			listCategoriesToSelectModel.addElement(category);
	}
	
	private void saveListTweets(){
		String[] selectedFilePathTmp = new String[]{ selectedFilePath[0], selectedFilePath[1]};
		selectSavePath();
		if(saveAsCancel == true){
			saveAsCancel = false;
		}else{
			Writer out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFilePath[0])));
				for(String s: this.streamWorker.getForSave()){
					out.write(s+"\r\n");
				}
			} catch (Exception e){
				e.printStackTrace();
			} finally {
			    try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		selectedFilePath[0] = selectedFilePathTmp[0];
		selectedFilePath[1] = selectedFilePathTmp[1];		
	}
	
	private void clearTrainer(){
		selectedFilePath[0] = null;
		selectedFilePath[1] = null;
		saveAsCancel = false;
		if(streamWorker != null)
			streamWorker.interrupt();
		lblCounter.updateCounter(0);
		btnStartTrainer.setVisible(true);
		btnStopTrainer.setVisible(false);
		tfToRecognice.clearTFNer();
		listCategoriesResultModel.clear();
		listCategoriesToSelectModel.clear();
		tfTagsStreaming.clear();
		tfTokenResult.setText("");
		tfTokenToAdd.setText("");
		( (TTokensModel) this.tokensTable.getModel() ).clear();
	}
}
