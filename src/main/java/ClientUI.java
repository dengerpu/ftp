package main.java;

import main.client.FtpClient;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;


public class ClientUI {

    // 表格上的title
    static String[] columnNames = new String[] { "名称", "修改日期", "类型", "大小" };
    // 表格中的内容，是一个二维数组
    static String[][] files = FtpClient.files;
    static String[][] newfile = new String[10][100];

    static JTable t = new JTable(newfile,columnNames);

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

        final JFrame f = new JFrame("FTP客户端");
        f.setSize(400, 340);
        f.setLocation(200, 200);
        f.setIconImage((new ImageIcon("src//img//ftp.png").getImage()));   //修改窗体图标
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        t.getSelectionModel().setSelectionInterval(0, 0);

        JPanel pOperation = new JPanel();

        JButton bAdd = new JButton("上传");
        JButton bDown = new JButton("下载");
        JButton bHelp = new JButton("帮助");
        pOperation.add(bAdd);
        pOperation.add(bDown);
        pOperation.add(bHelp);

        JPanel pPage = new JPanel();

        pPage.add(bFirst);
        pPage.add(bPre);
        pPage.add(cb);

        pPage.add(bNext);
        pPage.add(bLast);
        //帮助点击事件
        bHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "暂无帮助信息，如有问题请联系管理员！","消息提示",JOptionPane.OK_CANCEL_OPTION);
            }
        });
       // 上传操作
        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FtpClient ftpClient = new FtpClient();
                JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setDialogTitle("选择文件上传到Ftp服务器");
                int returnVal = chooser.showDialog(null, "上传");        //是否打开文件选择框
                File file = chooser.getSelectedFile();
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile().isFile()) {
                        ftpClient.UpLoadFun(file.getAbsolutePath());
                    }
                }
                // 更新table
                start = 0;
                updateTable();
                updateButtonStatus();
            }
        });
        //下载操作
        bDown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 判断是否选中
                int index = t.getSelectedRow();
                if (-1 == index) {
                    JOptionPane.showMessageDialog(f, "请选择需要下载的内容");
                    return;
                }
                System.out.println("索引"+index);
                FtpClient ftpClient = new FtpClient();
                JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());             //设置选择器
                chooser.setDialogTitle("选择文件目录保存文件 ");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal =  chooser.showSaveDialog(null);        //是否打开文件选择框
                File file = chooser.getSelectedFile();
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile().isDirectory()) {
                        String savePath =  file.getAbsolutePath()+ File.separator + newfile[index][0];
                        ftpClient.DownFun(newfile[index][0],savePath);
                    }

                }
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

      // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                new FtpClient().TrashFun();  //释放资源
                System.exit(0);
            }
        });

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

    //更新表格中的内容
    public static void updateTable() {

        int st = start;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                newfile[i][j] = files[st][j];
            }
            st++;
        }
        t.updateUI();
        t.getSelectionModel().setSelectionInterval(0, 0);
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
