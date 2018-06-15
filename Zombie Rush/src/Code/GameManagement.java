package Code;
import Code.bullet.Bullet;
import Code.entity.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

public class GameManagement extends JPanel implements ActionListener {
    private static GameManagement instance;

    private Timer timer;
    private Timer reloadTM;
    private Player player;
    private ArrayList<GameObject> currentObjects;
    private JLabel topPanel;
    private JLabel gameOver;
    private int score;
    private boolean status = false;
    private double weaponCD;
    private String playerName;
    private HashMap<String, Integer> highScoreTable;

    // начало игры
    private GameManagement() {
        try {
            File file = new File(System.getProperty("user.dir") + "/HighScore");
            if (!file.exists()) {
                highScoreTable = new HashMap<>();
            }
            else {
                FileInputStream fileIn = new FileInputStream("HighScore");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                highScoreTable = (HashMap<String, Integer>) in.readObject();
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        inputPlayerName();
        currentObjects = new ArrayList();
        timer = new Timer(20, this);
        reloadTM = new Timer(1000,new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                player.getWeapon().reload();
                reloadTM.stop();
            }
        });
        player = Player.getInstance();
        MakeInterface();
        startMenu();
    }
    // Singleton
    public synchronized static GameManagement getInstance() {
        if(instance == null) {
            instance = new GameManagement();
        }
        return instance;
    }
    // спавн игрока по центру
    private void RespawnPlayer() {
        player.setHP(10);
        player.moveTo(Game.width / 2 - 32, Game.height / 2 - 32);
        player.setVisible(true);
        for(int i=0 ; i < player.weapons.size();i++) {
            player.getWeapon().reload();
            player.nextWeapon();
        }
        player.weaponEq = 0;
    }

    // рандомный спавн противников
    private void EnemySpawn(int n) {
        for (int i = 0; i < n; i++) {
            Zombie enemy = new Zombie(player);
            int x = Game.width / 2;
            Random rand = new Random();

            while (x < (Game.width - enemy.getWidth()) && x > enemy.getWidth()) {
                x = rand.nextInt(Game.width);
            }
            int y = (int) (Math.random() * 600);

            enemy.moveTo(x, y);
            Rectangle zombieCol = enemy.getCol();
            for (int j = 0; j < currentObjects.size(); j++) {
                if (zombieCol.intersects(currentObjects.get(j).getCol()))
                    enemy.moveTo(x, y + getHeight() / 2);
            }
            currentObjects.add(enemy);
        }
    }
    // Отрисовка
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Bullet b : player.bullets) {
            if (b.isVisible()) {
                g2d.drawImage(b.getImage(), (int)b.getPos_x(), (int)b.getPos_y(), null);
            }
        }
        for (GameObject e : currentObjects) {
            if (e.isVisible())
                g2d.drawImage(e.getImage(), e.getAffineTransform(), this);
        }
        if (player.isVisible()) {
            g2d.drawImage(player.getImage(), player.getAffineTransform(), this);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    // Процесс игры
    public void actionPerformed(ActionEvent event) {
        for (int i = 0; i < currentObjects.size(); i++) {
            GameObject e = currentObjects.get(i);
            if (e.isVisible()) {
                e.move();
            } else {
                currentObjects.remove(i);
            }
        }
        for (int i = 0; i < player.bullets.size(); i++) {
            Bullet b = player.bullets.get(i);

            if (b.isVisible()) {
                b.move();
            } else {
                player.bullets.remove(i);
            }
        }
        ShotCD();
        player.move();
        Colshapes();
        NewWave();
        UpdateInterface();
        //revalidate();
        repaint();
    }

    // Для автоматического оружия
    private void ShotCD() {
        if (player.weaponEq == 0)
            weaponCD += 0.1;
        else if (player.weaponEq == 1)
            weaponCD += 0.07;
        else if (player.weaponEq == 2)
            weaponCD += 0.3;
        else
            weaponCD += 0.5;
    }

    private void NewWave() {
        if (currentObjects.size() < 5) {
            EnemySpawn(8);
        }
    }
    private void Colshapes() {
        // Для игрока
        Rectangle PlayerCol = player.getCol();
        for (GameObject e : currentObjects) {
            if (!e.isVisible())
                continue;
            Rectangle ObjectCol = e.getCol();
            if (PlayerCol.intersects(ObjectCol)) {
                player.changeHP(-1);
                player.playGetDMGSound();
                e.setVisible(false);
                score++;
                if (player.isDead()) {
                    player.playDeathSound();
                    timer.stop();
                    player.setVisible(false);
                    for (int i = 0; i < currentObjects.size(); i++) {
                        currentObjects.get(i).setVisible(false);
                    }
                    if(highScoreTable.containsKey(playerName) && highScoreTable.get(playerName) < score) {
                            highScoreTable.put(playerName, score);
                    }
                    else {
                        highScoreTable.put(playerName, score);
                    }
                    gameOver.setVisible(true);
                    gameOver.setText("<html>Game Over<br>Your Score: " + score + " <br> Press Space to continue</html>");
                    status = true;
                }
            }

        }
        // Для пуль
        for (Bullet b : player.bullets) {
            if (!b.isVisible())
                continue;
            Rectangle bulletCol = b.getCol();

            for (GameObject e : currentObjects) {
                if (!e.isVisible())
                    continue;
                Rectangle ObjectCol = e.getCol();

                if (bulletCol.intersects(ObjectCol)) {
                    Zombie.playDeathSound();
                    b.setVisible(false);
                    e.setVisible(false);
                    score++;
                }
            }
        }
    }

    // Главное меню
    private void startMenu() {
        JDialog panel = new JDialog(SwingUtilities.getWindowAncestor(this), "Main Menu", Dialog.ModalityType.APPLICATION_MODAL);
        panel.setSize(Game.width / 2, Game.height / 2);
        panel.setLocationRelativeTo(getParent());
        panel.setLayout(new GridLayout(4, 1));
        panel.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        panel.setResizable(false);
        panel.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        gameOver.setVisible(false);
        JLabel gamename = new JLabel("Zombie Rush", SwingConstants.CENTER);
        gamename.setFont(new Font("Helvetica", Font.BOLD, 28));
        gamename.setForeground(Color.RED);

        JButton button1 = new JButton("New Game");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File(System.getProperty("user.dir")  + "/" + playerName);
                if(!file.exists()) {
                    RespawnPlayer();
                    EnemySpawn(20);
                    score = 0;
                }
                else {
                    LoadGame();
                }
                timer.start();
                panel.setVisible(false);
                panel.dispose();
                status = false;
            }
        });

        JButton button2 = new JButton("Highscore Table");
        button2.setSize(Game.width / 3, Game.height / 3);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               // panel.setVisible(false);
                showHighScore(panel);

            }
        });

        JButton button3 = new JButton("Выход из игры");
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    saveGame();
                System.exit(0);
                }
                catch(Exception exc) {
                    System.err.println(exc);
                    System.err.println(exc.getMessage());
                }
            }
        });
        panel.add(gamename);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.setVisible(true);

    }

    // Верхняя панель
    private void MakeInterface() {
        // сенсоры
        addKeyListener(new TAdapter());
        addMouseMotionListener(new MAdapter());
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);
        // буфер для прорисовки в фоне
        setDoubleBuffered(true);
        setLayout(new BorderLayout());
        JPanel field = new Background(new ImageIcon(System.getProperty("user.dir") + "/src/Resources/bg.jpg").getImage());
        add(field, BorderLayout.CENTER);
        field.setLayout(new BorderLayout());
        field.setVisible(true);

        topPanel = new JLabel("");
        topPanel.setForeground(Color.GREEN);
        topPanel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.setVerticalAlignment(JLabel.CENTER);

        gameOver = new JLabel("Game Over");
        gameOver.setForeground(Color.RED);
        gameOver.setFont(new Font("Arial", Font.BOLD, 60));
        gameOver.setHorizontalAlignment(JLabel.CENTER);
        gameOver.setVerticalAlignment(JLabel.CENTER);

        field.add(topPanel, BorderLayout.NORTH);
        field.add(gameOver, BorderLayout.CENTER);
        gameOver.setVisible(false);

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                saveGame();
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                saveGame();
            }
        });

    }
    // Обновления верхней панели
    private void UpdateInterface() {
        if (timer.isRunning())
            topPanel.setText(playerName + " | " + player.getWeapon().name() + " | " + player.getHp()
                    + "HP" + " | " + player.getWeapon().ammoLeft() + "/" + player.getWeapon().maxAmmo() + " bullets" + "| Score: " + score);
        else
            topPanel.setText(playerName + " | " + player.getWeapon().name() + " | " + player.getHp()
                    + "HP" + " | " + player.getWeapon().ammoLeft() + "/" + player.getWeapon().maxAmmo() + " bullets" + "| Score: " + score + "| Pause");
    }
    // сенсор поворотов
    private class MAdapter extends MouseMotionAdapter {
        public void mouseMoved(MouseEvent e) {
            player.watch(e.getX(), e.getY());
        }
    }
    private class TAdapter extends KeyAdapter {
        // Ходьба
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            // Пауза
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (timer.isRunning()) {
                    timer.stop();
                    UpdateInterface();
                } else if (!player.isDead())
                    timer.start();
            }
            // Выход в главное меню
            else if (status && e.getKeyCode() == KeyEvent.VK_SPACE) {
                status = false;
                startMenu();
            }
            else {
                player.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // Задержка между выстрелами
                    if (weaponCD > 1 && !player.getWeapon().noAmmo() && !reloadTM.isRunning()) {
                        player.fire();
                        player.getWeapon().playFireSound();
                        weaponCD = 0;
                    }
                }
                else if(e.getKeyCode()  == KeyEvent.VK_R && !reloadTM.isRunning()) {
                    player.getWeapon().playReloadSound();
                    reloadTM.start();

                }
            }
        }
    }
    private void inputPlayerName() {
        playerName = JOptionPane.showInputDialog(null, "Input your name:", "Zombie Rush", JOptionPane.INFORMATION_MESSAGE);
        if(playerName == null) {
            playerName = "Player";
        }
    }
    private void saveGame () {
        if(score != 0 && !player.isDead()) {
            try {

                FileOutputStream fileOut = new FileOutputStream(playerName);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(status);

                out.writeObject(currentObjects);
                out.writeObject(player);
                out.writeObject(score);
                out.writeObject(player.getPos_x());
                out.writeObject(player.getPos_y());
                out.writeObject(player.getHp());
                out.writeObject(currentObjects.size());

                for(int i=0; i<currentObjects.size(); i++) {
                    GameObject temp = currentObjects.get(i);
                    double[] pos = { temp.getPos_x() , temp.getPos_y()};
                    out.writeObject(pos);
                }

                fileOut = new FileOutputStream("HighScore");
                out = new ObjectOutputStream(fileOut);
                out.writeObject(highScoreTable);

                out.close();
                fileOut.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    private void LoadGame() {
        try {
            FileInputStream fileIn = new FileInputStream(playerName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            status = (boolean) in.readObject();
            if(status) {
                RespawnPlayer();
                EnemySpawn(20);
                score = 0;
            }
            else {
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "An unfinished game was found, load it?",
                        "Load Game",
                        JOptionPane.YES_NO_OPTION);

                if(choice == 0) {
                    currentObjects = (ArrayList<GameObject>) in.readObject();
                    player = (Player) in.readObject();
                    score = (int) in.readObject();
                    player.moveTo((double) in.readObject(), (double) in.readObject());

                    player.changeHP((int) in.readObject());
                    int size = (int) in.readObject();

                    currentObjects = new ArrayList<>();
                    for(int i=0; i<size;i++) {
                        Zombie enemy = new Zombie(player);
                        currentObjects.add(enemy);
                        double[] pos = (double[]) in.readObject();
                        enemy.moveTo(pos[0], pos[1]);
                    }

                }
                else {
                    RespawnPlayer();
                    EnemySpawn(20);
                    score = 0;
                }
            }
            in.close();
            fileIn.close();
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private void showHighScore(JDialog prevPanel) {
        JDialog panel = new JDialog(prevPanel, "HighScore Table", Dialog.ModalityType.APPLICATION_MODAL);
        panel.setSize(Game.width / 2, Game.height / 2);
        panel.setLocationRelativeTo(prevPanel);
        panel.setResizable(false);
        panel.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        Map<String, Integer> orderedMap = highScoreTable.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(LinkedHashMap::new,
                        (m, c) -> m.put(c.getKey(), c.getValue()),
                        LinkedHashMap::putAll);

        JTable table = new JTable(toTableModel(orderedMap));
        panel.add(table);
        panel.setVisible(true);
    }
    private static TableModel toTableModel(Map<?,?> map) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Name", "Score" }, 0
        );
        for (Map.Entry<?,?> entry : map.entrySet()) {
            model.addRow(new Object[] { entry.getKey(), entry.getValue() });
        }
        return model;
    }
}


