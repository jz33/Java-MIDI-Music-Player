package view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MusicGUI<InstrumentType> extends JFrame {
	
	private static final long serialVersionUID = 4848056440659133882L;

	private JPanel pnl_cont;
	private JPanel pnl_ctrl;
	private JSplitPane splitPane;
	private JScrollPane spnl_up = new JScrollPane();
	private JScrollPane spnl_dn = new JScrollPane();
	private JLabel lbl_file = new JLabel("Music Files:");
	private JTextArea txt_up;
	private JTextArea txt_dn;
	private JComboBox<String> list_files = new JComboBox<String>();
	private JComboBox<InstrumentType> list_instruments = new JComboBox<InstrumentType>();
	private JButton btnLoad  = new JButton("Load");
	private JButton btnParse = new JButton("Parse");
	private JButton btnPlay  = new JButton("Play");
	private JButton btnStop  = new JButton("Stop");
	private IModelControlAdapter _model;

	public MusicGUI(IModelControlAdapter _model) {
		this._model = _model;
		initGUI();
		btnParse.setEnabled(false);
		btnPlay.setEnabled(false);
	}

	public void start(){
		setVisible(true);
	}
	
	public void addInstrument(InstrumentType instrument) {
	    this.list_instruments.addItem(instrument);
	}
	
	public void addMusicFiles(String filename) {
		this.list_files.addItem(filename);
	}

	private void initGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		pnl_cont = new JPanel();
		pnl_cont.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnl_cont.setLayout(new BorderLayout(0, 0));
		setContentPane(pnl_cont);
		
		/**
		 * Label
		 */
		pnl_ctrl = new JPanel();
		pnl_cont.add(pnl_ctrl, BorderLayout.NORTH);
		pnl_ctrl.add(lbl_file);
		
		/**
		 * File list
		 */
		pnl_ctrl.add(list_files);
		list_files.setToolTipText("Music Files");
		
		/**
		 * Split panels and text areas
		 */
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pnl_cont.add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.5);	
		splitPane.setLeftComponent(spnl_up);
		spnl_up.setViewportBorder(new TitledBorder(null, "File Contents", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        {
        	this.txt_up = new JTextArea();
        	spnl_up.setViewportView(txt_up);
        }

		splitPane.setRightComponent(spnl_dn);
		spnl_dn.setViewportBorder(new TitledBorder(null, "Parsed Contents", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    {
	        this.txt_dn = new JTextArea();
	        spnl_dn.setViewportView(txt_dn);
	        txt_dn.setWrapStyleWord(true);
	        txt_dn.setLineWrap(true);
	    }
	    
		/**
		 * Button load
		 */
		btnLoad.setToolTipText("Load file");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("LOAD: "+list_files.getSelectedItem());
                txt_up.setText(_model.load((String)list_files.getSelectedItem()));
				btnParse.setEnabled(true);
			}
		});
		pnl_ctrl.add(btnLoad);
		
		/**
		 * Button parse
		 */
		btnParse.setToolTipText("Parse file");
		btnParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("PARSE: "+list_files.getSelectedItem());
				txt_dn.setText(_model.parse());
		        btnPlay.setEnabled(true);
		        
			}
		});
		pnl_ctrl.add(btnParse);
		
		/**
		 * Instrument list
		 */
		pnl_ctrl.add(list_instruments);
		list_instruments.setToolTipText("Select one instrument");
			
		/**
		 * Button play
		 */
		btnPlay.setToolTipText("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("PLAY: "+list_files.getSelectedItem());
				_model.play(list_instruments.getItemAt(list_instruments.getSelectedIndex()));	
			}
		});
		pnl_ctrl.add(btnPlay);
		
		/**
		 * Button stop
		 */
		btnStop.setToolTipText("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("STOP: "+list_files.getSelectedItem());
				_model.stop();	
			}
		});
		pnl_ctrl.add(btnStop);
	}
}

