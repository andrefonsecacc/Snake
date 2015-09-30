package game;

public class Body {

	private int posX;
	private int posY;
	
	Body(int x, int y){
		posX=x;
		posY=y;
	}
	
	public int getX(){
		return posX;
	}
	public int getY(){
		return posY;
	}

	public void setY(int y) {
		posY=y;
	}

	public void setX(int x) {
		posX=x;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + posX;
		result = prime * result + posY;
		return result;
	}

	/**
	 * Dois body's são iguais quando se encontram no mesmo ponto do terminal
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Body other = (Body) obj;
		if (posX != other.posX)
			return false;
		if (posY != other.posY)
			return false;
		return true;
	}
	
	
}
