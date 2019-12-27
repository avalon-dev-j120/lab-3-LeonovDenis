package ru.avalon.java.dev.j120.labs.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Calc extends JFrame {

    private final int SIDEWIDTH = 308;    // Задаём размерность окна по Х
    private final int SIDEHEIGTH = 474;  //  Задаем размерность окна по У
    private final Dimension dimension = new Dimension(60, 60);//Задаем размер кнопок
    private final int MAXWIDTH = 508;    // Задаём макс размерность окна по Х
    private final int MAXHEIGTH = 674;  //  Задаем макс размерность окна по У

    private final JButton[] buttons = new JButton[16]; //создаём масств для будущих кнопок
    private final String[] textButton = new String[]{"7", "8", "9", "+",
        "4", "5", "6", "-",
        "1", "2", "3", "*",
        "CE", "0", ".", "/"};//создаем массив обозначений кнопок

    private final JLabel rez = new JLabel("0");//область вывода результата
    private final JButton ravno = new JButton("=");//кнопка "равно"
    private final Font FONT = new Font("По умолчанию", Font.ITALIC, 24); // Задаем шрифт кнопок
    private final Font BIGFONT = new Font("По умолчанию", Font.BOLD, 40);
    private JPanel pano = new JPanel(); //Создаем бозовую панель
    private final JPanel up = new JPanel();//верхняя панель
    private final JPanel center = new JPanel();//центральная панель
    private final JPanel down = new JPanel();//нижняя панель

    private static final Clipboard CLIPBOARD = ColorPicker.CLIPBOARD;//получаем экземпляр буффера обмена    
    private StringSelection selection; //объект для передачи текста в буфере

    private String firstWord = "", action = "", secondWord = ""; //инициализируем слова для работы 
    private String command = ""; //инициализируем команду от кнопки
    private final Pattern pattern = Pattern.compile("\\.0*$");//иницмализируем шаблон отбрасывания точки и нулей в хвосте
    private Matcher matcher; // объявляем поисковик
    private boolean isRezalt = false;//последнее действие результат?

    public Calc() {
        super("Калькулятор");
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Устанавлинаем реагирование на крестик

        LayoutManager lm = new BorderLayout(); //Создаем менеджер компановки
        pano.setLayout(lm);//Устанавливаем менеджер компановки в базовую панель

        rez.setFont(BIGFONT);//устанавлеваем шрифт
        rez.setHorizontalAlignment(SwingConstants.RIGHT);//выравнивание текста по правому краю
        LineBorder border = new LineBorder(Color.red, 1);//создаём рамку с линией
        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);//делаем пустую рамку 5п
        CompoundBorder compound = new CompoundBorder(border, emptyBorder);//объединяем две рамки

        rez.setBorder(compound);//добавляем рамки

        up.setLayout(new BorderLayout());//Устанавливаем менеджер компановки в верхнюю панель
        up.add(rez);//добавляем надпись в верхнюю панель
        up.setBorder(new EmptyBorder(15, 0, 10, 0));//добавляем рамку панели 5 п
        pano.add(up, BorderLayout.NORTH);//добавляем панель в верхушку окна

        ravno.setFont(BIGFONT);//устанавлеваем шрифт
        ravno.setFocusable(false);// убираем рамку фокуса

        emptyBorder = new EmptyBorder(10, 0, 0, 0);//создаем пустую рамку 10 пт
        ravno.setBorder(border);//добавляем рамку
        ravno.addActionListener(this::pressButton);//добавляем слушателя

        down.setLayout(new BorderLayout());//Устанавливаем менеджер компановки в нижнюю панель
        down.add(ravno);//добавляем кнопку на нижнюю панель
        down.setBorder(emptyBorder); //устанавливаем рамки
        pano.add(down, BorderLayout.SOUTH);//добавляем панель с кнопкой "равно" вниз окна

        LayoutManager gridLayout = new GridLayout(4, 4, 10, 10); //создаем табличный менеджер расположения 4*4 клетуи с отступами по 10п
        center.setLayout(gridLayout);//устанавливаем менеджер в центральную панель

        /**
         * Цикл по кнопкам с установкой параметров и инициализацией
         */
        for (int i = 0; i < 16; i++) {

            buttons[i] = new JButton(textButton[i]);//создаем кнопку
            buttons[i].setFont(FONT);//задаем шрифт
            buttons[i].setPreferredSize(dimension);//устанавливаем приоритетные размеры
            buttons[i].setBorder(border);//добавляем пустую рамку
            buttons[i].setAlignmentX(JComponent.CENTER_ALIGNMENT);//задаем выравнивание надписи по Х
            buttons[i].setAlignmentY(JComponent.CENTER_ALIGNMENT);//задаем выравнивание надписи по У
            buttons[i].setFocusable(false);//отключаем фокус рамки кнопки

            buttons[i].addActionListener(this::pressButton);//добавляем слушателя
            center.add(buttons[i]);//добавляем кнопку центральную напанель
        }

        pano.add(center);//добавляем центральную панель 

        emptyBorder = new EmptyBorder(0, 10, 10, 10);//создаем пустую рамку
        compound = new CompoundBorder(border, emptyBorder);//объединяем две рамки
        pano.setBorder(compound);//добавляем рамки в основное полотно

        setContentPane(pano);// устанавливаем базовую панель в верхний слой окна

        setMaximumSize(new Dimension(MAXWIDTH, MAXHEIGTH));//установка максимального размера окна
        setMinimumSize(new Dimension(SIDEWIDTH, SIDEHEIGTH));//установка минимального размера окна

        setKEYboard(); //добавляем клавиатуру к кнопкам     
        setLocationRelativeTo(null);//установка окна в центре экрана

    }

    /**
     * переоределяем метод для ограничения максимального размера окна
     *
     * @param g графический компанент
     */
    @Override
    public void paint(Graphics g) {
        Dimension size = getSize();//получаем размеры окна
        int width = size.width;
        int height = size.height;
        /**
         * если текущие размеры больше максимальных то перерировываем окно в
         * максимальный размер
         */
        if (width > MAXWIDTH || height > MAXHEIGTH) {
            Point location = getLocation();//получаем координаты окна
            size.width = Math.min(MAXWIDTH, size.width); //устанавливаем размерность окна
            size.height = Math.min(MAXHEIGTH, size.height);
            setVisible(false);//скрываем окно, чтобы небыло мерцаний
            setSize(size);//задаем размер окна
            if (location.x <= 0 && location.y <= 0) {
                setLocationRelativeTo(null);//установка окна в центре экрана
            } else {
                setLocation(location);//задаем позицию окна
            }
            setVisible(true);//делаем окно видимым
        }
        super.paint(g); //возвращаем стандартный метод
    }

    /**
     * Логика работы кнопок в ответ на нажатие
     *
     * @param ae - не используем
     */
    private void pressButton(ActionEvent ae) {
        command = ae.getActionCommand();//получаем имя нажатой кнопки
        /**
         * Действие при получении ошибки деления на ноль
         */
        if (firstWord.equals("Error")) {
            firstWord = "";
        }

        switch (command) {
            case ("0"):
                isRezalt();
                pressNull();
                break;
            case ("="):
                if (!secondWord.isEmpty()) {
                    firstWord = math(firstWord, secondWord, action);//получить результат
                    round();//откинуть квост
                    action = "";//сбросить слова
                    secondWord = "";
                    copyTobuffer();//скопировать рузультат в буффер
                    isRezalt = true;
                }
                break;
            case ("CE"):
                if (action.isEmpty()) {
                    firstWord = "";
                } else if (secondWord.isEmpty()) {
                    action = "";
                }
                secondWord = "";
                break;
            case ("."):
                isRezalt();
                pressPoint();
                break;
            case ("+"):
            case ("-"):
            case ("*"):
            case ("/"):
                if (!firstWord.isEmpty()) {
                    isRezalt = false;
                    action = command;//задать слово необходимого действия

                    if (!secondWord.isEmpty()) {
                        action = command;//задать слово необходимого действия
                        firstWord = math(firstWord, secondWord, action);//получить результат
                        round();//откинуть квост
                        secondWord = "";
                        copyTobuffer();//скопировать рузультат в буффер
                        isRezalt = true;
                    }
                    round();//откинуть хвост 
                }
                break;
            default:

                if (action.isEmpty()) {
                    firstWord += command;
                } else {
                    secondWord += command;

                }
                break;
        }
        print();//отображаем результат
    }

    /**
     * копирование в буффер результата
     */
    private void copyTobuffer() {
        selection = new StringSelection(firstWord);
        CLIPBOARD.setContents(selection, selection);//копируем в буффер текст
    }

    /**
     * сброс значения после результата
     */
    private void isRezalt() {
        if (isRezalt) {
            firstWord = "";
            isRezalt = false;
        }
    }

    /**
     * отработка нажатия точки с проверкой на корректность
     */
    private void pressPoint() {
        if (action.isEmpty()) {
            if (firstWord.isEmpty()) {
                firstWord = "0.";
            } else if (!firstWord.contains(".")) {
                firstWord += ".";
            }
        } else {
            if (secondWord.isEmpty()) {
                secondWord = "0.";
            } else if (!secondWord.contains(".")) {
                secondWord += ".";
            }
        }
    }

    /**
     * отработка нажатия на ноль с проверкой корректности
     */
    private void pressNull() {
        if (action.isEmpty()) {
            if (!firstWord.isEmpty()) {
                firstWord += "0";
            }
        } else {
            if (!secondWord.equals("0")) {
                secondWord += "0";
            }
        }
    }

    /**
     * обработка результата
     *
     * @param aa - первое число
     * @param bb - второе число
     * @param command - действие над числами
     * @return - результат или "Error" при делении на ноль
     */
    private String math(String aa, String bb, String command) {
        String text = "";
        double a, b, c = 0;
        a = Double.parseDouble(aa);
        b = Double.parseDouble(bb);
        switch (command) {
            case ("*"):
                c = a * b;
                break;
            case ("/"):
                if (b != 0) {
                    c = a / b;
                } else {
                    return "Error";
                }
                break;
            case ("+"):
                c = a + b;
                break;
            case ("-"):
                c = a - b;
                break;
        }
        text = String.format("%.5f", c);//округляем результат до 5 зн после запятой
        text = text.replaceAll(",", ".");//меняем запятые на точки
        return text;
    }

    /**
     * вывод текста в метку
     */
    private void print() {
        if (firstWord.isEmpty()) {
            rez.setText("0");
        } else {
            rez.setText(firstWord + action + secondWord);
        }
    }

    /**
     * поиск в первом слове совпадения с шаблоном и откидывание точки или нулей
     * с хвоста
     */
    private void round() {
        matcher = pattern.matcher(firstWord);
        while (matcher.find()) {
            firstWord = firstWord.substring(0, matcher.start());
        }

        while (firstWord.contains(".") && firstWord.endsWith("0")) {
            firstWord = firstWord.substring(0, firstWord.length() - 1);
        }
    }

    /**
     * Подключаем клавиатуру к кнопкам
     */
    private void setKEYboard() {

        JComponent root = getRootPane();//получаем экземплляр корневой панели
        /**
         * пробегаемся по массиву кнопок создаем событие с объектом и командой
         * получаем из корневой панели карту кнопок и кладем туда символ кнопки
         * и наименование команды получаем из корневой ранели карту действий и
         * кладем туда наименование команды и само действие - абстрактный класс
         * в обстрактном классе вызываем функцию с нужным событием
         *
         * i=12 кнопка "CE"
         */
        for (int i = 0; i < buttons.length; i++) {

            String com = buttons[i].getActionCommand();
            char comCh = 0;
            if (i != 12) {
                comCh = com.charAt(0);
            } else {
                comCh = 127;
            }
            ActionEvent actionEvent = new ActionEvent(buttons[i], i, com);
            root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(comCh), "Button #" + i);
            root.getActionMap().put("Button #" + i, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pressButton(actionEvent);
                }
            });
        }

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) 10), "ravno");
        root.getActionMap().put("ravno", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressButton(new ActionEvent(ravno, 21, "="));
            }
        });

    }
}
