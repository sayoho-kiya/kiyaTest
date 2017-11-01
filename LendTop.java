package screen;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import action.DB;
import validate.Validate;

public class LendTop implements ActionListener, MouseListener {

	JFrame frame, lendframe, borrowframe, rtnframe, extenframe;
	JPanel top, borrow, rtn, exten;
	JLabel labelT1, labelT2, labelT3, labelT4, labelT5;
	JLabel labelB1, labelB2, labelB3, labelB4, labelB5, labelB7, labelB8;
	JLabel labelR1, labelR2, labelR3, labelR4, labelR5, labelR6, labelR7, labelR8;//R7は受け渡し用
	JLabel labelE1, labelE2, labelE3, labelE4, labelE5, labelE6;//E6は受け渡し用
	JSpinner spinner1, spinner2, spinner3, spinner4;
	JButton btntop, btnrch, btnrst, btnbr, btnrtn, hbtn, btnext, btntop1, btnLcl, btnRr, btnRcl, btnEr, btnEcl, btn1, btnc1, btnc2, btnc3;
	ButtonGroup group, groupe;
	JTextField tx1, tx11;
	SpinnerDateModel model1, model2, model3, model4;
	JComboBox serchcmb;
	JRadioButton eover2, eover3, eover4, all2, lendOK, borrowing, reserving, all;
	JLabel star1, star2, star3, star4, star5;

	String totalPrice;
	JLabel labelbar, label0, label1, label2, label3, label4, label5, label6, label7, label8, label9, labelmesg, labelmesg2;
	JLabel label0r, label3r, label4r, label5r, label6r;
	JLabel label2a, label3a, label4a, label5a;
	String cancelnum1 = "";
	String cancelnum2 = "";
	String cancelnum3 = "";

	JTable tbl;
	Container con;
	DefaultTableModel model;
	//mouseclisck用(返却時の評価）
	int cnt1 = 0;
	int cnt2 = 0;
	int cnt3 = 0;
	int cnt4 = 0;
	int cnt5 = 0;
	int eval1 = 1;
	int eval2 = 1;
	int eval3 = 1;
	int eval4 = 0;
	int eval5 = 0;
	int stars = 0; //評価の条件用
	int st = 5;	//在庫の条件用
	int st2 = 5;	//在庫の条件用

	//本日の日付取得
	Date today = new Date(); //java.util.Date型
	SimpleDateFormat DataFmt = new SimpleDateFormat("yyyy-MM-dd");
	String Today = DataFmt.format(today);

	//貸出管理TOP画面
	public void lendTop() throws SQLException {
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Meiryo UI", Font.PLAIN, 20)));
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Meiryo UI", Font.PLAIN, 16)));

		lendframe = new JFrame("貸出管理");
		lendframe.setBounds(0, 0, 1650, 1000);
		lendframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lendframe.setVisible(true);

		top = new JPanel();
		top.setLayout(null);
		top.setBounds(10, 10, 1600, 880);
		top.setVisible(true);

		con = lendframe.getContentPane();
		con.add(top);

		//テーブルの作成
		String[] header = { "PID", "タイトル", "出版社", "著者", "ジャンル", "評価", "貸出状況", "エリア" };
		DB.connect();
		List<String[]> bookinfo = DB.makelendtopTable();
		DB.close();
		model = new DefaultTableModel(bookinfo.toArray(new String[0][0]), header);

		tbl = new JTable(model);
		tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//複数行選択できないように設定
		tbl.setBounds(10, 300, 1600, 600);
		tbl.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		tbl.setRowHeight(28);
		//ヘッダーの高さ
		JTableHeader jh = tbl.getTableHeader();
		jh.setFont(new Font("Meiryo UI", Font.PLAIN, 18));

		//列の幅を調整する
		tbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbl.getColumnModel().getColumn(0).setPreferredWidth(100);
		tbl.getColumnModel().getColumn(1).setPreferredWidth(400);
		tbl.getColumnModel().getColumn(2).setPreferredWidth(210);
		tbl.getColumnModel().getColumn(3).setPreferredWidth(210);
		tbl.getColumnModel().getColumn(4).setPreferredWidth(210);
		tbl.getColumnModel().getColumn(5).setPreferredWidth(190);
		tbl.getColumnModel().getColumn(6).setPreferredWidth(160);
		tbl.getColumnModel().getColumn(7).setPreferredWidth(85);

		//中央揃え(貸出状況・エリア）
		DefaultTableCellRenderer c_renderer = new DefaultTableCellRenderer();
		c_renderer.setHorizontalAlignment(SwingConstants.CENTER);
		tbl.getColumnModel().getColumn(6).setCellRenderer(c_renderer);
		tbl.getColumnModel().getColumn(7).setCellRenderer(c_renderer);

		JScrollPane sp;
		sp = new JScrollPane(tbl);
		sp.setBounds(10, 200, 1600, 600);
		top.add(sp);

		tbl.addMouseListener(this);

		//検索部分
		String[] serchList = { "タイトル", "出版社", "著者", "ジャンル" };
		serchcmb = new JComboBox(serchList);
		serchcmb.setBounds(700, 50, 150, 40);
		serchcmb.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		lendOK = new JRadioButton("在庫中");
		borrowing = new JRadioButton("貸出中");
		reserving = new JRadioButton("予約中");
		all = new JRadioButton("すべて", true);

		group = new ButtonGroup();
		group.add(lendOK);
		group.add(borrowing);
		group.add(reserving);
		group.add(all);

		lendOK.setBounds(770, 130, 100, 40);
		borrowing.setBounds(880, 130, 100, 40);
		reserving.setBounds(990, 130, 100, 40);
		all.setBounds(1100, 130, 100, 40);

		lendOK.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		borrowing.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		reserving.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		all.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		lendOK.addActionListener(this);
		borrowing.addActionListener(this);
		reserving.addActionListener(this);
		all.addActionListener(this);

		top.add(lendOK);
		top.add(borrowing);
		top.add(reserving);
		top.add(all);

		eover2 = new JRadioButton("★★2以上");
		eover3 = new JRadioButton("★★★3以上");
		eover4 = new JRadioButton("★★★★4以上");
		all2 = new JRadioButton("すべて", true);

		groupe = new ButtonGroup();
		groupe.add(eover2);
		groupe.add(eover3);
		groupe.add(eover4);
		groupe.add(all2);

		eover2.setBounds(770, 90, 110, 40);
		eover3.setBounds(890, 90, 130, 40);
		eover4.setBounds(1030, 90, 160, 40);
		all2.setBounds(1190, 90, 110, 40);

		eover2.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		eover3.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		eover4.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		eover4.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		all2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		eover2.addActionListener(this);
		eover3.addActionListener(this);
		eover4.addActionListener(this);
		all2.addActionListener(this);

		top.add(eover2);
		top.add(eover3);
		top.add(eover4);
		top.add(all2);

		//検索ワード入力用
		tx1 = new JTextField();
		tx1.setBounds(850, 50, 300, 40);
		tx1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		labelT1 = new JLabel(" 評価：");
		labelT2 = new JLabel(" 在庫：");
		labelT3 = new JLabel("※対象の書籍を選択して、該当のボタンを押してください。");
		labelT4 = new JLabel("※対象の書籍を右クリックすると、貸出/予約情報が表示されます。");
		labelT5 = new JLabel("※プルダウンから項目を選択して、キーワードを入力してください。");

		labelT1.setBounds(700, 90, 150, 40);
		labelT1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		labelT2.setBounds(700, 130, 150, 40);
		labelT2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		labelT3.setBounds(10, 135, 600, 40);
		labelT3.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		labelT4.setBounds(10, 160, 600, 40);
		labelT4.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		labelT5.setBounds(700, 15, 600, 40);
		labelT5.setFont(new Font("Meiryo UI", Font.PLAIN, 17));

		top.add(serchcmb);
		top.add(tx1);
		top.add(labelT1);
		top.add(labelT2);
		top.add(labelT3);
		top.add(labelT4);
		top.add(labelT5);

		btntop = new JButton("TOP");
		btnrch = new JButton("実行");
		btnrst = new JButton("リセット");
		btnbr = new JButton("貸出/予約");
		btnrtn = new JButton("返却");
		btnext = new JButton("延長");

		hbtn = new JButton("貸出履歴");
		hbtn.setBounds(150, 20, 110, 40);
		hbtn.setFont(new Font("Meiryo UI", Font.PLAIN, 17));
		hbtn.addActionListener(this);

		btntop.setBounds(20, 20, 100, 40);
		btnrch.setBounds(1300, 70, 100, 40);
		btnrst.setBounds(1300, 120, 100, 40);
		btnbr.setBounds(650, 850, 120, 40);
		btnrtn.setBounds(800, 850, 120, 40);
		btnext.setBounds(950, 850, 120, 40);

		btntop.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btnrch.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btnrst.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btnbr.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		btnrtn.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btnext.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		top.add(btntop);
		top.add(btnrch);
		top.add(btnrst);
		top.add(btnbr);
		top.add(btnrtn);
		top.add(btnext);
		top.add(hbtn);

		btntop.addActionListener(this);
		btnrch.addActionListener(this);
		btnrst.addActionListener(this);
		btnbr.addActionListener(this);
		btnrtn.addActionListener(this);
		btnext.addActionListener(this);

		labelT2.addMouseListener(this);
		labelT3.addMouseListener(this);

	}

	//貸出/予約登録
	public void Borrow() {

		borrowframe = new JFrame("貸出登録");
		borrowframe.setBounds(50, 50, 700, 380);
		borrowframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		borrowframe.setVisible(true);

		borrow = new JPanel();
		borrow.setLayout(null);
		borrowframe.add(borrow);
		borrow.setVisible(true);

		labelB1 = new JLabel("■タイトル：");
		labelB2 = new JLabel("■貸出日：");
		labelB3 = new JLabel("■借用者名：");
		labelB4 = new JLabel("■返却予定日：");
		labelB5 = new JLabel();//タイトル表示用
		labelB7 = new JLabel("※貸出日より後の日付で入力してください");
		labelB8 = new JLabel("");

		labelB1.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelB2.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelB3.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelB4.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelB5.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelB7.setFont(new Font("Meiryo UI", Font.PLAIN, 16));
		labelB8.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		labelB1.setBounds(15, 30, 150, 40);
		labelB2.setBounds(15, 80, 150, 40);
		labelB3.setBounds(15, 130, 200, 40);
		labelB4.setBounds(15, 180, 200, 40);
		labelB5.setBounds(155, 30, 500, 40);
		labelB7.setBounds(15, 220, 400, 40);
		labelB8.setBounds(340, 75, 400, 40);
		labelB8.setForeground(Color.blue);

		tx11 = new JTextField();
		tx11.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		tx11.setBounds(170, 130, 200, 35); 	//借用者名

		Calendar calendar = Calendar.getInstance();
		Date initDate = calendar.getTime();//今日の日付を初期値にする
		calendar.set(2017, 1, 1, 0, 0);
		Date startDate = calendar.getTime();
		model1 = new SpinnerDateModel(initDate, startDate, null, Calendar.DAY_OF_MONTH);

		//貸出日
		spinner1 = new JSpinner(model1);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner1, "yyyy-MM-dd");
		spinner1.setEditor(editor);
		spinner1.setBounds(160, 80, 160, 35);
		spinner1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		//返却予定日
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.DAY_OF_MONTH, 7);
		Date inischeDate = calendar2.getTime();
		model2 = new SpinnerDateModel(inischeDate, startDate, null, Calendar.DAY_OF_MONTH);
		JSpinner spinner2 = new JSpinner(model2);
		JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spinner2, "yyyy-MM-dd");
		spinner2.setEditor(editor2);
		spinner2.setBounds(190, 180, 180, 35);
		spinner2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btntop1 = new JButton("登録");
		btntop1.setBounds(320, 250, 100, 40);
		btntop1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btnLcl = new JButton("取消");
		btnLcl.setBounds(440, 250, 100, 40);
		btnLcl.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btntop1.addActionListener(this);
		btnLcl.addActionListener(this);

		borrow.add(labelB1);
		borrow.add(labelB2);
		borrow.add(labelB3);
		borrow.add(labelB4);
		borrow.add(labelB5);
		borrow.add(labelB7);
		borrow.add(labelB8);
		borrow.add(btntop1);
		borrow.add(btnLcl);
		borrow.add(tx11);

		borrow.add(spinner1);
		borrow.add(spinner2);

	}

	//返却
	public void Return() throws SQLException {

		rtnframe = new JFrame("返却登録");
		rtnframe.setBounds(50, 50, 700, 400);
		rtnframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rtnframe.setVisible(true);

		rtn = new JPanel();
		rtn.setVisible(true);
		rtn.setLayout(null);

		rtnframe.add(rtn);

		labelR1 = new JLabel("□名前：");
		labelR2 = new JLabel("□タイトル：");
		labelR3 = new JLabel("□返却日：");
		labelR4 = new JLabel("□評価：");
		labelR5 = new JLabel();	//名前表示用
		labelR6 = new JLabel();	//タイトル表示用
		labelR7 = new JLabel();	//NUM受け渡し用
		labelR8 = new JLabel("（☆をクリックして5段階で評価してください）");

		star1 = new JLabel("★");
		star2 = new JLabel("★");
		star3 = new JLabel("★");
		star4 = new JLabel("☆");
		star5 = new JLabel("☆");

		labelR1.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelR2.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelR3.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelR4.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelR5.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelR6.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelR8.setFont(new Font("Meiryo UI", Font.PLAIN, 16));

		star1.setFont(new Font("Meiryo UI", Font.PLAIN, 30));
		star2.setFont(new Font("Meiryo UI", Font.PLAIN, 30));
		star3.setFont(new Font("Meiryo UI", Font.PLAIN, 30));
		star4.setFont(new Font("Meiryo UI", Font.PLAIN, 30));
		star5.setFont(new Font("Meiryo UI", Font.PLAIN, 30));

		labelR1.setBounds(15, 40, 100, 40);
		labelR2.setBounds(15, 90, 130, 40);
		labelR3.setBounds(15, 140, 200, 40);
		labelR4.setBounds(15, 190, 200, 40);
		labelR5.setBounds(155, 40, 400, 40);
		labelR6.setBounds(155, 90, 600, 40);
		labelR8.setBounds(25, 225, 600, 40);

		star1.setBounds(145, 190, 30, 40);
		star2.setBounds(175, 190, 30, 40);
		star3.setBounds(205, 190, 30, 40);
		star4.setBounds(235, 190, 30, 40);
		star5.setBounds(265, 190, 30, 40);

		int row = tbl.getSelectedRow();
		String selectPID = tbl.getValueAt(row, 0).toString();//選択している行のPID
		String selectTitle = tbl.getValueAt(row, 1).toString();//選択している行のタイトル
		labelR6.setText(selectTitle);

		//貸出者を取得する
		DB.connect();
		String[] info = DB.getLendName(selectPID);
		DB.close();
		labelR5.setText(info[0]);
		labelR7.setText(info[1]);

		Calendar calendar = Calendar.getInstance();
		Date initDate = calendar.getTime();//今日の日付を初期値にする
		calendar.set(2017, 1, 1, 0, 0);
		Date startDate = calendar.getTime();
		model3 = new SpinnerDateModel(initDate, startDate, null, Calendar.DAY_OF_MONTH);
		JSpinner spinner3 = new JSpinner(model3);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner3, "yyyy-MM-dd");
		spinner3.setEditor(editor);
		spinner3.setBounds(155, 140, 160, 40);
		spinner3.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btnRr = new JButton("登録");
		btnRr.setBounds(300, 270, 100, 40);
		btnRr.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btnRcl = new JButton("取消");
		btnRcl.setBounds(420, 270, 100, 40);
		btnRcl.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btnRr.addActionListener(this);
		btnRcl.addActionListener(this);

		rtn.add(labelR1);
		rtn.add(labelR2);
		rtn.add(labelR3);
		rtn.add(labelR4);
		rtn.add(labelR5);
		rtn.add(labelR6);
		rtn.add(labelR8);
		rtn.add(btnRr);
		rtn.add(btnRcl);
		rtn.add(spinner3);
		rtn.add(star1);
		rtn.add(star2);
		rtn.add(star3);
		rtn.add(star4);
		rtn.add(star5);

		star1.addMouseListener(this);
		star2.addMouseListener(this);
		star3.addMouseListener(this);
		star4.addMouseListener(this);
		star5.addMouseListener(this);

	}

	//延長
	public void Extend() throws SQLException {

		extenframe = new JFrame("延長登録");
		extenframe.setBounds(50, 50, 700, 350);
		extenframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		extenframe.setVisible(true);

		exten = new JPanel();
		exten.setVisible(true);
		exten.setLayout(null);
		extenframe.add(exten);

		labelE1 = new JLabel("■タイトル：");
		labelE2 = new JLabel("■名前：");
		labelE3 = new JLabel("■返却予定日：");
		labelE4 = new JLabel();	//タイトル表示用
		labelE5 = new JLabel();	//名前表示用
		labelE6 = new JLabel();	//NUM受け渡し用

		labelE1.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelE2.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelE3.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelE4.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelE5.setFont(new Font("Meiryo UI", Font.PLAIN, 25));

		labelE1.setBounds(15, 40, 150, 40);
		labelE2.setBounds(15, 90, 100, 40);
		labelE3.setBounds(15, 140, 200, 40);
		labelE4.setBounds(155, 40, 500, 40);
		labelE5.setBounds(155, 90, 200, 40);

		btnEr = new JButton("登録");
		btnEr.setBounds(300, 200, 100, 40);
		btnEr.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btnEcl = new JButton("取消");
		btnEcl.setBounds(420, 200, 100, 40);
		btnEcl.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btnEr.addActionListener(this);
		btnEcl.addActionListener(this);

		exten.add(labelE1);
		exten.add(labelE2);
		exten.add(labelE3);
		exten.add(labelE4);
		exten.add(labelE5);
		exten.add(btnEr);
		exten.add(btnEcl);

		int row = tbl.getSelectedRow();
		String selectPID = tbl.getValueAt(row, 0).toString();//選択している行のPID
		String selectTitle = tbl.getValueAt(row, 1).toString();//選択している行のタイトル
		labelE4.setText(selectTitle);

		//貸出者を取得する
		DB.connect();
		String[] info = DB.getLendName(selectPID);
		DB.close();
		labelE5.setText(info[0]);//name
		labelE6.setText(info[1]);//num

		//String⇒intへの変換
		int yy = (Integer.parseInt(info[2].substring(0, 4)));
		int mm = (Integer.parseInt(info[2].substring(5, 7)));
		int dd = (Integer.parseInt(info[2].substring(8, 10)));

		Calendar calendar4 = Calendar.getInstance();
		calendar4.set(yy, mm - 1, dd);	//調整

		Date iniDate = calendar4.getTime();
		calendar4.set(2017, 1, 1, 0, 0);
		Date startDate = calendar4.getTime();

		model4 = new SpinnerDateModel(iniDate, startDate, null, Calendar.DAY_OF_MONTH);
		JSpinner spinner4 = new JSpinner(model4);
		JSpinner.DateEditor editor4 = new JSpinner.DateEditor(spinner4, "yyyy-MM-dd");
		spinner4.setEditor(editor4);
		spinner4.setBounds(155, 140, 160, 40);
		spinner4.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		spinner4.setBounds(190, 140, 160, 35);
		spinner4.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		exten.add(spinner4);

	}

	//テーブルのセット
	public void setTable() throws SQLException {
		DB.connect();
		List<String[]> bookinfo = DB.makelendtopTable();
		DB.close();
		model.setRowCount(0);
		for(int i = 0; i < bookinfo.size(); i++) {
			model.addRow(bookinfo.get(i));
		}

		cnt1 = 0;//すべて初期値に戻す
		cnt2 = 0;
		cnt3 = 0;
		cnt4 = 0;
		cnt5 = 0;
		eval1 = 1;
		eval2 = 1;
		eval3 = 1;
		eval4 = 0;
		eval5 = 0;
	}

	//LEND_LISTのNUMを割り振る
	public String getsetNum() throws SQLException {
		DB.connect();
		int max = DB.getmaxNum();
		DB.close();
		String setNum = "";
		if(max < 9) {
			setNum = "0000" + (max + 1);
		} else if(max < 99) {
			setNum = "000" + (max + 1);
		} else if(max < 999) {
			setNum = "00" + (max + 1);
		} else if(max < 9999) {
			setNum = "0" + (max + 1);
		} else {
			setNum = Integer.toString(max + 1);
		}
		return setNum;
	}

	//検索
	public void reserch() throws SQLException {
		String item = null;
		String a = "";
		String b = "";
		String c = "";
		String d = "";
		String Reserchstr = null;
		String keyword = tx1.getText();
		String selectItem = (String) serchcmb.getSelectedItem();
		if(selectItem.equals("タイトル")) {
			item = "title";
		} else if(selectItem.equals("出版社")) {
			item = "publisher";
		} else if(selectItem.equals("著者")) {
			item = "author";
		} else if(selectItem.equals("ジャンル")) {
			item = "genre";
		}
		if(eover2.isSelected()) {//検索用項目
			stars = 2;
		} else if(eover3.isSelected()) {
			stars = 3;
		} else if(eover4.isSelected()) {
			stars = 4;
		} else if(all2.isSelected()) {
			stars = 0;
		}
		if(lendOK.isSelected()) {
			st = 0;
			st2 = 5;
		} else if(borrowing.isSelected()) {
			st = 1;
			st2 = 5;
		} else if(reserving.isSelected()) {
			st = 5;
			st2 = 2;
		} else if(all.isSelected()) {
			st = 5;
			st2 = 5;
		}

		if((keyword.length() != 0)) {
			a = "A";
		}
		if(stars == 0) {
			b = "B";
		}
		if(st == 5) {
			c = "C";
		}
		if(st2 == 5) {
			d = "D";
		}
		Reserchstr = a + b + c + d;
		String Reserchsql = "";
		switch (Reserchstr) {
		//①キーワード②評価③在庫or貸出④予約

		case "ABCD"://①あり②null③null④null
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE " + item + " LIKE '%" + keyword + "%';";
			break;
		case "ABC"://①あり②null③null④予約中
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE " + item + " LIKE '%" + keyword + "%'" + " and STATE2=2;";
			break;
		case "ABD"://①あり②null③貸出or在庫チェックあり
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE " + item + " LIKE '%" + keyword + "%'" + " and STATE=" + st + ";";
			break;
		case "ACD"://①あり②評価あり③null④null
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE " + item + " LIKE '%" + keyword + "%' and AVE_EVA >=" + stars + ";";
			break;
		case "AC"://①あり②評価あり③null④予約中
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE " + item + " LIKE '%" + keyword + "%' and AVE_EVA >=" + stars + " and STATE2=" + st2 + ";";
			break;
		case "A"://①あり②評価あり③貸出or在庫チェックあり
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE " + item + " LIKE '%" + keyword + "%' and AVE_EVA >=" + stars + " and STATE=" + st + ";";
			break;
		case "BCD"://①条件にキーワード入力なし②評価なし③貸出or在庫チェックなし④なし
			Reserchsql = "SELECT*FROM BOOK_LIST;";
			break;
		case "BC"://①条件にキーワード入力なし②評価なし③貸出or在庫チェックなし④予約あり
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE STATE2='2';";
			break;
		case "BD"://①条件にキーワード入力なし②評価なし③貸出or在庫チェックあり
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE STATE=" + st + ";";
			break;
		case "CD"://①条件にキーワード入力なし②評価チェックあり③在庫のチェックなし④予約チェックなし
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE AVE_EVA >=" + stars + ";";
			break;
		case "C"://①条件にキーワード入力なし②評価チェックあり③在庫のチェックなし④予約チェックあり
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE  AVE_EVA >=" + stars + " and STATE2=" + st2 + ";";
			break;
		case "D"://①条件にキーワード入力なし②評価チェックあり③在庫のチェックあり④予約チェックなし
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE  AVE_EVA >=" + stars + " and STATE=" + st + ";";
			break;
		case ""://①条件にキーワード入力なし②評価チェックあり③在庫チェックあり④予約チェックあり
			Reserchsql = "SELECT*FROM BOOK_LIST WHERE  AVE_EVA >=" + stars + " and STATE=" + st + ";";
			break;
		}
		DB.connect();
		List<String[]> bookreserchinfo = DB.reserch(Reserchsql);
		DB.close();
		if(bookreserchinfo.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "条件に一致する書籍はありません。");
		} else {
			model.setRowCount(0);
			for(int i = 0; i < bookreserchinfo.size(); i++) {
				model.addRow(bookreserchinfo.get(i));
			}

		}
	}

	//貸出・予約情報の表示
	public void showLendinfo(int row) throws SQLException {

		String PID = tbl.getValueAt(row, 0).toString();//選択している行のPID
		String selectTitle = tbl.getValueAt(row, 1).toString();//選択している行のタイトル

		frame = new JFrame("貸出/予約情報");
		frame.setBounds(850, 50, 650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		JPanel pnl = new JPanel();
		pnl.setLayout(null);

		Container con = frame.getContentPane();
		con.add(pnl);

		label0 = new JLabel("＜貸出情報＞");
		label0.setFont(new Font("Meiryo UI", Font.PLAIN, 27));
		label0.setBounds(15, 20, 300, 40);

		labelbar = new JLabel("========================");
		labelbar.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelbar.setBounds(15, 220, 550, 40);

		pnl.add(label0);
		pnl.add(labelbar);
		DB.connect();
		String[] info = DB.getlendInfo(PID);

		if(info[0] == null) {
			labelmesg2 = new JLabel("貸出情報はありません。");
			labelmesg2.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			labelmesg2.setBounds(15, 100, 550, 40);
			pnl.add(labelmesg2);
		} else {

			label1 = new JLabel("タイトル：");
			label2 = new JLabel("借用者：");
			label3 = new JLabel("貸出日：");
			label4 = new JLabel("返却予定日：");
			label5 = new JLabel(selectTitle);
			label6 = new JLabel(info[0]);
			label7 = new JLabel(info[1]);
			label8 = new JLabel(info[2]);
			label9 = new JLabel();

			if(info[2].compareTo(Today) < 0) {
				label9.setText("※返却期限を過ぎています");
			}

			label1.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label2.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label3.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label4.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label5.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label6.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label7.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label8.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			label9.setFont(new Font("Meiryo UI", Font.PLAIN, 18));

			label1.setBounds(15, 60, 100, 40);
			label2.setBounds(15, 100, 100, 40);
			label3.setBounds(15, 140, 100, 40);
			label4.setBounds(15, 180, 160, 40);
			label5.setBounds(120, 60, 500, 40);
			label6.setBounds(120, 100, 200, 40);
			label7.setBounds(120, 140, 200, 40);
			label8.setBounds(175, 180, 200, 40);
			label9.setBounds(340, 180, 200, 40);

			label9.setForeground(Color.red);

			pnl.add(label1);
			pnl.add(label2);
			pnl.add(label3);
			pnl.add(label4);
			pnl.add(label5);
			pnl.add(label6);
			pnl.add(label7);
			pnl.add(label8);
			pnl.add(label9);

		}

		label0r = new JLabel("＜予約情報＞");
		label0r.setFont(new Font("Meiryo UI", Font.PLAIN, 27));
		label0r.setBounds(15, 270, 300, 40);
		pnl.add(label0r);

		btnc1 = new JButton("予約取消");
		btnc1.setBounds(400, 330, 100, 40);
		btnc1.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		btnc1.addActionListener(this);

		btnc2 = new JButton("予約取消");
		btnc2.setBounds(400, 530, 100, 40);
		btnc2.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		btnc2.addActionListener(this);

		btnc3 = new JButton("予約取消");
		btnc3.setBounds(400, 730, 100, 40);
		btnc3.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		btnc3.addActionListener(this);

		//貸出者・貸出日・返却予定日を取得する

		int cnt = DB.getRsvCnt(PID);

		if(cnt == 0) {
			labelmesg = new JLabel("予約情報はありません。");
			labelmesg.setBounds(20, 310, 300, 40);
			labelmesg.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
			pnl.add(labelmesg);
			DB.close();
		} else {
			ArrayList<String> rsvinfo = DB.getrsvinfo(PID);

			for(int i = 0; i < cnt; i++) {
				frame.setBounds(850, 50, 600, 400 + (200 * cnt));

				//1件目の表示
				label3r = new JLabel("貸出者：");
				label4r = new JLabel("貸出日：");
				label5r = new JLabel("返却予定日：");
				label6r = new JLabel("No.");

				label3r.setBounds(15, 350 + (180 * i), 100, 40);
				label4r.setBounds(15, 390 + (180 * i), 100, 40);
				label5r.setBounds(15, 430 + (180 * i), 150, 40);
				label6r.setBounds(15, 310 + (180 * i), 150, 40);

				label3r.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
				label4r.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
				label5r.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
				label6r.setFont(new Font("Meiryo UI", Font.PLAIN, 25));

				pnl.add(label3r);
				pnl.add(label4r);
				pnl.add(label5r);
				pnl.add(label6r);

				label2a = new JLabel(rsvinfo.get(0 + (4 * i)));//name
				label3a = new JLabel(rsvinfo.get(1 + (4 * i)));
				label4a = new JLabel(rsvinfo.get(2 + (4 * i)));
				label5a = new JLabel(rsvinfo.get(3 + (4 * i)));//num

				label2a.setBounds(145, 350 + (180 * i), 200, 40);
				label3a.setBounds(145, 390 + (180 * i), 200, 40);
				label4a.setBounds(175, 430 + (180 * i), 250, 40);
				label5a.setBounds(80, 310 + (180 * i), 250, 40);

				label2a.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
				label3a.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
				label4a.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
				label5a.setFont(new Font("Meiryo UI", Font.PLAIN, 25));

				pnl.add(label2a);
				pnl.add(label3a);
				pnl.add(label4a);
				pnl.add(label5a);

				if(i == 0) {
					pnl.add(btnc1);
					cancelnum1 = rsvinfo.get(3);
				} else if(i == 1) {
					pnl.add(btnc2);
					cancelnum2 = rsvinfo.get(7);
				} else if(i == 2) {
					pnl.add(btnc3);
					cancelnum3 = rsvinfo.get(11);
				}
			}
			DB.close();
		}

		btn1 = new JButton("閉");
		btn1.setBounds(470, 10, 70, 40);
		btn1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		btn1.addActionListener(this);

		pnl.add(btn1);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btntop) {	//TOP
			lendframe.dispose();
			Top top = new Top();
			try {
				top.Top();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == hbtn) {	//履歴
			lendframe.dispose();
			Rireki rireki = new Rireki();
			try {
				rireki.rireki();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == btnrch) {	//実行ボタン
			try {
				reserch();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
		} else if(e.getSource() == btnrst) {	//リセットボタン
			try {
				setTable();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			tx1.setText("");
			stars = 0; //評価の条件用
			st = 5;	//在庫の条件用
			st2 = 5;	//在庫の条件用
			group.clearSelection();
			groupe.clearSelection();
			all.setSelected(true);
			all2.setSelected(true);
		} else if(e.getSource() == btnbr) {	//貸出/予約ボタン
			int row = tbl.getSelectedRow();
			if(row == -1) {
				Validate.noSelect();
			} else {
				String selectState = tbl.getValueAt(row, 6).toString();//選択している行のstate
				if(selectState.equals("貸出中 ") || selectState.equals("貸出中 予約中")) {//680行目にあわせて、貸出中＋半角スペース必須
					String selectTitle = tbl.getValueAt(row, 1).toString();//選択している行のタイトル
					Borrow();
					labelB8.setText("※貸出中のため予約のみ可能※");
					labelB5.setText(selectTitle);
				} else {
					String selectTitle = tbl.getValueAt(row, 1).toString();//選択している行のタイトル
					Borrow();
					labelB5.setText(selectTitle);

				}
			}
		} else if(e.getSource() == btnrtn) {	//返却
			int row = tbl.getSelectedRow();
			if(row == -1) {
				Validate.noSelect();
			} else {
				String selectstate = tbl.getValueAt(row, 6).toString();//選択している行のstate
				if(selectstate.equals(" ")) {
					Validate.NotOK();
				} else {
					try {
						Return();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		} else if(e.getSource() == btnext) {//延長
			int row = tbl.getSelectedRow();
			if(row == -1) {
				Validate.noSelect();
			} else {
				String selectstate = tbl.getValueAt(row, 6).toString();//選択している行のstate
				if(selectstate.equals(" ") || selectstate.equals(" 予約中")) {
					Validate.NotOK();
				} else {
					try {
						Extend();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}
			}
		} else if(e.getSource() == btntop1) { //貸出登録ボタン
			int row = tbl.getSelectedRow();
			String selectPID = tbl.getValueAt(row, 0).toString();//選択している行のPID
			String selectState = tbl.getValueAt(row, 6).toString();//選択している行のstate

			//入力された値の取得
			String name = tx11.getText();
			Date brd = model1.getDate();
			Date rtn = model2.getDate();

			String bdate = DataFmt.format(brd);
			String rtndate = DataFmt.format(rtn);

			Pattern p = Pattern.compile("[^0123456789-]");
			Matcher m = p.matcher(bdate);
			Matcher m2 = p.matcher(rtndate);

			if(name.length() == 0 || rtndate.length() == 0 || bdate.length() == 0) {
				Validate.Null();
			} else if(rtndate.compareTo(bdate) <= 0) {//返却日が貸出日より過去（同日含む）
				Validate.DateCheck();
			} else if((bdate.compareTo(Today) < 0)) {//貸出日が今日より過去
				Validate.DateCheck();
			} else if(m.find() || m2.find()) {
				Validate.hankaku();
			} else {//エラーがない場合

				switch (selectState) {

				case "貸出中 ":
					//返却予定日を取得する
					String sche_dt = null;
					try {
						DB.connect();
						sche_dt = DB.getRtndate(selectPID);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					if(sche_dt.compareTo(bdate) > 0) {
						Validate.CannotRsv();
					} else {//通常処理
						String setNum = null;
						try {
							setNum = getsetNum();
							DB.borrowregister(setNum, selectPID, name, bdate, rtndate);
						} catch (SQLException e1) {

							e1.printStackTrace();
						}
						//BOOK_LIST ステータスの確認・変更
						if(bdate.compareTo(Today) == 0) {
							try {
								DB.updatelendstate(selectPID);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}

						} else if(bdate.compareTo(Today) > 0) {
							try {
								DB.updatersvstate(selectPID);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
						try {
							setTable();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						JOptionPane.showMessageDialog(lendframe, "登録完了しました。返却期限は" + rtndate + "です。");
						try {
							setTable();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						borrowframe.dispose();

					}
					DB.close();

				case "貸出中 予約中":
				case " 予約中":

					//貸出日と返却予定日を取得する（貸出日順に並べる）
					String[] OSinfo = null;
					try {
						DB.connect();
						OSinfo = DB.getOutandSchedate(selectPID);
					} catch (SQLException e1) {

						e1.printStackTrace();
					}
					String out_dt = OSinfo[0];
					sche_dt = OSinfo[1];

					//入力中の日付が予約日と返却日のあいだ
					if(out_dt.compareTo(bdate) < 0 && sche_dt.compareTo(bdate) > 0) {
						Validate.CannotRsv();

						//入力中の返却予定日が予約の貸出日と返却予定日のあいだ
					} else if(out_dt.compareTo(rtndate) < 0 && sche_dt.compareTo(rtndate) > 0) {
						Validate.CannotRsv();

						//予約より先に貸出、予約より後に返却の場合
					} else if(out_dt.compareTo(bdate) > 0 && sche_dt.compareTo(rtndate) < 0) {
						Validate.CannotRsv();

					} else {
						String setNum;
						try {
							setNum = getsetNum();
							DB.connect();
							DB.borrowregister(setNum, selectPID, name, bdate, rtndate);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						//BOOK_LIST ステータスの確認・変更
						if(bdate.compareTo(Today) == 0) {
							try {
								DB.connect();
								DB.updatelendstate(selectPID);
								DB.close();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}

						} else if(bdate.compareTo(Today) > 0) {
							try {
								DB.connect();
								DB.updatersvstate(selectPID);
								DB.close();
							} catch (SQLException e1) {

								e1.printStackTrace();
							}
							JOptionPane.showMessageDialog(lendframe, "登録完了しました。返却期限は" + rtndate + "です。");
							try {
								setTable();
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							borrowframe.dispose();
						}
					}

				case " ":
					String setNum;
					try {
						setNum = getsetNum();
						DB.connect();
						DB.borrowregister(setNum, selectPID, name, bdate, rtndate);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					//BOOK_LIST ステータスの確認・変更
					if(bdate.compareTo(Today) == 0) {
						try {
							DB.updatelendstate(selectPID);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

					} else if(bdate.compareTo(Today) > 0) {
						try {
							DB.updatersvstate(selectPID);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
					DB.close();
					JOptionPane.showMessageDialog(lendframe, "登録完了しました。返却期限は" + rtndate + "です。");
					try {
						setTable();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					borrowframe.dispose();
				}
			}
		} else if(e.getSource() == btnLcl) {	//	貸出/予約-取消
			borrowframe.dispose();
		} else if(e.getSource() == btnRr) {	//返却-登録
			int row = tbl.getSelectedRow();
			String selectPID = tbl.getValueAt(row, 0).toString();//選択している行のPID
			Date idt = model3.getDate();
			String indate = DataFmt.format(idt);
			String name = labelR5.getText();
			String rtnnum = labelR7.getText();
			//評価
			int total_eval = eval1 + eval2 + eval3 + eval4 + eval5;//★の合計値
			//返却処理
			try {
				DB.connect();
				totalPrice = DB.returnbook(indate, total_eval, rtnnum, name, selectPID);
				DB.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(lendframe, "登録完了しました。"
					+ "あなたは累計" + totalPrice + "円の本を読破しました！");

			rtnframe.dispose();
			try {
				setTable();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == btnRcl) {	//	返却-取消
			rtnframe.dispose();
		} else if(e.getSource() == btnEr) {	//	延長-登録
			int row = tbl.getSelectedRow();
			String selectPID = tbl.getValueAt(row, 0).toString();//選択している行のPID
			Date edt = model4.getDate();
			String extendate = DataFmt.format(edt);
			String extendnum = labelE6.getText();

			//予約件数を取得
			int getrsv = 0;
			try {
				DB.connect();
				getrsv = DB.getRsvCnt(selectPID);
				DB.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			if(getrsv == 0) {//予約がない場合
				//通常処理
				try {
					DB.connect();
					DB.extenLend(extendate, extendnum);
					DB.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(lendframe, "登録完了しました。");
				extenframe.dispose();
				try {
					setTable();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {//予約がほかにある場合

				//貸出日と返却予定日を取得する（貸出日順に並べる）
				String[] OSinfo = null;
				try {
					DB.connect();
					OSinfo = DB.getOutandSchedate(selectPID);
					DB.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				String out_dt = OSinfo[0];
				String sche_dt = OSinfo[1];

				//入力中の日付が予約日と返却日のあいだ
				if(out_dt.compareTo(extendate) < 0 && sche_dt.compareTo(extendate) > 0) {
					Validate.CannotRsv();
					//入力中の返却予定日が予約の返却予定日以上のとき
				} else if(sche_dt.compareTo(extendate) < 0) {
					Validate.CannotRsv();
					//予約より先に貸出、予約より後に返却の場合
				} else {
					//通常処理
					try {
						DB.connect();
						DB.extenLend(extendate, extendnum);
						DB.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(lendframe, "登録完了しました。");
					try {
						setTable();
						extenframe.dispose();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

				}
			}

		} else if(e.getSource() == btnEcl) {//延長-取消ボタン
			extenframe.dispose();
		}

		if(e.getSource() == btn1) {//閉じるボタン
			frame.dispose();
		} else if(e.getSource() == btnc1) {//予約取消
			String PID = null;
			int cnt = 0;
			try {
				DB.connect();
				PID = DB.getPID(cancelnum1);
				cnt = DB.getRsvCnt(PID);
				DB.cancel(cancelnum1);

			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			if(cnt == 1) {
				try {
					DB.changeRsvstate(PID);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(frame, "予約を取り消しました。");
			try {
				setTable();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			frame.dispose();
			DB.close();
		} else if(e.getSource() == btnc2) {//予約取消（2件目）
			try {
				DB.connect();
				DB.cancel(cancelnum2);
				DB.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(frame, "予約を取り消しました。");
			try {
				setTable();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			frame.dispose();
		} else if(e.getSource() == btnc3) {//予約取消（3件目）
			try {
				DB.connect();
				DB.cancel(cancelnum3);
				DB.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			JOptionPane.showMessageDialog(frame, "予約を取り消しました。");
			try {
				setTable();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}
			frame.dispose();
		}
	}

	@Override
	//返却時評価用
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == star2) {
			cnt2++;
			if(cnt2 % 2 == 1) {
				star2.setText("☆");
				eval2 = 0;
			} else if(cnt2 % 2 == 0) {
				star2.setText("★");
				eval2 = 1;
			}
		}
		if(e.getSource() == star3) {
			cnt3++;
			if(cnt3 % 2 == 1) {
				star3.setText("☆");
				eval3 = 0;
			} else if(cnt3 % 2 == 0) {
				star3.setText("★");
				eval3 = 1;
			}
		}
		if(e.getSource() == star4) {	//初期☆
			cnt4++;
			if(cnt4 % 2 == 0) {
				star4.setText("☆");
				eval4 = 0;
			} else if(cnt4 % 2 == 1) {
				star4.setText("★");
				eval4 = 1;
			}
		}
		if(e.getSource() == star5) {	//初期☆
			cnt5++;
			if(cnt5 % 2 == 0) {
				star5.setText("☆");
				eval5 = 0;
			} else if(cnt5 % 2 == 1) {
				star5.setText("★");
				eval5 = 1;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {	//貸出・予約情報の表示
		if(e.getButton() == MouseEvent.BUTTON3) { //右クリック

			int row = tbl.getSelectedRow();
			try {
				showLendinfo(row);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
