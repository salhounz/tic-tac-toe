package TicTacToe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import ArtificialIntelligence.*;

public class Window extends JFrame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private Board board;
    private Panel panel;
    private BufferedImage imageBackground, imageX, imageO;

    private enum Mode {Player, AI}
    private Mode mode;

    /** 
     ** The center point of each cell is stored and used for determining which 
     ** cell the player has clicked on
     **/
    private Point[] cells;

    /**
     ** The distance away from the each cell's center that 
     ** will allow a click to register
     **/
    private static final int DISTANCE = 100;

    private Window () {
    	// set mode to AI for now 
        this(Mode.AI);
    }

    /**
     ** Constructing the Window and setting mode, creating the new board
     ** loading the cells, setting the properties, and loading the background and X/O images
     ** working on expanding from only AI mode to Player vs Player in the Window
     **/
    private Window (Mode mode) {
        this.mode = mode;
        board = new Board();
        loadCells();
        panel = createPanel();
        setWindowProperties();
        loadImages();
    }

    /**
     ** Load the cell center locations via creating Points at the center 3x3=9 to determine
     ** when a cell has been clicked and to place the X/O accordingly
     **/
    private void loadCells () {
        cells = new Point[9];

        cells[0] = new Point(109, 109);
        cells[1] = new Point(299, 109);
        cells[2] = new Point(489, 109);
        cells[3] = new Point(109, 299);
        cells[4] = new Point(299, 299);
        cells[5] = new Point(489, 299);
        cells[6] = new Point(109, 489);
        cells[7] = new Point(299, 489);
        cells[8] = new Point(489, 489);
    }

    /**
     ** Set all the window properties; removing the ability to resize the window, 
     ** make the window fit all of the subcomponents, set the title, have the window
     ** exit when closed by default and to have the Window be visible
     **/
    private void setWindowProperties () {
        setResizable(false);
        pack();
        setTitle("CSC 540 - Assignment # 2 - Cody Sullins - Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     ** Create the panel and content pane with the correct dimensions along with
     ** a mouse listener and return the panel
     **/
    private Panel createPanel () {
        Panel panel = new Panel();
        Container cp = getContentPane();
        cp.add(panel);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.addMouseListener(new MyMouseAdapter());
        return panel;
    }

    private void loadImages () {
        imageBackground = getImage("background");
        imageX = getImage("x");
        imageO = getImage("o");
    }

    // Used to help get the image and return it from the hard drive/disk
    private static BufferedImage getImage (String path) {

        BufferedImage image;

        try {
            path = ".." + File.separator + "Resources" + File.separator + path + ".png";
            image = ImageIO.read(Window.class.getResource(path));
        } catch (IOException ex) {
            throw new RuntimeException("Image loading failed.");
        }

        return image;
    }

    /**
     ** Used to 'draw' the actual Tic Tac Toe board to the screen
     **/
    private class Panel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintTicTacToe((Graphics2D) g);
        }

        private void paintTicTacToe (Graphics2D g) {
        	//Set all the properties via the helper method
            setProperties(g);
            //Paint the physical board to the screen
            paintBoard(g);
            //Paint the winner to the board to show 'X wins! or 'O wins!'
            paintWinner(g);
        }

        /**
         ** Set the rendering hints for the Graphics object as well as
         ** the first time a string is drawn it normally has a small lag to being
         ** so loading or drawing something pointless to essentially 'jumpstart' was
         ** something I found online to resolve that issue which is why there is
         ** g.drawString at the end of this method
         **/
        private void setProperties (Graphics2D g) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(imageBackground, 0, 0, null);

            // The first time a string is drawn it tends to lag.
            // Drawing something trivial at the beginning loads the font up.
            // Better to lag at the beginning than during the middle of the game.
            g.drawString("", 0, 0);
        }

        /**
         ** Simply paints the background images along with the X's and O's via the
         ** Graphics objects that is actually performing the painting
         ** 
         **/
        private void paintBoard (Graphics2D g) {
            Board.State[][] boardArray = board.toArray();

            int offset = 20;

            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    if (boardArray[y][x] == Board.State.X) {
                        g.drawImage(imageX, offset + 190 * x, offset + 190 * y, null);
                    } else if (boardArray[y][x] == Board.State.O) {
                        g.drawImage(imageO, offset + 190 * x, offset + 190 * y, null);
                    }
                }
            }
        }

        /**
         ** Paints the winner to the screen
         **/
        private void paintWinner (Graphics2D g) {
            if (board.isGameOver()) {
                g.setColor(new Color(255, 255, 255));
                g.setFont(new Font("TimesRoman", Font.PLAIN, 50));

                String s;

                if (board.getWinner() == Board.State.Blank) {
                    s = "Draw";
                } else {
                    s = board.getWinner() + " Wins!";
                }

                g.drawString(s, 300 - getFontMetrics(g.getFont()).stringWidth(s)/2, 315);

            }
        }
    }

    private class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mouseClicked(e);

            if (board.isGameOver()) {
                board.reset();
                panel.repaint();
            } else {
                playMove(e);
            }

        }

        /**
         ** Determines if the move is valid and is so performs that move
         **/
        private void playMove (MouseEvent e) {
            int move = getMove(e.getPoint());

            if (!board.isGameOver() && move != -1) {
                boolean validMove = board.move(move);
                if (mode == Mode.AI && validMove && !board.isGameOver()) {
                    Algorithms.alphaBetav2(board);
                }
                panel.repaint();
            }
        }

        /**
         ** Determine the mouse clicks position on the board and return the index on the board
         ** while returning -1 if the click is invalid
         **/
        private int getMove (Point point) {
            for (int i = 0; i < cells.length; i++) {
                if (distance(cells[i], point) <= DISTANCE) {
                    return i;
                }
            }
            return -1;
        }

        /**
         ** Simply the distance between two points used to see if the player has pressed 
         ** on a cell in order for a move to be played and returning the distance between
         ** the two points to determine if it is < 100 as declared on line 36
         **/
        private double distance (Point p1, Point p2) {
            double xDiff = p1.getX() - p2.getX();
            double yDiff = p1.getY() - p2.getY();

            double xDiffSquared = xDiff*xDiff;
            double yDiffSquared = yDiff*yDiff;

            return Math.sqrt(xDiffSquared+yDiffSquared);
        }
    }

    public static void main(String[] args) {
    	// Setting the Game Mode to Player vs AI 
    	System.out.println("The game mode is currently in: Player vs. AI");
        SwingUtilities.invokeLater(() -> new Window(Mode.AI));

    }

}
