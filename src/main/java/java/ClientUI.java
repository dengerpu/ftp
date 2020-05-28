package main.java.java;


        import main.java.client.FtpClient;

        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;

        import javax.swing.JButton;
        import javax.swing.JComboBox;
        import javax.swing.JDialog;
        import javax.swing.JFrame;
        import javax.swing.JLabel;
        import javax.swing.JOptionPane;
        import javax.swing.JPanel;
        import javax.swing.JScrollPane;
        import javax.swing.JTable;
        import javax.swing.JTextField;
        import javax.swing.ListSelectionModel;


public class ClientUI {

  //  static HeroTableModel htm = new HeroTableModel();
    // 表格上的title
    static String[] columnNames = new String[] { "名称", "修改日期", "类型", "大小" };
    // 表格中的内容，是一个二维数组
    //static String[][] heros = new String[][] { { "1", "盖伦", "616", "100" },
          //  { "2", "提莫", "512", "102" }, { "3", "奎因", "832", "200" } };


    static String[][] files = FtpClient.files;

    static String[][] file = new String[10][100];


    static JTable t = new JTable(file,columnNames);

    // 把分页按钮放在这里，后面监听器好访问
    static JButton bFirst = new JButton("首页");
    static JButton bPre = new JButton("上一页");
    static JButton bNext = new JButton("下一页");
    static JButton bLast = new JButton("末页");
    static JComboBox<Integer> cb = new JComboBox<>();

    static int number = 10;// 每页显示11个
    static int start = 0;// 开始的页码
    private static boolean cbListenerEnabled = true;

    public  ClientUI() {

        final JFrame f = new JFrame("LoL");
        f.setSize(400, 340);
        f.setLocation(200, 200);

        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        t.getSelectionModel().setSelectionInterval(0, 0);

        JPanel pOperation = new JPanel();

        JButton bAdd = new JButton("上传");
        JButton bDelete = new JButton("下载");
        JButton bEdit = new JButton("帮助");
        pOperation.add(bAdd);
        pOperation.add(bDelete);
        pOperation.add(bEdit);

        JPanel pPage = new JPanel();

        pPage.add(bFirst);
        pPage.add(bPre);
        pPage.add(cb);

        pPage.add(bNext);
        pPage.add(bLast);

        bEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 判断是否选中
                int index = t.getSelectedRow();
                if (-1 == index) {
                    JOptionPane.showMessageDialog(f, "编辑前需要先选中一行");
                    return;
                }

                // 获取选中的对象
               // Hero hero = htm.heros.get(index);

                // 显示编辑Dialog

                EditDialog ed = new EditDialog(f);
              //  ed.tfName.setText(hero.name);
              //  ed.tfHp.setText(String.valueOf((int) hero.hp));

                ed.setVisible(true);

            }
        });

        bAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new AddDialog(f).setVisible(true);

                updateButtonStatus();
            }
        });
        bDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // 判断是否选中
                int index = t.getSelectedRow();
                if (-1 == index) {
                    JOptionPane.showMessageDialog(f, "删除前需要先选中一行");
                    return;
                }

                // 进行确认是否要删除
                if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(f, "确认要删除？"))
                    return;

                // 获取id
//                Hero hero = htm.heros.get(index);
//                int id = hero.id;



                // 更新table
                start = 0;
                updateTable();
                updateButtonStatus();

            }
        });

        addPageListener();

        cb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!cbListenerEnabled)
                    return;

                int currentPage = (int) cb.getSelectedItem();
                start = (currentPage-1)*number;
                updateTable();
                updateButtonStatus();

            }
        });

        JScrollPane sp = new JScrollPane(t);

        f.setLayout(null);
        sp.setBounds(0, 0, 394, 200);
        pOperation.setBounds(0, 200, 394, 50);
        pPage.setBounds(0, 250, 394, 200);
        f.add(sp);
        f.add(pOperation);
        f.add(pPage);
        updateButtonStatus();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setVisible(true);
    }

    private static void addPageListener() {

        bFirst.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                start = 0;
                updateTable();
                updateButtonStatus();
            }
        });
        bPre.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                start -= number;
                updateTable();
                updateButtonStatus();
            }
        });
        bNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                start += number;
                updateTable();
                updateButtonStatus();
            }
        });
        bLast.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                start = last();

                updateTable();

                updateButtonStatus();

            }

        });

    }

    private static void updateButtonStatus() {
        int last = last();

        // 是否有上一页
        if (0 != start) {
            bFirst.setEnabled(true);
            bPre.setEnabled(true);
        }

        // 是否是第一页
        if (0 == start) {
            bFirst.setEnabled(false);
            bPre.setEnabled(false);
        }

        // 是否是最后一页
        if (start == last) {
            bLast.setEnabled(false);
            bNext.setEnabled(false);
        }
        // 是否有下一页
        if (start < last) {
            bLast.setEnabled(true);
            bNext.setEnabled(true);
        }

        //总共的页数
        int pageNumber =last/number+1;
        cbListenerEnabled = false;
        cb.removeAllItems();

        for (int i = 0; i < pageNumber; i++) {

            cb.addItem(i+1);
        }
        cbListenerEnabled = true;

        int currentPage = start/number +1;
        cb.setSelectedItem(currentPage);

    }

    static class AddDialog extends JDialog {
        JLabel lName = new JLabel("名称");
        JLabel lHp = new JLabel("血量");

        JTextField tfName = new JTextField();
        JTextField tfHp = new JTextField();

        JButton bSubmit = new JButton("提交");

        AddDialog(JFrame f) {
            super(f);
            this.setModal(true);
            int gap = 50;
            this.setLayout(null);

            JPanel pInput = new JPanel();
            JPanel pSubmit = new JPanel();

            pInput.setLayout(new GridLayout(2, 2, gap, gap));
            pInput.add(lName);
            pInput.add(tfName);
            pInput.add(lHp);
            pInput.add(tfHp);

            pSubmit.add(bSubmit);

            pInput.setBounds(50, 20, 200, 100);
            pSubmit.setBounds(0, 130, 300, 150);

            this.add(pInput);
            this.add(pSubmit);

            this.setSize(300, 200);
            this.setLocationRelativeTo(f);
            bSubmit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

//                    if (checkEmpty(tfName, "名称")) {
//                        if (checkNumber(tfHp, "hp")) {
//
//                            String name = tfName.getText();
//                            int hp = Integer.parseInt(tfHp.getText());
//
////                            Hero h = new Hero();
////                            h.name = name;
////                            h.hp = hp;
//                            //上传操作
//
//                            JOptionPane.showMessageDialog(f, "提交成功 ");
//
//                            AddDialog.this.setVisible(false);
//                            start = 0;
//                            updateTable();
//                        }
//                    }
                    System.out.println("执行上传操作");
                }
            });

        }
    }

    //更新表格中的内容
    public static void updateTable() {

        int st = start;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                file[i][j] = files[st][j];
            }
            st++;
        }
        t.updateUI();
        t.getSelectionModel().setSelectionInterval(0, 0);
        System.out.println("更新表格内容");
    }

    private static boolean checkEmpty(JTextField tf, String msg) {
        String value = tf.getText();
        if (0 == value.length()) {
            JOptionPane.showMessageDialog(null, msg + " 不能为空");
            tf.grabFocus();
            return false;
        }
        return true;
    }

    private static boolean checkNumber(JTextField tf, String msg) {
        String value = tf.getText();
        if (0 == value.length()) {
            JOptionPane.showMessageDialog(null, msg + " 不能为空");
            tf.grabFocus();
            return false;
        }
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, msg + " 只能是整数");
            tf.grabFocus();
            return false;
        }

        return true;
    }

    static class EditDialog extends JDialog {
        JLabel lName = new JLabel("名称");
        JLabel lHp = new JLabel("血量");

        JTextField tfName = new JTextField();
        JTextField tfHp = new JTextField();

        JButton bSubmit = new JButton("提交");

        EditDialog(JFrame f) {
            super(f);
            this.setModal(true);
            int gap = 50;
            this.setLayout(null);

            JPanel pInput = new JPanel();
            JPanel pSubmit = new JPanel();

            pInput.setLayout(new GridLayout(2, 2, gap, gap));
            pInput.add(lName);
            pInput.add(tfName);
            pInput.add(lHp);
            pInput.add(tfHp);

            pSubmit.add(bSubmit);

            pInput.setBounds(50, 20, 200, 100);
            pSubmit.setBounds(0, 130, 300, 150);

            this.add(pInput);
            this.add(pSubmit);

            this.setSize(300, 200);
            this.setLocationRelativeTo(f);

            //编辑功能
           bSubmit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
//                    if (checkEmpty(tfName, "名称")) {
//                        if (checkNumber(tfHp, "hp")) {
//
//                            // 获取id
//                            int index = t.getSelectedRow();
//                            int id = htm.heros.get(index).id;
//
//                            String name = tfName.getText();
//                            int hp = Integer.parseInt(tfHp.getText());
//
//                            Hero h = new Hero();
//                            h.name = name;
//                            h.hp = hp;
//                            h.id = id;
//
//                            new HeroDAO().update(h);
//
//                            JOptionPane.showMessageDialog(f, "提交成功 ");
//
//                            EditDialog.this.setVisible(false);
//                            updateTable();
//                        }
//                    }
                    System.out.println("点击了编辑按钮");
                }
            });
       }
    }

    private static int last() {
        // 最后一页开始的位置
        int last;

        //int total = new HeroDAO().getTotal();     total代表总的数量
        int total = FtpClient.total;

        // 最后一页要看总是是否能够整除每页显示的数量number

        if (0 == total % number) {
            // 假设总数是20，那么最后一页开始的位置就是10
            last = total - number;
        } else {
            // 假设总数是21，那么最后一页开始的位置就是20
            last = total - total % number;
        }
        return last;

    }
}
