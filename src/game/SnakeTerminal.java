package game;

import game.Block;
import game.Body;
import game.Direction.DIRECTION;
import game.Food;
import game.Snake;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.input.*;

public class SnakeTerminal {

	private static final int HardBlockNumber = 100; // Numero de barreiras modo
													// hard
	private static final int MediumBlockNumber = 50; // Numero de barreiras modo
														// medium
	private static final int EasyBlockNumber = 20; // Numero de barreiras modo
													// easy
	private Terminal term; // terminal
	static Snake snake; // Snake
	static Food food; // Conjunto de comidas
	static Block block; // Conjunto de obstaculos
	private int initX;
	private int initY;
	private int terminalCols; // Numero de Colunas
	private int terminalRows; // Numero de Linhas

	public SnakeTerminal() {

		term = TerminalFacade.createTerminal();
		term.enterPrivateMode();
		term.setCursorVisible(false);
		initX = initY = 2;
		terminalCols = term.getTerminalSize().getColumns()-2;
		terminalRows = term.getTerminalSize().getRows()-2;
		

		if (showMenu() == 0) {
			while (true) {
				Key k = term.readInput();
				if (k != null) {
					switch (k.getKind()) {
					case Escape:
						term.exitPrivateMode();
						return;
					case ArrowLeft:
						snake.setLastDirection(DIRECTION.ArrowLeft);
						break;
					case ArrowRight:
						snake.setLastDirection(DIRECTION.ArrowRight);
						break;
					case ArrowDown:
						snake.setLastDirection(DIRECTION.ArrowDown);
						break;
					case ArrowUp:
						snake.setLastDirection(DIRECTION.ArrowUp);
						break;
					default:
						break;
					}
				}

				showLimits();

				snake.move(); // Mover a snake 1 casa

				// term.clearScreen();
				term.applySGR(Terminal.SGR.ENTER_BOLD);
				printScore(Terminal.Color.BLUE);
				printSnake(Terminal.Color.RED);
				printFood(Terminal.Color.YELLOW);
				printBlock(Terminal.Color.WHITE);

				// snake.move(); //Mover a snake 1 casa

				for (int i = 0; i < food.getSize(); i++) {
					if (food.hasBeenEaten(food.getBody(i), snake.getHead())) {
						food.makeNewFood(initX,initY,terminalCols,terminalRows, snake, block);

						snake.grow();
					}
				}

				// Testar se a snake está contida nos limites do terminal e se
				// está viva
				if (!isRunning()) {
					printLoseMessage(Terminal.Color.RED);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					term.exitPrivateMode();
					new SnakeTerminal();
					break;
				}

				// term.flush();

				try {
					Thread.sleep(200); // velocidade do jogo
					term.clearScreen();
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
		//Terminar o jogo com o Escape
		else
			term.exitPrivateMode();
	}

	/**
	 * Imprime caracteres no terminal, quando não é necessário imprimir Strings
	 * como o caso do body da snake ou mesmo das comidas
	 * 
	 * @param c
	 * @param x
	 * @param y
	 */
	private void show(char c, int x, int y) {
		term.moveCursor(x, y);
		term.putCharacter(c);
	}

	/**
	 * Imprime mensagens em formato String
	 * 
	 * @param c
	 * @param x
	 * @param y
	 */
	private void show(String c, int x, int y) {
		term.moveCursor(x, y);
		int length = c.length();
		for (int i = 0; i < length; i++) {
			term.putCharacter(c.charAt(i));
		}
	}

	/**
	 * Imprime os limites no terminal para dar um feedback ao utilizador
	 */
	private void showLimits() {
		term.applyBackgroundColor(Terminal.Color.WHITE);
		term.moveCursor(initX,initY);
		for (int i = initX; i < terminalCols; i++) {
			term.putCharacter(' ');
		}
		term.moveCursor(initX, terminalRows);
		for (int i = initX; i < terminalCols; i++) {
			term.putCharacter(' ');
		}
		for (int i = initY; i <= terminalRows; i++) {
			show(' ', initX, i);
			show(' ', terminalCols, i);
		}
		term.applyBackgroundColor(Terminal.Color.BLACK);
	}

	/**
	 * Mostra a snake no ecra com a cor enviada no argumento
	 * 
	 * @param color
	 */
	private void printSnake(Color color) {
		term.applyForegroundColor(color);

		// Mostrar a snake no ecra
		show(snake.getHeadFill(), snake.getHead().getX(), snake.getHead()
				.getY());
		for (int i = 1; i < snake.getSize(); i++) {
			Body s = snake.getBody(i);
			show(snake.getBodyFill(), s.getX(), s.getY());
		}
	}

	/**
	 * Mostra o score no canto superior Esquerdo
	 * 
	 * @param color
	 */
	private void printScore(Color color) {
		term.applyForegroundColor(color);
		show("Score:" + snake.getSize(), 0, 0);
	}

	/**
	 * Mostra os vários caracteres que representam a comida
	 * 
	 * @param color
	 */
	private void printFood(Color color) {
		term.applyForegroundColor(color);
		// Mostrar comida no ecra
		for (int i = 0; i < food.getSize(); i++) {
			Body f = food.getBody(i);
			show(food.getfill(), f.getX(), f.getY());
		}
	}

	/**
	 * Imprime os obstaculos no ecra
	 * 
	 * @param color
	 */
	private void printBlock(Color color) {
		term.applyBackgroundColor(Color.WHITE);
		// Mostrar comida no ecra
		for (int i = 0; i < block.getSize(); i++) {
			Body f = block.getBody(i);
			show(block.getfill(), f.getX(), f.getY());
		}
	}

	/**
	 * Faz surgir no ecra a meio do jogo uma mensagem a piscar informando o
	 * jogador que acabou o jogo
	 * 
	 * @param color
	 */
	private void printLoseMessage(Color color) {
		term.applyBackgroundColor(Terminal.Color.BLACK);
		term.applyForegroundColor(Terminal.Color.RED);
		show('X', snake.getHead().getX(), snake.getHead().getY());

		term.applySGR(Terminal.SGR.ENTER_BLINK);
		term.applyForegroundColor(Terminal.Color.GREEN);
		String youLose = "| Y O U   L O S E ! |";
		// Mostrar mensagem de perdedor no ecra centrada
		show("---------------------", terminalCols / 2 - youLose.length() / 2,
				terminalRows / 2 - 1);
		show(youLose, terminalCols / 2 - youLose.length() / 2, terminalRows / 2);
		show("---------------------", terminalCols / 2 - youLose.length() / 2,
				terminalRows / 2 + 1);
	}

	/**
	 * Testa se o jogo ainda corre ou não, isto é se a snake ainda está viva e
	 * dentro dos limites
	 * 
	 * @return
	 */
	private boolean isRunning() {
		Body head = snake.getHead();
		int snakeX = head.getX();
		int snakeY = head.getY();

		if(snake.isInside(initX, initY, terminalCols, terminalRows)){
				if (snake.isAlive(block)) {
					return true;
				}
	
		}

		return false;
	}

	/**
	 * Inicializa o ecra do jogo com o numero de obstaculos e comidas
	 * respectivas, consoante o nivel de dificuldade dado
	 * 
	 * @param dificulty
	 */
	private void initGame(int dificulty) {
		snake = new Snake(terminalCols, terminalRows);
		block = new Block();
		food = new Food();

		switch (dificulty) {
		case 1:
			// for(int i=0;i<block.getMaxEasy();i++){
			//block.makeNewBlock(initX,initY,terminalCols, terminalRows, snake);
			// }
			for (int i = 0; i < food.getMaxEasy(); i++) {
				food.makeNewFood(initX,initY,terminalCols,terminalRows, snake, block);
			}
			break;
		case 2:
			for (int i = 0; i < block.getMaxMedium(); i++) {
				block.makeNewBlock(initX,initY,terminalCols, terminalRows, snake);
			}
			for (int i = 0; i < food.getMaxMedium(); i++) {
				food.makeNewFood(initX,initY,terminalCols,terminalRows, snake, block);			
				}
			break;
		case 3:
			for (int i = 0; i < block.getMaxHard(); i++) {
				block.makeNewBlock(initX,initY,terminalCols, terminalRows, snake);
			}
			for (int i = 0; i < food.getMaxHard(); i++) {
				food.makeNewFood(initX,initY,terminalCols,terminalRows, snake, block);			
				}
			break;

		}

	}

	/**
	 * Mostra o menu inicial com o logo do jogo e as respectivas escolhas de
	 * dificuldade
	 */
	@SuppressWarnings("incomplete-switch")
	private int showMenu() {

		int option = 1; // Opção de default (easy)

		String gameLogo[] = { "Welcome to",
				"OOOOOOOO  OO      O  OOOOOOOOO  O      O  OOOOOOOO",
				"O         O O     O  O       O  O    O    O       ",
				"O         O  O    O  O       O  O  O      O       ",
				"OOOOOOOO  O   O   O  OOOOOOOOO  O O       OOOOO   ",
				"       O  O    O  O  O       O  O  O      O       ",
				"       O  O     O O  O       O  O    O    O       ",
				"OOOOOOOO  O      OO  O       O  O      O  OOOOOOOO" };

		String exit = "Press ESC to exit game";

		// Print de Welcome to
		show(gameLogo[0], terminalCols / 2 - gameLogo[0].length() / 2, 2);

		// Print do logo da Snake
		int initLine = 5;
		for (int i = 1; i < gameLogo.length; i++) {
			show(gameLogo[i], terminalCols / 2 - gameLogo[i].length() / 2,
					initLine);
			initLine++;
		}
		initLine += 4;

		// Imprime o menu de escolha
		printOptions(option, initLine);

		// Imprime a mensagem da tecla para sair do jogo
		show(exit, terminalCols / 2 - exit.length() / 2, terminalRows - 3);

		// Espera pelo input dado pelo user
		boolean keyIsPressed = false;
		while (!keyIsPressed) {
			Key k = term.readInput();
			if (k != null) {
				switch (k.getKind()) {
				case ArrowDown:
					if (option + 1 <= 3)
						option++;
					break;
				case ArrowUp:
					if (option - 1 >= 1)
						option--;
					break;
				case Escape:
					keyIsPressed = true;
					term.exitPrivateMode();
					return 1;
				case Enter:
					initGame(option);
					keyIsPressed = true;
					break;
				}
				printOptions(option, initLine);
			}
		}
		return 0;
	}

	/**
	 * Dados uma opçao e uma linha inicial reimprime o menu das escolhas Opt
	 * values: 1-Easy 2-Medium 3-Hard
	 * 
	 * @param opt
	 * @param initLine
	 */
	private void printOptions(int opt, int initLine) {
		String options[] = {
				"Choose your dificulty and press Enter to continue:", "Easy  ",
				"Medium", "Hard  ", };
		for (int i = 0; i < options.length; i++) {
			if (i == opt)
				show(">" + options[i], terminalCols / 2 - options[0].length()
						/ 2, initLine);
			else
				show(" " + options[i], terminalCols / 2 - options[0].length()
						/ 2, initLine);
			initLine++;
		}
	}

	public static void main (String[] args){
		new SnakeTerminal();
	}
	
}