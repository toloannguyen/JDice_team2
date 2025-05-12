package com.mycompany.jdiceteam2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.util.logging.*;

/**
 * JDice - Giao diện người dùng đơn giản cho chương trình tung xúc xắc.
 * Hỗ trợ chọn loại xúc xắc, hiển thị kết quả và đọc cấu hình từ file.
 */
public class JDice {

    static final String CLEAR = "Clear";
    static final String ROLL = "Roll Selection";

    // Logger cho ghi log khi roll xúc xắc
    private static final Logger logger = Logger.getLogger(JDice.class.getName());

    /**
     * Hiển thị hộp thoại lỗi với nội dung thông báo.
     *
     * @param s thông báo lỗi
     */
    static void showError(String s) {
        JOptionPane.showMessageDialog(null, s, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private static class JDiceListener implements ActionListener {

        Vector<String> listItems;
        JList<String> resultList;
        JComboBox<String> inputBox;
        long lastEvent;

        /**
         * Khởi tạo listener cho sự kiện giao diện.
         *
         * @param resultList danh sách hiển thị kết quả
         * @param inputBox ô nhập chuỗi xúc xắc
         */
        public JDiceListener(JList<String> resultList, JComboBox<String> inputBox) {
            this.listItems = new Vector<>();
            this.resultList = resultList;
            this.inputBox = inputBox;
            lastEvent = 0;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getWhen() == lastEvent) {
                return;
            }
            lastEvent = e.getWhen();

            if (e.getSource() instanceof JComboBox || e.getActionCommand().equals(ROLL)) {
                String s = inputBox.getSelectedItem().toString();
                if (!s.contains("=")) {
                    showError("Định dạng không hợp lệ! Vui lòng nhập theo mẫu: tên=2d6+3");
                    return;
                }
                String[] arr = s.split("=");
                if (arr.length < 2) {
                    showError("Chuỗi đầu vào thiếu thông tin xúc xắc.");
                    return;
                }

                String name = "";
                for (int i = 0; i < arr.length - 2; i++) {
                    name += arr[i] + "=";
                }
                name += arr[arr.length - 2];

                String diceString = arr[arr.length - 1];

                doRoll(name, diceString);
            } else if (e.getActionCommand().equals(CLEAR)) {
                doClear();
            }
        }

        /**
         * Xóa toàn bộ kết quả trong danh sách.
         */
        private void doClear() {
            resultList.clearSelection();
            listItems.clear();
            resultList.setListData(listItems);
        }

        /**
         * Thực hiện tung xúc xắc với chuỗi nhập vào.
         *
         * @param name tên mô tả kết quả
         * @param diceString chuỗi định dạng xúc xắc
         */
        private void doRoll(String name, String diceString) {
            logger.log(Level.INFO, "Người dùng thực hiện roll với chuỗi: {0}", diceString);

            String prepend = "";
            int start = 0;

            Vector<DieRoll> v = DiceParser.parseRoll(diceString);
            if (v == null) {
                showError("Chuỗi xúc xắc không hợp lệ: " + diceString);
                return;
            }

            if (name != null && !name.isEmpty()) {
                listItems.add(0, name);
                start = 1;
                prepend = "  ";
            }

            int[] selectionIndices = new int[start + v.size()];
            for (int i = 0; i < v.size(); i++) {
                DieRoll dr = v.get(i);
                RollResult rr = dr.makeRoll();
                String toAdd = prepend + dr + "  =>  " + rr;
                listItems.add(i + start, toAdd);
            }

            for (int i = 0; i < selectionIndices.length; i++) {
                selectionIndices[i] = i;
            }

            resultList.setListData(listItems);
            resultList.setSelectedIndices(selectionIndices);
        }
    }

    /**
     * Điểm bắt đầu của chương trình.
     *
     * @param args nếu có tham số là tên file, sẽ đọc các dòng làm cấu hình chọn
     * sẵn.
     */
    public static void main(String[] args) {
        Vector<String> v = new Vector<>();
        if (args.length >= 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                String s;
                while ((s = br.readLine()) != null) {
                    v.add(s);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("Không thể đọc file: " + args[0]);
            }
        }

        JFrame jf = new JFrame("Dice Roller");
        Container c = jf.getContentPane();
        c.setLayout(new BorderLayout());

        JList<String> jl = new JList<>();
        c.add(new JScrollPane(jl), BorderLayout.CENTER);

        JComboBox<String> jcb = new JComboBox<>(v);
        jcb.setEditable(true);
        c.add(jcb, BorderLayout.NORTH);

        JDiceListener jdl = new JDiceListener(jl, jcb);
        jcb.addActionListener(jdl);

        JPanel rightSide = new JPanel();
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
        String[] buttons = {ROLL, "d4", "d6", "d8", "d10", "d12", "d20", "d100", CLEAR};

        for (String buttonLabel : buttons) {
            JButton newButton = new JButton(buttonLabel);
            rightSide.add(newButton);
            newButton.addActionListener(jdl);
        }

        c.add(rightSide, BorderLayout.EAST);

        jf.setSize(450, 500);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
