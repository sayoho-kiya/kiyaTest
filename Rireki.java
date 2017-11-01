package screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import action.DB;

public class Rireki implements ActionListener {
	JFrame rframe, lframe;
	JPanel rpnl, lpnl;
	JButton topbtn, lankpbtn, clbtn;
	JTable rtbl;
	JScrollPane sp;
	JLabel msg, st1, st2, st3, st4, st5, st6, lankborder1, lankborder2, lankborder11, lankborder12, lankborder21, lankborder22, lendtimetitle, lendtime1, lendtime2, poptitle, pop1, pop2;
	JLabel highevaltitle, he1, he2;
	Container rcon, lcon;

	//本日の日付取得
	Date today = new Date(); //java.util.Date型
	SimpleDateFormat DataFmt = new SimpleDateFormat("yyyy-MM-dd");
	String Today = DataFmt.format(today);

	public void rireki() throws SQLException {

		rframe = new JFrame("貸出/返却履歴");
		rframe.setBounds(0, 0, 1850, 950);
		rframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rframe.setVisible(true);

		rpnl = new JPanel();
		rpnl.setLayout(null);

		topbtn = new JButton("貸出登録へ");
		topbtn.setBounds(10, 10, 110, 40);
		topbtn.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		topbtn.addActionListener(this);
		rpnl.add(topbtn);

		lankpbtn = new JButton("ランキング発表");
		lankpbtn.setBounds(130, 10, 120, 40);
		lankpbtn.setFont(new Font("Meiryo UI", Font.PLAIN, 15));
		lankpbtn.addActionListener(this);
		rpnl.add(lankpbtn);

		msg = new JLabel("※ヘッダーの項目を選択すると並び替えを行います。");
		msg.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		msg.setBounds(10, 65, 600, 40);
		rpnl.add(msg);

		DB.connect();
		List<String[]> list = DB.getRireki();
		DB.close();

		String[] header = { "No.", "名前", "タイトル", "出版社", "著者", "ジャンル", "評価", "貸出日", "返却予定", "返却日" };
		DefaultTableModel model = new DefaultTableModel(list.toArray(new String[0][0]), header);
		rtbl = new JTable(model) {

			@Override
			public Component prepareRenderer(TableCellRenderer tcr, int row, int column) {
				Component c = super.prepareRenderer(tcr, row, column);
				//返却期限過ぎても返却されていない場合に色づけ
				if((getValueAt(row, 8).toString().compareTo(Today) <= 0 && column == 8)) {

					c.setBackground(Color.pink);

					if(((getValueAt(row, 6).toString()).equals("★")
							|| (getValueAt(row, 6).toString()).equals("★★")
							|| (getValueAt(row, 6).toString()).equals("★★★")
							|| (getValueAt(row, 6).toString()).equals("★★★★")
							|| (getValueAt(row, 6).toString()).equals("★★★★★"))) {
						c.setBackground(getBackground());

					}

				} else {

					c.setBackground(getBackground());
				}

				return c;

			}

		};

		rtbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//複数行選択できないように設定
		rtbl.setBounds(10, 30, 1750, 750);
		rtbl.setFont(new Font("Meiryo UI", Font.PLAIN, 18));
		rtbl.setRowHeight(28);

		JTableHeader jh = rtbl.getTableHeader();
		jh.setFont(new Font("Meiryo UI", Font.PLAIN, 18));

		//列の幅を調整する
		rtbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		rtbl.setAutoCreateRowSorter(true);
		rtbl.getColumnModel().getColumn(0).setPreferredWidth(90);
		rtbl.getColumnModel().getColumn(1).setPreferredWidth(170);
		rtbl.getColumnModel().getColumn(2).setPreferredWidth(370);
		rtbl.getColumnModel().getColumn(3).setPreferredWidth(200);
		rtbl.getColumnModel().getColumn(4).setPreferredWidth(200);
		rtbl.getColumnModel().getColumn(5).setPreferredWidth(200);
		rtbl.getColumnModel().getColumn(6).setPreferredWidth(130);
		rtbl.getColumnModel().getColumn(7).setPreferredWidth(145);
		rtbl.getColumnModel().getColumn(8).setPreferredWidth(145);
		rtbl.getColumnModel().getColumn(9).setPreferredWidth(145);

		sp = new JScrollPane(rtbl);
		sp.setBounds(10, 100, 1800, 750);

		rcon = rframe.getContentPane();

		rcon.add(rpnl);
		rpnl.add(sp);

	}

	public void lankPrint() throws SQLException {

		lframe = new JFrame("ランキング");
		lframe.setBounds(50, 50, 650, 800);
		lframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lframe.setVisible(true);

		lcon = lframe.getContentPane();

		lpnl = new JPanel();
		lpnl.setLayout(null);
		lcon.add(lpnl);

		lankborder1 = new JLabel("-------------------------------------");
		lankborder1.setBounds(110, 10, 500, 40);
		lankborder1.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		lpnl.add(lankborder1);

		lankborder2 = new JLabel("-------------------------------------");
		lankborder2.setBounds(110, 70, 500, 40);
		lankborder2.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		lpnl.add(lankborder2);

		lankborder11 = new JLabel("-------------------------------------");
		lankborder11.setBounds(110, 230, 500, 40);
		lankborder11.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		lpnl.add(lankborder11);

		lankborder12 = new JLabel("-------------------------------------");
		lankborder12.setBounds(110, 290, 500, 40);
		lankborder12.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		lpnl.add(lankborder12);

		lankborder21 = new JLabel("-----------------------------------------");
		lankborder21.setBounds(100, 450, 500, 40);
		lankborder21.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		lpnl.add(lankborder21);

		lankborder22 = new JLabel("-----------------------------------------");
		lankborder22.setBounds(100, 510, 500, 40);
		lankborder22.setFont(new Font("Meiryo UI", Font.PLAIN, 25));
		lpnl.add(lankborder22);

		clbtn = new JButton("閉");
		clbtn.setBounds(530, 680, 50, 40);
		clbtn.setFont(new Font("Meiryo UI", Font.PLAIN, 15));

		clbtn.addActionListener(this);

		lpnl.add(clbtn);
		DB.connect();
		ArrayList<String> lendtime = DB.getLendbookRank();
		lendtimetitle = new JLabel("  借りた回数が多い人　TOP3");
		lendtimetitle.setBounds(150, 40, 500, 40);
		lendtimetitle.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		lpnl.add(lendtimetitle);

		st1 = new JLabel("●");
		st1.setBounds(130, 40, 40, 40);
		st1.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		st1.setForeground(Color.ORANGE);
		lpnl.add(st1);

		st2 = new JLabel("●");
		st2.setBounds(470, 40, 500, 40);
		st2.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		st2.setForeground(Color.ORANGE);
		lpnl.add(st2);

		for(int i = 0; i < 3; i++) {

			lendtime1 = new JLabel("【" + (i + 1) + "】　" + lendtime.get(0 + (i * 2)) + " さん");
			lendtime2 = new JLabel(lendtime.get(1 + (i * 2)) + "冊");
			lendtime1.setBounds(120, 100 + (i * 40), 700, 40);
			lendtime2.setBounds(470, 100 + (i * 40), 700, 40);

			lendtime1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
			lendtime2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
			lpnl.add(lendtime1);
			lpnl.add(lendtime2);

		}

		//多く借りられている本ランキング
		ArrayList<String> popbook = DB.getpopularbookRank();

		poptitle = new JLabel(" 読んでる人が多い本　TOP3");
		poptitle.setBounds(170, 260, 700, 40);
		poptitle.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		lpnl.add(poptitle);

		st3 = new JLabel("●");
		st3.setBounds(140, 260, 40, 40);
		st3.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		st3.setForeground(Color.MAGENTA);
		lpnl.add(st3);

		st4 = new JLabel("●");
		st4.setBounds(480, 260, 500, 40);
		st4.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		st4.setForeground(Color.MAGENTA);
		lpnl.add(st4);

		for(int j = 0; j < 3; j++) {

			pop1 = new JLabel("【" + (j + 1) + "】　" + popbook.get(j + 3));
			pop2 = new JLabel("    " + popbook.get(j) + "回");
			pop1.setBounds(80, 320 + (j * 40), 400, 40);
			pop2.setBounds(470, 320 + (j * 40), 400, 40);
			pop1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
			pop2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
			lpnl.add(pop1);
			lpnl.add(pop2);

		}

		//評価が高いランキング
		ArrayList<String> highevalbook = DB.gethighevalbook();
		DB.close();

		highevaltitle = new JLabel("読んだ人の評価が高い本　TOP3");
		highevaltitle.setBounds(150, 480, 700, 40);
		highevaltitle.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		lpnl.add(highevaltitle);

		st5 = new JLabel("●");
		st5.setBounds(115, 480, 40, 40);
		st5.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		st5.setForeground(Color.BLUE);
		lpnl.add(st5);

		st6 = new JLabel("●");
		st6.setBounds(500, 480, 500, 40);
		st6.setFont(new Font("Meiryo UI", Font.PLAIN, 26));
		st6.setForeground(Color.BLUE);
		lpnl.add(st6);

		for(int k = 0; k < 3; k++) {

			he1 = new JLabel("【" + (k + 1) + "】 " + highevalbook.get(0 + (k * 2)));
			he2 = new JLabel("    ★" + highevalbook.get(1 + (k * 2)));
			he1.setBounds(80, 540 + (k * 40), 500, 40);
			he2.setBounds(470, 540 + (k * 40), 700, 40);
			he1.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
			he2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
			lpnl.add(he1);
			lpnl.add(he2);

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == clbtn) {
			lframe.dispose();
		}
		if(e.getSource() == topbtn) {
			rframe.dispose();
			LendTop lendt = new LendTop();
			try {
				lendt.lendTop();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == lankpbtn) {
			try {
				lankPrint();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}

		}

	}
}
