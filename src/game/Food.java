package game;

import java.util.LinkedList;
import java.util.Random;

public class Food {

	LinkedList<Body> food;
	private final char foodfill;
	private final int maxFoodEasy = 50;
	private final int maxFoodMedium = 20;
	private final int maxFoodHard = 10;

	public Food() {
		food = new LinkedList<Body>();
		foodfill = '$';
	}

	/**
	 * Cria uma nova comida num sitio random do tabuleiro onde nenhuma 
	 * outra componente esteja (snake, comidas, blocos) e dentro dos 
	 * limites do jogo
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param sk
	 * @param block
	 */
	public void makeNewFood(int x1, int y1, int x2, int y2, Snake sk, Block block) {
		Random rand = new Random();
		boolean isPossible = false;
		Body fd=null;
		int randX;
		int randY;
		
		// Enquanto não for possivel
		while (!isPossible) {
			isPossible = true;
			randX = rand.nextInt(x2);
			randY = rand.nextInt(y2);
			fd = new Body(randX, randY);
			if (randX > x1 && randX < x2 && randY > y1 && randY < y2) {

				for (int i = 0; i < sk.getSize() &&isPossible; i++) {
					// igual a uma das posicoes da cobra
					if (fd.equals(sk.getBody(i))) {
						isPossible=false;
					}
				}
				for (int i = 0; i < block.getSize() && isPossible; i++) {
					// igual a uma das posicoes dos blocos
					if (fd.equals(block.getBody(i))) {
						isPossible = false;
					}
				}
				//é preciso tambem ver se não criamos o objecto em cima de outro da propria estrutura
				for (int i = 0; i < food.size() && isPossible; i++){
					if (fd.equals(food.get(i))){
						isPossible=false;
					}
				}
			}
			else{
				isPossible=false;
			}
		}
		food.addLast(fd);
	}

	/**
	 * Testa se alguma das comidas foi devorada
	 * 
	 * @param fd
	 * @param snake
	 * @return
	 */
	public boolean hasBeenEaten(Body fd, Body snake) {
		if (snake.getX() == fd.getX() && snake.getY() == fd.getY()) {
			food.remove(fd);
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
		return food.size();
	}

	/**
	 * Obtem um determinado componente da comida localizado na lista no indice
	 * fornecido pelo parametro
	 * 
	 * @param i
	 * @return
	 */
	public Body getBody(int i) {
		return food.get(i);
	}

	/**
	 * Obtem o carater que representa a estrutura
	 * 
	 * @return
	 */
	public char getfill() {
		return foodfill;
	}

	public int getMaxEasy() {
		return maxFoodEasy;
	}

	public int getMaxMedium() {
		return maxFoodMedium;
	}

	public int getMaxHard() {
		return maxFoodHard;
	}

}