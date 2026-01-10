import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

class Payment extends JFrame implements ActionListener 
{
	private JPanel p1,p2;
	private JTextField t1,t2,t3;
	private JComboBox<String>cb;
	private JTable jt;
	private JLabel label;
	private JScrollPane jsp;
	private static Connection con = null;
	private PreparedStatement ps;
	private String sql;
	private String mem_name;
	private ResultSet rs;
	private DefaultTableModel model;

	public Payment() throws Exception
	{
		super("Members Management");

		p1 = new JPanel();
		p1.setLayout(null);

		p1.setBounds(0,0, 200, 700);
		this.add(p1);
		p1.setBackground(Color.RED);
		label=new JLabel(new ImageIcon("C:/Users/HP/OneDrive/Desktop/12gg.png"));
		label.setBounds(35,30,120,100);
		p1.add(label);

		String arr[] = {"MEMBER", "COACHS", "LOGOUT"};

		int x=200;

		for(int i = 0; i < arr.length; i++)
		{
			JButton b=new JButton(arr[i]);     
        	b.setBounds(40,x,100,30);     
        	p1.add(b);
        	b.setBackground(Color.red);
        	b.setFont(new Font("Calabri",Font.BOLD,13));
        	b.setBorder(null);
        	b.addActionListener(this);
			x +=50;  
    	}

    	p2 = new JPanel();
		p2.setLayout(null);

		p2.setBounds(5,220,200,200);
		this.add(p2);
		p2.setBackground(Color.CYAN);

		JLabel lbl = new JLabel("Manage Finance");
		lbl.setBounds(435,30,250,45);
		p2.add(lbl);
		lbl.setFont(new Font("Algerian",Font.BOLD,22));
		lbl.setForeground(Color.RED);


		lbl = new JLabel("Date");
		lbl.setBounds(220,100,80,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Date
		t1 = new JTextField();
		t1.setBounds(220,125,120,30);
		p2.add(t1);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		LocalDateTime now = LocalDateTime.now();    
		String t=(dtf.format(now));
		t1.setText(t);
		t1.setEditable(false);


		lbl = new JLabel("Members");
		lbl.setBounds(220,180,90,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//member JComboBox
		Vector <String> members = new Vector <String>();
		sql = "select mem_name from members";
		connect();
		ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			members.add(rs.getString("mem_name"));
		}
		rs.close();
		ps.close();
		disconnect();

		cb=new JComboBox<String>(members);
		cb.setBounds(220,205,150,30);
		p2.add(cb);
		


		lbl = new JLabel("Amount");
		lbl.setBounds(220,265,80,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Amount
		t2 = new JTextField();
		t2.setBounds(220,290,120,30);
		p2.add(t2);

		lbl = new JLabel("Search Name");
		lbl.setBounds(530,100,80,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));
		//Textfield of payments
		t3=new JTextField();
		t3.setBounds(510,125,120,30);
		p2.add(t3);

		JButton d = new JButton("SEARCH");
		d.setBounds(655,125,100,30);
		p2.add(d);
		d.addActionListener(this);

		JButton e = new JButton("REFRESH");
		e.setBounds(775,125,100,30);
		p2.add(e);
		e.addActionListener(this);

		String arr2[] = {"PAY", "RESET","EDIT","DELETE"};

		int y = 220;

		for(int i = 0; i < arr2.length; i++)
		{
			JButton b = new JButton(arr2[i]);
			b.setBounds(y,350, 100, 30);
			p2.add(b);
			b.addActionListener(this);
			y += 130;
		}




		jt = new JTable();
		jsp=new JScrollPane(jt); 
    	jsp.setBounds(510,180,350,110);   
    	p2.add(jsp);
    	getPaymentTableData();

    	jt.addMouseListener(new MouseAdapter()
    	{
    		@Override
    		public void mouseClicked(MouseEvent e)
    		{
    			model = (DefaultTableModel)jt.getModel();
    			int i=jt.getSelectedRow();
    			cb.setSelectedItem(model.getValueAt(i, 0).toString());
    			t1.setText(model.getValueAt(i,1).toString());
    			t2.setText(model.getValueAt(i,2).toString());
    			

    		}
    	});     


		this.setVisible(true);
		this.setSize(1000,700);
		this.setResizable(false);

	
}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand().trim().toUpperCase();
		try
		{
			switch(cmd)
			{
				case"PAY":
					try
					{
						String date = t1.getText().trim().toUpperCase();
						String name=(String)cb.getSelectedItem();
						int amount= Integer.parseInt(t2.getText());

						sql = "insert into payment values (?,?,?)";
						connect();
						ps = con.prepareStatement(sql);

						ps.setString(1, name);
						ps.setString(2, date);
						ps.setInt(3,amount);
						
						int n = ps.executeUpdate();
						ps.close();
						disconnect();

						if(n == 1)
						{
							JOptionPane.showMessageDialog(this,"Payment Succesfull");
							model = (DefaultTableModel)jt.getModel();
							model.getDataVector().removeAllElements();
							model.fireTableDataChanged();
							getPaymentTableData();
						}
						else
						{
							JOptionPane.showMessageDialog(this,"Opps!! Unable to insert payment..");
						}


					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(this, "please inserted values and select option");
					}
					break;

                    case "EDIT":
							try
							{

								String m=(String)cb.getSelectedItem();
								String amount = t2.getText().trim().toUpperCase();
								
								sql = "update payment set p_amount= ? where m_name=?";
								connect();
								ps = con.prepareStatement(sql);
								ps.setString(1, amount);
								ps.setString(2, m);
								
								int z = ps.executeUpdate();
								ps.close();
								disconnect();
								if(z == 1)
								{	
	
									model = (DefaultTableModel)jt.getModel();
									model.getDataVector().removeAllElements();
									model.fireTableDataChanged();
									getPaymentTableData();
									
									JOptionPane.showMessageDialog(this,"Record Update..");

									
								}
								else
								{
									JOptionPane.showMessageDialog(this,"Record not Update..");
								}
							}
							catch(Exception ex)
							{
								JOptionPane.showMessageDialog(this,ex);
							}	


							break;

			case "DELETE":
				try
						{
							String name=(String)cb.getSelectedItem();

							sql = "delete from payment where m_name = ?";
							connect();
							ps = con.prepareStatement(sql);
							ps.setString(1,name);
							int y = ps.executeUpdate();
							ps.close();
							disconnect();

							if(y == 1)
							{
								model = (DefaultTableModel)jt.getModel();
								model.getDataVector().removeAllElements();
								model.fireTableDataChanged();
								getPaymentTableData();

								JOptionPane.showMessageDialog(this,"Record Deleted..");
							    t2.setText("");
							    t3.setText("");
							   cb.setSelectedItem(null);
							

							}
							else
							{
								JOptionPane.showMessageDialog(this," Unable to Delet record..");
							}

						}
						catch(Exception ex)
						{
							JOptionPane.showMessageDialog(this,"please select row");
						}					

				break;


			case "SEARCH":

            try
            {
                String S = t3.getText();
                String sql = "select * from payment where m_name like '" + S +"%'";
                connect();
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery(sql);
                rs.next();

                
                cb.setSelectedItem(rs.getString(1));
                t1.setText(rs.getString(2));
                t2.setText(rs.getString(3));
           
                
                int rowCount = model.getRowCount();

                for (int i = rowCount - 1; i >= 0; i--) {
                    model.removeRow(i);
                }
                String date = t1.getText();
                String m_name=(String)cb.getSelectedItem();
    			String amount =t2.getText();
    			model.addRow(new Object[]{m_name,date,amount});
										                
        		jt = new JTable(model);
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(this,"Member not found");
            }
            break;



			case "RESET": 
				
					t2.setText("");
					cb.setSelectedItem(null);
					break;

			case "REFRESH":
					model = (DefaultTableModel)jt.getModel();
							model.getDataVector().removeAllElements();
							model.fireTableDataChanged();
							getPaymentTableData();

					break;

			case "MEMBER":
						try
			  			{
			   				Member mba = new Member();
			   				this.dispose();
			  			}
			 			catch(Exception ex)
			  			{
			  				JOptionPane.showMessageDialog(this,ex);
			  			}
			   			break;

			   	    case "COACHS":
			   	    	try
			  			{
			   				Coach mba = new Coach();
			   				this.dispose();
			  			}
			 			catch(Exception ex)
			  			{
			  				JOptionPane.showMessageDialog(this,ex);
			  			}
			   			break;

			   	    case "LOGOUT":
			   	    	int result=JOptionPane.showConfirmDialog(this,"Are you sure to logout?");
	    					if(result==JOptionPane.YES_OPTION)
	    					{
	    						LoginFrame lf = new LoginFrame();
	    						this.dispose();
	    					}
	    					break;
			}
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex);
				
		}


	}
	public void getPaymentTableData()
		{
			try
			{
				//jt=new JTable();
				connect();
				Statement ps=con.createStatement();
				String sql="select * from payment";
				ResultSet rs=ps.executeQuery(sql);
				ResultSetMetaData rsmd=rs.getMetaData();
				//jt=new JTable();
				model=(DefaultTableModel)jt.getModel();

				int cols=rsmd.getColumnCount();
				String[]colName=new String[cols];
				for(int i=0;i<cols;i++)
					colName[i]=rsmd.getColumnName(i+1);

				model.setColumnIdentifiers(colName);
				jt.setModel(model);
				
    			

    			String m_name,date,p_amount;

    			while(rs.next())
    			{
    				m_name=rs.getString(1);
    				date=rs.getString(2);
    				p_amount=rs.getString(3);
    				

    				String[] row={m_name,date,p_amount};
    				model.addRow(row);

    			}
    			rs.close();
				ps.close();
				disconnect();
				jt.setModel(model);
    			JTableHeader th=jt.getTableHeader();
    			th.setBackground(Color.PINK);
    			th.setForeground(Color.BLUE);

			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Invalid input");
				
			}
		}

		public void connect()
		{
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gym", "root", "");
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, ex);
			}
		}	

		public void disconnect()
		{
			try
			{
				if(!con.isClosed())
				con.close();
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, ex);
			}
		}		
}

class Member extends JFrame implements ActionListener
{
	private JPanel p1,p2;
	private JTextField t1,t2,t3,t4,t5;
	private JComboBox<String>cb1,cb2,cb3;
	private JTable jt;
	private JScrollPane jsp;
	private JLabel label;
	private static Connection con = null;
	private PreparedStatement ps;
	private String sql;
	private String mem_name;

	public Member() throws Exception
	{
		super("Members Management");

		p1 = new JPanel();
		p1.setLayout(null);

		p1.setBounds(0,0, 200, 700);
		this.add(p1);
		p1.setBackground(Color.RED);

		label=new JLabel(new ImageIcon("C:/Users/HP/OneDrive/Desktop/12gg.png"));
		label.setBounds(35,30,120,100);
		p1.add(label);


		String arr[] = {"COACHS", "PAYMENT", "LOGOUT"};

		int x=200;

		for(int i = 0; i < arr.length; i++)
		{
			JButton b=new JButton(arr[i]);     
        	b.setBounds(40,x,100,30);    
        	p1.add(b);
        	b.setBackground(Color.red); 
        	b.setFont(new Font("Calabri",Font.BOLD,13));
        	b.setBorder(null);
        	b.addActionListener(this);
			x +=50;  
    	}

		p2 = new JPanel();
		p2.setLayout(null);

		p2.setBounds(5,220,200,200);
		this.add(p2);
		p2.setBackground(Color.CYAN);

		JLabel lbl = new JLabel("Manage Members");
		lbl.setBounds(435,30,250,45);
		p2.add(lbl);
		lbl.setFont(new Font("Algerian",Font.BOLD,22));
		lbl.setForeground(Color.RED);

		lbl = new JLabel("Members Name");
		lbl.setBounds(210,100,110,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Members Name
		t1 = new JTextField();
		t1.setBounds(210,125,150,30);
		p2.add(t1);


		lbl = new JLabel("Phone Number");
		lbl.setBounds(400,100,110,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of phone number
		t2 = new JTextField();
		t2.setBounds(400,125,150,30);
		p2.add(t2);

		lbl = new JLabel("Age");
		lbl.setBounds(600,100,90,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Age
		t3 = new JTextField();
		t3.setBounds(600,125,60,30);
		p2.add(t3);


		lbl = new JLabel("Amount");
		lbl.setBounds(210,180,90,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Amount
		t4 = new JTextField();
		t4.setBounds(210,205,80,30);
		p2.add(t4);

		lbl = new JLabel("Timing");
		lbl.setBounds(320,180,90,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Timing JComboBox
		String arr1[]={"","6AM to 8AM","8AM to 10AM","4PM to 6PM","6PM to 8PM","8PM to 10PM"};
		cb1=new JComboBox<String>(arr1);
		cb1.setBounds(320,205,100,30);
		p2.add(cb1);
		cb1.addActionListener(this);

		lbl = new JLabel("Coachs");
		lbl.setBounds(460,180,90,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Coach JComboBox
		Vector <String> coaches = new Vector <String>();
		sql = "select c_name from coaches";
		connect();
		ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			coaches.add(rs.getString("c_name"));
		}
		rs.close();
		ps.close();
		disconnect();

		cb2=new JComboBox<String>(coaches);
		cb2.setBounds(440,205,120,30);
		p2.add(cb2);
		cb2.addActionListener(this);


		lbl = new JLabel("Gender");
		lbl.setBounds(600,180,90,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Coach JComboBox
		String arr4[]={"","Male","Female"};
		cb3=new JComboBox<String>(arr4);
		cb3.setBounds(600,205,70,30);
		p2.add(cb3);
		cb3.addActionListener(this);
  

		String arr3[] = {"ADD", "EDIT", "DELETE"};

		int m = 265;

		for(int i = 0; i < arr.length; i++)
		{
			JButton c = new JButton(arr3[i]);
			c.setBounds(m, 300, 100, 30);
			p2.add(c);
			c.addActionListener(this);
			m += 115;
		}

		JButton d = new JButton("CLEAR");
		d.setBounds(610,300,100,30);
		p2.add(d);
		d.addActionListener(this);

		jt = new JTable();
		jsp=new JScrollPane(jt); 
    	jsp.setBounds(250,400,670,150);   
    	p2.add(jsp);

		getTableData();

		jt.addMouseListener(new MouseAdapter()
    	{
    		@Override
    		public void mouseClicked(MouseEvent e)
    		{
    			DefaultTableModel model = (DefaultTableModel)jt.getModel();
    			int i=jt.getSelectedRow();
    			t1.setText(model.getValueAt(i,0).toString());
    			mem_name = t1.getText();
    			t2.setText(model.getValueAt(i,1).toString());
    			t3.setText(model.getValueAt(i,2).toString());
    			t4.setText(model.getValueAt(i,3).toString());
    			cb1.setSelectedItem(model.getValueAt(i, 4).toString());
    			cb2.setSelectedItem(model.getValueAt(i, 5).toString());
    			cb3.setSelectedItem(model.getValueAt(i, 6).toString());
    		}
    	});     

		this.setVisible(true);
		this.setSize(1000,700);
		this.setResizable(false);

	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
			String cmd = e.getActionCommand().trim().toUpperCase();
			switch(cmd)
			{
				case "ADD": 

					try
					{

						String member_name = t1.getText().trim().toUpperCase();
						String phpno = t2.getText();
						int age= Integer.parseInt(t3.getText());
						int amount= Integer.parseInt(t4.getText());
						String timing=(String)cb1.getSelectedItem();
						String coach=(String)cb2.getSelectedItem();
						String gender=(String)cb3.getSelectedItem();


						sql = "insert into members values (?,?,?,?,?,?,?)";
						connect();
						ps = con.prepareStatement(sql);
						ps.setString(1, member_name);
						ps.setString(2, phpno);
						ps.setInt(3, age);
						ps.setInt(4,amount);
						ps.setString(5,timing);
						ps.setString(6,coach);
						ps.setString(7,gender);

						int n = ps.executeUpdate();
						ps.close();
						disconnect();
                              
						if(n == 1)
						{
							DefaultTableModel model = (DefaultTableModel)jt.getModel();
							model.getDataVector().removeAllElements();
							model.fireTableDataChanged();
							getTableData();

							JOptionPane.showMessageDialog(this,"Member inserted..");
						    t1.setText("");
								    	t2.setText("");
							     		t3.setText("");
										t4.setText("");
										cb1.setSelectedItem(null);
										cb2.setSelectedItem(null);
										cb3.setSelectedItem(null);

							
						}
						else
						{
							System.out.println("Opps!! Unable to insert record..");
						}

						

					}
					catch(Exception ex)
					{

						JOptionPane.showMessageDialog(this, "Invalid input");
					}

					break;
				
						case "DELETE":
	
								try
							    {
									String m_name = t1.getText().trim().toUpperCase();

									sql = "delete from members where mem_name = ?";
									connect();
									ps = con.prepareStatement(sql);
									ps.setString(1,m_name);
									int y = ps.executeUpdate();
									ps.close();
									disconnect();

									if(y == 1)
									{
										DefaultTableModel model = (DefaultTableModel)jt.getModel();
										model.getDataVector().removeAllElements();
										model.fireTableDataChanged();
										getTableData();

										JOptionPane.showMessageDialog(this,"Record Deleted..");

										t1.setText("");
								    	t2.setText("");
							     		t3.setText("");
										t4.setText("");
										cb1.setSelectedItem(null);
										cb2.setSelectedItem(null);
										cb3.setSelectedItem(null);

									}
									else
									{
										JOptionPane.showMessageDialog(this,"Opps!! Unable to Delet record..");
									}

								}
								catch(Exception ex)
								{
									JOptionPane.showMessageDialog(this, "please select members");
								}							
						
							break;

						case "EDIT":
							int res=JOptionPane.showConfirmDialog(this,"Are you sure to update the record?");
	    					if(res==JOptionPane.YES_OPTION)
	    					{
	    						try
								{
									String name = t1.getText().trim().toUpperCase();
									String phpno = t2.getText();
									int age= Integer.parseInt(t3.getText());
									int amount= Integer.parseInt(t4.getText());
									String timing=(String)cb1.getSelectedItem();
									String coach=(String)cb2.getSelectedItem();
									String gender=(String)cb3.getSelectedItem();
								
									sql = "update members set mem_name = ?,m_phone_no=?,m_age=?,m_amount=?,m_timing=?,m_coach=?,m_gender=? where mem_name=?";
									connect();
									ps = con.prepareStatement(sql);
									ps.setString(1, name);
									ps.setString(2, phpno);
									ps.setInt(3, age);
									ps.setInt(4,amount);
									ps.setString(5,timing);
									ps.setString(6,coach);
									ps.setString(7,gender);
									ps.setString(8, mem_name);
									int z = ps.executeUpdate();
									ps.close();
									disconnect();
									if(z == 1)
									{	
	
										DefaultTableModel model = (DefaultTableModel)jt.getModel();
										model.getDataVector().removeAllElements();
										model.fireTableDataChanged();
										getTableData();
									
										JOptionPane.showMessageDialog(this,"Record Update..");

									
									}
									else
									{
										JOptionPane.showMessageDialog(this,"Record not Update..");
									}
								}
								catch(Exception ex)
								{
									JOptionPane.showMessageDialog(this,ex);
								}	
	    					}


						case "CLEAR": 
					
								t1.setText("");
								t2.setText("");
								t3.setText("");
								t4.setText("");
								cb1.setSelectedItem(null);
								cb2.setSelectedItem(null);
								cb3.setSelectedItem(null);
							break;

						case "COACHS":
				              Coach ca = new Coach();
				              this.dispose();
				              break;

				        case "PAYMENT":
				        	try
				        	{
				        	 Payment py = new Payment();
				        	 this.dispose();
				        	}
				        	catch(Exception ex)
				        	{
				        		JOptionPane.showMessageDialog(this,ex);
				        	}
				        	break;

				        case "LOGOUT":
				        	int result=JOptionPane.showConfirmDialog(this,"Are you sure to logout?");
	    					if(result==JOptionPane.YES_OPTION)
	    					{
	    						LoginFrame lf = new LoginFrame();
	    						this.dispose();
	    					}	
	    					
	    		break;


				}


		}
		

		public void getTableData()
		{
			try
			{
				//jt=new JTable();
				connect();
				Statement ps=con.createStatement();
				String sql="select * from members";
				ResultSet rs=ps.executeQuery(sql);
				ResultSetMetaData rsmd=rs.getMetaData();
				//jt=new JTable();
				DefaultTableModel model=(DefaultTableModel)jt.getModel();

				int cols=rsmd.getColumnCount();
				String[]colName=new String[cols];
				for(int i=0;i<cols;i++)
					colName[i]=rsmd.getColumnName(i+1);

				model.setColumnIdentifiers(colName);
				jt.setModel(model);
				
    			

    			String m_name,m_phone_no,m_age,m_amount,m_timing,m_coachs,m_coach,m_gender;

    			while(rs.next())
    			{
    				m_name=rs.getString(1);
    				m_phone_no=rs.getString(2);
    				m_age=rs.getString(3);
    				m_amount=rs.getString(4);
    				m_timing=rs.getString(5);
    				m_coachs=rs.getString(6);
    				m_gender=rs.getString(7);

    				String[] row={m_name,m_phone_no,m_age,m_amount,m_timing,m_coachs,m_gender};
    				model.addRow(row);

    			}
    			rs.close();
				ps.close();
				disconnect();
				jt.setModel(model);
    			JTableHeader th=jt.getTableHeader();
    			th.setBackground(Color.PINK);
    			th.setForeground(Color.BLUE);

			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Invalid input");
				
			}
		}
		public void connect()
		{
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gym", "root", "");
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, "please start mysql");
			}
		}	

		public void disconnect()
		{
			try
			{
				if(!con.isClosed())
				con.close();
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, ex);
			}
		}	
}

class Coach extends JFrame implements ActionListener
{
	private JPanel p1,p2;
	private JTextField t1,t2,t3,t4;
	private ButtonGroup bg1;
	private JRadioButton rb1,rb2;
	private JComboBox<String>cb3;
	private JTable jt;
	private JScrollPane jsp;
	private JLabel label;

	private static Connection con = null;
	private PreparedStatement ps;
	private String sql;
	private String c_name;

	public Coach()
	{
		super("Coach Management");

		p1 = new JPanel();
		p1.setLayout(null);

		p1.setBounds(0,0, 200, 700);
		this.add(p1);
		p1.setBackground(Color.RED);

		label=new JLabel(new ImageIcon("C:/Users/HP/OneDrive/Desktop/12gg.png"));
		label.setBounds(35,30,120,100);
		p1.add(label);



		String arr[] = {"MEMBERS", "PAYMENT", "LOGOUT"};

		int x=200;

		for(int i = 0; i < arr.length; i++)
		{
			JButton b=new JButton(arr[i]);     
        	b.setBounds(40,x,100,30);    
        	p1.add(b);
        	b.setBackground(Color.red);
        	b.setFont(new Font("Calabri",Font.BOLD,13)); 
        	b.setBorder(null);
        	b.addActionListener(this);
			x +=50;  
    	}

		p2 = new JPanel();
		p2.setLayout(null);

		p2.setBounds(5,220,200,200);
		this.add(p2);
		p2.setBackground(Color.CYAN);

		JLabel lbl = new JLabel("Manage Trainers");
		lbl.setBounds(435,30,250,45);
		p2.add(lbl);
		lbl.setFont(new Font("Algerian",Font.BOLD,22));
		lbl.setForeground(Color.RED);

		lbl = new JLabel("Coache Name");
		lbl.setBounds(250,100,110,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Coache Name
		t1 = new JTextField();
		t1.setBounds(250,125,150,30);
		p2.add(t1);

		lbl = new JLabel("Phone Number");
		lbl.setBounds(450,100,110,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Phone Number
		t2= new JTextField();
		t2.setBounds(450,125,150,30);
		p2.add(t2);

		lbl = new JLabel("Age");
		lbl.setBounds(700,100,110,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of age
		t3 = new JTextField();
		t3.setBounds(650,125,80,30);
		p2.add(t3);

		
		lbl = new JLabel("Adress");
		lbl.setBounds(300,190,110,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		//Textfield of Adress
		t4 = new JTextField();
		t4.setBounds(300,215,150,30);
		p2.add(t4);

		lbl = new JLabel("Gender");
		lbl.setBounds(500,190,110,30);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,18));

		String arr1[]={"Male","Female"};
		cb3=new JComboBox<String>(arr1);
		cb3.setBounds(500,215,100,30);
		p2.add(cb3);

		String arr2[] = {"ADD", "EDIT", "DELETE"};

		int y = 280;

		for(int i = 0; i < arr.length; i++)
		{
			JButton b = new JButton(arr2[i]);
			b.setBounds(y,290, 100, 30);
			p2.add(b);
			b.addActionListener(this);
			y += 130;
		}
		JButton d = new JButton("CLEAR");
		d.setBounds(670,290,100,30);
		p2.add(d);
		d.addActionListener(this);

		jt = new JTable();
		jsp=new JScrollPane(jt); 
    	jsp.setBounds(250,400,670,150);   
    	p2.add(jsp);
    	getTableData();

    	jt.addMouseListener(new MouseAdapter()
    	{
    		@Override
    		public void mouseClicked(MouseEvent e)
    		{
    			DefaultTableModel model = (DefaultTableModel)jt.getModel();
    			int i=jt.getSelectedRow();
    			t1.setText(model.getValueAt(i,0).toString());
    			c_name = t1.getText();
    			t2.setText(model.getValueAt(i,1).toString());
    			t3.setText(model.getValueAt(i,2).toString());
    			t4.setText(model.getValueAt(i,3).toString());
    			cb3.setSelectedItem(model.getValueAt(i, 4).toString());
    			
    		}
    	});



		this.setVisible(true);
		this.setSize(1000,700);
		this.setResizable(false);

	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand().trim().toUpperCase();

		switch(cmd)
		{
			case "ADD": 

				try
				{
					String coach_name = t1.getText().trim().toUpperCase();
					int age= Integer.parseInt(t3.getText());
					String phpno = t2.getText();
					String adress = t4.getText().trim().toUpperCase();
					String gender=(String)cb3.getSelectedItem();
						

					sql = "insert into coaches values (?,?,?,?,?)";
					connect();
					ps = con.prepareStatement(sql);
					ps.setString(1, coach_name);
					ps.setString(2, phpno);
					ps.setInt(3,age);
					ps.setString(4,adress);
					ps.setString(5,gender);

					int n = ps.executeUpdate();
					ps.close();
					disconnect();

					if(n == 1)
					{
						DefaultTableModel model = (DefaultTableModel)jt.getModel();
						model.getDataVector().removeAllElements();
						model.fireTableDataChanged();
						getTableData();
						JOptionPane.showMessageDialog(this,"New Coache is inserted..");
					}
					else
					{
						JOptionPane.showMessageDialog(this,"Unable to insert record..");
					}
				}

				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(this, "invalid input");
				}
					
					
				break;

				case "DELETE":
	
						try
						{
							String c_name = t1.getText().trim().toUpperCase();

							sql = "delete from coaches where c_name = ?";
							connect();
							ps = con.prepareStatement(sql);
							ps.setString(1,c_name);
							int y = ps.executeUpdate();
							ps.close();
							disconnect();

							if(y == 1)
							{
								DefaultTableModel model = (DefaultTableModel)jt.getModel();
								model.getDataVector().removeAllElements();
								model.fireTableDataChanged();
								getTableData();

								JOptionPane.showMessageDialog(this,"Record Deleted..");
								t1.setText("");
							    t2.setText("");
							    t3.setText("");
							    t4.setText("");
							   cb3.setSelectedItem(null);
							

							}
							else
							{
								JOptionPane.showMessageDialog(this," Unable to Delet record..");
							}

						}
						catch(Exception ex)
						{
							JOptionPane.showMessageDialog(this,ex);
						}							
						
						break;

						case "CLEAR":
							t1.setText("");
							t2.setText("");
							t3.setText("");
							t4.setText("");
							cb3.setSelectedItem(null);
							
							break;


						case "EDIT":
							int res=JOptionPane.showConfirmDialog(this,"Are you sure to update the record?");
	    					if(res==JOptionPane.YES_OPTION)
	    					{
	    						try
								{
        							String name = t1.getText().trim().toUpperCase();
        							int age= Integer.parseInt(t3.getText());
									String phpno = t2.getText();
									String adress = t4.getText().trim().toUpperCase();
									String gender=(String)cb3.getSelectedItem();

									sql = "update coaches set c_name= ?,c_phoneno= ?,c_age= ?,c_adress= ?,c_gender= ? where c_name= ?";
									connect();
									ps = con.prepareStatement(sql);
									ps.setString(1, name);
									ps.setString(2, phpno);
									ps.setInt(3,age);
									ps.setString(4,adress);
									ps.setString(5,gender);
									ps.setString(6,c_name);
								
									int z = ps.executeUpdate();
									ps.close();
									disconnect();
									if(z == 1)
									{	
										DefaultTableModel model = (DefaultTableModel)jt.getModel();
										model.getDataVector().removeAllElements();
										model.fireTableDataChanged();
										getTableData();
										
										JOptionPane.showMessageDialog(this,"Record Update..");
	
									
									}	
									else
									{
										JOptionPane.showMessageDialog(this,"Record not Update..");
									}
								}
								catch(Exception ex)
								{
									JOptionPane.showMessageDialog(this,"please selecte coach");
								}
	    					}
							break;

					
				case "MEMBERS":
			  		try
			  		{
			   			Member mba = new Member();
			   			this.dispose();
			  		}
			 		catch(Exception ex)
			  		{
			  			JOptionPane.showMessageDialog(this,ex);
			  		}
			   		break;

			case "PAYMENT":
				        	try
				        	{
				        	 Payment py = new Payment();
				        	 this.dispose();
				        	}
				        	catch(Exception ex)
				        	{
				        		JOptionPane.showMessageDialog(this,ex);
				        	}
				        	break;

	    	case "LOGOUT":
	    		int result=JOptionPane.showConfirmDialog(this,"Are you sure to logout?");
	    		if(result==JOptionPane.YES_OPTION)
	    		{
	    			LoginFrame lf = new LoginFrame();
	    			this.dispose();
	    		}	
	    		else
	    		{

	    		}
	    		break;
			}

		}

		public void getTableData()
		{
			try
			{
				//jt=new JTable();
				connect();
				Statement ps=con.createStatement();
				String sql="select * from coaches";
				ResultSet rs=ps.executeQuery(sql);
				ResultSetMetaData rsmd=rs.getMetaData();
				//jt=new JTable();
				DefaultTableModel model=(DefaultTableModel)jt.getModel();

				int cols=rsmd.getColumnCount();
				String[]colName=new String[cols];
				for(int i=0;i<cols;i++)
					colName[i]=rsmd.getColumnName(i+1);

				model.setColumnIdentifiers(colName);
				jt.setModel(model);
				
    			

    			String c_name,c_phoneno,c_age,c_adress,c_gender;

    			while(rs.next())
    			{
    				c_name=rs.getString(1);
    				c_phoneno=rs.getString(2);
    				c_age=rs.getString(3);
    				c_adress=rs.getString(4);
    				c_gender=rs.getString(5);

    				String[] row={c_name,c_phoneno,c_age,c_adress,c_gender};
    				model.addRow(row);

    			}
    			rs.close();
				ps.close();
				disconnect();
				jt.setModel(model);
    			JTableHeader th=jt.getTableHeader();
    			th.setBackground(Color.PINK);
    			th.setForeground(Color.BLUE);

			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, "Invalid input");
				
			}
		}

		public void connect()
		{
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gym", "root", "");
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, ex);
			}
		}	

		public void disconnect()
		{
			try
			{
				if(!con.isClosed())
				con.close();
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, ex);
			}
		}	

}



class LoginFrame extends JFrame implements ActionListener 
{
	private JPanel p1,p2,p3;
	private JTextField t1;
	private JLabel label;
	private JPasswordField pass;

	public LoginFrame()
	{
	

		p1 = new JPanel();
		p1.setLayout(null);

		p1.setBounds(0,0, 200, 700);
		this.add(p1);
		p1.setBackground(Color.RED);

		JLabel lbl = new JLabel("Fitness Corner");
		lbl.setBounds(25,290,180,50);
		p1.add(lbl);

		lbl.setFont(new Font("Gabriola",Font.BOLD,30));
		lbl.setForeground(Color.WHITE);

		

		p2 = new JPanel();
		p2.setLayout(null);

		p2.setBounds(200,0,200,700);
		this.add(p2);
		p2.setBackground(Color.WHITE);

		lbl = new JLabel("Fitness Corner");
		lbl.setBounds(420,30,150,45);
		p2.add(lbl);
		lbl.setFont(new Font("Gabriola",Font.BOLD,25));
		lbl.setForeground(Color.RED);



		label=new JLabel(new ImageIcon("C:/Users/HP/OneDrive/Desktop/Gym1.jpg"));
		label.setBounds(330,90,310,300);
		p2.add(label);


		lbl = new JLabel("Username");
		lbl.setBounds(300,410,110,50);
		lbl.setFont(new Font("Gabriola",Font.BOLD,25));
		p2.add(lbl);

		
		//Textfield of Username
		t1 = new JTextField();
		t1.setBounds(400,410,150,30);
		p2.add(t1);

		
		lbl = new JLabel("Password");
		lbl.setBounds(300,470,110,50);
		lbl.setFont(new Font("Gabriola",Font.BOLD,25));
		p2.add(lbl);
		
		pass=new JPasswordField();
		pass.setBounds(400,470,150,30);
		p2.add(pass);
		pass.setEchoChar('*');
		
		JButton b = new JButton("Login");
		b.setBounds(400,540, 100, 30);
		p2.add(b);
		b.addActionListener(this);



		this.setVisible(true);
		this.setSize(800,700);
		this.setResizable(false);

	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String userid=t1.getText();
		char pass1[]=pass.getPassword();
		String password=new String(pass1);

		if(userid.equals("1") && password.equals("1"))
		{
			try
			{
			JOptionPane.showMessageDialog(this,"Login Succesfull...");
			Member mb = new Member();
			this.dispose();
		    }
		    catch(Exception ex)
		    {
		    	JOptionPane.showMessageDialog(this,"Please start mysql");
		    }
		}
		else
		{
			JOptionPane.showMessageDialog(null,"incorrect userid and password");
		}
	}
	

}
class Gym
{
	public static void main(String args[]) 
	{
		LoginFrame f = new LoginFrame();
	}
}