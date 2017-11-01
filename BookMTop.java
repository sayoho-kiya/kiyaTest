package screen;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import action.DB;
import validate.Validate;

public class BookMTop implements ActionListener {

	JLabel labelr17, labelr18, labelr19, labelr20, shiftPID;
	JTextField txr1, txr2, txr3, txr4, txr5;
	JButton btntop, btnrch, btnrst, btnr, btnup, btndlt, btnir, btnr1, btnr2, btnr3, btnr4;

	JFrame bmframe, rframe;
	JTextField getkeyword;
	JPanel top, registerp, updatep;
	JTable tbl;
	DefaultTableModel model;

	JComboBox combo, cor6;
	String[] header = { "PID", "タイトル", "出版社", "著者", "ジャンル", "評価", "エリア", "定価" };

	public void display_top() throws SQLException {
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Meiryo UI", Font.PLAIN, 20)));
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Meiryo UI", Font.PLAIN, 16)));

		bmframe = new JFrame("書籍情報登録");
		bmframe.setBounds(0, 0, 1650, 1050);
		bmframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bmframe.setVisible(true);

		top = new JPanel();
		top.setLayout(null);
		top.setBounds(10, 10, 1600, 750);
		top.setVisible(true);
		Container con;
		con = bmframe.getContentPane();
		con.add(top);

		//書籍一覧を取得
		DB.connect();
		List<String[]> bookinfo = DB.getbooklist();
		DB.close();
		model = new DefaultTableModel(bookinfo.toArray(new String[0][0]), header);
		tbl = new JTable(model);

		tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//複数行選択できないように設定
		tbl.setBounds(10, 250, 1550, 600);
		tbl.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		tbl.setRowHeight(28);

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
		tbl.getColumnModel().getColumn(6).setPreferredWidth(80);
		tbl.getColumnModel().getColumn(7).setPreferredWidth(125);

		//右揃え(定価）
		DefaultTableCellRenderer r_renderer = new DefaultTableCellRenderer();
		r_renderer.setHorizontalAlignment(SwingConstants.RIGHT);
		tbl.getColumnModel().getColumn(7).setCellRenderer(r_renderer);

		//中央揃え（エリア）
		DefaultTableCellRenderer c_renderer = new DefaultTableCellRenderer();
		c_renderer.setHorizontalAlignment(SwingConstants.CENTER);
		tbl.getColumnModel().getColumn(6).setCellRenderer(c_renderer);
		JScrollPane sp;
		sp = new JScrollPane(tbl);
		sp.setBounds(30, 250, 1550, 600);

		top.add(sp);

		String[] combodata = { "タイトル", "出版社", "著者", "ジャンル" };
		combo = new JComboBox(combodata);
		combo.setBounds(30, 100, 200, 50);
		combo.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		getkeyword = new JTextField();
		getkeyword.setBounds(230, 100, 300, 50);
		getkeyword.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		top.add(combo);
		top.add(getkeyword);

		btntop = new JButton("TOP");
		btnrch = new JButton("実行");
		btnrst = new JButton("リセット");
		btnr = new JButton("新規登録");
		btnup = new JButton("編集");
		btndlt = new JButton("削除");
		btnir = new JButton("【引用】新規登録");

		btntop.setBounds(30, 10, 100, 40);
		btnrch.setBounds(550, 100, 100, 40);
		btnrst.setBounds(670, 100, 100, 40);
		btnr.setBounds(530, 900, 120, 40);
		btnup.setBounds(850, 900, 120, 40);
		btndlt.setBounds(1000, 900, 120, 40);
		btnir.setBounds(680, 900, 140, 40);

		btntop.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btnrch.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btnrst.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		btnr.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		btnup.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btndlt.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		btnir.setFont(new Font("Meiryo UI", Font.PLAIN, 14));

		btntop.addActionListener(this);
		btnrch.addActionListener(this);
		btnrst.addActionListener(this);
		btnr.addActionListener(this);
		btnup.addActionListener(this);
		btndlt.addActionListener(this);
		btnir.addActionListener(this);

		JLabel label1, label2, label3;
		label1 = new JLabel("※プルダウンから項目を選択して、キーワードを入力してください。");
		label1.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		label1.setBounds(30, 70, 600, 30);

		label2 = new JLabel("※新規登録の場合は「新規登録ボタン」を押してください。");
		label3 = new JLabel("※編集・削除の場合は、対象の書籍を選択してそれぞれのボタンを押してください。");
		label2.setBounds(30, 190, 800, 20);
		label3.setBounds(30, 210, 800, 20);
		label2.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		label3.setFont(new Font("Meiryo UI", Font.PLAIN, 18));

		top.add(btntop);
		top.add(btnrch);
		top.add(btnrst);
		top.add(btnr);
		top.add(btnup);
		top.add(btndlt);
		top.add(btnir);
		top.add(label1);
		top.add(label2);
		top.add(label3);

	}

	//登録・編集画面
	public void iregisterpnl() {

		rframe = new JFrame("書籍情報登録");
		rframe.setBounds(50, 50, 650, 670);
		rframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rframe.setVisible(true);

		registerp = new JPanel();
		registerp.setLayout(null);

		rframe.add(registerp);
		JLabel labelr1, labelr2, labelr3, labelr4, labelr5, labelr6, labelr7;
		labelr1 = new JLabel("タイトル：");
		labelr2 = new JLabel("出版社：");
		labelr3 = new JLabel("著者：");
		labelr4 = new JLabel("ジャンル：");
		labelr5 = new JLabel("定価：");
		labelr6 = new JLabel("エリア：");

		labelr7 = new JLabel(); //編集時受け渡し用
		JLabel labelr11, labelr12, labelr13, labelr14, labelr15, labelr16;
		labelr11 = new JLabel("※全角・半角入力可");
		labelr12 = new JLabel("※全角・半角入力可");
		labelr13 = new JLabel("※全角・半角入力可");
		labelr14 = new JLabel("※全角・半角入力可");
		labelr15 = new JLabel("※半角数字で入力。");
		labelr16 = new JLabel("※プルダウンより選択。");
		labelr17 = new JLabel("＜最大全角20文字＞");
		labelr18 = new JLabel("＜最大全角10文字＞");
		labelr19 = new JLabel("＜最大全角10文字＞");
		labelr20 = new JLabel("＜最大全角10文字＞");

		txr1 = new JTextField();
		txr2 = new JTextField();
		txr3 = new JTextField();
		txr4 = new JTextField();
		txr5 = new JTextField();

		String[] arealist = { "A", "B", "C", "D", "E", "F", "G", "H" };
		cor6 = new JComboBox(arealist);

		labelr1.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelr2.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelr3.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelr4.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelr5.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		labelr6.setFont(new Font("Meiryo UI", Font.PLAIN, 25));

		labelr11.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		labelr12.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		labelr13.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		labelr14.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		labelr15.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		labelr16.setFont(new Font("Meiryo UI", Font.PLAIN, 15));

		labelr17.setFont(new Font("Meiryo UI", Font.PLAIN, 17));
		labelr18.setFont(new Font("Meiryo UI", Font.PLAIN, 17));
		labelr19.setFont(new Font("Meiryo UI", Font.PLAIN, 17));
		labelr20.setFont(new Font("Meiryo UI", Font.PLAIN, 17));

		txr1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		txr2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		txr3.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		txr4.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		txr5.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
		cor6.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		labelr1.setBounds(15, 40, 100, 25);
		labelr11.setBounds(115, 43, 400, 25);
		txr1.setBounds(15, 70, 400, 32);
		labelr2.setBounds(15, 120, 100, 25);
		labelr12.setBounds(115, 123, 400, 25);
		txr2.setBounds(15, 150, 400, 32);
		labelr3.setBounds(15, 200, 100, 25);
		labelr13.setBounds(115, 203, 400, 25);
		txr3.setBounds(15, 230, 400, 32);
		labelr4.setBounds(15, 280, 120, 25);
		labelr14.setBounds(115, 283, 400, 25);
		txr4.setBounds(15, 310, 400, 32);
		labelr5.setBounds(15, 360, 100, 25);
		labelr15.setBounds(115, 363, 400, 25);
		txr5.setBounds(15, 390, 400, 32);
		labelr6.setBounds(15, 440, 100, 25);
		labelr16.setBounds(115, 443, 400, 25);
		cor6.setBounds(15, 470, 400, 32);

		labelr17.setBounds(435, 70, 400, 32);
		labelr18.setBounds(435, 150, 400, 32);
		labelr19.setBounds(435, 230, 400, 32);
		labelr20.setBounds(435, 310, 400, 32);

		btnr2 = new JButton("取消");
		btnr2.setBounds(440, 550, 100, 40);
		btnr2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));

		registerp.add(labelr1);
		registerp.add(labelr2);
		registerp.add(labelr3);
		registerp.add(labelr4);
		registerp.add(labelr5);
		registerp.add(labelr6);
		registerp.add(labelr7);
		registerp.add(labelr11);
		registerp.add(labelr12);
		registerp.add(labelr13);
		registerp.add(labelr14);
		registerp.add(labelr15);
		registerp.add(labelr16);
		registerp.add(labelr17);
		registerp.add(labelr18);
		registerp.add(labelr19);
		registerp.add(labelr20);

		registerp.add(btnr2);
		registerp.add(txr1);
		registerp.add(txr2);
		registerp.add(txr3);
		registerp.add(txr4);
		registerp.add(txr5);
		registerp.add(cor6);

		btnr2.addActionListener(this);
	}

	//採番するPID番号の
	public String getsetPID() throws SQLException {
		DB.connect();
		int max = DB.getMaxPid();
		DB.close();
		String setPID = "";
		if(max < 9) {
			setPID = "000" + (max + 1);
		} else if(max < 99) {
			setPID = "00" + (max + 1);
		} else if(max < 999) {
			setPID = "0" + (max + 1);
		} else {
			setPID = Integer.toString(max + 1);
		}
		return setPID;
	}

	public String[] registercheck() throws UnsupportedEncodingException {
		String registerinfo[] = null;
		//入力された値の取得
		String title = txr1.getText();
		String publisher = txr2.getText();
		String author = txr3.getText();
		String genre = txr4.getText();
		String price = txr5.getText();

		//バイト数
		int titlebyt = title.getBytes("Shift-JIS").length;
		int publisherbyt = publisher.getBytes("Shift-JIS").length;
		int authorbyt = author.getBytes("Shift-JIS").length;
		int genrebyt = genre.getBytes("Shift-JIS").length;

		//半角数字チェック
		Pattern p = Pattern.compile("[^0123456789]");
		Matcher m = p.matcher(price);

		if(title.length() == 0 || publisher.length() == 0 || author.length() == 0 || genre.length() == 0 || price.length() == 0) {
			Validate.noInput();
		} else if(price.length() > 9) {
			Validate.numcheck();
		} else if(m.find()) {//半角数字チェック
			Validate.numcheck();
		} else if(titlebyt > 40 || publisherbyt > 20 || authorbyt > 20 || genrebyt > 20) {
			Validate.cntover();
			labelr17.setForeground(Color.red);
			labelr18.setForeground(Color.red);
			labelr19.setForeground(Color.red);
			labelr20.setForeground(Color.red);

			if(titlebyt > 40) {
				labelr17.setText(titlebyt - 40 + "バイトオーバー");
			} else {
				labelr17.setText("OK");
			}
			if(publisherbyt > 20) {
				labelr18.setText(publisherbyt - 20 + "バイトオーバー");
			} else {
				labelr18.setText("OK");
			}
			if(authorbyt > 20) {
				labelr19.setText(authorbyt - 20 + "バイトオーバー");
			} else {
				labelr19.setText("OK");
			}
			if(genrebyt > 20) {
				labelr20.setText(genrebyt - 20 + "バイトオーバー");
			} else {
				labelr20.setText("OK");
			}

		} else {
			registerinfo = new String[6];
			registerinfo[0] = txr1.getText();
			registerinfo[1] = txr2.getText();
			registerinfo[2] = txr3.getText();
			registerinfo[3] = txr4.getText();
			registerinfo[4] = txr5.getText();
			registerinfo[5] = cor6.getSelectedItem().toString();//selectarea

		}
		return registerinfo;
	}

	public void setbookTable() throws SQLException {
		DB.connect();
		List<String[]> bookinfo = DB.getbooklist();
		DB.close();

		model.setRowCount(0);
		for(int i = 0; i < bookinfo.size(); i++) {
			model.addRow(bookinfo.get(i));
		}

	}

	public void reserch() throws SQLException {
		String keyword = getkeyword.getText();	//入力したキーワード
		String item = null;
		String selectItem = (String) combo.getSelectedItem(); //プルダウンから選択した項目
		if(selectItem.equals("タイトル")) {
			item = "title";
		} else if(selectItem.equals("出版社")) {
			item = "publisher";
		} else if(selectItem.equals("著者")) {
			item = "author";
		} else if(selectItem.equals("ジャンル")) {
			item = "genre";
		}
		DB.connect();
		List<String[]> bookreserchinfo = DB.reserchbook(keyword, item);
		DB.close();
		if(bookreserchinfo.isEmpty()) {
			JOptionPane.showMessageDialog(rframe, "条件に一致する書籍はありません。");
		} else {
			model.setRowCount(0);
			for(int i = 0; i < bookreserchinfo.size(); i++) {
				model.addRow(bookreserchinfo.get(i));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ここにコードを挿入
		int row = tbl.getSelectedRow(); //選択された行

		if(e.getSource() == btntop) {		//TOP
			bmframe.dispose();
			Top top = new Top();
			try {
				top.Top();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == btnrch) {		//検索実行ボタン
			try {
				reserch();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if(e.getSource() == btnrst) {		//検索リセットボタン
			try {
				setbookTable();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			getkeyword.setText("");

		} else if(e.getSource() == btnr) {	//新規登録ボタン
			iregisterpnl();

			btnr1 = new JButton("登録");
			btnr1.setBounds(320, 550, 100, 40);
			btnr1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
			btnr1.addActionListener(this);
			registerp.add(btnr1);

		} else if(e.getSource() == btnir) {	//引用ー新規登録ボタン
			row = tbl.getSelectedRow();
			if(row == -1) {		//どのセルも選択されていない場合のエラー処理
				Validate.noSelect();
			} else {
				String selectT = tbl.getValueAt(row, 1).toString();//選択している行のタイトル
				String selectP = tbl.getValueAt(row, 2).toString();//選択している行の出版社
				String selectA = tbl.getValueAt(row, 3).toString();//選択している行の著者
				String selectG = tbl.getValueAt(row, 4).toString();//選択している行のジャンル
				String selectPr = tbl.getValueAt(row, 7).toString();//選択している行の定価
				String selectAr = tbl.getValueAt(row, 6).toString();//選択している行のエリア

				iregisterpnl();
				btnr3 = new JButton("登録");
				btnr3.setBounds(320, 550, 100, 40);
				btnr3.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
				btnr3.addActionListener(this);
				registerp.add(btnr3);

				//テキストフィールドに値をセットする
				txr1.setText(selectT);
				txr2.setText(selectP);
				txr3.setText(selectA);
				txr4.setText(selectG);
				txr5.setText((selectPr.substring(1)).replace(",", ""));//通貨形式⇒数値への変換
				cor6.setSelectedItem(selectAr);

			}
		} else if(e.getSource() == btnup) {	//編集ボタン
			row = tbl.getSelectedRow();
			String PID = tbl.getValueAt(row, 0).toString();
			if(row == -1) {		//どのセルも選択されていない場合のエラー処理
				Validate.noSelect();
			} else {
				String selectT = tbl.getValueAt(row, 1).toString();//選択している行のタイトル
				String selectP = tbl.getValueAt(row, 2).toString();//選択している行の出版社
				String selectA = tbl.getValueAt(row, 3).toString();//選択している行の著者
				String selectG = tbl.getValueAt(row, 4).toString();//選択している行のジャンル
				String selectPr = tbl.getValueAt(row, 7).toString();//選択している行の定価
				String selectAr = tbl.getValueAt(row, 6).toString();//選択している行のエリア

				iregisterpnl();
				btnr4 = new JButton("登録");
				btnr4.setBounds(320, 550, 100, 40);
				btnr4.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
				btnr4.addActionListener(this);
				registerp.add(btnr4);
				shiftPID = new JLabel(PID);

				//テキストフィールドに値をセットする
				txr1.setText(selectT);
				txr2.setText(selectP);
				txr3.setText(selectA);
				txr4.setText(selectG);
				txr5.setText((selectPr.substring(1)).replace(",", ""));//通貨形式⇒数値への変換
				cor6.setSelectedItem(selectAr);

			}

		} else if(e.getSource() == btndlt) {//削除ボタン
			row = tbl.getSelectedRow();

			if(row == -1) {
				Validate.noSelect();
			} else {
				String PID = tbl.getValueAt(row, 0).toString();
				int option = JOptionPane.showConfirmDialog(null, "本当に削除しますか？",
						"DELETE確認", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.OK_OPTION) {
					try {
						DB.connect();
						DB.delete(PID);
						DB.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					//削除した行を削除する
					model.removeRow(row);
				}
			}
		} else if(e.getSource() == btnr3 || e.getSource() == btnr1) {	//新規登録-登録ボタン

			try {
				String[] registerinfo = registercheck();
				if(registerinfo != null) {
					String setNum = getsetPID();
					DB.connect();
					DB.bookregister(setNum, registerinfo[0], registerinfo[1], registerinfo[2], registerinfo[3], registerinfo[4], registerinfo[5]);
					DB.close();
					rframe.dispose();
					setbookTable();
					JOptionPane.showMessageDialog(bmframe, "登録完了しました。");
				}
			} catch (UnsupportedEncodingException | SQLException e1) {
				e1.printStackTrace();
			}

		} else if(e.getSource() == btnr2) {	//新規登録-取消ボタン
			rframe.dispose();
		} else if(e.getSource() == btnr4) {	//編集-登録ボタン
			String PID = shiftPID.getText();
			try {
				String[] registerinfo = registercheck();
				if(registerinfo != null) {
					DB.connect();
					DB.bookupdate(registerinfo[0], registerinfo[1], registerinfo[2], registerinfo[3], registerinfo[4], registerinfo[5], PID);
					DB.close();
					rframe.dispose();
					setbookTable();
					JOptionPane.showMessageDialog(bmframe, "登録完了しました。");
				}
			} catch (UnsupportedEncodingException | SQLException e1) {
				e1.printStackTrace();
			}

		}
	}
}
