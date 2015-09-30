package game;

import java.util.LinkedList;
import java.util.Random;

import game.Direction.DIRECTION;


public class Snake {

	private final int initPositionX;
	private final int initPositionY;
	private final char headfill = '@';
	private final char bodyfill = 'O';
	DIRECTION dir;

	public LinkedList<Body> snake;				//objecto da snake formado por multiplos objectos Body
	private DIRECTION lastDirection;	//ultima direçao dada à snake, a primeira direcao sera aleatoria
	
	
	/**
	 * A Snake comeca sempre no centro com uma direccao aleatoria
	 * @param termX
	 * @param termY
	 */
	public Snake(int termX, int termY){
		snake = new LinkedList<Body>();
		if((Integer)termX==null || (Integer)termY == null){
			throw new IllegalArgumentException();
		}
		initPositionX = termX/2;
		initPositionY = termY/2;
		
		Body b = new Body(initPositionX, initPositionY);
		snake.addLast(b);
		
		Random rand=new Random();
		rand=new Random();
		switch(rand.nextInt(4)){
		case 0: dir = DIRECTION.ArrowUp; break;
		case 1: dir = DIRECTION.ArrowDown; break;
		case 2: dir = DIRECTION.ArrowLeft; break;
		case 3: dir = DIRECTION.ArrowRight; break;
		}
		lastDirection=dir;
		
	}
	
	/**
	 * Funcao que faz o crescer da snake
	 * 
	 * Pensamento:
	 * Obter coordenadas do ultimo componente da snake
	 * Criar um novo componente Body com os pontos obtidos anteriormente
	 * Adicionar o componente criado a snake
	 * 
	 * Quando o terminal actualizar e todas as pecas se movimentarem 1 casa 
	 * esta peca tera de esperar de forma a ficar imediatamente atras da 
	 * actual penultima
	 */
	public void grow(){
		int x = snake.getLast().getX();			//posicao X da cauda da snake
		int y = snake.getLast().getY();			//posicoa Y da cauda da snake
		Body tail = new Body(x,y);
		snake.addLast(tail);
	}
	
	
	/**
	 * Funcao que faz o movimento da snake
	 * É removido o ultimo elemento da lista e adicionado um novo elemento a 
	 * cabeca da snake fazendo assim o moviemento desta 
	 * 
	 */
	public void move(){
		
		int lastX=snake.getFirst().getX();
		int lastY=snake.getFirst().getY();
				
		switch (lastDirection) {
		case ArrowLeft:
			lastX--;
			break;
		case ArrowRight:
			lastX++;
			break;
		case ArrowDown:
			lastY++;
			break;
		case ArrowUp:
			lastY--;
			break;
		}
		
		Body newHead = new Body(lastX, lastY);
		snake.addFirst(newHead);
		snake.removeLast();
	}
	

	
	
	
	/**
	 * Funcao que verifica se a serpente (neste caso a cabeca) esta contida
	 * nos limites da tela, passado como argumento
	 * 
	 * @return true (caso esteja dentro) ou false (caso esteja fora)
	 */
	public boolean isInside(int initX, int initY, int termX, int termY){
		int snakeHeadX = snake.getFirst().getX();		//posicao X da cabeca da snake
		int snakeHeadY = snake.getFirst().getY();		//posicao Y da cabeca da snake
		if (snakeHeadX > initX && snakeHeadX < termX){ 		//esta dentro dos limites de X
			if (snakeHeadY > initY && snakeHeadY < termY){  //esta dentro dos limites de Y
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Muda o valor da ultima direcao que a snake tinha
	 * @param d
	 */
	public void setLastDirection(DIRECTION d){
		//casos em que nao é possivel mudar a direcao
		if(d==DIRECTION.ArrowDown && lastDirection==DIRECTION.ArrowUp)
			return;
		if(d==DIRECTION.ArrowUp && lastDirection==DIRECTION.ArrowDown)
			return;
		if(d==DIRECTION.ArrowLeft && lastDirection==DIRECTION.ArrowRight)
			return;
		if(d==DIRECTION.ArrowRight && lastDirection==DIRECTION.ArrowLeft)
			return;
		
		lastDirection=d;
	}
	
	/**
	 * Retorna o elemento Body na posição i
	 * @param i
	 * @return body at index i
	 */
	public Body getBody(int i){
		return snake.get(i);
	}
	
	/**
	 * Retorna o tamanho da snake
	 * 
	 * @return size of snake
	 */
	public int getSize(){
		return snake.size();
	}
	
	/**
	 * Obtem o primeiro elemento da Serpente (Cabeça)
	 * @return
	 */
	public Body getHead(){
		return snake.getFirst();
	}

	/**
	 * Funcao que testa se a snake esta ou nao viva
	 * 
	 * @return boolean true when alive and false when dead
	 */
	public boolean isAlive(Block block){
		Body head = snake.getFirst();
		//percorrer todos os elementos body e ver se a cabeça não bate em nenhum
		//apenas podemos fazer com tamanho superior a 2, devido a excepçao da adição do corpo quando a snake come pela primeira vez
		if(snake.size()>2)
			for (int i=1;i<snake.size();i++){
				Body bd = snake.get(i);
				if(head.equals(bd))
					return false;
			}
		for(int i=0;i<block.getSize();i++){
			Body blockUnity = block.getBody(i);
			if(head.equals(blockUnity)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Retorna o caracter que representa a cabeça da serpente
	 * @return
	 */
	public char getHeadFill(){
		return headfill;
	}
	
	/**
	 *  
	 * @return
	 */
	public char getBodyFill(){
		return bodyfill;
	}
	
}
