import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * Created by Markus on 22/11/16.
 */
public class  GamePanel extends JPanel {
    JPanel view1 = new JPanel();
    JPanel view2 = new JPanel();
    JPanel views = new JPanel();
    JPanel switchboard = new JPanel();
    JButton fire = new JButton("Fire");
    JLabel target = new JLabel("Target");
    JLabel score = new JLabel("Score: 0 - 0");
    JLabel[][] bfl1;
    JLabel[][] bfl2;
    int[] coordinates = {-1, -1};
    private int player1score = 0;
    private int player2score = 0;
    private int maxplayerscore = 0;

    // parameeter player on enda lisatud, teise välja y koordinaadid algavad 10-st, mitte 0-st
    public JLabel[][] createBattleField(int player) {
        JLabel battleFieldLocations[][] = new JLabel[BattleWindow.battleFieldSize][BattleWindow.battleFieldSize];
        //init JLabels
        for (int x=0;x<BattleWindow.battleFieldSize;x++){
            for (int y=0;y<BattleWindow.battleFieldSize;y++){
                battleFieldLocations[x][y]= new JLabel("~");
                battleFieldLocations[x][y].setForeground(Color.cyan);
                battleFieldLocations[x][y].setBackground(Color.BLUE);
                battleFieldLocations[x][y].setOpaque(true);
                battleFieldLocations[x][y].setPreferredSize(new Dimension(20, 20));
                battleFieldLocations[x][y].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        int locationx = -1;
                        int locationy = -1;
                        for (int x=0;x<BattleWindow.battleFieldSize;x++) {
                            for (int y = 0; y < BattleWindow.battleFieldSize; y++) {
                                if (e.getSource() == battleFieldLocations[x][y]) {
                                    locationx = x;
                                    locationy = y + player;
                                }
                            }
                        }
                        coordinates[0] = locationx;
                        coordinates[1] = locationy;
                        if (locationy < 10) {
                            target.setText(BattleWindow.player1.name + " x: " + locationx + " y: " + locationy);
                        }
                        else {
                            target.setText(BattleWindow.player2.name + " x: " + locationx + " y: " + (locationy-10));
                        }

                    }
                });
            }
        }
        return battleFieldLocations;

    }

    public void displayGamePanelContents() {
        views.setPreferredSize(new Dimension(400,200));
        views.setLayout(new GridLayout(1,2));
        view1.setPreferredSize(new Dimension(200, 200));
        view1.setOpaque(true);
        view1.setLayout(new GridLayout(BattleWindow.battleFieldSize,BattleWindow.battleFieldSize));
        view2.setPreferredSize(new Dimension(200, 200));
        view2.setLayout(new GridLayout(BattleWindow.battleFieldSize,BattleWindow.battleFieldSize));
        view2.setOpaque(true);
        view1.setLocation(0,0);
        view2.setLocation(200,0);
        for (int x = 0; x < BattleWindow.battleFieldSize; x++) {
            for (int y = 0; y < BattleWindow.battleFieldSize; y++) {
                bfl1 = createBattleField(0);
                bfl2 = createBattleField(10);
            }
        }
        for (int x = 0; x < BattleWindow.battleFieldSize; x++) {
            for (int y = 0; y < BattleWindow.battleFieldSize; y++) {
                view1.add(bfl1[x][y]);
                view2.add(bfl2[x][y]);
            }
        }

        fire.addActionListener(fireListener);
        switchboard.setLayout(new GridLayout(3,1));
        switchboard.add(fire);
        switchboard.add(target);
        switchboard.add(score);
        switchboard.setVisible(true);
        switchboard.repaint();
        views.add(view1);
        views.add(view2);
        views.setVisible(true);
        views.repaint();
        add(views);
        add(switchboard);
        revalidate();
        repaint();
    }

    public GamePanel() {
        setLayout(new FlowLayout());
        createBattleField(0);
        displayGamePanelContents();
    }

    ActionListener fireListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (BattleWindow.currentPlayer.name == BattleWindow.player1.name) {
                if (coordinates[1] > 9) {
                    target.setText("Vale laud");
                }
                else if (coordinates[1] < 10){
                    if (BattleWindow.player1.planningfield[coordinates[0]][coordinates[1]] == SeaConstants.SHIP) {
                        BattleWindow.player1.planningfield[coordinates[0]][coordinates[1]] = SeaConstants.HIT_SHIP;
                        bfl1[coordinates[0]][coordinates[1]].setBackground(Color.RED);
                        bfl1[coordinates[0]][coordinates[1]].repaint();
                        player1score++;
                        if (shipSunk(coordinates[0],coordinates[1])){
                            target.setText("Player 1 ship sunk");
                        }
                        else {
                            target.setText("Player 1 ship hit");
                        }
                        setScore();
                    }
                    else {
                        target.setText("Laeva ei olnud");
                        bfl1[coordinates[0]][coordinates[1]].setBackground(Color.GREEN);
                        bfl1[coordinates[0]][coordinates[1]].repaint();
                        BattleWindow.currentPlayer = BattleWindow.player2;
                    }
                }
            }
            else if (BattleWindow.currentPlayer.name == BattleWindow.player2.name) {
                if (coordinates[1] > 9) {
                    if (BattleWindow.player2.planningfield[coordinates[0]][coordinates[1] - 10] == SeaConstants.SHIP) {
                        BattleWindow.player2.planningfield[coordinates[0]][coordinates[1] - 10] = SeaConstants.HIT_SHIP;
                        bfl2[coordinates[0]][coordinates[1] - 10].setBackground(Color.RED);
                        bfl2[coordinates[0]][coordinates[1] - 10].repaint();
                        player2score++;
                        if (shipSunk(coordinates[0],coordinates[1]-10)){
                            target.setText("Player2 ship sunk");
                        }
                        else {
                            target.setText("Player 2 ship hit");
                        }
                        setScore();
                    }
                    else {
                        target.setText("Laeva ei olnud");
                        bfl2[coordinates[0]][coordinates[1] - 10].setBackground(Color.GREEN);
                        bfl2[coordinates[0]][coordinates[1] - 10].repaint();
                        BattleWindow.currentPlayer = BattleWindow.player1;
                    }
                }
                else if (coordinates[1] < 10) {
                    target.setText("Vale laud");
                }
            }

        }

    };

    private void setScore() {
        score.setText("Score: " + player1score + " - " + player2score);
    }
    private boolean shipSunk(int locationx, int locationy) {
        LinkedList<Integer[]> shipCoordinates = new LinkedList<>();
        boolean breakout = false;
        boolean breakout2 = false;
        int[] direction = {-1, -1};
        BattleWindow.currentPlayer.planningfield[locationx][locationy] = SeaConstants.SEA;
        for (int x = locationx - 1; x < locationx + 2; x++) {
            for (int y = locationy - 1; y < locationy + 2; y++) {
                if (x >= 0 && x < BattleWindow.battleFieldSize && y >= 0 && y < BattleWindow.battleFieldSize ) {
                    if (BattleWindow.currentPlayer.planningfield[x][y] == SeaConstants.SHIP || BattleWindow.currentPlayer.planningfield[x][y] == SeaConstants.HIT_SHIP) {
                        direction = new int[]{x - locationx, y - locationy};
                        //
                    }

                }
            }
        }
        BattleWindow.currentPlayer.planningfield[locationx][locationy] = SeaConstants.HIT_SHIP;
        for(int d = 0;d < 5;d++) {
            int mx = (d*direction[0])+ locationx;
            int my = (d*direction[1])+ locationy;
            for (int x = mx; x < mx + 1 ; x++) {
                for (int y = my; y < my + 1; y++) {
                    if (x >= 0 && x < BattleWindow.battleFieldSize && y >= 0 && y < BattleWindow.battleFieldSize) {
                        if (BattleWindow.currentPlayer.planningfield[x][y] == SeaConstants.SHIP || BattleWindow.currentPlayer.planningfield[x][y] == SeaConstants.HIT_SHIP) {
                            shipCoordinates.add(new Integer[]{x, y});
                        }
                        else {
                            breakout = true;
                        }
                        if (breakout) break;
                    }
                    if (breakout) break;
                }
                if (breakout) break;
            }
            if (breakout) break;
        }
        for(int d = 0;d < 5;d++) {
            int mx = -(d*direction[0])+ locationx;
            int my = -(d*direction[1])+ locationy;
            for (int x = mx; x < mx + 1 ; x++) {
                for (int y = my; y < my + 1; y++) {
                    if (x >= 0 && x < BattleWindow.battleFieldSize && y >= 0 && y < BattleWindow.battleFieldSize) {
                        if (BattleWindow.currentPlayer.planningfield[x][y] == SeaConstants.SHIP || BattleWindow.currentPlayer.planningfield[x][y] == SeaConstants.HIT_SHIP) {
                            shipCoordinates.add(new Integer[]{x, y});
                        }
                        else {
                            breakout2 = true;
                        }
                        if (breakout2) break;
                    }
                    if (breakout2) break;
                }
                if (breakout2) break;
            }
            if (breakout2) break;
        }
        for (Integer[] m : shipCoordinates) {
            if (BattleWindow.currentPlayer.planningfield[m[0]][m[1]] != SeaConstants.HIT_SHIP) {
                return false;
            }
        }

        return true;
    }





}
