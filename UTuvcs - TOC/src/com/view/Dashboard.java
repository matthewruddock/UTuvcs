package com.view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.border.LineBorder;

import javax.swing.JTextArea;
import java.awt.Insets;

public class Dashboard extends JFrame implements ActionListener{

	private static final long serialVersionUID = 2791548885028568900L;
	
	private JLabel carState_lbl;
	private JPanel contentPane;
	private CarViewPanel carView_panel;
	private JButton parkBtn;
	private JButton reverseBtn;
	private JButton driveBtn;
	private JButton holdBrakeBtn;
	private JButton brakePressedBtn;
	private JButton accelerateBtn;
	private JButton startBtn;
	private JButton engageSeatBeltBtn;
	private JButton cruiseControlBtn;
	private JTextArea symbol_textArea;
	
	private CurrentState currentState;
	private JLabel speed_lbl;
	private JLabel accept_reject_lbl;
	private int speed = 0;


	
	@SuppressWarnings("unused")
	private class CurrentState{
		private String stateName;
		private States state;
		
		public CurrentState() {
			stateName = "VEHICLE OFF";
			state = States.Q0;
		}
		
		public CurrentState(String stateName, States state) {
			this.stateName = stateName;
			this.state = state;
		}
		
		public void setStateName(String stateName) {
			this.stateName = stateName;
		}
		
		public void setState(States state) {
			this.state = state;
		}
		
		public String getStateName() {
			return this.stateName;
		}
		
		public States getState() {
			return this.state;
		}
		
	}
	
	private enum Operation{
		INCREMENT, DECREMENT, ZERO;
	}
	
	private enum Alphabet{
		START('S'), HOLD_BRAKE('H'), ENGAGE_SEATBELT('B'), PARK('P'), ACCELERATE('A'),
		REVERSE('R'), PRESS_BRAKE('K'), DRIVE('D'), CRUISE_CONTROL('C');
		
		final char accept;
		
		Alphabet(char accept) {
			this.accept = accept;
		}
	}
	
	private enum States {
		/**
		 * AUTOMATON'S STATES 'Q'
		 * TRUE = ACCEPT STATE
		 * FALSE = REJECT STATE
		 */
		Q0(true), Q1(false), Q2(false), 
		Q3(false), Q4(false), Q5(false), 
		Q6(false), Q7(false);

		final boolean accept;
		
		States(boolean accept) {
			this.accept = accept;
		}
		
		States START;
		States HOLD_BRAKE;
		States ENGAGE_SEATBELT;
		States PARK;
		States ACCELERATE;
		States REVERSE;
		States PRESS_BRAKE;
		States DRIVE;
		States CRUISE_CONTROL;
		
		static {

			Q0.START = Q2; Q0.HOLD_BRAKE = Q1; 
			Q1.START = Q3;
			Q2.START = Q0; Q2.HOLD_BRAKE = Q1;
			Q3.START = Q0; Q3.ENGAGE_SEATBELT = Q4;
			Q4.ACCELERATE = Q4; Q4.PARK = Q3; Q4.REVERSE = Q6; Q4.DRIVE = Q5;
			Q5.ACCELERATE = Q5; Q5.PRESS_BRAKE = Q5; Q5.CRUISE_CONTROL = Q7;
			Q6.HOLD_BRAKE = Q4; Q6.ACCELERATE = Q6; Q6.PRESS_BRAKE = Q6;
			Q7.ACCELERATE = Q7; Q7.PRESS_BRAKE = Q5;
			
		}
		
		States transition(Alphabet symbol) {
			switch(symbol) {
				case START:
					return this.START;
				case HOLD_BRAKE:
					return this.HOLD_BRAKE;
				case ENGAGE_SEATBELT:
					return this.ENGAGE_SEATBELT;
				case PARK:
					return this.PARK;
				case ACCELERATE:
					return this.ACCELERATE;
				case REVERSE: 
					return this.REVERSE;
				case PRESS_BRAKE: 
					return this.PRESS_BRAKE;
				case DRIVE:
					return this.DRIVE;
				case CRUISE_CONTROL:
					return this.CRUISE_CONTROL;
				default:
					return null;
			}
		}
	}

	public Dashboard() {
		initializeComponents();
		registerListeners();
	}
	

	private void initializeComponents(){
        currentState = new CurrentState();
        setTitle("UTuvcs");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Changes frame icon
		ImageIcon frameIcon = new ImageIcon("./Resources/img/Utech_logo.jpg");
		setIconImage(frameIcon.getImage());
		
		setBounds(100, 100, 760, 652);
		
		//Centers frame on screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel carStatePanel = new JPanel();
		carStatePanel.setBackground(new Color(0, 0, 0));
		carStatePanel.setBounds(0, 0, 744, 46);
		contentPane.add(carStatePanel);
		
		carState_lbl = new JLabel(currentState.getStateName());
		carState_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		carState_lbl.setPreferredSize(new Dimension(250, 30));
		carState_lbl.setMaximumSize(new Dimension(200, 30));
		carState_lbl.setFont(new Font("Arial Rounded MT Bold", Font.ITALIC, 16));
		carState_lbl.setForeground(Color.WHITE);
		carStatePanel.add(carState_lbl);		
		
		accept_reject_lbl = new JLabel();
		accept_reject_lbl.setPreferredSize(new Dimension(30, 30));
		carStatePanel.add(accept_reject_lbl);
		
		carView_panel = new CarViewPanel();
		carView_panel.setBounds(0, 46, 744, 391);
		contentPane.add(carView_panel);
		
		brakePressedBtn = new JButton("Press Brake");
		brakePressedBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
		brakePressedBtn.setBorder(null);
		brakePressedBtn.setBounds(164, 286, 89, 23);
		carView_panel.add(brakePressedBtn);
		
		accelerateBtn = new JButton();
		accelerateBtn.setIcon(resizeImage("ACCELERATE.jpg", 22, 40));
		accelerateBtn.setBorder(null);
		accelerateBtn.setBorderPainted(false);
		accelerateBtn.setContentAreaFilled(false);
		accelerateBtn.setBounds(273, 276, 24, 45);
		carView_panel.add(accelerateBtn);
		
		parkBtn = new JButton("P");
		parkBtn.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 25));
		parkBtn.setForeground(new Color(255, 255, 255));
		parkBtn.setActionCommand("P");
		parkBtn.setBackground(new Color(0, 0, 0));
		parkBtn.setBorder(null);
		parkBtn.setBounds(321, 165, 89, 45);
		carView_panel.add(parkBtn);
		
		reverseBtn = new JButton("R");
		reverseBtn.setForeground(new Color(255, 255, 255));
		reverseBtn.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 25));
		reverseBtn.setBackground(new Color(0, 0, 0));
		reverseBtn.setBorder(null);
		reverseBtn.setBounds(321, 230, 89, 45);
		carView_panel.add(reverseBtn);
		
		driveBtn = new JButton("D");
		driveBtn.setForeground(new Color(255, 255, 255));
		driveBtn.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 25));
		driveBtn.setBackground(new Color(0, 0, 0));
		driveBtn.setBorder(null);
		driveBtn.setBounds(321, 286, 89, 45);
		carView_panel.add(driveBtn);
		
		holdBrakeBtn = new JButton("BRAKE HOLD");
		holdBrakeBtn.setForeground(new Color(255, 255, 255));
		holdBrakeBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
		holdBrakeBtn.setBackground(new Color(0, 0, 0));
		holdBrakeBtn.setBorder(null);
		holdBrakeBtn.setBounds(308, 351, 117, 29);
		carView_panel.add(holdBrakeBtn);
		
		speed_lbl = new JLabel("0");
		speed_lbl.setOpaque(true);
		speed_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		speed_lbl.setBorder(new LineBorder(new Color(255, 255, 255), 3, true));
		speed_lbl.setForeground(new Color(255, 255, 255));
		speed_lbl.setBackground(new Color(0, 0, 0));
		speed_lbl.setFont(new Font("Times New Roman", Font.BOLD, 20));
		speed_lbl.setBounds(560, 31, 54, 38);
		carView_panel.add(speed_lbl);
		
		startBtn = new JButton();
		startBtn.setIcon(new ImageIcon(Dashboard.class.getResource("/img/engineOffBtn.png")));
		startBtn.setBorder(null);
		startBtn.setBorderPainted(false);
		startBtn.setContentAreaFilled(false);
		startBtn.setBounds(147, 448, 65, 59);
		contentPane.add(startBtn);
		
		engageSeatBeltBtn = new JButton();
		engageSeatBeltBtn.setIcon(new ImageIcon(Dashboard.class.getResource("/img/engageSeatbelt.png")));
		engageSeatBeltBtn.setBorder(null);
		engageSeatBeltBtn.setBounds(500, 448, 72, 59);
		engageSeatBeltBtn.setBorderPainted(false);
		engageSeatBeltBtn.setContentAreaFilled(false);
		contentPane.add(engageSeatBeltBtn);
		
		cruiseControlBtn = new JButton("Criuse Control");
		cruiseControlBtn.setForeground(new Color(255, 255, 255));
		cruiseControlBtn.setBackground(new Color(0, 0, 0));
		cruiseControlBtn.setBorder(null);
		cruiseControlBtn.setFont(new Font("Times New Roman", Font.BOLD, 16));
		cruiseControlBtn.setBounds(292, 461, 155, 23);
		contentPane.add(cruiseControlBtn);
		
		symbol_textArea = new JTextArea();
		symbol_textArea.setEditable(false);
		symbol_textArea.setLineWrap(true);
		symbol_textArea.setMargin(new Insets(5, 5, 5, 5));
		symbol_textArea.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		symbol_textArea.setBounds(65, 543, 561, 59);
		contentPane.add(symbol_textArea);
		
		JLabel inputSymbol_lbl = new JLabel("Symbols Entered");
		inputSymbol_lbl.setFont(new Font("Times New Roman", Font.BOLD, 15));
		inputSymbol_lbl.setBounds(36, 506, 116, 23);
		contentPane.add(inputSymbol_lbl);
		
		
		changeCarControl(currentState.getStateName());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            }
        });
        
        this.setVisible(true);
        
	}
	
	private void registerListeners() {
		startBtn.addActionListener(this);
		holdBrakeBtn.addActionListener(this);
		parkBtn.addActionListener(this);
		accelerateBtn.addActionListener(this);
		reverseBtn.addActionListener(this);
		brakePressedBtn.addActionListener(this);
		driveBtn.addActionListener(this);
		engageSeatBeltBtn.addActionListener(this);
		cruiseControlBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(startBtn))
			askChangeState(Alphabet.START);
		
		else if(e.getSource().equals(holdBrakeBtn))
			askChangeState(Alphabet.HOLD_BRAKE);
		
		else if(e.getSource().equals(engageSeatBeltBtn))
			askChangeState(Alphabet.ENGAGE_SEATBELT);
		
		else if(e.getSource().equals(parkBtn))
			askChangeState(Alphabet.PARK);
		
		else if(e.getSource().equals(accelerateBtn))
			askChangeState(Alphabet.ACCELERATE);
		
		else if(e.getSource().equals(reverseBtn))
			askChangeState(Alphabet.REVERSE);
		
		else if(e.getSource().equals(brakePressedBtn))
			askChangeState(Alphabet.PRESS_BRAKE);
		
		else if(e.getSource().equals(driveBtn))
			askChangeState(Alphabet.DRIVE);
		
		else if(e.getSource().equals(cruiseControlBtn))
			askChangeState(Alphabet.CRUISE_CONTROL);		
		
	}
	
	private void askChangeState(Alphabet symbol){
		boolean noTransition = false;
		String prevState = currentState.getStateName();
		
		
		//Check if symbol entered moves transitions 
		if(currentState.state.transition(symbol) == null)
			noTransition = true;
		else
			currentState.state = currentState.state.transition(symbol);
		
		if(!noTransition) {
			if(currentState.state == States.Q0)
				currentState.setStateName("VEHICLE OFF");
			else if(currentState.state == States.Q1)
				currentState.setStateName("HOLD");
			else if(currentState.state == States.Q2)
				currentState.setStateName("IGNITION ON");
			else if(currentState.state == States.Q3)
				currentState.setStateName("ENGINE START");
			else if(currentState.state == States.Q4) {
				currentState.setStateName("STATIONARY");
				setSpeed(Operation.ZERO);
			}
			else if(currentState.state == States.Q5)
				currentState.setStateName("IN FORWARD");
			else if(currentState.state == States.Q6)
				currentState.setStateName("IN REVERSE");
			else if(currentState.state == States.Q7)
				currentState.setStateName("CRUISE CONTROL ENGAGED");

			
			/**
			 * If the vehicle is in the stationary position and it receives the drive-selected signal, 
			 * it moves into the in-forward-motion position and the speed is set to 1.
			 * 
			 * If the vehicle is in the stationary position and it receives the reverse-selected signal, 
			 * it moves into the in-reverse-motion position and the speed is set to 1.
			 * 
			 * In the in-forward-motion or the in-reverse-motion positions, 
			 * if the accelerate signal is received the speed is incremented,
			 * and if the brake-pressed signal is received the speed is decremented.
			 */
			
			if(currentState.getStateName().equals("IN FORWARD") || 
					currentState.getStateName().equals("IN REVERSE")) {
				
				if(prevState.equals("STATIONARY"))
					setSpeed(Operation.INCREMENT);
				
				if(symbol.equals(Alphabet.ACCELERATE)) 
					setSpeed(Operation.INCREMENT);
				
				else if (prevState != "CRUISE CONTROL ENGAGED" &&
						symbol.equals(Alphabet.PRESS_BRAKE)) {
					if(speed > 0)
						setSpeed(Operation.DECREMENT);
					
					/**
					 * If the vehicle is in the in-forward-motion or the in-reverse-motion positions 
					 * and the speed is at 0, then the vehicle returns to the stationary position.
					 */
					if(speed == 0) {
						currentState.setState(States.Q4);
						currentState.setStateName("STATIONARY");
					}
				}
			}
			
			//Adds symbol to symbol list
			symbol_textArea.append(String.valueOf(symbol.accept));
			
			changeCarControl(currentState.getStateName());
		}
	}
	
	private void changeCarControl(String carState) {
		
		carState_lbl.setText(carState);
		
		if(currentState.state.accept)
			accept_reject_lbl.setIcon(resizeImage("acceptString.png", 25, 25));
		else
			accept_reject_lbl.setIcon(resizeImage("rejectString.png", 25, 25));
		
	}

	// Resize icon to fit on label 
	private ImageIcon resizeImage(String image, int width, int height) {
		ImageIcon img = new ImageIcon(new ImageIcon(Dashboard.class.getResource("/img/" + image)
				).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
		
		return img;
	}
	
	
	private void setSpeed(Operation opt) {
		if(opt.equals(Operation.INCREMENT))
			++speed;
		else if(opt.equals(Operation.DECREMENT))
			--speed;
		else if(opt.equals(Operation.ZERO))
			speed = 0;
		
		speed_lbl.setText(String.valueOf(speed));
	}
	
}

