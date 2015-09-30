package game;

import java.util.LinkedList;
import java.util.Random;

public class Block {
	LinkedList<Body> block;
	private final char blockfill;
	private final int maxBlockEasy = 20;
	private final int maxBlockMedium = 50;
	private final int maxBlockHard = 100;

	public Block() {
		block = new LinkedList<Body>();
		blockfill = ' ';
	}

	/**
	 * Cria uma nova comida num sitio random do tabuleiro onde nenhuma das
	 * componentes da cobra esteja
	 * 
	 * @param x
	 * @param y
	 * @param sk
	 */
	public void makeNewBlock(int x1, int y1, int x2, int y2, Snake sk) {
		Random rand = new Random();
		boolean isPossible = false;
		Body fd = null;
		//int randX = rand.nextInt(x2);
		//int randY = rand.nextInt(y2);
		//fd = new Body(randX, randY);
		int randX;
		int randY;
		
		// Enquanto não for possivel
		while (!isPossible) {
			isPossible = true;
			// Se fora dos limites do jogo avança para a proxima tentativa
			randX = rand.nextInt(x2);
			randY = rand.nextInt(y2);
			fd = new Body(randX, randY);
			if (randX > x1 && randX < x2 && randY > y1 && randY < y2) {
				
				for (int i = 0; i < sk.getSize() && isPossible; i++) {
					// igual a uma das posicoes da cobra
					if (fd.equals(sk.getBody(i))) {
						isPossible=false;
					}
				}
				//é preciso tambem ver se não criamos o objecto em cima de outro da propria estrutura
				for (int i = 0; i < block.size() && isPossible; i++){
					if (fd.equals(block.get(i))){
						isPossible=false;
					}
				}
			}
			else{
				isPossible=false;
			}
		}
		block.addLast(fd);
	}

	/**
	 * Testa se alguma das comidas foi devorada
	 * 
	 * @param fd
	 * @param snake
	 * @return
	 */
	public boolean hasBeenTouched(Body fd, Body snake) {
		if (snake.getX() == fd.getX() && snake.getY() == fd.getY()) {
			return true;
		} else
			return false;
	}

	/**
	 * retorna o tamanho da lista de comidas
	 * 
	 * @return
	 */
	public int getSize() {
		return block.size();
	}

	/**
	 * Obtem um determinado componente da comida localizado na lista no indice
	 * fornecido pelo parametro
	 * 
	 * @param i
	 * @return
	 */
	public Body getBody(int i) {
		return block.get(i);
	}

	/**
	 * Obtem o carater que representa a estrutura
	 * 
	 * @return
	 */
	public char getfill() {
		return blockfill;
	}

	public int getMaxEasy(){
		return maxBlockEasy;
	}
	public int getMaxMedium(){
		return maxBlockMedium;
	}
	public int getMaxHard(){
		return maxBlockHard;
	}
	
}
