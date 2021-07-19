package simulation.view;

import simulation.controller.SimulationController;
import simulation.support.DataInter;
import simulation.view.datatable.DataTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphicInterface extends JFrame
{
    public GraphicInterface()
    {
        setTitle("Bankers' Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = initialBackgroundPanel();
        mainPanel.add(initialRequestLabel(), new Constraints(0, 0, 1, 1));
        mainPanel.add(initialCommandTextField(), new Constraints(1, 0, 1, 1));
        mainPanel.add(initialRequestButton(), new Constraints(2, 0, 1, 1));
        mainPanel.add(initialDataTablePanel(), new Constraints(0, 1, 3, 2));
        mainPanel.add(new JScrollPane(initialInfoArea()), new Constraints(3, 1, 1, 2));
        initialPopupMenu();
        initialBackgroundTimer().start();
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(910, 605));
        setBounds(0, 0, 1200, 800);
        setVisible(true);
    }

    public void setController(SimulationController controller)
    {
        this.controller = controller;
    }

    public void setDataTable(DataTable dataTable)
    {
        dataTablePanel.setViewportView(dataTable);
    }

    public void printMessage(String message)
    {
        synchronized (infoAreaLock)
        {
            infoArea.append(message + '\n');
            infoArea.setCaretPosition(infoArea.getDocument().getLength());
        }
    }

    public void clearInfoArea()
    {
        synchronized (infoAreaLock)
        {
            infoArea.setText("Info Area:\n");
            infoArea.setCaretPosition(infoArea.getDocument().getLength());
        }
    }

    private BackgroundPanel initialBackgroundPanel()
    {
        if (backgroundImagePaths.length < 1) throw new IllegalArgumentException();
        BackgroundPanel mainPanel = new BackgroundPanel(new ImageIcon(backgroundImagePaths[0]).getImage());
        mainPanel.setLayout(new GridBagLayout());
        return mainPanel;
    }

    private Timer initialBackgroundTimer()
    {
        backgroundSwitchTimer = new Timer(60000, new ActionListener()
        {
            int imageIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (imageIndex == backgroundImagePaths.length) imageIndex = 0;
                mainPanel.setImage(new ImageIcon(backgroundImagePaths[imageIndex++]).getImage());
            }
        });
        return backgroundSwitchTimer;
    }

    private JLabel initialRequestLabel()
    {
        JLabel requestLabel = new JLabel("Command:");
        requestLabel.setFont(new Font("楷体", Font.BOLD, 16));
        return requestLabel;
    }

    private JTextField initialCommandTextField()
    {
        commandTextField = new JTextField();
        commandTextField.setColumns(20);
        commandTextField.setFont(new Font("楷体", Font.BOLD, 16));
        commandTextField.setHorizontalAlignment(JTextField.LEADING);
        commandTextField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    controller.acceptCommandText(commandTextField.getText());
                    commandTextField.setText("");
                }
            }
        });
        return commandTextField;
    }

    private JButton initialRequestButton()
    {
        JButton requestButton = new JButton("Input");
        requestButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.acceptCommandText(commandTextField.getText());
                commandTextField.setText("");
            }
        });
        return requestButton;
    }

    private JScrollPane initialDataTablePanel()
    {
        dataTablePanel = new JScrollPane(new DataTable());
        return dataTablePanel;
    }

    private JTextArea initialInfoArea()
    {
        infoArea = new JTextArea("Info Area:\n", 22, 30);
        infoArea.setLineWrap(true);
        infoArea.setFont(new Font("楷体", Font.BOLD, 16));
        infoArea.setForeground(Color.BLUE);
        infoArea.setEditable(false);
        return infoArea;
    }


    private JPopupMenu initialPopupMenu()
    {
        popupMenu = new JPopupMenu();
        popupMenu.add(initialOpenMenuItem());
        popupMenu.addSeparator();
        popupMenu.add(initialExitMenuItem());
        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                showPopupMenu(e);
            }

            public void mouseReleased(MouseEvent e)
            {
                showPopupMenu(e);
            }

            private void showPopupMenu(MouseEvent e)
            {
                if (e.isPopupTrigger()) popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        return popupMenu;
    }

    private JMenuItem initialOpenMenuItem()
    {
        JMenuItem item = new JMenuItem("打开(O)", KeyEvent.VK_O);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileChooser = new JFileChooser(".\\");
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                    controller.setDataFromInter(new DataInter(fileChooser.getSelectedFile()));
            }
        });
        return item;
    }

    private JMenuItem initialExitMenuItem()
    {
        JMenuItem item = new JMenuItem("退出(E)", KeyEvent.VK_E);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        item.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        return item;
    }


    private class Constraints extends GridBagConstraints
    {
        public Constraints(int gridx, int gridy, int gridwidth, int gridheight)
        {
            //this.fill = GridBagConstraints.BOTH;
            this.gridx = gridx;
            this.gridy = gridy;
            this.gridwidth = gridwidth;
            this.gridheight = gridheight;
        }
    }

    private final BackgroundPanel mainPanel;
    private Timer backgroundSwitchTimer;
    private final String[] backgroundImagePaths = {"img/0.png", "img/1.png", "img/2.png", "img/3.png"};
    private SimulationController controller;
    private JTextField commandTextField;
    private JTextArea infoArea;
    private JScrollPane dataTablePanel;
    private JPopupMenu popupMenu;
    private static final Object infoAreaLock = new Object();
}
