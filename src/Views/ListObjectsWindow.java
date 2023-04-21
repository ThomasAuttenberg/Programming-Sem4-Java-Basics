package Views;
import Models.Entities.Entity;
import Models.Entities.GoldFish;
import Models.Entities.Guppie;
import Models.WindowModel;


import java.util.ArrayList;

import java.lang.Exception;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;


public class ListObjectsWindow extends JFrame{
    private JButton button_close;
    private JList list;
    private String[] text;
    private JScrollPane scroll;
    private WindowModel model;
    private JPanel container;
    public ListObjectsWindow(WindowModel model){
        setTitle("List objects");
        setSize(400,300);
        setLocationRelativeTo(null);
        container = new JPanel();
        container.setVisible(true);
        container.setLayout(null);
        container.setBackground(Color.darkGray);
        setResizable(false);
        Init_button_close();
        add(container);
        this.model = model;
        LoadInfo(model.getHabitat().getEntities(), model.getHabitat().getBornTime());
        Init_list();
    }

    public void LoadInfo(LinkedList<Entity> objects, TreeMap<Integer, Long> times){
        text = new String[objects.size()];
        int i = -1;
        for(Entity ent : objects){
            i++;
            int entityID = ent.getId();
            String line = "";
            line += "ID: " + entityID;
            line += "\t";
            line += " | Type: ";
            if(ent.getClass() == GoldFish.class) {
                line += "Goldfish";
            }
            if(ent.getClass() == Guppie.class){
                line += "Guppie";
            }
            line += "\t";
            line += " | Generated on: " + times.get(entityID)/1000 + " sec"; // добавить время
            text[i] = line;
            System.out.println(line);
        }
    }
    public void Show(){
        setVisible(true);
    }
    private void Init_button_close(){
        Font font = new Font("Verdana", Font.BOLD, 9);
        Color textColor = new Color(217, 217, 217);
        this.button_close = new JButton("Close");
        button_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        button_close.setFont(font);
        button_close.setEnabled(true);
        button_close.setOpaque(true);
        button_close.setBounds(150,220,90,30);
        button_close.setBackground(Color.gray);
        button_close.setForeground(textColor);
        container.add(button_close);
    }

    private void Init_list(){
        list = new JList<String>();
        list.setListData(text);
        scroll = new JScrollPane(list);
        //scroll.setPreferredSize(new Dimension(300,100));
        scroll.setBounds(10,10,360,190);
        container.add(scroll);
    }




}
