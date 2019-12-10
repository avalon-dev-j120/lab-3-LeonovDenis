package ru.avalon.java.dev.j120.labs.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Chooser extends JFrame {

    static final Toolkit TOOL = Toolkit.getDefaultToolkit(); //Получаем набор инструментов
    static final Dimension SCREENSIZE = TOOL.getScreenSize();  //Получаем размерность экрана
    private final int SIZEWIDTH = 441;    // Задаём размерность окна по Х
    private final int SIZEHEIGTH = 248;  //  Задаем размерность окна по У
    private final int LOCX = (SCREENSIZE.width - SIZEWIDTH) / 2;  // Получаем координату нашего окна по оси Х
    private final int LOCY = (SCREENSIZE.height - SIZEHEIGTH) / 2;  // Получаем координату нашего окна по оси Y
    private final Font FONT = new Font("По умолчанию", Font.ITALIC, 18); // Задаем шрифт кнопок

    public Chooser() {
        super("Выбирете программу"); //Вызываем родителя с названием окна
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //Устанавлинаем реагирование на крестик
        setBounds(LOCX, LOCY, SIZEWIDTH, SIZEHEIGTH); //Нстанавливаем положение  и  размер окна

        JPanel pano = new JPanel(); //Создаем бозовую панель
        LayoutManager lm = new FlowLayout(); //Создаем менеджер компановки
        pano.setLayout(lm);//Устанавливаем менеджер компановки в базовую панель

        JButton buttonColorP = new JButton("Цветовая палитра"); //Создаем кнопку для цветовой палитры
        JButton buttonCalc = new JButton("Канкулятор"); //Создаем кнопку для канкулятора

        /**
         * Устанавливаем предпочтительный размер и шрифт кнопкам
         */
        for (JButton button : new JButton[]{buttonColorP, buttonCalc}) {
            button.setPreferredSize(new Dimension(200, 200));
            button.setFont(FONT);
            button.setFocusable(false);
        }

        buttonColorP.setBackground(Color.YELLOW); //Устанавливаем цвет кнопки
        buttonCalc.setBackground(Color.cyan);

        buttonColorP.addActionListener(this::openColorP);//Добавляем кнопке прослушивателя событий
        buttonCalc.addActionListener(this::openCalc);// 

        pano.add(buttonColorP); //Добавляем кнопку в базовую панель
        Component strut = Box.createHorizontalStrut(5); //Создаем проставку на 5 пи
        pano.add(strut);//Добавляем разделитель 
        pano.add(buttonCalc);

        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5); //Создаем пустую рамку на 5 пи
        pano.setBorder(emptyBorder); //Добавляем рамку
        setContentPane(pano); //Устанавливаем нашу базовую панель в верхний слой окна
        setResizable(false); //Запрещаем изменение окна
    }

    /**
     * Метод заменяющий метод actionPerformed абстрактного класса ActionListener
     * Создает дополнительное окно {
     *
     * @see ColorPicker#ColorPicker()} Текущее окно делает невидимым, а новое
     * видимым
     * @param ae не используется
     */
    private void openColorP(ActionEvent ae) {
        JFrame colorP = new ColorPicker();
        setVisible(false);
        colorP.setVisible(true);
    }

    /**
     * Метод заменяющий метод actionPerformed абстрактного класса ActionListener
     * Создает дополнительное окно {
     *
     * @see Calc#Calc()} Текущее окно делает невидимым, а новое видимым
     * @param ae не используется
     */
    private void openCalc(ActionEvent ae) {
        JFrame calc = new Calc();
        setVisible(false);
        calc.setVisible(true);
    }
}
