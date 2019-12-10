package ru.avalon.java.dev.j120.labs.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;

public class ColorPicker extends JFrame {

    private final int SIDEWIDTH = 426;    // Задаём размерность окна по Х
    private final int SIDEHEIGTH = 209;  //  Задаем размерность окна по У
    private final int LOCX = (Chooser.SCREENSIZE.width - SIDEWIDTH) / 2;  // Получаем координату нашего окна по оси Х
    private final int LOCY = (Chooser.SCREENSIZE.height - SIDEHEIGTH) / 2;  // Получаем координату нашего окна по оси Y
    private Color color = new Color(125, 125, 125); //цвет по умолчанию

    private String toolTip = "#7d7d7d"; //Текст подсказки для вывода цвета по умолчанию 125-125-125

    static final Toolkit TOOL = Chooser.TOOL;
    static final Clipboard CLIPBOARD = TOOL.getSystemClipboard(); //получаем экземпляр буффера обмена    
    private StringSelection selection = new StringSelection(toolTip); //объект для передачи текста в буфере

    private JPanel holst = new JPanel(); //создаем кнопку(в виде панели)
    private JSlider[] sliders = new JSlider[3]; //создаем массив слайдеров

    private GridBagConstraints gbc;//объявляем описание копманента

    public ColorPicker() {
        super("Цветовая палитра");

        setDefaultCloseOperation(EXIT_ON_CLOSE); //Устанавлинаем реагирование на крестик
        setBounds(LOCX, LOCY, SIDEWIDTH, SIDEHEIGTH); //Нстанавливаем положение  и  размер окна

        JPanel pano = new JPanel(); //Создаем бозовую панель
        LayoutManager lm = new GridBagLayout(); //Создаем менеджер компановки
        pano.setLayout(lm);//Устанавливаем менеджер компановки в базовую панель

        gbc = new GridBagConstraints(); //создаем описание компанента
        gbc.gridx = 0; //номер ячейки по Х
        gbc.gridy = 0; //номер ячейки по У
        gbc.fill = GridBagConstraints.BOTH; //растягивание компонента во все стороны, если есть свободное место
        gbc.gridheight = 3; // заполнение компонентом 3 ячеек по вертикали
        gbc.anchor = GridBagConstraints.WEST; //выравнивание компанента в ячейке
        gbc.insets = new Insets(5, 5, 5, 15); //задаем отступы вокруг ячейки
        gbc.ipadx = 100;//задаем размеры ячейки по осям
        gbc.ipady = 100;
        gbc.weightx = 1.0f;//задаём процент заполнения свободного пространства компонентом по осям
        gbc.weighty = 1.0f;

        holst.setToolTipText(toolTip);//добавляем подсказку
        holst.setBackground(color);//устанавливаем цвет фона кнопки

        pano.add(holst, gbc);//добавляем кнопку на общую панель

        CLIPBOARD.setContents(selection, selection);//копируем в буффер текс цвета

        gbc.gridx = 2;// работаем в 3-м столбце
        gbc.gridheight = 1; // заполнение компонентом 1 ячейки по вертикали
        gbc.ipadx = 0;//задаем размеры ячейки по осям
        gbc.ipady = 0;
        gbc.insets.right = 5; //задаем отступ справа

        /**
         * создаем три слайдера, настраиваем и добавляем в общую панель
         */
        for (int i = 0; i < 3; i++) {
            gbc.gridy = i;

            sliders[i] = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 125);//создаем горизонтальный слайдер 0-125-255
            sliders[i].setPaintTicks(true); //включаем отображение делений
            sliders[i].setPaintLabels(true); //включаем отображение делений
            sliders[i].setMajorTickSpacing(255); //шаг больших делений
            sliders[i].setMajorTickSpacing(20);//шаг маленьких делений
            sliders[i].addChangeListener(this::changeColor);
            pano.add(sliders[i], gbc);// добавляем слайдер на общую панель
        }

        gbc = new GridBagConstraints(); //создаем описание компанента
        gbc.gridx = 1; //номер ячейки по Х
        gbc.gridy = 0; //номер ячейки по У
        gbc.anchor = GridBagConstraints.WEST; //выравнивание компанента в ячейке
        gbc.insets = new Insets(5, 0, 5, 0); //задаем отступы вокруг ячейки

        /**
         * создаём и добавляем 3 кнопки на общую панель
         */
        for (String text : new String[]{"Red:", "Green", "Blue"}) {

            pano.add(new JLabel(text), gbc);//добавляем надпись на общую панель
            gbc.gridy++; //увеличиваем номер ячейки по У
        }
        EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);//создаем пустую рамку
        LineBorder lineBorder = new LineBorder(Color.RED, 1);//создаем рамку с линией
        CompoundBorder compoundBorder = new CompoundBorder(emptyBorder, lineBorder);//объединяем две рамки
        pano.setBorder(compoundBorder);//добавляем рамки в основное полотно

        setContentPane(pano); //Устанавливаем нашу базовую панель в верхний слой окна   
        setMinimumSize(new Dimension(SIDEWIDTH, SIDEHEIGTH)); // Устанавливаем минимальный размер окна
    }

    /**
     * обрабатываем событие изменения цвета
     *
     * @param ce - не используем
     */
    private void changeColor(ChangeEvent ce) {
        color = new Color(sliders[0].getValue(), sliders[1].getValue(), sliders[2].getValue());// создаем цвет на основе значений ползунков
        holst.setBackground(color); //устанавливаем цвет фона кнопки
        toolTip = "#" + Integer.toHexString(color.getRed()) + Integer.toHexString(color.getGreen())
                + Integer.toHexString(color.getBlue()); //Текст подсказки для вывода цвета
        holst.setToolTipText(toolTip);// меняем текст подсказки кнопки

        selection = new StringSelection(toolTip); //объект для передачи текста в буфере
        CLIPBOARD.setContents(selection, selection);//копируем в буффер текс цвета
    }
}
